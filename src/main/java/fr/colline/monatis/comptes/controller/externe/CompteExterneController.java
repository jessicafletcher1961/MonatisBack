package fr.colline.monatis.comptes.controller.externe;

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

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.typologies.model.TypeCompte;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/comptes/externe")
@Transactional
public class CompteExterneController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private CompteExterneService compteExterneService;

	@GetMapping("/all")
	public List<CompteResponseDto> getAllCompte() throws ServiceException {

		return compteExterneService.rechercherTous()
				.stream()
				.sorted((c1, c2) -> {return c1.getIdentifiant().compareTo(c2.getIdentifiant());})
				.map((c) -> {return CompteExterneResponseDtoMapper.mapperModelToBasicResponseDto(c);})
				.toList();
	}

	@GetMapping("/get/{identifiant}")
	public CompteResponseDto getCompteParIdentifiant(
			@PathVariable String identifiant) throws ServiceException, ControllerException {

		CompteExterne compteExterne = (CompteExterne) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.EXTERNE, 
				identifiant, 
				OBLIGATOIRE);
		return CompteExterneResponseDtoMapper.mapperModelToDetailedResponseDto(compteExterne);
	}

	@PostMapping("/new")
	public CompteResponseDto creerCompte(
			@RequestBody CompteExterneRequestDto dto) throws ServiceException, ControllerException {
		
		CompteExterne compteExterne = new CompteExterne();
		compteExterne = mapperCreationRequestDtoToModel(dto, compteExterne);
		compteExterne = compteExterneService.creerCompte(compteExterne);
		return CompteExterneResponseDtoMapper.mapperModelToSimpleResponseDto(compteExterne);
	}

	@PutMapping("/mod/{identifiant}")
	public CompteResponseDto modifierCompte(
			@PathVariable String identifiant,
			@RequestBody CompteExterneRequestDto dto) throws ControllerException, ServiceException {
		
		CompteExterne compteExterne = (CompteExterne) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.EXTERNE, 
				identifiant, 
				OBLIGATOIRE);
		compteExterne = mapperModificationRequestDtoToModel(dto, compteExterne);
		compteExterne = compteExterneService.modifierCompte(compteExterne);
		return CompteExterneResponseDtoMapper.mapperModelToSimpleResponseDto(compteExterne);
	}

	@DeleteMapping("/del/{identifiant}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerCompte(
			@PathVariable String identifiant) throws ControllerException, ServiceException {

		CompteExterne compteExterne = (CompteExterne) verificateur.verifierCompteEtTypeCompte(
				TypeCompte.EXTERNE, 
				identifiant, 
				OBLIGATOIRE);
		compteExterneService.supprimerCompte(compteExterne);
	}

	@PostMapping("/selection")
	public List<CompteResponseDto> selectionnerComptes(
			@RequestBody CompteExterneSelectionRequestDto requestDto) throws ControllerException, ServiceException {

		final String identifiant = verificateur.standardiserIdentifiantFonctionnel(requestDto.identifiantContient);
		final String libelle = verificateur.verifierLibelle(requestDto.libelleContient, FACULTATIF, null);

		return compteExterneService.rechercherTous()
				.stream()
				.filter((c) -> {return identifiant == null 
						|| c.getIdentifiant().contains(identifiant);})
				.filter((c) -> {return libelle == null 
						|| c.getLibelle().toUpperCase().contains(libelle.toUpperCase());})
				.sorted((c1, c2) -> {return c1.getIdentifiant().compareTo(c2.getIdentifiant());})
				.map((c) -> {return CompteExterneResponseDtoMapper.mapperModelToBasicResponseDto(c);})
				.toList();
	}

	private CompteExterne mapperCreationRequestDtoToModel(CompteExterneRequestDto dto, CompteExterne compteExterne) throws ControllerException, ServiceException {
		
		compteExterne.setIdentifiant(verificateur.verifierIdentifiantValideEtUnique(dto.identifiant, compteExterne.getId(), OBLIGATOIRE));
		compteExterne.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return compteExterne;
	}
	
	private CompteExterne mapperModificationRequestDtoToModel(CompteExterneRequestDto dto, CompteExterne compteExterne) throws ControllerException, ServiceException {

		if ( dto.identifiant != null ) compteExterne.setIdentifiant(verificateur.verifierIdentifiantValideEtUnique(dto.identifiant, compteExterne.getId(), OBLIGATOIRE));
		if ( dto.libelle  != null ) compteExterne.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));

		return compteExterne;
	}
	
}
