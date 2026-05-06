package fr.colline.monatis.references.controller.souscategorie;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.controller.ReferenceResponseDto;
import fr.colline.monatis.references.controller.ReferenceSelectionRequestDto;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.service.SousCategorieService;
import fr.colline.monatis.typologies.model.TypeReference;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/references/souscategorie")
@Transactional
public class SousCategorieController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private SousCategorieService sousCategorieService;

	@GetMapping("/all")
	public List<ReferenceResponseDto> getAllReference() throws ServiceException {

		return sousCategorieService.rechercherTous()
				.stream()
				.sorted((sc1, sc2) -> {return sc1.getNom().compareTo(sc2.getNom());})
				.map((sc) -> {return SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto(sc);})
				.toList();
	}

	@GetMapping("/get/{nom}")
	public ReferenceResponseDto getReferenceParNom(
			@PathVariable String nom) throws ServiceException, ControllerException {

		SousCategorie sousCategorie = verificateur.verifierSousCategorie(nom, OBLIGATOIRE);
		return SousCategorieResponseDtoMapper.mapperModelToDetailedResponseDto(sousCategorie);
	}

	@PostMapping("/new")
	public ReferenceResponseDto creerReference(
			@RequestBody SousCategorieRequestDto dto) throws ServiceException, ControllerException {
		
		SousCategorie sousCategorie = new SousCategorie();
		sousCategorie = mapperCreationRequestDtoToModel(dto, sousCategorie);
		sousCategorie = sousCategorieService.creerReference(sousCategorie);
		return SousCategorieResponseDtoMapper.mapperModelToSimpleResponseDto(sousCategorie);
	}

	@PutMapping("/mod/{nom}")
	public ReferenceResponseDto modifierReference(
			@PathVariable String nom,
			@RequestBody SousCategorieRequestDto dto) throws ControllerException, ServiceException {
		
		SousCategorie sousCategorie = verificateur.verifierSousCategorie(nom, OBLIGATOIRE);
		sousCategorie = mapperModificationRequestDtoToModel(dto, sousCategorie);
		sousCategorie = sousCategorieService.modifierReference(sousCategorie);
		return SousCategorieResponseDtoMapper.mapperModelToSimpleResponseDto(sousCategorie);
	}

	@DeleteMapping("/del/{nom}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerReference(
			@PathVariable String nom) throws ControllerException, ServiceException {

		SousCategorie sousCategorie = verificateur.verifierSousCategorie(nom, OBLIGATOIRE);
		sousCategorieService.supprimerReference(sousCategorie);
	}

	@PostMapping("/selection")
	public List<ReferenceResponseDto>selectionnerSousCategorie (
			@RequestBody ReferenceSelectionRequestDto requestDto) throws ControllerException, ServiceException {
		
		final String nom = verificateur.standardiserIdentifiantFonctionnel(requestDto.nomContient);
		final String libelle = verificateur.verifierLibelle(requestDto.libelleContient, FACULTATIF, null);
		
		return sousCategorieService.rechercherTous()
				.stream()
				.filter((sc) -> {return nom == null 
						|| sc.getNom().contains(nom);})
				.filter((sc) -> {return libelle == null 
						|| sc.getLibelle().toUpperCase().contains(libelle.toUpperCase());})
				.sorted(Comparator.comparing(SousCategorie::getNom))
				.map((sc) -> {return SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto(sc);})
				.toList();
	}

	private SousCategorie mapperCreationRequestDtoToModel(SousCategorieRequestDto dto, SousCategorie sousCategorie) throws ControllerException, ServiceException {
		
		sousCategorie.setNom(verificateur.verifierNomValideEtUnique(TypeReference.SOUS_CATEGORIE, dto.nom, sousCategorie.getId(), OBLIGATOIRE));
		sousCategorie.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		sousCategorie.changerCategorie(verificateur.verifierCategorie(dto.nomCategorie, OBLIGATOIRE));
		
		return sousCategorie;
	}
	
	private SousCategorie mapperModificationRequestDtoToModel(SousCategorieRequestDto dto, SousCategorie sousCategorie) throws ControllerException, ServiceException {

		if ( dto.nom != null ) sousCategorie.setNom(verificateur.verifierNomValideEtUnique(TypeReference.SOUS_CATEGORIE, dto.nom, sousCategorie.getId(), OBLIGATOIRE));
		if ( dto.libelle != null ) sousCategorie.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		if ( dto.nomCategorie != null ) sousCategorie.changerCategorie(verificateur.verifierCategorie(dto.nomCategorie, OBLIGATOIRE));
		
		return sousCategorie;
	}

}
