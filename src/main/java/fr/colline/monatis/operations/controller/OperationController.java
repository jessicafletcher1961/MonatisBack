package fr.colline.monatis.operations.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationControleErreur;
import fr.colline.monatis.operations.controller.request.OperationBaseRequestDto;
import fr.colline.monatis.operations.controller.request.OperationCompleteRequestDto;
import fr.colline.monatis.operations.controller.request.OperationCreationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationLigneRequestDto;
import fr.colline.monatis.operations.controller.request.OperationModificationRequestDto;
import fr.colline.monatis.operations.controller.response.OperationDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.OperationSimpleResponseDto;
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
	public List<OperationSimpleResponseDto> getAllOperation() throws ServiceException, ControllerException {

		List<OperationSimpleResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("dateValeur");
		List<Operation> liste = operationService.rechercherTous(tri);
		for ( Operation operation : liste ) {
			resultat.add(OperationResponseDtoMapper.mapperModelToSimpleResponseDto(operation));
		}
		return resultat;
	}

	@GetMapping("/get/{numero}")
	public OperationDetailedResponseDto getOperationParNumero(@PathVariable String numero) throws ControllerException, ServiceException {

		Operation operation = verificateur.verifierOperation(
				numero, 
				OBLIGATOIRE);
		return OperationResponseDtoMapper.mapperModelToDetailedResponseDto(operation);
	}

	@PostMapping("/new")
	public OperationDetailedResponseDto creerOperation(@RequestBody OperationCreationRequestDto dto) throws ControllerException, ServiceException {

		verifierTypeOperationAutorise(TypeOperation.findByCode(dto.codeTypeOperation));
		
		Operation operation = new Operation();
		operation = mapperCreationRequestDtoToModel(dto, operation);
		operation = operationService.creerOperation(operation);
		return OperationResponseDtoMapper.mapperModelToDetailedResponseDto(operation);
	}

	@PutMapping("/mod/{numero}")
	public OperationDetailedResponseDto modifierOperation(
			@PathVariable String numero, 
			@RequestBody OperationModificationRequestDto dto) throws ControllerException, ServiceException {

		Operation operation = verificateur.verifierOperation(numero, OBLIGATOIRE);
		verifierTypeOperationAutorise(operation.getTypeOperation());
		operation = mapperModificationRequestDtoToModel(dto, operation);
		operation = operationService.modifierOperation(operation);
		return OperationResponseDtoMapper.mapperModelToDetailedResponseDto(operation);
	}

	@DeleteMapping("/del/{numero}")
	public void supprimerOperation(@PathVariable String numero) throws ControllerException, ServiceException {

		Operation operation = verificateur.verifierOperation(numero, OBLIGATOIRE);
		verifierTypeOperationAutorise(operation.getTypeOperation());
		operationService.supprimerOperation(operation);
	}

	@PostMapping("/transfert")
	public OperationDetailedResponseDto effectuerTransfert(@RequestBody OperationBaseRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationBaseCreationRequestDto(TypeOperation.TRANSFERT, requestDto));
	}

	@PostMapping("/depot")
	public OperationDetailedResponseDto effectuerDepot(@RequestBody OperationBaseRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationBaseCreationRequestDto(TypeOperation.DEPOT, requestDto));
	}

	@PostMapping("/retrait")
	public OperationDetailedResponseDto effectuerRetrait(@RequestBody OperationBaseRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationBaseCreationRequestDto(TypeOperation.RETRAIT, requestDto));
	}

	@PostMapping("/achat")
	public OperationDetailedResponseDto effectuerAchat(@RequestBody OperationBaseRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationBaseCreationRequestDto(TypeOperation.ACHAT, requestDto));
	}

	@PostMapping("/vente")
	public OperationDetailedResponseDto effectuerVente(@RequestBody OperationBaseRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationBaseCreationRequestDto(TypeOperation.VENTE, requestDto));
	}

	@PostMapping("/recette")
	public OperationDetailedResponseDto effectuerRecette(@RequestBody OperationCompleteRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationCompleteCreationRequestDto(TypeOperation.RECETTE, requestDto));
	}

	@PostMapping("/depense")
	public OperationDetailedResponseDto effectuerDepense(@RequestBody OperationCompleteRequestDto requestDto) throws ControllerException, ServiceException {

		return creerOperation(calculerOperationCompleteCreationRequestDto(TypeOperation.DEPENSE, requestDto));
	}

	private Operation mapperCreationRequestDtoToModel(
			OperationCreationRequestDto dto, 
			Operation operation) throws ControllerException, ServiceException {

		// Préparation nouvelle opération
		//
		operation.setNumero(verificateur.verifierNumeroValideEtUnique(dto.numero, operation.getId(), FACULTATIF));
		operation.setTypeOperation(verificateur.verifierTypeOperation(dto.codeTypeOperation, OBLIGATOIRE, null));
		operation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		operation.setDateValeur(verificateur.verifierDate(dto.dateValeur, FACULTATIF, LocalDate.now()));
		operation.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));
		operation.setCompteDepense(verificateur.verifierCompte(dto.identifiantCompteDepense, OBLIGATOIRE));
		operation.setCompteRecette(verificateur.verifierCompte(dto.identifiantCompteRecette, OBLIGATOIRE));

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
		
		operation = mapperModificationRequestDtoToModel(operation, dto.lignes);

		return operation;
	}

	private Operation mapperModificationRequestDtoToModel(
			Operation operation, 
			List<OperationLigneRequestDto> listeDto) throws ControllerException, ServiceException {

		int dernierNumeroLigne = rechercherDernierNumeroLigne(operation.getLignes());

		Set<OperationLigne> nouvellesLignes = new HashSet<>();
		
		for ( OperationLigneRequestDto dto : listeDto ) {
			OperationLigne ligne;
			if ( dto.numeroLigne == null ) {
				// Nouvelle ligne d'opération
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
	
	private OperationCreationRequestDto calculerOperationBaseCreationRequestDto(
			TypeOperation typeOperation, 
			OperationBaseRequestDto requestDto) throws ServiceException, ControllerException {

		OperationCreationRequestDto dto = new OperationCreationRequestDto();
		
		dto.codeTypeOperation = typeOperation.getCode();
		dto.numero = requestDto.numeroOperation;
		dto.dateValeur = requestDto.dateOperation;
		dto.identifiantCompteDepense = requestDto.identifiantCompteDepense;
		dto.identifiantCompteRecette = requestDto.identifiantCompteRecette;
		dto.libelle = requestDto.libelleOperation;
		dto.montantEnCentimes = requestDto.montantOperationEnCentimes;
		dto.nomsBeneficiaires = null;
		dto.nomSousCategorie = null;

		return dto;
	}

	private OperationCreationRequestDto calculerOperationCompleteCreationRequestDto(
			TypeOperation typeOperation,
			OperationCompleteRequestDto requestDto) throws ServiceException, ControllerException {
		
		OperationCreationRequestDto dto = calculerOperationBaseCreationRequestDto(typeOperation, requestDto);
	
		dto.nomSousCategorie = requestDto.nomSousCategorie;
		dto.nomsBeneficiaires = requestDto.nomsBeneficiaires;
		
		return dto;
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

	private void verifierTypeOperationAutorise(TypeOperation typeOperation) throws ControllerException {
		
		if ( typeOperation != null ) {
			if ( typeOperation == TypeOperation.PLUS_SOLDE || typeOperation == TypeOperation.MOINS_SOLDE ) {
				throw new ControllerException(
						OperationControleErreur.TYPE_OPERATION_INTERDIT, 
						typeOperation.getCode(), 
						typeOperation.getLibelle());
			}
		}
	}
}
