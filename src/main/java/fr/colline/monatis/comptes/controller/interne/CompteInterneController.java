package fr.colline.monatis.comptes.controller.interne;

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

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Titulaire;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/comptes/interne")
@Transactional
public class CompteInterneController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private CompteInterneService compteInterneService;

	@GetMapping("/all")
	public List<CompteResponseDto> getAllCompte() throws ServiceException {

		List<CompteResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("identifiant");
		List<CompteInterne> liste = compteInterneService.rechercherTous(tri);
		for ( CompteInterne compteInterne : liste ) {
			resultat.add(CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(compteInterne));
		}
		return resultat;
	}

	@GetMapping("/get/{identifiant}")
	public CompteResponseDto getCompteParIdentifiant(
			@PathVariable String identifiant) throws ServiceException, ControllerException {

		CompteInterne compteInterne = (CompteInterne) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.INTERNE, 
				identifiant, 
				OBLIGATOIRE);
		return CompteInterneResponseDtoMapper.mapperModelToDetailedResponseDto(compteInterne);
	}

	@PostMapping("/new")
	public CompteResponseDto creerCompte(
			@RequestBody CompteInterneRequestDto dto) throws ServiceException, ControllerException {
		
		CompteInterne compteInterne = new CompteInterne();
		compteInterne = mapperCreationRequestDtoToModel(dto, compteInterne);
		compteInterne = compteInterneService.creerCompte(compteInterne);
		return CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(compteInterne);
	}

	@PutMapping("/mod/{identifiant}")
	public CompteResponseDto modifierCompte(
			@PathVariable String identifiant,
			@RequestBody CompteInterneRequestDto dto) throws ControllerException, ServiceException {
		
		CompteInterne compteInterne = (CompteInterne) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.INTERNE, 
				identifiant, 
				OBLIGATOIRE);
		compteInterne = mapperModificationRequestDtoToModel(dto, compteInterne);
		compteInterne = compteInterneService.modifierCompte(compteInterne);
		return CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(compteInterne);
	}

	@DeleteMapping("/del/{identifiant}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerCompte(
			@PathVariable String identifiant) throws ControllerException, ServiceException {

		CompteInterne compteInterne = (CompteInterne) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.INTERNE, 
				identifiant, 
				OBLIGATOIRE);
		compteInterneService.supprimerCompte(compteInterne);
	}

	@GetMapping("/{codeTypeFonctionnement}")
	public List<CompteResponseDto> getAllCompteParTypeFonctionnement(@PathVariable String codeTypeFonctionnement) throws ServiceException, ControllerException {

		TypeFonctionnement typeFonctionnement = verificateur.verifierTypeFonctionnement(codeTypeFonctionnement, OBLIGATOIRE, null);
		
		return compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement)
				.stream()
				.sorted((c1,c2)->{return c1.getIdentifiant().compareTo(c2.getIdentifiant());})
				.map((c)->{return CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(c);})
				.toList();
	}

	private CompteInterne mapperCreationRequestDtoToModel(CompteInterneRequestDto dto, CompteInterne compteInterne) throws ControllerException, ServiceException {
		
		compteInterne.setIdentifiant(verificateur.verifierIdentifiantValideEtUnique(dto.identifiant, compteInterne.getId(), OBLIGATOIRE));
		compteInterne.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		compteInterne.setDateCloture(verificateur.verifierDate(dto.dateCloture, FACULTATIF, null));
		compteInterne.setTypeFonctionnement(verificateur.verifierTypeFonctionnement(dto.codeTypeFonctionnement, OBLIGATOIRE, null));
		compteInterne.setDateSoldeInitial(verificateur.verifierDate(dto.dateSoldeInitial, FACULTATIF, LocalDate.now()));
		compteInterne.setMontantSoldeInitialEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantSoldeInitialEnCentimes, FACULTATIF, 0L));
		compteInterne.changerBanque(verificateur.verifierBanque(dto.nomBanque, FACULTATIF));
		Set<Titulaire> titulaires = new HashSet<>(); 
		if ( dto.nomsTitulaires != null ) {
			for ( String nomTitulaire : dto.nomsTitulaires ) {
				titulaires.add(verificateur.verifierTitulaire(nomTitulaire, OBLIGATOIRE));
			}
		}
		compteInterne.changerTitulaires(titulaires);

		return compteInterne;
	}
	
	private CompteInterne mapperModificationRequestDtoToModel(CompteInterneRequestDto dto, CompteInterne compteInterne) throws ControllerException, ServiceException {

		if ( dto.identifiant != null ) compteInterne.setIdentifiant(verificateur.verifierIdentifiantValideEtUnique(dto.identifiant, compteInterne.getId(), OBLIGATOIRE));
		if ( dto.libelle  != null ) compteInterne.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		if ( dto.dateCloture != null ) compteInterne.setDateCloture(verificateur.verifierDate(dto.dateCloture, FACULTATIF, null));
		if ( dto.codeTypeFonctionnement != null ) compteInterne.setTypeFonctionnement(verificateur.verifierTypeFonctionnement(dto.codeTypeFonctionnement, OBLIGATOIRE, null));
		if ( dto.dateSoldeInitial != null ) compteInterne.setDateSoldeInitial(verificateur.verifierDate(dto.dateSoldeInitial, FACULTATIF, LocalDate.now()));
		if ( dto.montantSoldeInitialEnCentimes != null ) compteInterne.setMontantSoldeInitialEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantSoldeInitialEnCentimes, FACULTATIF, 0L));
		if ( dto.nomBanque != null ) compteInterne.changerBanque(verificateur.verifierBanque(dto.nomBanque, FACULTATIF));
		if ( dto.nomsTitulaires != null ) {
			Set<Titulaire> titulaires = new HashSet<>(); 
			for ( String nomTitulaire : dto.nomsTitulaires ) {
				titulaires.add(verificateur.verifierTitulaire(nomTitulaire, OBLIGATOIRE));
			}
			compteInterne.changerTitulaires(titulaires);
		}
		
		return compteInterne;
	}
	
}
