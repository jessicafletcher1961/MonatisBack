package fr.colline.monatis.operations.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.comptes.controller.CompteResponseDtoMapper;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationControleErreur;
import fr.colline.monatis.operations.controller.request.OperationCreationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationLigneModificationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationModificationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationSelectionRequestDto;
import fr.colline.monatis.operations.controller.response.CompatibilitesResponseDto;
import fr.colline.monatis.operations.controller.response.OperationResponseDto;
import fr.colline.monatis.operations.controller.response.TypeOperationResponseDto;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.references.model.Beneficiaire;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/operations")
@Transactional
public class OperationController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private OperationService operationService;

	@GetMapping("/all")
	public List<OperationResponseDto> getAllOperation() throws ServiceException, ControllerException {

		List<OperationResponseDto> dto = new ArrayList<>();
		Sort tri = Sort.by("dateValeur");
		List<Operation> liste = operationService.rechercherTous(tri);
		for ( Operation operation : liste ) {
			dto.add(OperationResponseDtoMapper.mapperModelToBasicResponseDto(operation));
		}
		return dto;
	}

	@GetMapping("/get/{numero}")
	public OperationResponseDto getOperationParNumero(@PathVariable String numero) throws ControllerException, ServiceException {

		Operation operation = verificateur.verifierOperation(
				numero, 
				OBLIGATOIRE);
		return OperationResponseDtoMapper.mapperModelToDetailedResponseDto(operation);
	}

	@PostMapping("/new")
	public OperationResponseDto creerOperation(@RequestBody OperationCreationRequestDto dto) throws ControllerException, ServiceException {

		Operation operation = new Operation();
		operation = mapperCreationRequestDtoToModel(dto, operation);
		operation = operationService.creerOperation(operation);
		return OperationResponseDtoMapper.mapperModelToSimpleResponseDto(operation);
	}

	@PutMapping("/mod/{numero}")
	public OperationResponseDto modifierOperation(
			@PathVariable String numero, 
			@RequestBody OperationModificationRequestDto dto) throws ControllerException, ServiceException {

		Operation operation = verificateur.verifierOperation(numero, OBLIGATOIRE);
		operation = mapperModificationRequestDtoToModel(dto, operation);
		operation = operationService.modifierOperation(operation);
		return OperationResponseDtoMapper.mapperModelToSimpleResponseDto(operation);
	}

	@DeleteMapping("/del/{numero}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerOperation(@PathVariable String numero) throws ControllerException, ServiceException {

		Operation operation = verificateur.verifierOperation(numero, OBLIGATOIRE);
		operationService.supprimerOperation(operation);
	}
	
	@GetMapping("/typologie/operation")
	public List<TypeOperationResponseDto> getTypesOperations() {
		
		return Arrays.asList(TypeOperation.values())
				.stream()
				.map((to) -> {return OperationResponseDtoMapper.mapperTypeOperation(to);})
				.toList();
	}

	@GetMapping("/selection/dernieres_par_compte")
	public List<OperationResponseDto> selectionnnerDernieresOperationsParCompte(
			@RequestBody OperationSelectionRequestDto requestDto) throws ServiceException, ControllerException {

		Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte, OBLIGATOIRE);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateValeurFin, FACULTATIF, LocalDate.now());
		long limite = requestDto.nombreLimite == null ? 5L : requestDto.nombreLimite; 
				
		return operationService.rechercherDernieresParCompte(compte, dateFin, limite)
				.stream()
				.map((o) -> {return OperationResponseDtoMapper.mapperModelToBasicResponseDto(o);})
				.toList();
	}

	@GetMapping("/compatibilite/comptes/{codeTypeOperationChoisi}") 
	public CompatibilitesResponseDto getComptesCompatiblesTypeOperation(
			@PathVariable String codeTypeOperationChoisi) throws ServiceException, ControllerException {
		
		TypeOperation typeOperationChoisi = verificateur.verifierTypeOperation(codeTypeOperationChoisi, OBLIGATOIRE, null);
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.comptesCompatiblesDepense = new ArrayList<CompteResponseDto>();
		for ( Compte compte : operationService.rechercherComptesCompatiblesDepense(typeOperationChoisi) ) {
			if ( compte.getTypeCompte() == TypeCompte.TECHNIQUE ) {
				dto.comptesCompatiblesDepense = null;
				break;
			}
			dto.comptesCompatiblesDepense.add(CompteResponseDtoMapper.mapperModelToBasicResponseDto(compte));
		}
		
		dto.comptesCompatiblesRecette = new ArrayList<CompteResponseDto>();
		for ( Compte compte : operationService.rechercherComptesCompatiblesRecette(typeOperationChoisi) ) {
			if ( compte.getTypeCompte() == TypeCompte.TECHNIQUE ) {
				dto.comptesCompatiblesRecette = null;
				break;
			}
			dto.comptesCompatiblesRecette.add(CompteResponseDtoMapper.mapperModelToBasicResponseDto(compte));
		}
		
		return dto;
	}

	@GetMapping("/compatibilite/typesoperations/{identifiantCompteChoisi}") 
	public CompatibilitesResponseDto getTypesOperationsCompatiblesCompte(
			@PathVariable String identifiantCompteChoisi) throws ServiceException, ControllerException {
		
		Compte compteChoisi = verificateur.verifierCompte(identifiantCompteChoisi, OBLIGATOIRE);
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.typesOperationsCompatiblesDepense = operationService.rechercherTypesOperationCompatiblesDepense(compteChoisi)
				.stream()
				.map((to) -> {return OperationResponseDtoMapper.mapperTypeOperation(to);})
				.toList();
		
		dto.typesOperationsCompatiblesRecette = operationService.rechercherTypesOperationCompatiblesRecette(compteChoisi)
				.stream()
				.map((to) -> {return OperationResponseDtoMapper.mapperTypeOperation(to);})
				.toList();

		return dto;
	}
	
	@GetMapping("/compatibilite/comptes/depense/{codeTypeOperationChoisi}/{identifiantCompteChoisiRecette}") 
	public CompatibilitesResponseDto getComptesDepenseCompatiblesTypeOperationCompteRecette(
			@PathVariable String codeTypeOperationChoisi,
			@PathVariable String identifiantCompteChoisiRecette) throws ServiceException, ControllerException {
		
		TypeOperation typeOperationChoisi = verificateur.verifierTypeOperation(codeTypeOperationChoisi, OBLIGATOIRE, null);
		Compte compteChoisiRecette = verificateur.verifierCompte(identifiantCompteChoisiRecette, OBLIGATOIRE);
		
		if ( ! operationService.verifierCompatibiliteRecette(compteChoisiRecette, typeOperationChoisi) ) {
			throw new ControllerException(
					OperationControleErreur.INCOMPATIBILITE_ENTRE_TYPE_OPERATION_CHOISI_ET_COMPTE_CHOISI_RECETTE,
					typeOperationChoisi.getCode(),
					compteChoisiRecette.getIdentifiant());
		}
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.comptesCompatiblesDepense = new ArrayList<CompteResponseDto>();
		for ( Compte compte : operationService.rechercherComptesCompatiblesDepense(typeOperationChoisi) ) {
			if ( compte.getTypeCompte() == TypeCompte.TECHNIQUE ) {
				dto.comptesCompatiblesDepense = null;
				break;
			}
			if ( compte.getId().equals(compteChoisiRecette.getId()) ) {
				continue;
			}
			dto.comptesCompatiblesDepense.add(CompteResponseDtoMapper.mapperModelToBasicResponseDto(compte));
		}
		
		return dto;
	}

	@GetMapping("/compatibilite/comptes/recette/{codeTypeOperationChoisi}/{identifiantCompteChoisiDepense}") 
	public CompatibilitesResponseDto getComptesRecetteCompatiblesTypeOperationCompteDepense(
			@PathVariable String codeTypeOperationChoisi,
			@PathVariable String identifiantCompteChoisiDepense) throws ServiceException, ControllerException {
		
		TypeOperation typeOperationChoisi = verificateur.verifierTypeOperation(codeTypeOperationChoisi, OBLIGATOIRE, null);
		Compte compteChoisiDepense = verificateur.verifierCompte(identifiantCompteChoisiDepense, OBLIGATOIRE);
		
		if ( ! operationService.verifierCompatibiliteDepense(compteChoisiDepense, typeOperationChoisi) ) {
			throw new ControllerException(
					OperationControleErreur.INCOMPATIBILITE_ENTRE_TYPE_OPERATION_CHOISI_ET_COMPTE_CHOISI_DEPENSE,
					typeOperationChoisi.getCode(),
					compteChoisiDepense.getIdentifiant());
		}
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.comptesCompatiblesRecette = new ArrayList<CompteResponseDto>();
		for ( Compte compte : operationService.rechercherComptesCompatiblesRecette(typeOperationChoisi) ) {
			if ( compte.getTypeCompte() == TypeCompte.TECHNIQUE ) {
				dto.comptesCompatiblesRecette = null;
				break;
			}
			if ( compte.getId().equals(compteChoisiDepense.getId()) ) {
				continue;
			}
			dto.comptesCompatiblesRecette.add(CompteResponseDtoMapper.mapperModelToBasicResponseDto(compte));
		}
		
		return dto;
	}

	private Operation mapperCreationRequestDtoToModel(
			OperationCreationRequestDto dto, 
			Operation operation) throws ControllerException, ServiceException {

		// Préparation nouvelle opération
		//
		operation.setNumero(verificateur.verifierNumeroValideEtUnique(dto.numero, operation.getId(), FACULTATIF));
		operation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		operation.setTypeOperation(verificateur.verifierTypeOperation(dto.codeTypeOperation, OBLIGATOIRE, null));
		operation.setDateValeur(verificateur.verifierDate(dto.dateValeur, FACULTATIF, LocalDate.now()));
		operation.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));
		operation.setCompteDepense(verificateur.verifierCompte(dto.identifiantCompteDepense, OBLIGATOIRE));
		operation.setCompteRecette(verificateur.verifierCompte(dto.identifiantCompteRecette, OBLIGATOIRE));
		operation.setPointee(Boolean.FALSE);

		// Préparation de la première ligne de détail
		//
		OperationLigne ligne = new OperationLigne();
		
		ligne.setOperation(operation);
		operation.getLignes().add(ligne);

		ligne.setNumeroLigne(0);
		ligne.setLibelle(operation.getLibelle());
		ligne.setDateComptabilisation(operation.getDateValeur());
		ligne.setMontantEnCentimes(operation.getMontantEnCentimes());
		ligne.setSousCategorie(verificateur.verifierSousCategorie(dto.nomSousCategorie, FACULTATIF));
		Set<Beneficiaire> beneficiaires = new HashSet<>();
		if ( dto.nomsBeneficiaires != null ) {
			for ( String nomBeneficiaire : dto.nomsBeneficiaires ) {
				beneficiaires.add(verificateur.verifierBeneficiaire(nomBeneficiaire, OBLIGATOIRE));
			}
		}
		ligne.changerBeneficiaires(beneficiaires);

		return operation;
	}

	private Operation mapperModificationRequestDtoToModel(
			OperationModificationRequestDto dto, 
			Operation operation) throws ControllerException, ServiceException {

		// Modification de l'opération
		//
		if ( dto.numero != null ) operation.setNumero(verificateur.verifierNumeroValideEtUnique(dto.numero, operation.getId(), OBLIGATOIRE));
		if ( dto.codeTypeOperation != null ) operation.setTypeOperation(verificateur.verifierTypeOperation(dto.codeTypeOperation, OBLIGATOIRE, null));
		if ( dto.libelle != null ) operation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		if ( dto.dateValeur != null ) operation.setDateValeur(verificateur.verifierDate(dto.dateValeur, FACULTATIF, LocalDate.now()));
		if ( dto.montantEnCentimes != null ) operation.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));
		if ( dto.identifiantCompteDepense != null ) operation.setCompteDepense(verificateur.verifierCompte(dto.identifiantCompteDepense, OBLIGATOIRE));
		if ( dto.identifiantCompteRecette != null ) operation.setCompteRecette(verificateur.verifierCompte(dto.identifiantCompteRecette, OBLIGATOIRE));
		if ( dto.pointee != null ) operation.setPointee(dto.pointee);
		if ( dto.lignes != null && !dto.lignes.isEmpty() ) {
			operation = mapperModificationRequestDtoToModel(dto.lignes, operation);
		}

		return operation;
	}

	private Operation mapperModificationRequestDtoToModel(
			List<OperationLigneModificationRequestDto> listeDto,
			Operation operation) throws ControllerException, ServiceException {

		int dernierNumeroLigne = rechercherDernierNumeroLigne(operation.getLignes());

		Set<OperationLigne> nouvellesLignes = new HashSet<>();
		
		for ( OperationLigneModificationRequestDto dto : listeDto ) {
			OperationLigne ligne;
			if ( dto.numeroLigne == null ) {
				// Nouvelle ligne d'opération à créer
				ligne = new OperationLigne();
				ligne.setOperation(operation);
				ligne.setNumeroLigne(++dernierNumeroLigne);
				ligne.setDateComptabilisation(verificateur.verifierDate(dto.dateComptabilisation, FACULTATIF, operation.getDateValeur()));
				ligne.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, operation.getLibelle()));
				ligne.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));
				ligne.setSousCategorie((verificateur.verifierSousCategorie(dto.nomSousCategorie, FACULTATIF)));
				Set<Beneficiaire> beneficiaires = new HashSet<>();
				if ( dto.nomsBeneficiaires != null ) {
					for ( String nomBeneficiaire : dto.nomsBeneficiaires ) {
						beneficiaires.add(verificateur.verifierBeneficiaire(nomBeneficiaire, OBLIGATOIRE));
					}
				}
				ligne.changerBeneficiaires(beneficiaires);
			}
			else {
				// Ligne existante : on va la chercher et on la modifie (ou pas)
				ligne = rechercherParNumeroLigne(operation.getLignes(), dto.numeroLigne);
				if ( ligne == null ) {
					throw new ControllerException(
							OperationControleErreur.NON_TROUVE_PAR_NUMERO_LIGNE,
							operation.getNumero(),
							dto.numeroLigne);
				}
				
				if ( dto.libelle != null ) ligne.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
				if ( dto.dateComptabilisation != null ) ligne.setDateComptabilisation(verificateur.verifierDate(dto.dateComptabilisation, FACULTATIF, operation.getDateValeur()));
				if ( dto.montantEnCentimes != null ) ligne.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));
				if ( dto.nomSousCategorie != null ) ligne.setSousCategorie(verificateur.verifierSousCategorie(dto.nomSousCategorie, FACULTATIF));
				if ( dto.nomsBeneficiaires != null ) {
					Set<Beneficiaire> beneficiaires = new HashSet<>();
					for ( String nomBeneficiaire : dto.nomsBeneficiaires ) {
						beneficiaires.add(verificateur.verifierBeneficiaire(nomBeneficiaire, OBLIGATOIRE));
					}
					ligne.changerBeneficiaires(beneficiaires);
				}
			}
			nouvellesLignes.add(ligne);
		}

		// Mise à jour de l'opération avec l'enregistrement des nouvelles lignes et la suppression des anciennes lignes 
		// non reprises dans la liste des dto
		operation.changerLignes(nouvellesLignes);
		
		return operation;
	}

	private int rechercherDernierNumeroLigne(Set<OperationLigne> lignes) {

		int numeroLigne = 0;
		for ( OperationLigne ligne : lignes ) {
			numeroLigne = Math.max(numeroLigne, ligne.getNumeroLigne());
		}
		return numeroLigne;
	}

	private OperationLigne rechercherParNumeroLigne(Set<OperationLigne> lignes, int numeroLigne) {
	
		for ( OperationLigne ligne : lignes ) {
			if ( ligne.getNumeroLigne() == numeroLigne ) {
				return ligne;
			}
		}
		return null;
	}

}
