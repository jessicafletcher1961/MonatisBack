package fr.colline.monatis.references.controller.categorie;

import java.util.ArrayList;
import java.util.List;

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
import fr.colline.monatis.references.controller.ReferenceResponseDto;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.TypeReference;
import fr.colline.monatis.references.service.CategorieService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/references/categorie")
@Transactional
public class CategorieController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private CategorieService categorieService;

	@GetMapping("/all")
	public List<ReferenceResponseDto> getAllReference() throws ServiceException {
		
		List<ReferenceResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<Categorie> liste = categorieService.rechercherTous(tri);
		for ( Categorie categorie : liste ) {
			resultat.add(CategorieResponseDtoMapper.mapperModelToSimpleResponseDto(categorie));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public ReferenceResponseDto getReferenceParNom(
			@PathVariable String nom) throws ServiceException, ControllerException {

		Categorie categorie = verificateur.verifierCategorie(nom, OBLIGATOIRE);
		return CategorieResponseDtoMapper.mapperModelToDetailedResponseDto(categorie);
	}

	@PostMapping("/new")
	public ReferenceResponseDto creerReference(
			@RequestBody CategorieRequestDto dto) throws ServiceException, ControllerException {
		
		Categorie categorie = new Categorie();
		categorie = mapperCreationRequestDtoToModel(dto, categorie);
		categorie = categorieService.creerReference(categorie);
		return CategorieResponseDtoMapper.mapperModelToDetailedResponseDto(categorie);
	}

	@PutMapping("/mod/{nom}")
	public ReferenceResponseDto modifierReference(
			@PathVariable String nom,
			@RequestBody CategorieRequestDto dto) throws ControllerException, ServiceException {
		
		Categorie categorie = verificateur.verifierCategorie(nom, OBLIGATOIRE);
		categorie = mapperModificationRequestDtoToModel(dto, categorie);
		categorie = categorieService.modifierReference(categorie);
		return CategorieResponseDtoMapper.mapperModelToDetailedResponseDto(categorie);
	}

	@DeleteMapping("/del/{nom}")
	public void supprimerReference(
			@PathVariable String nom) throws ControllerException, ServiceException {

		Categorie categorie = verificateur.verifierCategorie(nom, OBLIGATOIRE);
		categorieService.supprimerReference(categorie);
	}

	private Categorie mapperCreationRequestDtoToModel(CategorieRequestDto dto, Categorie categorie) throws ControllerException, ServiceException {
		
		categorie.setNom(verificateur.verifierNomValideEtUnique(TypeReference.CATEGORIE, dto.nom, categorie.getId(), OBLIGATOIRE));
		categorie.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return categorie;
	}
	
	private Categorie mapperModificationRequestDtoToModel(CategorieRequestDto dto, Categorie categorie) throws ControllerException, ServiceException {

		if ( dto.nom != null ) categorie.setNom(verificateur.verifierNomValideEtUnique(TypeReference.CATEGORIE, dto.nom, categorie.getId(), OBLIGATOIRE));
		if ( dto.libelle != null ) categorie.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return categorie;
	}

}
