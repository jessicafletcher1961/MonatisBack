package fr.colline.monatis.rapports.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.rapports.RapportControleErreur;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.EtatBilanPatrimoineRequestDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.EtatBilanPatrimoineResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteRequestDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteResponseDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierRequestDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueRequestDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteRequestDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.controller.releve_non_categorise.ReleveNonCategoriseRequestDto;
import fr.colline.monatis.rapports.controller.releve_non_categorise.ReleveNonCategoriseResponseDto;
import fr.colline.monatis.rapports.controller.releve_sous_categorie.ReleveSousCategorieRequestDto;
import fr.colline.monatis.rapports.controller.releve_sous_categorie.ReleveSousCategorieResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.EtatRemunerationsFraisRequestDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.EtatRemunerationsFraisResponseDto;
import fr.colline.monatis.rapports.controller.resumes_comptes_internes.ResumeCompteInterneRequestDto;
import fr.colline.monatis.rapports.controller.resumes_comptes_internes.ResumeCompteInterneResponseDto;
import fr.colline.monatis.rapports.model.Echeancier;
import fr.colline.monatis.rapports.model.EtatBilanPatrimoine;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValue;
import fr.colline.monatis.rapports.model.EtatRemunerationsFrais;
import fr.colline.monatis.rapports.model.ReleveNonCategorise;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.model.ReleveSousCategorie;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
import fr.colline.monatis.rapports.service.RapportService;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.service.SousCategorieService;
import fr.colline.monatis.typologies.model.TypeCompte;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypePeriode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/rapports")
@Transactional
public class RapportController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private RapportService rapportService;

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private SousCategorieService sousCategorieService;

	@GetMapping("/releve_compte")
	public ReleveCompteResponseDto getReleveCompte(
			@RequestBody ReleveCompteRequestDto requestDto) throws ControllerException, ServiceException {

		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}
		Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte, OBLIGATOIRE);

		ReleveCompte releve = rapportService.rechercherReleveOperationCompte(compte, dateDebut, dateFin);
		
		return RapportResponseDtoMapper.mapperReleveCompte(releve);
	}

	@GetMapping("/releve_non_categorise")
	public ReleveNonCategoriseResponseDto getReleveNonCategorise(
			@RequestBody ReleveNonCategoriseRequestDto requestDto) throws ControllerException, ServiceException {

		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		
		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		ReleveNonCategorise releve = rapportService.rechercherReleveOperationNonCategorise(dateDebut, dateFin);
		
		return RapportResponseDtoMapper.mapperReleveNonCategorise(releve);
	}

	@GetMapping("/releve_sous_categorie") 
	public ReleveSousCategorieResponseDto getReleveSousCategorie(
			@RequestBody ReleveSousCategorieRequestDto requestDto) throws ControllerException, ServiceException {

		SousCategorie sousCategorie = verificateur.verifierSousCategorie(requestDto.nomSousCategorie, OBLIGATOIRE);
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		
		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		ReleveSousCategorie releve = rapportService.rechercherReleveOperationSousCategorie(sousCategorie, dateDebut, dateFin);
		return RapportResponseDtoMapper.mapperReleveSousCategorie(releve);

	}
	
	@GetMapping("/resumes_comptes_internes")
	public List<ResumeCompteInterneResponseDto> getListeResumeCompteInterne(
			@RequestBody ResumeCompteInterneRequestDto requestDto) throws ControllerException, ServiceException {
		
		LocalDate dateCible = verificateur.verifierDate(requestDto.dateSolde, FACULTATIF, LocalDate.now());
		TypeFonctionnement typeFonctionnement = verificateur.verifierTypeFonctionnement(requestDto.codeTypeFonctionnement, FACULTATIF, null);
		List<CompteInterne> comptesInternes;
		if ( requestDto.identifiantsComptesInternes == null || requestDto.identifiantsComptesInternes.isEmpty() ) {
			if ( typeFonctionnement == null ) {
				comptesInternes = compteInterneService.rechercherTous();
			}
			else {
				comptesInternes = compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement);
			}
			Collections.sort(comptesInternes, (o1, o2) -> {return o1.getIdentifiant().compareTo(o2.getIdentifiant());});
		} 
		else {
			comptesInternes = new ArrayList<CompteInterne>();
			for ( String identifiantCompteInterne : requestDto.identifiantsComptesInternes ) {
				CompteInterne compteInterne = (CompteInterne) verificateur.verifierCompteEtTypeCompte(
						TypeCompte.INTERNE, 
						identifiantCompteInterne, 
						OBLIGATOIRE);
				if ( typeFonctionnement == null || typeFonctionnement == compteInterne.getTypeFonctionnement() ) {
					comptesInternes.add(compteInterne);
				}
			}
		}
		
		List<ResumeCompteInterneResponseDto> dto = new ArrayList<>();
		for ( CompteInterne compteInterne : comptesInternes ) {
			ResumeCompteInterne resume = rapportService.rechercherResumeCompteInterne(
					compteInterne, 
					dateCible);
			dto.add(RapportResponseDtoMapper.mapperResumeCompteInterne(resume));
		}
		
		return dto;
	}
	
	@PostMapping("/echeancier")
	public EcheancierResponseDto getEcheancier(
			@RequestBody EcheancierRequestDto requestDto ) throws ControllerException, ServiceException {
		
		Emprunt emprunt = verificateur.verifierEmprunt(requestDto.cleEmprunt, OBLIGATOIRE);
		LocalDate dateCible = verificateur.verifierDate(requestDto.dateCible, FACULTATIF, null);

		Echeancier etat = rapportService.rechercherEcheancier(
				emprunt,
				dateCible);
		
		return RapportResponseDtoMapper.mapperEcheancier(etat);
	}
	
	@GetMapping("/depense_recette")
	public EtatDepenseRecetteResponseDto getEtatDepenseRecette
	(@RequestBody EtatDepenseRecetteRequestDto requestDto) throws ServiceException, ControllerException {

		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		TypePeriode typePeriode = verificateur.verifierTypePeriode(requestDto.codeTypePeriode, FACULTATIF, null);
		
		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		List<SousCategorie> sousCategories = new ArrayList<SousCategorie>();
		if ( requestDto.nomsSousCategories != null && ! requestDto.nomsSousCategories.isEmpty() ) {
			for ( String nomSousCategorie : requestDto.nomsSousCategories ) {
				SousCategorie sousCategorie = verificateur.verifierSousCategorie(nomSousCategorie, OBLIGATOIRE);
				sousCategories.add(sousCategorie);
			}
		}
		else if ( requestDto.nomsCategories != null && ! requestDto.nomsCategories.isEmpty() ) {
			for ( String nomCategorie : requestDto.nomsCategories ) {
				Categorie categorie = verificateur.verifierCategorie(nomCategorie, OBLIGATOIRE);
				sousCategories.addAll(categorie.getSousCategories());
			}
		}
		else {
			sousCategories.addAll(sousCategorieService.rechercherTous());
		}
		
		Beneficiaire beneficiaire = null;
		if ( requestDto.nomBeneficiaire != null ) {
			beneficiaire = verificateur.verifierBeneficiaire(requestDto.nomBeneficiaire, OBLIGATOIRE);
		}
		
		EtatDepenseRecette etat = rapportService.rechercherEtatDepenseRecette(
				sousCategories,
				beneficiaire,
				dateDebut, 
				dateFin, 
				typePeriode);
		
		return RapportResponseDtoMapper.mapperEtatDepenseRecette(etat);
	}
	
	@GetMapping("/plus_moins_value")
	public EtatPlusMoinsValueResponseDto getEtatPlusMoinsValue(
			@RequestBody EtatPlusMoinsValueRequestDto requestDto) throws ControllerException, ServiceException {

		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		TypePeriode typePeriode = verificateur.verifierTypePeriode(requestDto.codeTypePeriode, FACULTATIF, null);

		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}
		
		List<CompteInterne> comptesInternes = new ArrayList<CompteInterne>();
		if ( requestDto.identifiantsComptesInternes != null && ! requestDto.identifiantsComptesInternes.isEmpty() ) {
			for ( String identifiantCompteInterne : requestDto.identifiantsComptesInternes ) {
				Compte compte = verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, identifiantCompteInterne, OBLIGATOIRE);
				comptesInternes.add((CompteInterne) compte);
			}
		}
		List<TypeFonctionnement> typesFonctionnements;
		if ( requestDto.codesTypesFonctionnements == null || requestDto.codesTypesFonctionnements.isEmpty() ) {
			typesFonctionnements = Arrays.asList(TypeFonctionnement.values());
		}
		else {
			typesFonctionnements = new ArrayList<TypeFonctionnement>();
			for ( String codeTypeFonctionnement : requestDto.codesTypesFonctionnements ) {
				TypeFonctionnement typeFonctionnement = verificateur.verifierTypeFonctionnement(codeTypeFonctionnement, OBLIGATOIRE, null);
				typesFonctionnements.add(typeFonctionnement);
			}
		}
		Banque banque = verificateur.verifierBanque(requestDto.nomBanque, FACULTATIF);
		Titulaire titulaire = verificateur.verifierTitulaire(requestDto.nomTitulaire, FACULTATIF);
		
		EtatPlusMoinsValue etat = rapportService.rechercherEtatPlusMoinsValue(
				comptesInternes,
				typesFonctionnements,
				banque,
				titulaire,
				dateDebut,
				dateFin,
				typePeriode);
			
		return RapportResponseDtoMapper.mapperEtatPlusMoinsValue(etat);
	}

	@GetMapping("/remunerations_frais")
	public EtatRemunerationsFraisResponseDto getEtatRemunerationsFrais(
			@RequestBody EtatRemunerationsFraisRequestDto requestDto) throws ServiceException, ControllerException {
		
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		TypePeriode typePeriode = verificateur.verifierTypePeriode(requestDto.codeTypePeriode, FACULTATIF, null);
		
		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		List<CompteInterne> comptesInternes = new ArrayList<CompteInterne>();
		if ( requestDto.identifiantsComptesInternes != null && ! requestDto.identifiantsComptesInternes.isEmpty() ) {
			for ( String identifiantCompteInterne : requestDto.identifiantsComptesInternes ) {
				Compte compte = verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, identifiantCompteInterne, OBLIGATOIRE);
				comptesInternes.add((CompteInterne) compte);
			}
		}
		List<TypeFonctionnement> typesFonctionnements;
		if ( requestDto.codesTypesFonctionnements == null || requestDto.codesTypesFonctionnements.isEmpty() ) {
			typesFonctionnements = Arrays.asList(TypeFonctionnement.values());
		}
		else {
			typesFonctionnements = new ArrayList<TypeFonctionnement>();
			for ( String codeTypeFonctionnement : requestDto.codesTypesFonctionnements ) {
				TypeFonctionnement typeFonctionnement = verificateur.verifierTypeFonctionnement(codeTypeFonctionnement, OBLIGATOIRE, null);
				typesFonctionnements.add(typeFonctionnement);
			}
		}
		Titulaire titulaire = verificateur.verifierTitulaire(requestDto.nomTitulaire, FACULTATIF);
		
		EtatRemunerationsFrais etat = rapportService.rechercherEtatRemunerationsFrais(
				comptesInternes,
				typesFonctionnements,
				titulaire,
				dateDebut, 
				dateFin, 
				typePeriode);
		
		return RapportResponseDtoMapper.mapperEtatRemunerationsFrais(etat);
	}

	@GetMapping("/bilan_patrimoine")
	public EtatBilanPatrimoineResponseDto getEtatBilanPatrimoine(
			@RequestBody EtatBilanPatrimoineRequestDto requestDto) throws ControllerException, ServiceException {
				
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		TypePeriode typePeriode = verificateur.verifierTypePeriode(requestDto.codeTypePeriode, FACULTATIF, null);

		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}
		
		List<CompteInterne> comptesInternes = new ArrayList<CompteInterne>();
		if ( requestDto.identifiantsComptesInternes != null && ! requestDto.identifiantsComptesInternes.isEmpty() ) {
			for ( String identifiantCompteInterne : requestDto.identifiantsComptesInternes ) {
				Compte compte = verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, identifiantCompteInterne, OBLIGATOIRE);
				comptesInternes.add((CompteInterne) compte);
			}
		}
		List<TypeFonctionnement> typesFonctionnements;
		if ( requestDto.codesTypesFonctionnements == null || requestDto.codesTypesFonctionnements.isEmpty() ) {
			typesFonctionnements = Arrays.asList(TypeFonctionnement.values());
		}
		else {
			typesFonctionnements = new ArrayList<TypeFonctionnement>();
			for ( String codeTypeFonctionnement : requestDto.codesTypesFonctionnements ) {
				TypeFonctionnement typeFonctionnement = verificateur.verifierTypeFonctionnement(codeTypeFonctionnement, OBLIGATOIRE, null);
				typesFonctionnements.add(typeFonctionnement);
			}
		}
		Titulaire titulaire = verificateur.verifierTitulaire(requestDto.nomTitulaire, FACULTATIF);
		
		EtatBilanPatrimoine etat = rapportService.rechercherEtatBilanPatrimoine(
				comptesInternes,
				typesFonctionnements,
				titulaire,
				dateDebut, 
				dateFin, 
				typePeriode);
		
		return RapportResponseDtoMapper.mapperEtatBilanPatrimoine(etat);
	}

	@GetMapping(value = "/releve_compte/pdf", produces = "application/pdf")
	public void getReleveComptePdf(
			HttpServletResponse response,
			@RequestBody ReleveCompteRequestDto requestDto) throws ControllerException, ServiceException, IOException {

		Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte, OBLIGATOIRE);
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());

		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		ReleveCompte releve = rapportService.rechercherReleveOperationCompte(compte, dateDebut, dateFin);
		
		RapportResponsePdfMapper.mapperReleveCompteToPdf(releve, response.getOutputStream());
	}
}
