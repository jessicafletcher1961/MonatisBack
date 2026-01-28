package fr.colline.monatis.references.controller.banque;

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

import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.controller.ReferenceResponseDto;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.TypeReference;
import fr.colline.monatis.references.service.BanqueService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/references/banque")
@Transactional
public class BanqueController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private BanqueService banqueService;

	@GetMapping("/all")
	public List<ReferenceResponseDto> getAllReference() throws ServiceException {
		
		List<ReferenceResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<Banque> liste = banqueService.rechercherTous(tri);
		for ( Banque banque : liste ) {
			resultat.add(BanqueResponseDtoMapper.mapperModelToBasicResponseDto(banque));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public ReferenceResponseDto getReferenceParNom(@PathVariable String nom) throws ServiceException, ControllerException {

		Banque banque = verificateur.verifierBanque(nom, OBLIGATOIRE);
		return BanqueResponseDtoMapper.mapperModelToDetailedResponseDto(banque);
	}

	@PostMapping("/new")
	public ReferenceResponseDto creerReference(@RequestBody BanqueRequestDto dto) throws ServiceException, ControllerException {
		
		Banque banque = new Banque();
		banque = mapperCreationRequestDtoToModel(dto, banque);
		banque = banqueService.creerReference(banque);
		return BanqueResponseDtoMapper.mapperModelToSimpleResponseDto(banque);
	}

	@PutMapping("/mod/{nom}")
	public ReferenceResponseDto modifierReference(
			@PathVariable String nom,
			@RequestBody BanqueRequestDto dto) throws ControllerException, ServiceException {
		
		Banque banque = verificateur.verifierBanque(nom, OBLIGATOIRE);
		banque = mapperModificationRequestDtoToModel(dto, banque);
		banque = banqueService.modifierReference(banque);
		return BanqueResponseDtoMapper.mapperModelToSimpleResponseDto(banque);
	}

	@DeleteMapping("/del/{nom}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerReference(@PathVariable String nom) throws ControllerException, ServiceException {
		
		Banque banque = verificateur.verifierBanque(nom, OBLIGATOIRE);
		banqueService.supprimerReference(banque);
	}

	private Banque mapperCreationRequestDtoToModel(BanqueRequestDto dto, Banque banque) throws ControllerException, ServiceException {
		
		banque.setNom(verificateur.verifierNomValideEtUnique(TypeReference.BANQUE, dto.nom, banque.getId(), OBLIGATOIRE));
		banque.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return banque;
	}
	
	private Banque mapperModificationRequestDtoToModel(BanqueRequestDto dto, Banque banque) throws ControllerException, ServiceException {

		if ( dto.nom != null ) banque.setNom(verificateur.verifierNomValideEtUnique(TypeReference.BANQUE, dto.nom, banque.getId(), OBLIGATOIRE));
		if ( dto.libelle != null ) banque.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return banque;
	}

}
