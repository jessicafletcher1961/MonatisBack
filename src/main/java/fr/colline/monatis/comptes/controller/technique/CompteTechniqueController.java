package fr.colline.monatis.comptes.controller.technique;

import java.util.ArrayList;
import java.util.List;

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
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/comptes/technique")
@Transactional
public class CompteTechniqueController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private CompteTechniqueService compteTechniqueService;

	@GetMapping("/all")
	public List<CompteResponseDto> getAllCompte() throws ServiceException {
		
		List<CompteResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("identifiant");
		List<CompteTechnique> liste = compteTechniqueService.rechercherTous(tri);
		for ( CompteTechnique compteTechnique : liste ) {
			resultat.add(CompteTechniqueResponseDtoMapper.mapperModelToBasicResponseDto(compteTechnique));
		}
		return resultat;
	}

	@GetMapping("/get/{identifiant}")
	public CompteResponseDto getCompteParIdentifiant(
			@PathVariable String identifiant) throws ServiceException, ControllerException {

		CompteTechnique compteTechnique = (CompteTechnique) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.TECHNIQUE, 
				identifiant, 
				OBLIGATOIRE);
		return CompteTechniqueResponseDtoMapper.mapperModelToDetailedResponseDto(compteTechnique);
	}

	@PostMapping("/new")
	public CompteResponseDto creerCompte(
			@RequestBody CompteTechniqueRequestDto dto) throws ServiceException, ControllerException {
		
		CompteTechnique compteTechnique = new CompteTechnique();
		compteTechnique = mapperCreationRequestDtoToModel(dto, compteTechnique);
		compteTechnique = compteTechniqueService.creerCompte(compteTechnique);
		return CompteTechniqueResponseDtoMapper.mapperModelToSimpleResponseDto(compteTechnique);
	}

	@PutMapping("/mod/{identifiant}")
	public CompteResponseDto modifierCompte(
			@PathVariable String identifiant,
			@RequestBody CompteTechniqueRequestDto dto) throws ControllerException, ServiceException {
		
		CompteTechnique compteTechnique = (CompteTechnique) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.TECHNIQUE, 
				identifiant, 
				OBLIGATOIRE);
		compteTechnique = mapperModificationRequestDtoToModel(dto, compteTechnique);
		compteTechnique = compteTechniqueService.modifierCompte(compteTechnique);
		return CompteTechniqueResponseDtoMapper.mapperModelToSimpleResponseDto(compteTechnique);
	}

	@DeleteMapping("/del/{identifiant}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerCompte(
			@PathVariable String identifiant) throws ControllerException, ServiceException {

		CompteTechnique compteTechnique = (CompteTechnique) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.TECHNIQUE, 
				identifiant, 
				OBLIGATOIRE);
		compteTechniqueService.supprimerCompte(compteTechnique);
	}

	private CompteTechnique mapperCreationRequestDtoToModel(CompteTechniqueRequestDto dto, CompteTechnique compteTechnique) throws ControllerException, ServiceException {
		
		compteTechnique.setIdentifiant(verificateur.verifierIdentifiantValideEtUnique(dto.identifiant, compteTechnique.getId(), OBLIGATOIRE));
		compteTechnique.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return compteTechnique;
	}
	
	private CompteTechnique mapperModificationRequestDtoToModel(CompteTechniqueRequestDto dto, CompteTechnique compteTechnique) throws ControllerException, ServiceException {

		if ( dto.identifiant != null ) compteTechnique.setIdentifiant(verificateur.verifierIdentifiantValideEtUnique(dto.identifiant, compteTechnique.getId(), OBLIGATOIRE));
		if ( dto.libelle  != null ) compteTechnique.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return compteTechnique;
	}
	
}
