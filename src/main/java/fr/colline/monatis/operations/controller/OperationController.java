package fr.colline.monatis.operations.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

import fr.colline.monatis.admin.AdminControleErreur;
import fr.colline.monatis.admin.AdminController;
import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.comptes.controller.CompteResponseDtoMapper;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationControleErreur;
import fr.colline.monatis.operations.OperationTechniqueErreur;
import fr.colline.monatis.operations.controller.request.OperationCreationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationExempleRequestDto;
import fr.colline.monatis.operations.controller.request.OperationLigneModificationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationModificationRequestDto;
import fr.colline.monatis.operations.controller.request.OperationPageRequestDto;
import fr.colline.monatis.operations.controller.response.CompatibilitesResponseDto;
import fr.colline.monatis.operations.controller.response.OperationPageResponseDto;
import fr.colline.monatis.operations.controller.response.OperationResponseDto;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.service.CompatibiliteService;
import fr.colline.monatis.operations.service.OperationFiltreConstructeur;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDtoMapper;
import fr.colline.monatis.typologies.model.TypeCompte;
import fr.colline.monatis.typologies.model.TypeOperation;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/operations")
@Transactional
public class OperationController {

	private final int TAILLE_PAGE_OPERATION_PAR_DEFAUT = 25;
	private final int TAILLE_PAGE_SUGGESTIONS = 5;
	private final int TAILLE_PAGE_DOUBLONS = 1;
	
	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private OperationService operationService;
	@Autowired private CompatibiliteService compatibiliteService;

	@GetMapping("/all")
	public List<OperationResponseDto> getAllOperation() throws ServiceException, ControllerException {

		return operationService.rechercherTous()
				.sorted((o1, o2) -> {return o1.getNumero().compareTo(o2.getNumero());})
				.map((o) -> {return OperationResponseDtoMapper.mapperModelToBasicResponseDto(o);})
				.toList();
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
	
	@PostMapping("/page")
	public OperationPageResponseDto paginerOperations(
			@RequestBody OperationPageRequestDto requestDto) throws ServiceException, ControllerException {

		final int numeroPage = verificateur.verifierNumeroPage(requestDto.numeroPage, FACULTATIF, 0);
		final int taillePage = verificateur.verifierTaillePage(requestDto.taillePage, FACULTATIF, TAILLE_PAGE_OPERATION_PAR_DEFAUT);
	    
		PageRequest pagination = PageRequest.of(
	            numeroPage, // MODIFIE: API en base 1 convertie en base 0 Spring.
	            taillePage, // MODIFIE: borne min/max appliquee.
	            Sort.by(Sort.Direction.DESC, "dateValeur")
	                .and(Sort.by(Sort.Direction.DESC, "id"))); // MODIFIE: tri stable.
		
		String recherche = verificateur.verifierLibelle(requestDto.recherche, FACULTATIF, null);
		
		String numero = verificateur.standardiserIdentifiantFonctionnel(requestDto.numeroContient);
		LocalDate dateCreation = verificateur.verifierDate(requestDto.dateCreationApproximative, FACULTATIF, null);
		LocalDate dateValeurDebut = verificateur.verifierDate(requestDto.dateValeurDepuisLe, FACULTATIF, null);
		LocalDate dateValeurFin = verificateur.verifierDate(requestDto.dateValeurJusqueAu, FACULTATIF, null);
		Boolean pointee = verificateur.verifierBoolean(requestDto.pointee, FACULTATIF, null);
		Long montantPlancher = verificateur.verifierMontantEnCentimes(requestDto.montantEnCentimesPlancher, FACULTATIF, null);
		Long montantPlafond = verificateur.verifierMontantEnCentimes(requestDto.montantEnCentimesPlafond, FACULTATIF, null);

		String libelle = verificateur.verifierLibelle(requestDto.libelleContient, FACULTATIF, null);
		if ( libelle != null && !libelle.isBlank() && !libelle.matches("[0-9 ]+") ) {
			libelle = libelle.replaceAll("\\d+", "%");
		}

		Long compte1Id = null;
		if ( requestDto.compte1Id != null ) {
			if ( verificateur.verifierExistenceCompte(requestDto.compte1Id) ) {
				compte1Id = requestDto.compte1Id;
			}
		}
		if ( compte1Id == null && requestDto.identifiantCompte1 != null ) 
		{
			Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte1, FACULTATIF);
			compte1Id = compte == null ? null : compte.getId();
		}

		Long compte2Id = null;
		if ( requestDto.compte2Id != null ) {
			if ( verificateur.verifierExistenceCompte(requestDto.compte2Id) ) {
				compte2Id = requestDto.compte2Id;
			}
		}
		if ( compte2Id == null && requestDto.identifiantCompte2 != null ) 
		{
			Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte2, FACULTATIF);
			compte2Id = compte == null ? null : compte.getId();
		}

		Set<Long> sousCategoriesIds = new HashSet<Long>();
		if ( requestDto.sousCategoriesIds != null && !requestDto.sousCategoriesIds.isEmpty() ) {
			for ( Long id : requestDto.sousCategoriesIds ) {
				if ( verificateur.verifierExistenceSousCategorie(id) ) {
					sousCategoriesIds.add(id);
				}
			}
		}
		if ( sousCategoriesIds.isEmpty() && requestDto.nomsSousCategories != null && !requestDto.nomsSousCategories.isEmpty() ) {
			for ( String nom : requestDto.nomsSousCategories ) {
				SousCategorie sousCategorie = verificateur.verifierSousCategorie(nom, FACULTATIF);
				if ( sousCategorie != null ) sousCategoriesIds.add(sousCategorie.getId());
			}
		}
		
		Set<Long> beneficiairesIds = new HashSet<Long>();
		if ( requestDto.beneficiairesIds != null && !requestDto.beneficiairesIds.isEmpty() ) {
			for ( Long id : requestDto.beneficiairesIds ) {
				if ( verificateur.verifierExistenceBeneficiaire(id) ) {
					beneficiairesIds.add(id);
				}
			}
		}
		if ( beneficiairesIds.isEmpty() && requestDto.nomsBeneficiaires != null && !requestDto.nomsBeneficiaires.isEmpty() ) {
			for ( String nom : requestDto.nomsBeneficiaires ) {
				Beneficiaire beneficiaire = verificateur.verifierBeneficiaire(nom, FACULTATIF);
				if ( beneficiaire != null ) beneficiairesIds.add(beneficiaire.getId());
			}
		}
		 
		Set<TypeOperation> typesOperation = new HashSet<TypeOperation>();
		if ( requestDto.codesTypeOperation != null && !requestDto.codesTypeOperation.isEmpty() ) {
			for ( String codeTypeOperation : requestDto.codesTypeOperation ) {
				typesOperation.add(verificateur.verifierTypeOperation(codeTypeOperation, OBLIGATOIRE, null));
			}
		}

		Specification<Operation> filtre = OperationFiltreConstructeur.initialiserFiltre();
		
		filtre = OperationFiltreConstructeur.ajouterFiltreRecherche(filtre, recherche);
		
		filtre = OperationFiltreConstructeur.ajouterFiltreNumero(filtre, numero);
		filtre = OperationFiltreConstructeur.ajouterFiltreLibelle(filtre, libelle);
		filtre = OperationFiltreConstructeur.ajouterFiltreDateCreationApproximative(filtre, dateCreation);
		filtre = OperationFiltreConstructeur.ajouterFiltreDateValeur(filtre, dateValeurDebut, dateValeurFin);
		filtre = OperationFiltreConstructeur.ajouterFiltreCompte(filtre, compte1Id);
		filtre = OperationFiltreConstructeur.ajouterFiltreCompte(filtre, compte2Id);
		filtre = OperationFiltreConstructeur.ajouterFiltrePointee(filtre, pointee);
		filtre = OperationFiltreConstructeur.ajouterFiltreMontant(filtre, montantPlancher, montantPlafond);
		filtre = OperationFiltreConstructeur.ajouterFiltreSousCCategories(filtre, sousCategoriesIds);
		filtre = OperationFiltreConstructeur.ajouterFiltreBeneficiaires(filtre, beneficiairesIds);
		filtre = OperationFiltreConstructeur.ajouterFiltreTypesOperation(filtre, typesOperation);
		
		Page<Operation> page = operationService.rechercherOperationsParFiltre(
				filtre,
				pagination);
		
	    return OperationResponseDtoMapper.mapperPageToResponseDto(page); // MODIFIE: transforme Page<Operation> en contrat API front-friendly.

	}

	@PostMapping("/suggestions")
	public OperationPageResponseDto paginerSuggestions(
			@RequestBody OperationExempleRequestDto requestDto) throws ControllerException, ServiceException {

		final int numeroPage = verificateur.verifierNumeroPage(requestDto.numeroPage, FACULTATIF, 0);
		final int taillePage = verificateur.verifierTaillePage(requestDto.taillePage, FACULTATIF, TAILLE_PAGE_SUGGESTIONS);

		PageRequest pagination = PageRequest.of(
	            numeroPage,
	            taillePage,
	            Sort.by(Sort.Direction.DESC, "dateValeur")
	                .and(Sort.by(Sort.Direction.DESC, "id")));
	    
		Operation exempleOperation = new Operation();
		
		exempleOperation.setLibelle(verificateur.verifierLibelle(requestDto.libelle, FACULTATIF, null));
		exempleOperation.setCompteRecette(verificateur.verifierCompte(requestDto.identifiantCompteRecette, FACULTATIF));
		exempleOperation.setCompteDepense(verificateur.verifierCompte(requestDto.identifiantCompteDepense, FACULTATIF));
		exempleOperation.setTypeOperation(verificateur.verifierTypeOperation(requestDto.codeTypeOperation, FACULTATIF, null));
		
		Page<Operation> page = operationService.rechercherOperationsParExemple(
				exempleOperation,
				pagination);
		
	    return OperationResponseDtoMapper.mapperPageToResponseDto(page); // MODIFIE: transforme Page<Operation> en contrat API front-friendly.
	}

	@PostMapping("/doublons")
	public OperationPageResponseDto paginerDoublons(
			@RequestBody OperationExempleRequestDto requestDto) throws ControllerException, ServiceException {

		final int numeroPage = verificateur.verifierNumeroPage(requestDto.numeroPage, FACULTATIF, 0);
		final int taillePage = verificateur.verifierTaillePage(requestDto.taillePage, FACULTATIF, TAILLE_PAGE_DOUBLONS);

		PageRequest pagination = PageRequest.of(
	            numeroPage,
	            taillePage,
	            Sort.by(Sort.Direction.DESC, "dateValeur")
	                .and(Sort.by(Sort.Direction.DESC, "id")));
	    
		Operation exempleOperation = new Operation();
		
		exempleOperation.setLibelle(verificateur.verifierLibelle(requestDto.libelle, FACULTATIF, null));
		exempleOperation.setCompteRecette(verificateur.verifierCompte(requestDto.identifiantCompteRecette, FACULTATIF));
		exempleOperation.setCompteDepense(verificateur.verifierCompte(requestDto.identifiantCompteDepense, FACULTATIF));
		exempleOperation.setTypeOperation(verificateur.verifierTypeOperation(requestDto.codeTypeOperation, FACULTATIF, null));
		
		exempleOperation.setDateValeur(verificateur.verifierDate(requestDto.dateValeur, FACULTATIF, null));
		exempleOperation.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(requestDto.montantEnCentimes, FACULTATIF, null));
		
		Page<Operation> page = operationService.rechercherOperationsParExemple(
				exempleOperation,
				pagination);
		
	    return OperationResponseDtoMapper.mapperPageToResponseDto(page); // MODIFIE: transforme Page<Operation> en contrat API front-friendly.
	}

	@GetMapping("/compatibilite/comptes/{codeTypeOperationChoisi}") 
	public CompatibilitesResponseDto getComptesCompatiblesTypeOperation(
			@PathVariable String codeTypeOperationChoisi) throws ServiceException, ControllerException {
		
		TypeOperation typeOperationChoisi = verificateur.verifierTypeOperation(codeTypeOperationChoisi, OBLIGATOIRE, null);
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.comptesCompatiblesDepense = new ArrayList<CompteResponseDto>();
		for ( Compte compte : compatibiliteService.rechercherComptesCompatiblesDepense(typeOperationChoisi) ) {
			if ( compte.getTypeCompte() == TypeCompte.TECHNIQUE ) {
				dto.comptesCompatiblesDepense = null;
				break;
			}
			dto.comptesCompatiblesDepense.add(CompteResponseDtoMapper.mapperModelToBasicResponseDto(compte));
		}
		
		dto.comptesCompatiblesRecette = new ArrayList<CompteResponseDto>();
		for ( Compte compte : compatibiliteService.rechercherComptesCompatiblesRecette(typeOperationChoisi) ) {
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
		
		dto.typesOperationsCompatiblesDepense = compatibiliteService.rechercherTypesOperationCompatiblesDepense(compteChoisi)
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();
		
		dto.typesOperationsCompatiblesRecette = compatibiliteService.rechercherTypesOperationCompatiblesRecette(compteChoisi)
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();

		return dto;
	}
	
	@GetMapping("/compatibilite/comptes/depense/{codeTypeOperationChoisi}/{identifiantCompteChoisiRecette}") 
	public CompatibilitesResponseDto getComptesDepenseCompatiblesTypeOperationCompteRecette(
			@PathVariable String codeTypeOperationChoisi,
			@PathVariable String identifiantCompteChoisiRecette) throws ServiceException, ControllerException {
		
		TypeOperation typeOperationChoisi = verificateur.verifierTypeOperation(codeTypeOperationChoisi, OBLIGATOIRE, null);
		Compte compteChoisiRecette = verificateur.verifierCompte(identifiantCompteChoisiRecette, OBLIGATOIRE);
		
		if ( ! compatibiliteService.isCompatibiliteRecette(compteChoisiRecette, typeOperationChoisi) ) {
			throw new ControllerException(
					OperationControleErreur.INCOMPATIBILITE_ENTRE_TYPE_OPERATION_CHOISI_ET_COMPTE_CHOISI_RECETTE,
					typeOperationChoisi.getCode(),
					compteChoisiRecette.getIdentifiant());
		}
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.comptesCompatiblesDepense = new ArrayList<CompteResponseDto>();
		for ( Compte compte : compatibiliteService.rechercherComptesCompatiblesDepense(typeOperationChoisi) ) {
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
		
		if ( ! compatibiliteService.isCompatibiliteDepense(compteChoisiDepense, typeOperationChoisi) ) {
			throw new ControllerException(
					OperationControleErreur.INCOMPATIBILITE_ENTRE_TYPE_OPERATION_CHOISI_ET_COMPTE_CHOISI_DEPENSE,
					typeOperationChoisi.getCode(),
					compteChoisiDepense.getIdentifiant());
		}
		
		CompatibilitesResponseDto dto = new CompatibilitesResponseDto();
		
		dto.comptesCompatiblesRecette = new ArrayList<CompteResponseDto>();
		for ( Compte compte : compatibiliteService.rechercherComptesCompatiblesRecette(typeOperationChoisi) ) {
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

	@GetMapping("/addcsv/{nomFichierCsv}")
	public List<OperationResponseDto> creerOperationsFromCsv(
			@PathVariable String nomFichierCsv) throws ControllerException, ServiceException {
		
		nomFichierCsv = AdminController.REPERTOIRE_ECHANGE
				+ "/" 
				+ nomFichierCsv;
		File fichierCsv = new File(nomFichierCsv);
		if ( ! fichierCsv.exists() || fichierCsv.isDirectory() ) {
			throw new ControllerException(AdminControleErreur.FICHIER_NON_TROUVE, fichierCsv.getAbsolutePath());
		}

		List<OperationResponseDto> dto = new ArrayList<OperationResponseDto>();
		
		CSVFormat format = CSVFormat.RFC4180.builder().get();
				
		try ( FileReader reader = new FileReader(fichierCsv);
				CSVParser parser = format.parse(reader) ) {
			for (CSVRecord record : parser.getRecords() ) {
				
				OperationCreationRequestDto operationDto = new OperationCreationRequestDto();
				
				operationDto.numero = record.get(0);
				operationDto.libelle = record.get(1);
				operationDto.codeTypeOperation = record.get(2);
				operationDto.dateValeur = LocalDate.parse(record.get(3));
				operationDto.montantEnCentimes = Long.decode(record.get(4));
				operationDto.identifiantCompteDepense = record.get(5);
				operationDto.identifiantCompteRecette = record.get(6);
				if ( record.size() > 6 ) {
					if ( record.get(7) != null && ! record.get(7).isBlank() ) {
						operationDto.nomSousCategorie = record.get(7);
					}
				}
				if ( record.size() > 7 ) {
					operationDto.nomsBeneficiaires = new ArrayList<String>();
					for ( int numeroColonne = 8 ; numeroColonne < record.size() ; numeroColonne++ ) {
						if ( record.get(numeroColonne) != null && ! record.get(numeroColonne).isBlank() ) {
							operationDto.nomsBeneficiaires.add(record.get(numeroColonne));
						}
					}
				}
				
				dto.add(creerOperation(operationDto));
				
			}
		} 
		catch (CSVException e) {
			throw new ControllerException(e, OperationTechniqueErreur.FICHIER_NON_PARSABLE, fichierCsv.getAbsolutePath());
		} 
		catch (IOException e) {
			throw new ControllerException(e, OperationTechniqueErreur.FICHIER_NON_LISIBLE, fichierCsv.getAbsolutePath());
		}
		
		return dto;
	}
	
	@Deprecated(forRemoval = true) //-- A SUPPRIMER POUR V2 
	@GetMapping("/typologie/operation")
	public List<TypologieResponseDto> getTypesOperations() {
		
		return Arrays.asList(TypeOperation.values())
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();
	}

	private Operation mapperCreationRequestDtoToModel(
			OperationCreationRequestDto dto, 
			Operation operation) throws ControllerException, ServiceException {

		// Préparation nouvelle opération
		//
		operation.setNumero(verificateur.verifierNumeroValideEtUnique(dto.numero, operation.getId(), FACULTATIF));
		operation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		operation.setTypeOperation(verificateur.verifierTypeOperation(dto.codeTypeOperation, OBLIGATOIRE, null));
		operation.setDateCreation(verificateur.verifierDate(dto.dateCreation, FACULTATIF, LocalDate.now()));
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
		ligne.setMontantEnCentimes(operation.getMontantEnCentimes());
		
		ligne.setDateComptabilisation(verificateur.verifierDate(dto.dateComptabilisation, FACULTATIF, operation.getDateValeur()));
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
		if ( dto.dateCreation != null ) operation.setDateCreation(verificateur.verifierDate(dto.dateCreation, FACULTATIF, LocalDate.now()));
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
