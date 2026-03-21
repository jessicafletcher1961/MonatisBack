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
import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireBasicResponseDto;
import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireRequestDto;
import fr.colline.monatis.references.controller.mapper.BeneficiaireDtoMapper;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.service.BeneficiaireService;

@RestController
@RequestMapping("/monatis/references/beneficiaire")
@Transactional
public class BeneficiaireController {

	@Autowired private BeneficiaireService beneficiaireService;

	@GetMapping("/all")
	public List<BeneficiaireBasicResponseDto> getAllBeneficiaire() throws ServiceException {

		List<BeneficiaireBasicResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<Beneficiaire> liste = beneficiaireService.rechercherTous(tri);
		for ( Beneficiaire beneficiaire : liste ) {
			resultat.add(BeneficiaireDtoMapper.modelToBasicResponseDto(beneficiaire));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public BeneficiaireDetailedResponseDto getBeneficiaireParNom(
			@PathVariable String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			Beneficiaire beneficiaire = beneficiaireService.rechercherParNom(nom);
			if ( beneficiaire == null ) {
				throw new ControllerException(
						ErreurControle.BENEFICIAIRE_NON_TROUVE_PAR_NOM,
						nom);
			}
			return BeneficiaireDtoMapper.modelToDetailedResponseDto(beneficiaire);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.BENEFICIAIRE_CONSULTATION_PROBLEME,
					nom);
		}
	}

	@PostMapping("/new")
	public BeneficiaireDetailedResponseDto creerBeneficiaire(@RequestBody BeneficiaireRequestDto dto) 
			throws ControllerException {
		
		try {
			Beneficiaire beneficiaire = new Beneficiaire();
			beneficiaire = creationRequestDtoToModel(beneficiaire, dto);
			beneficiaire = beneficiaireService.creerReference(beneficiaire);
			return BeneficiaireDtoMapper.modelToDetailedResponseDto(beneficiaire);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.BENEFICIAIRE_CREATION_PROBLEME);
		}
	}

	@PutMapping("/mod/{nom}")
	public BeneficiaireDetailedResponseDto modifierBeneficiaire(
			@PathVariable(name = "nom") String nom,
			@RequestBody BeneficiaireRequestDto dto) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}
		
		try {
			Beneficiaire beneficiaire = beneficiaireService.rechercherParNom(nom);
			if ( beneficiaire == null ) {
				throw new ControllerException(
						ErreurControle.BENEFICIAIRE_NON_TROUVE_PAR_NOM,
						nom);
			}
			beneficiaire = modificationRequestDtoToModel(beneficiaire, dto);
			beneficiaire = beneficiaireService.modifierReference(beneficiaire);
			return BeneficiaireDtoMapper.modelToDetailedResponseDto(beneficiaire);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.BENEFICIAIRE_MODIFICATION_PROBLEME,
					nom);
		}
	}

	@DeleteMapping("/del/{nom}")
	public void supprimerBeneficiaire(@PathVariable(name = "nom") String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try  {
			Beneficiaire beneficiaire = beneficiaireService.rechercherParNom(nom);
			if ( beneficiaire == null ) {
				throw new ControllerException(
						ErreurControle.BENEFICIAIRE_NON_TROUVE_PAR_NOM,
						nom);
			}
			beneficiaireService.supprimerReference(beneficiaire.getId());
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.BENEFICIAIRE_SUPPRESSION_PROBLEME,
					nom);
		}
	}
	
	private Beneficiaire creationRequestDtoToModel(
			Beneficiaire beneficiaire,
			BeneficiaireRequestDto dto) throws ControllerException {
		
		beneficiaire.setNom(verifierNom(dto.nom));
		beneficiaire.setLibelle(verifierLibelle(dto.libelle));
		
		return beneficiaire;
	}
	
	private Beneficiaire modificationRequestDtoToModel(
			Beneficiaire beneficiaire,
			BeneficiaireRequestDto dto) throws ControllerException {
		
		if ( dto.nom != null ) beneficiaire.setNom(verifierNom(dto.nom));
		if ( dto.libelle != null ) beneficiaire.setLibelle(verifierLibelle(dto.libelle));
		
		return beneficiaire;
	}
	
	private String verifierNom(String nom) throws ControllerException {
		
		if ( nom == null || nom.isBlank() ) { 
			throw new ControllerException(
					ErreurControle.BENEFICIAIRE_NOM_OBLIGATOIRE);
		}
		return nom;
	}
	
	private String verifierLibelle(String libelle) {
		
		if ( libelle == null || libelle.isBlank() ) {
			libelle = null;
		}
		return libelle;
	}
}
