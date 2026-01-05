package fr.colline.monatis.references.controller.titulaire;

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
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.model.TypeReference;
import fr.colline.monatis.references.service.TitulaireService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/references/titulaire")
@Transactional
public class TitulaireController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private TitulaireService titulaireService;

	@GetMapping("/all")
	public List<ReferenceResponseDto> getAllReference() throws ServiceException {
		
		List<ReferenceResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<Titulaire> liste = titulaireService.rechercherTous(tri);
		for ( Titulaire titulaire : liste ) {
			resultat.add(TitulaireResponseDtoMapper.mapperModelToSimpleResponseDto(titulaire));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public ReferenceResponseDto getReferenceParNom(
			@PathVariable String nom) throws ServiceException, ControllerException {

		Titulaire titulaire = verificateur.verifierTitulaire(nom, OBLIGATOIRE);
		return TitulaireResponseDtoMapper.mapperModelToDetailedResponseDto(titulaire);
	}

	@PostMapping("/new")
	public ReferenceResponseDto creerReference(
			@RequestBody TitulaireRequestDto dto) throws ServiceException, ControllerException {
		
		Titulaire titulaire = new Titulaire();
		titulaire = mapperCreationRequestDtoToModel(dto, titulaire);
		titulaire = titulaireService.creerReference(titulaire);
		return TitulaireResponseDtoMapper.mapperModelToDetailedResponseDto(titulaire);
	}

	@PutMapping("/mod/{nom}")
	public ReferenceResponseDto modifierReference(
			@PathVariable String nom,
			@RequestBody TitulaireRequestDto dto) throws ControllerException, ServiceException {
		
		Titulaire titulaire = verificateur.verifierTitulaire(nom, OBLIGATOIRE);
		titulaire = mapperModificationRequestDtoToModel(dto, titulaire);
		titulaire = titulaireService.modifierReference(titulaire);
		return TitulaireResponseDtoMapper.mapperModelToDetailedResponseDto(titulaire);
	}

	@DeleteMapping("/del/{nom}")
	public void supprimerReference(
			@PathVariable String nom) throws ControllerException, ServiceException {

		Titulaire titulaire = verificateur.verifierTitulaire(nom, OBLIGATOIRE);
		titulaireService.supprimerReference(titulaire);
	}

	private Titulaire mapperCreationRequestDtoToModel(TitulaireRequestDto dto, Titulaire titulaire) throws ControllerException, ServiceException {
		
		titulaire.setNom(verificateur.verifierNomValideEtUnique(TypeReference.TITULAIRE, dto.nom, titulaire.getId(), OBLIGATOIRE));
		titulaire.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return titulaire;
	}

	private Titulaire mapperModificationRequestDtoToModel(TitulaireRequestDto dto, Titulaire titulaire) throws ControllerException, ServiceException {

		if ( dto.nom != null ) titulaire.setNom(verificateur.verifierNomValideEtUnique(TypeReference.TITULAIRE, dto.nom, titulaire.getId(), OBLIGATOIRE));
		if ( dto.libelle != null ) titulaire.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return titulaire;
	}
}
