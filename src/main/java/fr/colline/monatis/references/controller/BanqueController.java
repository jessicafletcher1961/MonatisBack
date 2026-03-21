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
import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.references.controller.dto.banques.BanqueBasicResponseDto;
import fr.colline.monatis.references.controller.dto.banques.BanqueDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.banques.BanqueRequestDto;
import fr.colline.monatis.references.controller.mapper.BanqueDtoMapper;
import fr.colline.monatis.references.service.BanqueService;

@RestController
@RequestMapping("/monatis/references/banque")
@Transactional
public class BanqueController {

	@Autowired private BanqueService banqueService;

	@GetMapping("/all")
	public List<BanqueBasicResponseDto> getAllBanques() throws ServiceException {

		List<BanqueBasicResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("nom");
		List<Banque> liste = banqueService.rechercherTous(tri);
		for ( Banque banque : liste ) {
			resultat.add(BanqueDtoMapper.modelToBasicResponseDto(banque));
		}
		return resultat;
	}

	@GetMapping("/get/{nom}")
	public BanqueDetailedResponseDto getBanqueParNom(
			@PathVariable(name = "nom") String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			Banque banque = banqueService.rechercherParNom(nom);
			if ( banque == null ) {
				throw new ControllerException(
						ErreurControle.BANQUE_NON_TROUVEE_PAR_NOM,
						nom);
			}
			return BanqueDtoMapper.modelToDetailedResponseDto(banque);
		} 
		catch (Throwable t) {
			throw new ControllerException(
					t, 
					ErreurControle.BANQUE_CONSULTATION_PROBLEME,
					nom);
		}
	}

	@PostMapping("/new")
	public BanqueDetailedResponseDto creerBanque(
			@RequestBody BanqueRequestDto dto) throws ControllerException {

		try {
			Banque banque = new Banque();
			banque = creationRequestDtoToModel(banque, dto);
			banque = banqueService.creerReference(banque);
			return BanqueDtoMapper.modelToDetailedResponseDto(banque);
		}
		catch (Throwable t) {
			throw new ControllerException(
					t, 
					ErreurControle.BANQUE_CREATION_PROBLEME);
		}
	}

	@PutMapping("/mod/{nom}")
	public BanqueDetailedResponseDto modifierBanque(
			@PathVariable(name = "nom") String nom,
			@RequestBody BanqueRequestDto dto) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			Banque banque = banqueService.rechercherParNom(nom);
			if ( banque == null ) {
				throw new ControllerException(
						ErreurControle.BANQUE_NON_TROUVEE_PAR_NOM,
						nom);
			}
			banque = modificationRequestDtoToModel(banque, dto);
			banque = banqueService.modifierReference(banque);
			return BanqueDtoMapper.modelToDetailedResponseDto(banque);
		}
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.BANQUE_MODIFICATION_PROBLEME,
					nom);
		}
	}

	@DeleteMapping("/del/{nom}")
	public void supprimerBanque(
			@PathVariable(name = "nom") String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_NOM_OBLIGATOIRE);
		}

		try {
			Banque banque = banqueService.rechercherParNom(nom);
			if ( banque == null ) {
				throw new ControllerException(
						ErreurControle.BANQUE_NON_TROUVEE_PAR_NOM,
						nom);
			}
			banqueService.supprimerReference(banque.getId());
		}
		catch (Throwable t) {
			throw new ControllerException(
					t,
					ErreurControle.BANQUE_SUPPRESSION_PROBLEME,
					nom);
		}
	}

	private Banque creationRequestDtoToModel(
			Banque banque,
			BanqueRequestDto dto) throws ControllerException {

		banque.setNom(verifierNom(dto.nom));
		banque.setLibelle(verifierLibelle(dto.libelle));

		return banque;
	}

	private Banque modificationRequestDtoToModel(
			Banque banque,
			BanqueRequestDto dto) throws ControllerException {

		if ( dto.nom != null ) banque.setNom(verifierNom(dto.nom));
		if ( dto.libelle != null ) banque.setLibelle(verifierLibelle(dto.libelle));

		return banque;
	}

	private String verifierNom(String nom) throws ControllerException {

		if ( nom == null || nom.isBlank() ) { 
			throw new ControllerException(
					ErreurControle.BANQUE_NOM_OBLIGATOIRE);
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
