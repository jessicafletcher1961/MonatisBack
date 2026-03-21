package fr.colline.monatis.comptes.controller;

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

import fr.colline.monatis.comptes.controller.dto.request.CompteTiersRequestDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteTiersBasicResponseDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteTiersDetailedResponseDto;
import fr.colline.monatis.comptes.controller.mapper.CompteTiersDtoMapper;
import fr.colline.monatis.comptes.model.CompteTiers;
import fr.colline.monatis.comptes.service.CompteTiersService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurControle;

@RestController
@RequestMapping("/monatis/comptes/tiers")
@Transactional
public class CompteTiersController {

	@Autowired private CompteTiersService compteTiersService;

	@GetMapping("/all")
	public List<CompteTiersBasicResponseDto> getAllComptesTiers() throws ServiceException {

		List<CompteTiersBasicResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("identifiant");
		List<CompteTiers> liste = compteTiersService.rechercherTous(tri);
		for ( CompteTiers compteTiers : liste ) {
			resultat.add(CompteTiersDtoMapper.modelToBasicResponseDto(compteTiers));
		}
		return resultat;
	}

	@GetMapping("/get/{identifiant}")
	public CompteTiersDetailedResponseDto getCompteTiersParIdentifiant(
			@PathVariable(name = "identifiant") String identifiant) throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		try {
			CompteTiers compteTiers = compteTiersService.rechercherParIdentifiant(identifiant);
			if ( compteTiers == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_TIERS_NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			return CompteTiersDtoMapper.modelToDetailedResponseDto(compteTiers);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_TIERS_CONSULTATION_PROBLEME,
					identifiant);
		}
	}

	@PostMapping("/new")
	public CompteTiersDetailedResponseDto creerCompteTiers(
			@RequestBody CompteTiersRequestDto dto) throws ControllerException {

		try {
			CompteTiers compteTiers = new CompteTiers();
			compteTiers = creationRequestDtoToModel(compteTiers, dto);
			compteTiers = compteTiersService.creerCompte(compteTiers);
			return CompteTiersDtoMapper.modelToDetailedResponseDto(compteTiers);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_TIERS_CREATION_PROBLEME,
					dto.identifiant);
		}
	}

	@PutMapping("/mod/{identifiant}")
	public CompteTiersDetailedResponseDto modifierCompteTiers(
			@PathVariable(name = "identifiant") String identifiant,
			@RequestBody CompteTiersRequestDto dto) throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		try {
			CompteTiers compteTiers = compteTiersService.rechercherParIdentifiant(identifiant);
			if ( compteTiers == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_TIERS_NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			compteTiers = modificationRequestDtoToModel(compteTiers, dto);
			compteTiers = compteTiersService.modifierCompte(compteTiers);
			return CompteTiersDtoMapper.modelToDetailedResponseDto(compteTiers);
		}
		catch ( Throwable t ) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_TIERS_MODIFICATION_PROBLEME,
					identifiant);
		}
	}

	@DeleteMapping("/del/{identifiant}")
	public void supprimerCompteTiers(
			@PathVariable(name = "identifiant") String identifiant) 
					throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		try {
			CompteTiers compteTiers = compteTiersService.rechercherParIdentifiant(identifiant);
			if ( compteTiers == null ) {
				throw new ControllerException(
						ErreurControle.COMPTE_TIERS_NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			compteTiersService.supprimerCompte(compteTiers.getId());
		}
		catch ( Throwable t ) {
			throw new ControllerException(
					t,
					ErreurControle.COMPTE_TIERS_SUPPRESSION_PROBLEME,
					identifiant);
		}
	}

	private CompteTiers creationRequestDtoToModel(
			CompteTiers compteTiers,
			CompteTiersRequestDto dto) throws ControllerException {
		
		compteTiers.setIdentifiant(verifierIdentifiant(dto.identifiant));
		compteTiers.setLibelle(verifierLibelle(dto.libelle));
		
		return compteTiers;
	}
	
	private CompteTiers modificationRequestDtoToModel(
			CompteTiers compteTiers,
			CompteTiersRequestDto dto) throws ControllerException {

		if ( dto.identifiant != null ) compteTiers.setIdentifiant(verifierIdentifiant(dto.identifiant));
		if ( dto.libelle != null ) compteTiers.setLibelle(verifierLibelle(dto.libelle));

		return compteTiers;
	}

	private String verifierIdentifiant(String identifiant) throws ControllerException {
		
		if ( identifiant == null || identifiant.isBlank() ) { 
			throw new ControllerException(
					ErreurControle.COMPTE_TIERS_IDENTIFIANT_OBLIGATOIRE);
		}
		return identifiant;
	}

	private String verifierLibelle(String libelle) {
		
		if ( libelle == null || libelle.isBlank() ) {
			libelle = null;
		}
		return libelle;
	}
}
