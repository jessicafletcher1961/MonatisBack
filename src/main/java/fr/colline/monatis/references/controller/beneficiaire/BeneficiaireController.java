package fr.colline.monatis.references.controller.beneficiaire;

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
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.TypeReference;
import fr.colline.monatis.references.service.BeneficiaireService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/references/beneficiaire")
@Transactional
public class BeneficiaireController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private BeneficiaireService beneficiaireService;

	@GetMapping("/all")
	public List<ReferenceResponseDto> getAllReference() throws ServiceException {
		
		List<ReferenceResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<Beneficiaire> liste = beneficiaireService.rechercherTous(tri);
		for ( Beneficiaire beneficiaire : liste ) {
			resultat.add(BeneficiaireResponseDtoMapper.mapperModelToSimpleResponseDto(beneficiaire));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public ReferenceResponseDto getReferenceParNom(@PathVariable String nom) throws ServiceException, ControllerException {

		Beneficiaire beneficiaire = verificateur.verifierBeneficiaire(nom, OBLIGATOIRE);
		return BeneficiaireResponseDtoMapper.mapperModelToDetailedResponseDto(beneficiaire);
	}

	@PostMapping("/new")
	public ReferenceResponseDto creerReference(@RequestBody BeneficiaireRequestDto dto) throws ServiceException, ControllerException {
		
		Beneficiaire beneficiaire = new Beneficiaire();
		beneficiaire = mapperCreationRequestDtoToModel(dto, beneficiaire);
		beneficiaire = beneficiaireService.creerReference(beneficiaire);
		return BeneficiaireResponseDtoMapper.mapperModelToDetailedResponseDto(beneficiaire);
	}

	@PutMapping("/mod/{nom}")
	public ReferenceResponseDto modifierReference(@PathVariable String nom, @RequestBody BeneficiaireRequestDto dto) throws ControllerException, ServiceException {
		
		Beneficiaire beneficiaire = verificateur.verifierBeneficiaire(nom, OBLIGATOIRE);
		beneficiaire = mapperModificationRequestDtoToModel(dto, beneficiaire);
		beneficiaire = beneficiaireService.modifierReference(beneficiaire);
		return BeneficiaireResponseDtoMapper.mapperModelToDetailedResponseDto(beneficiaire);
	}

	@DeleteMapping("/del/{nom}")
	public void supprimerReference(@PathVariable String nom) throws ControllerException, ServiceException {

		Beneficiaire beneficiaire = verificateur.verifierBeneficiaire(nom, OBLIGATOIRE);
		beneficiaireService.supprimerReference(beneficiaire);
	}

	private Beneficiaire mapperCreationRequestDtoToModel(BeneficiaireRequestDto dto, Beneficiaire beneficiaire) throws ControllerException, ServiceException {
		
		beneficiaire.setNom(verificateur.verifierNomValideEtUnique(TypeReference.BENEFICIAIRE, dto.nom, beneficiaire.getId(), OBLIGATOIRE));
		beneficiaire.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return beneficiaire;
	}
	
	private Beneficiaire mapperModificationRequestDtoToModel(BeneficiaireRequestDto dto, Beneficiaire beneficiaire) throws ControllerException, ServiceException {

		if ( dto.nom != null ) beneficiaire.setNom(verificateur.verifierNomValideEtUnique(TypeReference.BENEFICIAIRE, dto.nom, beneficiaire.getId(), OBLIGATOIRE));
		if ( dto.libelle != null ) beneficiaire.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return beneficiaire;
	}
	
}
