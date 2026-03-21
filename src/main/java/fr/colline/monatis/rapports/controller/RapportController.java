package fr.colline.monatis.rapports.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.rapports.RapportControleErreur;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.EtatBilanPatrimoineRequestDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.EtatBilanPatrimoineResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteRequestDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueRequestDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteRequestDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.EtatRemunerationsFraisRequestDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.EtatRemunerationsFraisResponseDto;
import fr.colline.monatis.rapports.controller.resumes_comptes_internes.ResumeCompteInterneRequestDto;
import fr.colline.monatis.rapports.controller.resumes_comptes_internes.ResumeCompteInterneResponseDto;
import fr.colline.monatis.rapports.model.EtatBilanPatrimoine;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValue;
import fr.colline.monatis.rapports.model.EtatRemunerationsFrais;
import fr.colline.monatis.rapports.model.ReleveOperationCompte;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
import fr.colline.monatis.rapports.service.RapportService;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.utils.TypePeriode;
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
	@Autowired private CategorieService categorieService;

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

		ReleveOperationCompte releve = rapportService.rechercherReleveOperationCompte(compte, dateDebut, dateFin);
		
		return RapportResponseDtoMapper.mapperReleveCompte(releve);
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
		List<Categorie> categories;
		if ( requestDto.nomsCategories == null || requestDto.nomsCategories.isEmpty() ) {
			Sort tri = Sort.by("nom");
			categories = categorieService.rechercherTous(tri);
		}
		else {
			categories = new ArrayList<Categorie>();
			for ( String nomCategorie : requestDto.nomsCategories ) {
				Categorie categorie = verificateur.verifierCategorie(nomCategorie, OBLIGATOIRE);
				categories.add(categorie);
			}
		}
		Beneficiaire beneficiaire = verificateur.verifierBeneficiaire(requestDto.nomBeneficiaire, FACULTATIF);
		
		EtatDepenseRecette etat = rapportService.rechercherEtatDepenseRecette(
				sousCategories,
				categories,
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
		Titulaire titulaire = verificateur.verifierTitulaire(requestDto.nomTitulaire, FACULTATIF);
		
		EtatPlusMoinsValue etat = rapportService.rechercherEtatPlusMoinsValue(
				comptesInternes,
				typesFonctionnements,
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

		ReleveOperationCompte releve = rapportService.rechercherReleveOperationCompte(compte, dateDebut, dateFin);
		
		RapportResponsePdfMapper.mapperReleveCompteToPdf(releve, response.getOutputStream());
	}
}
