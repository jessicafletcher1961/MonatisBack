package fr.colline.monatis.references.controller;

import java.util.ArrayList;
import java.util.List;

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

import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurControle;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieBasicResponseDto;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieRequestDto;
import fr.colline.monatis.references.controller.mapper.SousCategorieDtoMapper;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.references.service.SousCategorieService;

@RestController
@RequestMapping("/monatis/references/souscategorie")
@Transactional
public class SousCategorieController {

	@Autowired private SousCategorieService sousCategorieService;
	@Autowired private CategorieService categorieService;

	@GetMapping("/all")
	public List<SousCategorieBasicResponseDto> getAllSousCategories() throws ServiceException {

		List<SousCategorieBasicResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<SousCategorie> liste = sousCategorieService.rechercherTous(tri);
		for ( SousCategorie sousCategorie : liste ) {
			resultat.add(SousCategorieDtoMapper.modelToBasicResponseDto(sousCategorie));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public SousCategorieDetailedResponseDto getSousCategorieParNom(
			@PathVariable(name = "nom") String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			SousCategorie sousCategorie = sousCategorieService.rechercherParNom(nom);
			if ( sousCategorie == null ) {
				throw new ControllerException(
						ErreurControle.SOUS_CATEGORIE_NON_TROUVEE_PAR_NOM,
						nom);
			}
			return SousCategorieDtoMapper.modelToDetailedResponseDto(sousCategorie);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t, 
					ErreurControle.SOUS_CATEGORIE_CONSULTATION_PROBLEME,
					nom);
		}
	}

	@PostMapping("/new")
	public SousCategorieDetailedResponseDto creerSousCategorie(
			@RequestBody SousCategorieRequestDto dto) throws ControllerException {

		try {
			SousCategorie sousCategorie = new SousCategorie();
			sousCategorie = creationRequestDtoToModel(sousCategorie, dto);
			sousCategorie = sousCategorieService.creerReference(sousCategorie);
			return SousCategorieDtoMapper.modelToDetailedResponseDto(sousCategorie);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t, 
					ErreurControle.SOUS_CATEGORIE_CREATION_PROBLEME,
					dto.nom);
		}
	}

	@PutMapping("/mod/{nom}")
	public SousCategorieDetailedResponseDto modifierSousCategorie(
			@PathVariable(name = "nom") String nom,
			@RequestBody SousCategorieRequestDto dto) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			SousCategorie sousCategorie = sousCategorieService.rechercherParNom(nom);
			if ( sousCategorie == null ) {
				throw new ControllerException(
						ErreurControle.SOUS_CATEGORIE_NON_TROUVEE_PAR_NOM,
						nom);
			}
			sousCategorie = modificationRequestDtoToModel(sousCategorie, dto);
			sousCategorie = sousCategorieService.modifierReference(sousCategorie);
			return SousCategorieDtoMapper.modelToDetailedResponseDto(sousCategorie);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.SOUS_CATEGORIE_MODIFICATION_PROBLEME,
					nom);
		}
	}

	@DeleteMapping("/del/{nom}")
	public void supprimerSousCategorie(@PathVariable(name = "nom") String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			SousCategorie sousCategorie = sousCategorieService.rechercherParNom(nom);
			if ( sousCategorie == null ) {
				throw new ControllerException(
						ErreurControle.SOUS_CATEGORIE_NON_TROUVEE_PAR_NOM,
						nom);
			}
			sousCategorieService.supprimerReference(sousCategorie.getId());
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.SOUS_CATEGORIE_SUPPRESSION_PROBLEME,
					nom);
		}
	}

	private SousCategorie creationRequestDtoToModel(
			SousCategorie sousCategorie,
			SousCategorieRequestDto dto) throws ControllerException, ServiceException {

		sousCategorie.setNom(verifierNom(dto.nom));
		sousCategorie.setLibelle(verifierLibelle(dto.libelle));
		sousCategorie.changerCategorie(verifierCategorieEnregistree(dto.nomCategorie));

		return sousCategorie;
	}

	private SousCategorie modificationRequestDtoToModel(
			SousCategorie sousCategorie,
			SousCategorieRequestDto dto) throws ControllerException, ServiceException {

		if ( dto.nom != null ) sousCategorie.setNom(verifierNom(dto.nom));
		if ( dto.libelle != null ) sousCategorie.setLibelle(verifierLibelle(dto.libelle));
		if ( dto.nomCategorie != null ) sousCategorie.changerCategorie(verifierCategorieEnregistree(dto.nomCategorie));

		return sousCategorie;
	}

	private String verifierNom(String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) { 
			throw new ControllerException(
					ErreurControle.SOUS_CATEGORIE_NOM_OBLIGATOIRE);
		}
		return nom;
	}

	private String verifierLibelle(String libelle) {
		
		if ( libelle == null || libelle.isBlank() ) {
			libelle = null;
		}
		return libelle;
	}

	private Categorie verifierCategorieEnregistree(String nomCategorie) 
			throws ControllerException, ServiceException {

		if ( nomCategorie == null || nomCategorie.isBlank() ) {
			throw new ControllerException(
					ErreurControle.SOUS_CATEGORIE_NOM_CATEGORIE_OBLIGATOIRE);
		}

		Categorie categorie = categorieService.rechercherParNom(nomCategorie);
		if ( categorie == null ) {
			throw new ControllerException(
					ErreurControle.CATEGORIE_NON_TROUVEE_PAR_NOM, 
					nomCategorie);
		}
		return categorie;
	}
}