package fr.colline.monatis.operations.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationControleErreur;
import fr.colline.monatis.operations.controller.request.OperationCreationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationLigneModificationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationModificationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationRequestDto;
import fr.colline.monatis.operations.controller.response.OperationResponseDto;
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

		List<OperationResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("dateValeur");
		List<Operation> liste = operationService.rechercherTous(tri);
		for ( Operation operation : liste ) {
			resultat.add(OperationResponseDtoMapper.mapperModelToBasicResponseDto(operation));
		}
		return resultat;
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

	@PostMapping("/transfert")
	public OperationResponseDto effectuerTransfert(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		OperationResponseDto operationTransfert = creerOperationTransfert(
				requestDto.numero, 
				requestDto.libelle, 
				requestDto.dateValeur,
				requestDto.montantEnCentimes, 
				requestDto.identifiantCompteCourantRecette, 
				requestDto.identifiantCompteCourantDepense);
		
		return operationTransfert;
	}

	@PostMapping("/depense")
	public OperationResponseDto effectuerDepense(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		OperationResponseDto operationDepense = creerOperationDepense(
				requestDto.numero,
				requestDto.libelle,
				requestDto.dateValeur,
				requestDto.montantEnCentimes,
				requestDto.identifiantCompteExterne,
				requestDto.identifiantCompteCourant,
				requestDto.nomSousCategorie,
				requestDto.nomsBeneficiaires);

		return operationDepense;
	}

	@PostMapping("/recette")
	public OperationResponseDto effectuerRecette(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		OperationResponseDto operationRecette = creerOperationRecette(
				requestDto.numero,
				requestDto.libelle,
				requestDto.dateValeur,
				requestDto.montantEnCentimes,
				requestDto.identifiantCompteExterne,
				requestDto.identifiantCompteCourant,
				requestDto.nomSousCategorie,
				requestDto.nomsBeneficiaires);

		return operationRecette;
	}

	@PostMapping("/vente")
	public OperationResponseDto effectuerVente(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		// On fait un versement au vendeur
		creerOperationRecette(
				null,
				requestDto.libelle,
				requestDto.dateValeur,
				requestDto.montantEnCentimes,
				requestDto.identifiantCompteExterne,
				requestDto.identifiantCompteCourant,
				requestDto.nomSousCategorie,
				requestDto.nomsBeneficiaires);
		
		// Le vendeur fournit le bien
		OperationResponseDto operationVente = creerOperationVente(
				requestDto.numero,
				requestDto.libelle,
				requestDto.dateValeur,
				requestDto.montantEnCentimes,
				requestDto.identifiantCompteBien,
				requestDto.identifiantCompteExterne);
		
		return operationVente;
	}

	@PostMapping("/achat")
	public OperationResponseDto effectuerAchat(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		creerOperationDepense(
				null,
				requestDto.libelle,
				requestDto.dateValeur,
				requestDto.montantEnCentimes,
				requestDto.identifiantCompteExterne,
				requestDto.identifiantCompteCourant,
				requestDto.nomSousCategorie,
				requestDto.nomsBeneficiaires);
		
		OperationResponseDto operationAchat = creerOperationAchat(
				requestDto.numero,
				requestDto.libelle,
				requestDto.dateValeur,
				requestDto.montantEnCentimes,
				requestDto.identifiantCompteBien,
				requestDto.identifiantCompteExterne);
		
		return operationAchat;
	}

	@PostMapping("/retrait")
	public OperationResponseDto effectuerRetrait(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		OperationResponseDto operationRetrait = creerOperationRetrait(
				requestDto.numero, 
				requestDto.libelle, 
				requestDto.dateValeur,
				requestDto.montantEnCentimes, 
				requestDto.identifiantCompteFinancier, 
				requestDto.identifiantCompteCourant);
		
		return operationRetrait;
	}
	
	@PostMapping("/liquidation")
	public OperationResponseDto effectuerLiquidation(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		OperationResponseDto operationLiquidation = creerOperationLiquidation(
				requestDto.numero, 
				requestDto.libelle, 
				requestDto.dateValeur,
				requestDto.montantEnCentimes, 
				requestDto.identifiantCompteFinancier, 
				requestDto.identifiantCompteCourant);
		
		return operationLiquidation;
	}
	
	@PostMapping("/depot")
	public OperationResponseDto effectuerDepot(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {

		OperationResponseDto operationDepot = creerOperationDepot(
				requestDto.numero, 
				requestDto.libelle, 
				requestDto.dateValeur,
				requestDto.montantEnCentimes, 
				requestDto.identifiantCompteFinancier, 
				requestDto.identifiantCompteCourant);
		
		return operationDepot;
	}
	
	@PostMapping("/investissement")
	public OperationResponseDto effectuerInvestissement(@RequestBody OperationRequestDto requestDto) throws ControllerException, ServiceException {


		OperationResponseDto operationInvestissement = creerOperationInvestissement(
				requestDto.numero, 
				requestDto.libelle, 
				requestDto.dateValeur,
				requestDto.montantEnCentimes, 
				requestDto.identifiantCompteFinancier, 
				requestDto.identifiantCompteCourant);
		
		return operationInvestissement;
	}
	
	private OperationResponseDto creerOperationDepense(
			String numero,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			String identifiantCompteExterne,
			String identifiantCompteCourant,
			String nomSousCategorie,
			List<String> nomsBeneficiaires) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.DEPENSE.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteCourant;
		dto.identifiantCompteRecette = identifiantCompteExterne;
		dto.nomSousCategorie = nomSousCategorie;
		dto.nomsBeneficiaires = nomsBeneficiaires;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationRecette(
			String numero, 
			String libelle, 
			LocalDate dateValeur,
			Long montantEnCentimes, 
			String identifiantCompteExterne, 
			String identifiantCompteCourant,
			String nomSousCategorie, 
			List<String> nomsBeneficiaires) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.RECETTE.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteExterne;
		dto.identifiantCompteRecette = identifiantCompteCourant;
		dto.nomSousCategorie = nomSousCategorie;
		dto.nomsBeneficiaires = nomsBeneficiaires;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationTransfert(
			String numero, 
			String libelle, 
			LocalDate dateValeur,
			Long montantEnCentimes, 
			String identifiantCompteCourantRecette, 
			String identifiantCompteCourantDepense) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.TRANSFERT.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteCourantDepense;
		dto.identifiantCompteRecette = identifiantCompteCourantRecette;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationAchat(
			String numero,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			String identifiantComptePatrimoine,
			String identifiantCompteExterne) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.ACHAT.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteExterne;
		dto.identifiantCompteRecette = identifiantComptePatrimoine;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationVente(
			String numero,
			String libelle, 
			LocalDate dateValeur, 
			Long montantEnCentimes,
			String identifiantComptePatrimoine, 
			String identifiantCompteExterne) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.VENTE.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantComptePatrimoine;
		dto.identifiantCompteRecette = identifiantCompteExterne;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationDepot(
			String numero,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			String identifiantCompteEpargne,
			String identifiantCompteCourant) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.DEPOT.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteCourant;
		dto.identifiantCompteRecette = identifiantCompteEpargne;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationInvestissement(
			String numero,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			String identifiantCompteEpargne,
			String identifiantCompteCourant) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.INVESTISSEMENT.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteCourant;
		dto.identifiantCompteRecette = identifiantCompteEpargne;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationRetrait(
			String numero,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			String identifiantCompteEpargne,
			String identifiantCompteCourant) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.RETRAIT.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteEpargne;
		dto.identifiantCompteRecette = identifiantCompteCourant;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
	}

	private OperationResponseDto creerOperationLiquidation(
			String numero,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			String identifiantCompteEpargne,
			String identifiantCompteCourant) throws ControllerException, ServiceException {
		
		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.numero = numero;
		dto.libelle = libelle;
		dto.codeTypeOperation = TypeOperation.LIQUIDATION.getCode();
		dto.dateValeur = dateValeur;
		dto.montantEnCentimes = montantEnCentimes;
		dto.identifiantCompteDepense = identifiantCompteEpargne;
		dto.identifiantCompteRecette = identifiantCompteCourant;
		dto.nomSousCategorie = null;
		dto.nomsBeneficiaires = null;
		
		return creerOperation(dto);
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
			operation = mapperModificationRequestDtoToModel(operation, dto.lignes);
		}

		return operation;
	}

	private Operation mapperModificationRequestDtoToModel(
			Operation operation, 
			List<OperationLigneModificationRequestDto> listeDto) throws ControllerException, ServiceException {

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
