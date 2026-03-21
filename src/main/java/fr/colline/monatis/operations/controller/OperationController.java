package fr.colline.monatis.operations.controller;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTousTypesService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurControle;
import fr.colline.monatis.operations.controller.dto.request.DetailOperationRequestDto;
import fr.colline.monatis.operations.controller.dto.request.OperationCreationRequestDto;
import fr.colline.monatis.operations.controller.dto.request.OperationModificationRequestDto;
import fr.colline.monatis.operations.controller.dto.request.OperationSoldeRequestDto;
import fr.colline.monatis.operations.controller.dto.response.OperationBasicResponseDto;
import fr.colline.monatis.operations.controller.dto.response.OperationDetailedResponseDto;
import fr.colline.monatis.operations.controller.mapper.OperationDtoMapper;
import fr.colline.monatis.operations.model.DetailOperation;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.service.BeneficiaireService;
import fr.colline.monatis.references.service.SousCategorieService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/operations")
@Transactional
public class OperationController {

	@Autowired private OperationService operationService;

	@Autowired private CompteTousTypesService compteTousTypesService;
	@Autowired private SousCategorieService sousCategorieService;
	@Autowired private BeneficiaireService beneficiaireService;

	@Autowired private CompteInterneService compteInterneService;

	@GetMapping("/all")
	public List<OperationBasicResponseDto> getAllOperation() 
			throws ServiceException {

		List<OperationBasicResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("dateValeur");
		List<Operation> liste;
		liste = operationService.rechercherTous(tri);
		for ( Operation operation : liste ) {
			resultat.add(OperationDtoMapper.modelToBasicResponseDto(operation));
		}
		return resultat;
	}

	@GetMapping("/get/{numero}")
	public OperationDetailedResponseDto getOperationParNumero(
			@PathVariable(name = "numero") String numero) throws ControllerException {

		if ( numero == null || numero.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NUMERO_OBLIGATOIRE);
		}

		try {
			Operation operation = operationService.rechercherParNumero(numero);
			if ( operation == null ) {
				throw new ControllerException(
						ErreurControle.OPERATION_NON_TROUVEE_PAR_NUMERO,
						numero);
			}
			return OperationDtoMapper.modelToDetailedResponseDto(operation);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.OPERATION_CONSULTATION_PROBLEME);
		}
	}

	@PostMapping("/new")
	public OperationDetailedResponseDto creerOperation(
			@RequestBody OperationCreationRequestDto dto) throws ControllerException {

		try {
			Operation operation = new Operation();
			operation = creationRequestDtoToModel(operation, dto);
			operation = operationService.creerOperation(operation);
			return OperationDtoMapper.modelToDetailedResponseDto(operation);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.OPERATION_CREATION_PROBLEME);
		}
	}

	@PutMapping("/mod/{numero}")
	public OperationDetailedResponseDto modifierOperation(
			@PathVariable(name = "numero") String numero,
			@RequestBody OperationModificationRequestDto dto) throws ControllerException {

		if ( numero == null || numero.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NUMERO_OBLIGATOIRE);
		}

		try {
			Operation operation = operationService.rechercherParNumero(numero);
			if ( operation == null ) {
				throw new ControllerException(
						ErreurControle.OPERATION_NON_TROUVEE_PAR_NUMERO,
						numero);
			}
			operation = modificationRequestDtoToModel(operation, dto);
			operation = operationService.modifierOperation(operation);
			return OperationDtoMapper.modelToDetailedResponseDto(operation);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.OPERATION_MODIFICATION_PROBLEME,
					numero);
		}
	}

	@DeleteMapping("/del/{numero}")
	public void supprimerOperation(
			@PathVariable(name = "numero") String numero) throws ControllerException {

		if ( numero == null || numero.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NUMERO_OBLIGATOIRE);
		}

		try {
			Operation operation = operationService.rechercherParNumero(numero);
			if ( operation == null ) {
				throw new ControllerException(
						ErreurControle.OPERATION_NON_TROUVEE_PAR_NUMERO,
						numero);
			}
			operationService.supprimerOperation(operation.getId());
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.OPERATION_SUPPRESSION_PROBLEME);
		}
	}
	
	@PostMapping("/ajustement/{identifiant}")
	public OperationDetailedResponseDto ajusterCompteInterne(
			@PathVariable(name = "identifiant") String identifiantCompteAjustable,
			@RequestBody OperationSoldeRequestDto dto) throws ControllerException {
		
		if ( identifiantCompteAjustable.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}
		if ( dto.montantSoldeEnCentimes == null ) {
			throw new ControllerException(
					ErreurControle.OPERATION_AJUSTEMENT_COMPTE_SOLDE_OBLIGATOIRE,
					identifiantCompteAjustable);
		}

		if ( dto.identifiantCompteContrepartie == null 
				|| dto.identifiantCompteContrepartie.isBlank() ) {
			dto.identifiantCompteContrepartie = "AJUSTEMENT";
		}
		if ( dto.dateValeur == null ) {
			dto.dateValeur = ZonedDateTime.now();
		}
		
		CompteInterne compteAjustable;
		CompteInterne compteAjustement;
		try {
			compteAjustable = compteInterneService.rechercherParIdentifiant(identifiantCompteAjustable);
			if ( compteAjustable == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
						identifiantCompteAjustable);
			}
			compteAjustement = compteInterneService.rechercherParIdentifiant(dto.identifiantCompteContrepartie);
			if ( compteAjustement == null ) {
				compteAjustement = new CompteInterne();
				compteAjustement.setIdentifiant(dto.identifiantCompteContrepartie);
				compteAjustement.setLibelle(TypeCompteInterne.CONTREPARTIE.getLibelle());
				compteAjustement.setTypeCompteInterne(TypeCompteInterne.CONTREPARTIE);
				compteAjustement.setDateSoldeInitial(dto.dateValeur);
				compteAjustement.setMontantSoldeInitialEnCentimes(0L);
				compteAjustement = compteInterneService.creerCompte(compteAjustement);
			}
			
			Operation operation = operationService.creerOperationAjustement(
					compteAjustable,
					compteAjustement,
					dto.dateValeur,
					dto.montantSoldeEnCentimes);
			
			return OperationDtoMapper.modelToDetailedResponseDto(operation); 
		}
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.OPERATION_AJUSTEMENT_PROBLEME,
					identifiantCompteAjustable);
		}
	}

	@PostMapping("/actualisation/{identifiant}")
	public OperationDetailedResponseDto actualiserCompteInterne(
			@PathVariable(name = "identifiant") String identifiantCompteActualisable,
			@RequestBody OperationSoldeRequestDto dto) throws ControllerException {
		
		if ( identifiantCompteActualisable == null || identifiantCompteActualisable.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}
		if ( dto.montantSoldeEnCentimes == null ) {
			throw new ControllerException(
					ErreurControle.OPERATION_ACTUALISATION_COMPTE_SOLDE_OBLIGATOIRE,
					identifiantCompteActualisable);
		}

		if ( dto.identifiantCompteContrepartie == null 
				|| dto.identifiantCompteContrepartie.isBlank() ) {
			dto.identifiantCompteContrepartie = "ACTUALISATION";
		}
		if ( dto.dateValeur == null ) {
			dto.dateValeur = ZonedDateTime.now();
		}
		
		try {
			CompteInterne compteActualisable = compteInterneService.rechercherParIdentifiant(identifiantCompteActualisable);
			if ( compteActualisable == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
						identifiantCompteActualisable);
			}
			
			CompteInterne compteActualisation = compteInterneService.rechercherParIdentifiant(dto.identifiantCompteContrepartie);
			if ( compteActualisation == null ) {
				compteActualisation = new CompteInterne();
				compteActualisation.setIdentifiant(dto.identifiantCompteContrepartie);
				compteActualisation.setLibelle(TypeCompteInterne.CONTREPARTIE.getLibelle());
				compteActualisation.setTypeCompteInterne(TypeCompteInterne.CONTREPARTIE);
				compteActualisation.setDateSoldeInitial(ZonedDateTime.now());
				compteActualisation.setMontantSoldeInitialEnCentimes(0L);
				compteActualisation = compteInterneService.creerCompte(compteActualisation);
			}
			
			Operation operation = operationService.creerOperationActualisation(
					compteActualisable,
					compteActualisation,
					dto.dateValeur,
					dto.montantSoldeEnCentimes);
			
			return OperationDtoMapper.modelToDetailedResponseDto(operation); 
		}
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.OPERATION_ACTUALISATION_PROBLEME,
					identifiantCompteActualisable);
		}
	}

	@GetMapping("/pmval/{identifiant}")
	public double getPlusValue(
			@PathVariable(name = "identifiant") String compteInvestissementIdentifiant,
			@RequestParam(name = "dateValeur", required = false) ZonedDateTime dateValeur,
			@RequestParam(name = "valeur") Long valeur) throws ControllerException, ServiceException {
	
		if ( compteInvestissementIdentifiant == null 
				|| compteInvestissementIdentifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}
		if ( valeur == null ) {
			throw new ControllerException(
					ErreurControle.OPERATION_ACTUALISATION_COMPTE_SOLDE_OBLIGATOIRE,
					compteInvestissementIdentifiant);
		}

		CompteInterne compte = compteInterneService.rechercherParIdentifiant(compteInvestissementIdentifiant);
		if ( compte == null ) {
			throw new ControllerException(
					ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
					compteInvestissementIdentifiant);
		}
		if ( dateValeur == null ) {
			dateValeur = ZonedDateTime.now();
		}

		return operationService.rechercherPourcentagePlusOuMoinsValue(
				compte, 
				dateValeur,
				valeur);
	}
	
	private Operation creationRequestDtoToModel(
			Operation operation, 
			OperationCreationRequestDto dto) throws ControllerException, ServiceException {

		// Préparation nouvelle opération
		//
		operation.setTypeOperation(verifierTypeOperation(dto.codeTypeOperation));
		operation.setNumero(verifierNumero(dto.numero));
		operation.setLibelle(verifierLibelle(dto.libelle));
		operation.setDateValeur(verifierDate(dto.dateValeur));
		operation.setMontantTotalEnCentimes(verifierMontantEnCentimes(dto.montantTotalEnCentimes));
		operation.setCompteDepense(verifierCompteDepenseEnregistre(dto.identifiantCompteDepense));
		operation.setCompteRecette(verifierCompteRecetteEnregistre(dto.identifiantCompteRecette));

		// Préparation de la première ligne de détail
		//
		DetailOperation detailOperation = new DetailOperation();
		detailOperation.setSequence(0);
		detailOperation.setLibelle(operation.getLibelle());
		detailOperation.setDateComptabilisation(operation.getDateValeur());
		detailOperation.setMontantDetailEnCentimes(operation.getMontantTotalEnCentimes());
		detailOperation.setSousCategorie(verifierSousCategorieEnregistree(dto.nomSousCategorie));
		detailOperation.setBeneficiaires(verifierBeneficiairesEnregistres(dto.nomsBeneficiaires));
		detailOperation.setOperation(operation);

		operation.getDetailsOperation().add(detailOperation);

		return operation;
	}

	private Operation modificationRequestDtoToModel(
			Operation operation, 
			OperationModificationRequestDto dto) throws ControllerException, ServiceException {

		// Modification de l'opération
		//
		if ( dto.codeTypeOperation != null ) operation.setTypeOperation(verifierTypeOperation(dto.codeTypeOperation));
		if ( dto.numero != null ) operation.setNumero(verifierNumero(dto.numero));
		if ( dto.libelle != null ) operation.setLibelle(verifierLibelle(dto.libelle));
		if ( dto.dateValeur != null ) operation.setDateValeur(verifierDate(dto.dateValeur));
		if ( dto.montantTotalEnCentimes != null ) operation.setMontantTotalEnCentimes(verifierMontantEnCentimes(dto.montantTotalEnCentimes));
		if ( dto.identifiantCompteDepense != null ) operation.setCompteDepense(verifierCompteDepenseEnregistre(dto.identifiantCompteDepense));
		if ( dto.identifiantCompteRecette != null ) operation.setCompteRecette(verifierCompteRecetteEnregistre(dto.identifiantCompteRecette));
		
		if ( dto.detailsOperation != null ) operation.setDetailsOperation(modificationRequestDtoToModel(operation, dto.detailsOperation));

		return operation;
	}

	private Set<DetailOperation> modificationRequestDtoToModel(
			Operation operation, 
			List<DetailOperationRequestDto> listeDto) throws ControllerException, ServiceException {

		Set<DetailOperation> detailsOperation = new HashSet<>();

		int dernierNumeroSequence = rechercherDerniereSequence(operation.getDetailsOperation());

		for ( DetailOperationRequestDto dto : listeDto ) {
			DetailOperation detailOperation;
			if ( dto.sequence == null ) {
				// Nouveau détail
				detailOperation = new DetailOperation();
				detailOperation.setSequence(++dernierNumeroSequence);
				detailOperation.setOperation(operation);
			}
			else {
				// Détail existant
				detailOperation = rechercherParSequence(operation.getDetailsOperation(), dto.sequence);
				if ( detailOperation == null ) {
					throw new ControllerException(
							ErreurControle.DETAIL_OPERATION_NON_TROUVE_PAR_SEQUENCE,
							operation.getNumero(),
							dto.sequence);
				}
			}
			if ( dto.libelle != null ) detailOperation.setLibelle(verifierLibelle(dto.libelle));
			if ( dto.dateComptabilisation != null ) detailOperation.setDateComptabilisation(verifierDate(dto.dateComptabilisation));
			if ( dto.montantDetailEnCentimes != null ) detailOperation.setMontantDetailEnCentimes(verifierMontantEnCentimes(dto.montantDetailEnCentimes));
			if ( dto.nomSousCategorie != null ) detailOperation.setSousCategorie(verifierSousCategorieEnregistree(dto.nomSousCategorie));
			if ( dto.nomsBeneficiaires != null ) detailOperation.setBeneficiaires(verifierBeneficiairesEnregistres(dto.nomsBeneficiaires));

			detailsOperation.add(detailOperation);
		}

		return detailsOperation;
	}

	private String verifierNumero(String numero) {

		if ( numero == null || numero.isBlank() ) {
			numero = null;
		}
		return numero;
	}

	private String verifierLibelle(String libelle) {

		if ( libelle == null || libelle.isBlank() ) {
			libelle = null;
		}
		return libelle;
	}

	private TypeOperation verifierTypeOperation(
			String codeTypeOperation) throws ControllerException {

		if ( codeTypeOperation == null || codeTypeOperation.isBlank() ) {
			throw new ControllerException(
					ErreurControle.OPERATION_TYPE_OPERATION_OBLIGATOIRE);
		}

		TypeOperation typeOperation = TypeOperation.findByCode(codeTypeOperation);
		if ( typeOperation == null ) {
			throw new ControllerException(
					ErreurControle.TYPE_OPERATION_NON_TROUVE_PAR_CODE,
					codeTypeOperation);
		}
		return typeOperation;
	}

	private ZonedDateTime verifierDate(ZonedDateTime date) {

		if( date == null ) {
			return ZonedDateTime.from(Instant.now());
		}
		return date;
	}

	private Long verifierMontantEnCentimes(Long montantEnCentimes) throws ControllerException {

		if ( montantEnCentimes == null || montantEnCentimes.longValue() == 0 ) {
			throw new ControllerException(
					ErreurControle.OPERATION_MONTANT_OBLIGATOIRE);
		}
		return montantEnCentimes;
	}

	private Compte verifierCompteDepenseEnregistre(String identifiantCompteDepense) 
			throws ControllerException, ServiceException {

		if ( identifiantCompteDepense == null || identifiantCompteDepense.isBlank() ) {
			throw new ControllerException(
					ErreurControle.OPERATION_COMPTE_DEPENSE_OBLIGATOIRE);
		}

		Compte compte = compteTousTypesService.rechercherParIdentifiant(identifiantCompteDepense);
		if ( compte == null ) {
			throw new ControllerException(
					ErreurControle.COMPTE_NON_TROUVE_PAR_IDENTIFIANT, 
					identifiantCompteDepense);
		}
		
		return compte;
	}

	private Compte verifierCompteRecetteEnregistre(String identifiantCompteRecette) 
			throws ControllerException, ServiceException {

		if ( identifiantCompteRecette == null || identifiantCompteRecette.isBlank() ) {
			throw new ControllerException(
					ErreurControle.OPERATION_COMPTE_RECETTE_OBLIGATOIRE);
		}

		Compte compte = compteTousTypesService.rechercherParIdentifiant(identifiantCompteRecette);
		if ( compte == null ) {
			throw new ControllerException(
					ErreurControle.COMPTE_NON_TROUVE_PAR_IDENTIFIANT, 
					identifiantCompteRecette);
		}
		
		return compte;
	}

	private SousCategorie verifierSousCategorieEnregistree(
			String nomSousCategorie) throws ControllerException, ServiceException {

		if ( nomSousCategorie == null || nomSousCategorie.isBlank() ) {
			return null;
		}

		SousCategorie sousCategorie = sousCategorieService.rechercherParNom(nomSousCategorie);
		if ( sousCategorie == null ) {
			throw new ControllerException(
					ErreurControle.SOUS_CATEGORIE_NON_TROUVEE_PAR_NOM, 
					nomSousCategorie);
		}
		
		return sousCategorie;
	}

	private Set<Beneficiaire> verifierBeneficiairesEnregistres(
			List<String> nomsBeneficiaires) throws ControllerException, ServiceException {

		Set<Beneficiaire> beneficiaires = new HashSet<>();

		if ( nomsBeneficiaires == null ) {
			return beneficiaires;
		}

		for ( String nomBeneficiaire : nomsBeneficiaires ) {
				Beneficiaire beneficiaire = beneficiaireService.rechercherParNom(nomBeneficiaire);
				if ( beneficiaire == null ) {
					throw new ControllerException(
							ErreurControle.BENEFICIAIRE_NON_TROUVE_PAR_NOM, 
							nomBeneficiaire);
				}
				beneficiaires.add(beneficiaire);
		} 

		return beneficiaires;
	}

	private int rechercherDerniereSequence(Set<DetailOperation> detailsOperation) {

		int derniereSequence = 0;
		for ( DetailOperation detailOperation : detailsOperation ) {
			derniereSequence = Math.max(derniereSequence, detailOperation.getSequence());
		}
		return derniereSequence;
	}

	private DetailOperation rechercherParSequence(Set<DetailOperation> detailsOperation, int sequence) {
	
		for ( DetailOperation detailOperation : detailsOperation ) {
			if ( detailOperation.getSequence() == sequence ) {
				return detailOperation;
			}
		}
		return null;
	}
}
