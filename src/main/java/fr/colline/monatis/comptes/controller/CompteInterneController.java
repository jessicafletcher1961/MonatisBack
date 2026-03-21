package fr.colline.monatis.comptes.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.controller.dto.request.CompteInterneRequestDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteInterneBasicResponseDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteInterneDetailedResponseDto;
import fr.colline.monatis.comptes.controller.mapper.CompteInterneDtoMapper;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurControle;
import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.service.BanqueService;
import fr.colline.monatis.references.service.TitulaireService;

@RestController
@RequestMapping("/monatis/comptes/interne")
@Transactional
public class CompteInterneController {

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private BanqueService banqueService;
	@Autowired private TitulaireService titulaireService;

	@GetMapping("/all")
	public List<CompteInterneBasicResponseDto> getAllComptesInternes() throws ServiceException {

		List<CompteInterneBasicResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("identifiant");
		List<CompteInterne> liste = compteInterneService.rechercherTous(tri);
		for ( CompteInterne compteInterne : liste ) {
			resultat.add(CompteInterneDtoMapper.modelToBasicResponseDto(compteInterne));
		}
		return resultat;
	}

	@GetMapping("/get/{identifiant}")
	public CompteInterneDetailedResponseDto getCompteInterneParIdentifiant(
			@PathVariable(name = "identifiant") String identifiant) 
					throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		try {
			CompteInterne compteInterne = compteInterneService.rechercherParIdentifiant(identifiant);
			if ( compteInterne == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			return CompteInterneDtoMapper.modelToDetailedResponseDto(compteInterne);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_INTERNE_CONSULTATION_PROBLEME,
					identifiant);
		}
	}

	@PostMapping("/new")
	public CompteInterneDetailedResponseDto creerCompteInterne(
			@RequestBody CompteInterneRequestDto dto) throws ControllerException {

		try {
			CompteInterne compteInterne = new CompteInterne();
			compteInterne = creationRequestDtoToModel(compteInterne, dto);
			compteInterne = compteInterneService.creerCompte(compteInterne);
			return CompteInterneDtoMapper.modelToDetailedResponseDto(compteInterne);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_INTERNE_CREATION_PROBLEME,
					dto.identifiant);
		}
	}

	@PutMapping("/mod/{identifiant}")
	public CompteInterneDetailedResponseDto modifierCompteInterne(
			@PathVariable(name = "identifiant") String identifiant,
			@RequestBody CompteInterneRequestDto dto) throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		try {
			CompteInterne compteInterne = compteInterneService.rechercherParIdentifiant(identifiant);
			if ( compteInterne == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			compteInterne = modificationRequestDtoToModel(compteInterne, dto);
			compteInterne = compteInterneService.modifierCompte(compteInterne);
			return CompteInterneDtoMapper.modelToDetailedResponseDto(compteInterne);
		}
		catch ( Throwable t ) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_INTERNE_MODIFICATION_PROBLEME,
					identifiant);
		}
	}

	@DeleteMapping("/del/{identifiant}")
	public void supprimerCompteInterne(@PathVariable(name = "identifiant") String identifiant) throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		try {
			CompteInterne compteInterne = compteInterneService.rechercherParIdentifiant(identifiant);
			if ( compteInterne == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			compteInterneService.supprimerCompte(compteInterne.getId());
		}
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_INTERNE_SUPPRESSION_PROBLEME,
					identifiant);
		}
	}
	
	private CompteInterne creationRequestDtoToModel(
			CompteInterne compteInterne,
			CompteInterneRequestDto dto) throws ControllerException, ServiceException {

		compteInterne.setIdentifiant(verifierIdentifiant(dto.identifiant));
		compteInterne.setLibelle(verifierLibelle(dto.libelle));
		compteInterne.setDateSoldeInitial(verifierDateSoldeInitial(dto.dateSoldeInitial));
		compteInterne.setMontantSoldeInitialEnCentimes(verifierMontantSoldeInitial(dto.montantSoldeInitialEnCentimes));
		compteInterne.setTypeCompteInterne(verifierTypeCompteInterne(dto.codeTypeCompteInterne));
		compteInterne.changerBanque(verifierBanqueEnregistree(compteInterne.getTypeCompteInterne(), dto.nomBanque));
		compteInterne.changerTitulaires(verifierTitulairesEnregistres(compteInterne.getTypeCompteInterne(), dto.nomsTitulaires));

		return compteInterne;
	}

	private CompteInterne modificationRequestDtoToModel(
			CompteInterne compteInterne,
			CompteInterneRequestDto dto) throws ControllerException, ServiceException {

		if ( dto.identifiant != null ) compteInterne.setIdentifiant(verifierIdentifiant(dto.identifiant));
		if ( dto.libelle != null ) compteInterne.setLibelle(verifierLibelle(dto.libelle));
		if ( dto.dateSoldeInitial != null ) compteInterne.setDateSoldeInitial(verifierDateSoldeInitial(dto.dateSoldeInitial));
		if ( dto.montantSoldeInitialEnCentimes != null ) compteInterne.setMontantSoldeInitialEnCentimes(verifierMontantSoldeInitial(dto.montantSoldeInitialEnCentimes));
		if ( dto.codeTypeCompteInterne != null ) compteInterne.setTypeCompteInterne(verifierTypeCompteInterne(dto.codeTypeCompteInterne));
		if ( dto.nomBanque != null ) compteInterne.changerBanque(verifierBanqueEnregistree(compteInterne.getTypeCompteInterne(), dto.nomBanque));
		if ( dto.nomsTitulaires != null ) compteInterne.changerTitulaires(verifierTitulairesEnregistres(compteInterne.getTypeCompteInterne(), dto.nomsTitulaires));

		return compteInterne;
	}

	private String verifierIdentifiant(String identifiant) throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) { 
			throw new ControllerException(
					ErreurControle.COMPTE_INTERNE_IDENTIFIANT_OBLIGATOIRE);
		}
		return identifiant;
	}

	private String verifierLibelle(String libelle) {

		if ( libelle == null || libelle.isBlank() ) {
			libelle = null;
		}
		return libelle;
	}

	private ZonedDateTime verifierDateSoldeInitial(ZonedDateTime dateSoldeInitial) {

		if ( dateSoldeInitial == null ) {
			dateSoldeInitial = ZonedDateTime.now();
		}
		return dateSoldeInitial;
	}

	private Long verifierMontantSoldeInitial(Long montantSoldeInitialEnCentimes) {

		if ( montantSoldeInitialEnCentimes == null ) {
			montantSoldeInitialEnCentimes = 0L;
		}
		return montantSoldeInitialEnCentimes;
	}

	private TypeCompteInterne verifierTypeCompteInterne(String codeTypeCompteInterne) throws ControllerException {

		if ( codeTypeCompteInterne == null || codeTypeCompteInterne.isBlank() ) {
			throw new ControllerException(
					ErreurControle.COMPTE_INTERNE_TYPE_COMPTE_OBLIGATOIRE);
		}

		TypeCompteInterne typeCompteInterne = TypeCompteInterne.findByCode(codeTypeCompteInterne);
		if ( typeCompteInterne == null ) {
			throw new ControllerException(
					ErreurControle.COMPTE_INTERNE_TYPE_COMPTE_NON_TROUVE_PAR_CODE,
					codeTypeCompteInterne);

		}

		return typeCompteInterne;
	}

	private Banque verifierBanqueEnregistree(TypeCompteInterne typeCompteInterne, String nomBanque) 
			throws ControllerException, ServiceException {

		if ( nomBanque == null || nomBanque.isBlank() ) {
			if ( typeCompteInterne.isBanqueObligatoire() ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_BANQUE_OBLIGATOIRE);
			}
			return null;
		}

		Banque banque = banqueService.rechercherParNom(nomBanque);
		if ( banque == null ) {
			throw new ControllerException(
					ErreurControle.BANQUE_NON_TROUVEE_PAR_NOM, 
					nomBanque);
		}
		return banque;
	}

	private Set<Titulaire> verifierTitulairesEnregistres(TypeCompteInterne typeCompteInterne, List<String> nomsTitulaires) 
			throws ControllerException, ServiceException {

		Set<Titulaire> titulaires = new HashSet<>();

		if ( nomsTitulaires == null || nomsTitulaires.size() == 0 ) {
			if ( typeCompteInterne.isAuMoinsUnTitulaireObligatoire() ) {
				throw new ControllerException(
						ErreurControle.COMPTE_INTERNE_AU_MOINS_UN_TITULAIRE_REQUIS);
			} 
			else {
				return titulaires;
			}
		}

		for ( String nomTitulaire : nomsTitulaires ) {
			Titulaire titulaire = titulaireService.rechercherParNom(nomTitulaire);
			if ( titulaire == null ) {
				throw new ControllerException(
						ErreurControle.TITULAIRE_NON_TROUVE_PAR_NOM, 
						nomTitulaire);
			}
			titulaires.add(titulaire);
		}

		return titulaires;
	}
}
