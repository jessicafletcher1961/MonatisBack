package fr.colline.monatis.emprunts.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.emprunts.controller.request.ConditionEmpruntRequestDto;
import fr.colline.monatis.emprunts.controller.request.EmpruntCreationRequestDto;
import fr.colline.monatis.emprunts.controller.request.EmpruntModificationRequestDto;
import fr.colline.monatis.emprunts.controller.response.EcheanceResponseDto;
import fr.colline.monatis.emprunts.controller.response.EmpruntBasicResponseDto;
import fr.colline.monatis.emprunts.controller.response.EmpruntDetailedResponseDto;
import fr.colline.monatis.emprunts.controller.response.EmpruntSimpleResponseDto;
import fr.colline.monatis.emprunts.model.ConditionEmprunt;
import fr.colline.monatis.emprunts.model.Echeance;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.emprunts.service.EmpruntService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.typologies.model.TypeCompte;
import fr.colline.monatis.typologies.model.TypePeriode;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/emprunts")
@Transactional
public class EmpruntController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private EmpruntService empruntService;
	@Autowired private ControllerVerificateurService verificateur;
	
	@GetMapping("/all")
	public List<EmpruntBasicResponseDto> getAllEmprunt() throws ServiceException, ControllerException {

		return empruntService.rechercherTous()
				.stream()
				.sorted((e1, e2) -> {return e1.getConditionEmpruntInitiale().getDatePremiereEcheance().compareTo(e2.getConditionEmpruntInitiale().getDatePremiereEcheance());})
				.map((e) -> {return EmpruntResponseDtoMapper.mapperModelToBasicResponseDto(e);})
				.toList();
	}

	@GetMapping("/get/{cle}")
	public EmpruntDetailedResponseDto getEmpruntParCle(@PathVariable String cle) throws ControllerException, ServiceException {

		Emprunt emprunt = verificateur.verifierEmprunt(
				cle, 
				OBLIGATOIRE);
		empruntService.genererEcheances(emprunt);
		return EmpruntResponseDtoMapper.mapperModelToDetailedResponseDto(emprunt);
	}

	@PostMapping("/new")
	public EmpruntSimpleResponseDto creerEmprunt(@RequestBody EmpruntCreationRequestDto dto) throws ControllerException, ServiceException {

		Emprunt emprunt = new Emprunt();
		emprunt = mapperCreationRequestDtoToModel(dto, emprunt);
		emprunt = empruntService.creerEmprunt(emprunt);
		return EmpruntResponseDtoMapper.mapperModelToSimpleResponseDto(emprunt);
	}

	@PutMapping("/mod/{cle}")
	public EmpruntSimpleResponseDto modifierEmprunt(
			@PathVariable String cle, 
			@RequestBody EmpruntModificationRequestDto dto) throws ControllerException, ServiceException {

		Emprunt emprunt = verificateur.verifierEmprunt(cle, OBLIGATOIRE);
		emprunt = mapperModificationRequestDtoToModel(dto, emprunt);
		emprunt = empruntService.modifierEmprunt(emprunt);
		return EmpruntResponseDtoMapper.mapperModelToSimpleResponseDto(emprunt);
	}

	@DeleteMapping("/del/{cle}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerEmprunt(@PathVariable String cle) throws ControllerException, ServiceException {

		Emprunt Emprunt = verificateur.verifierEmprunt(cle, OBLIGATOIRE);
		empruntService.supprimerEmprunt(Emprunt);
	}

	@GetMapping("/get/{cleEmprunt}/date/{dateEcheance}")
	public ResponseEntity<EcheanceResponseDto> rechercherEcheanceParDate(
			@PathVariable String cleEmprunt,
			@PathVariable LocalDate dateEcheance) throws ControllerException, ServiceException {
		
		Emprunt emprunt = verificateur.verifierEmprunt(cleEmprunt, OBLIGATOIRE);
		final int MARGE_APPROXIMATION_EN_JOURS = 3;
		
		Optional<Echeance> echeanceTrouvee = empruntService.genererEcheances(emprunt)
				.stream()
				.filter((e) -> {return ! e.getDateEcheance().isBefore(dateEcheance.minus(MARGE_APPROXIMATION_EN_JOURS, ChronoUnit.DAYS));})
				.filter((e) -> {return ! e.getDateEcheance().isAfter(dateEcheance.plus(MARGE_APPROXIMATION_EN_JOURS, ChronoUnit.DAYS));})
				.findFirst();
		if ( echeanceTrouvee.isEmpty() ) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(EmpruntResponseDtoMapper.mapperModelToResponseDto(echeanceTrouvee.get()), HttpStatus.OK);
	}
	
	@GetMapping("/get/{cleEmprunt}/numero/{numeroEcheance}")
	public ResponseEntity<EcheanceResponseDto> rechercherEcheanceParNumero(
			@PathVariable String cleEmprunt,
			@PathVariable int numeroEcheance) throws ControllerException, ServiceException {
		
		Emprunt emprunt = verificateur.verifierEmprunt(cleEmprunt, OBLIGATOIRE);
		Optional<Echeance> echeanceTrouvee = empruntService.genererEcheances(emprunt)
				.stream()
				.filter((e) -> {return e.getNumeroEcheance() == numeroEcheance;})
				.findFirst();
		if ( echeanceTrouvee.isEmpty() ) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(EmpruntResponseDtoMapper.mapperModelToResponseDto(echeanceTrouvee.get()), HttpStatus.OK);
	}

	private Emprunt mapperCreationRequestDtoToModel(EmpruntCreationRequestDto dto, Emprunt emprunt) throws ControllerException, ServiceException {

		emprunt.setCle(verificateur.verifierCleEmpruntValideEtUnique(dto.cle, emprunt.getId(), FACULTATIF));
		emprunt.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		emprunt.setCompteInterne((CompteInterne) verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, dto.identifiantCompteInterne, FACULTATIF));
		emprunt.getConditionsEmprunt().add(mapperCreationRequestDtoToModel(dto.conditionEmpruntInitiale, emprunt));
		
		return emprunt;
	}
	
	private Emprunt mapperModificationRequestDtoToModel(EmpruntModificationRequestDto dto, Emprunt emprunt) throws ControllerException, ServiceException {

		if ( dto.cle != null ) emprunt.setCle(verificateur.verifierCleEmpruntValideEtUnique(dto.cle, emprunt.getId(), OBLIGATOIRE));
		if ( dto.libelle != null ) emprunt.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		if ( dto.identifiantCompteInterne != null ) emprunt.setCompteInterne((CompteInterne) verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, dto.identifiantCompteInterne, FACULTATIF));
		if ( dto.conditionsEmprunt != null && ! dto.conditionsEmprunt.isEmpty() ) {
			emprunt = mapperModificationRequestDtoToModel(dto.conditionsEmprunt, emprunt);
		}
		
		return emprunt;
	}
	
	private Emprunt mapperModificationRequestDtoToModel(List<ConditionEmpruntRequestDto> listeDto, Emprunt emprunt) throws ControllerException {

		List<ConditionEmprunt> nouvellesConditionEmprunt = new ArrayList<ConditionEmprunt>();
		for ( ConditionEmpruntRequestDto dto : listeDto ) {
			ConditionEmprunt conditionEmprunt = rechercherParNumeroPremiereEcheance(emprunt, dto.numeroPremiereEcheance);
			if ( conditionEmprunt == null ) {
				conditionEmprunt = mapperCreationRequestDtoToModel(dto, emprunt); 
			}
			else {
				mapperModificationRequestDtoToModel(dto, conditionEmprunt);
			}
			nouvellesConditionEmprunt.add(conditionEmprunt);
		}
		emprunt.changerConditionsEmprunt(nouvellesConditionEmprunt);
		
		return emprunt;
	}

	private ConditionEmprunt mapperCreationRequestDtoToModel(ConditionEmpruntRequestDto dto, Emprunt emprunt) throws ControllerException {

		ConditionEmprunt conditionEmprunt = new ConditionEmprunt();
		conditionEmprunt.setEmprunt(emprunt);
		
		conditionEmprunt.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		conditionEmprunt.setTauxAnnuel(verificateur.verifierNombre(dto.tauxAnnuel, OBLIGATOIRE, null, 0d, 100d));
		conditionEmprunt.setCapitalEmprunteEnCentimes(verificateur.verifierNombre(dto.capitalEmprunteEnCentimes, OBLIGATOIRE, null, 100L, null));
		conditionEmprunt.setDuree(verificateur.verifierNombre(dto.duree, OBLIGATOIRE, null, 1, null));
		conditionEmprunt.setTypePeriodeEcheances(verificateur.verifierTypePeriode(dto.codeTypePeriodeEcheances, FACULTATIF, TypePeriode.MENSUEL));
		conditionEmprunt.setNumeroPremiereEcheance(verificateur.verifierNombre(dto.numeroPremiereEcheance, FACULTATIF, 1, 1, null));
		conditionEmprunt.setDatePremiereEcheance(verificateur.verifierDate(dto.datePremiereEcheance, OBLIGATOIRE, null));
		conditionEmprunt.setMontantFraisFixesEcheanceEnCentimes(verificateur.verifierNombre(dto.montantFraisFixesEcheanceEnCentimes, OBLIGATOIRE, null, 0L, null));
		conditionEmprunt.setMontantTotalEcheanceEnCentimes(verificateur.verifierNombre(dto.montantTotalEcheanceEnCentimes, OBLIGATOIRE, null, 0L, null));

		return conditionEmprunt;
	}

	private void mapperModificationRequestDtoToModel(ConditionEmpruntRequestDto dto, ConditionEmprunt conditionEmprunt) throws ControllerException {

		if ( dto.libelle != null ) conditionEmprunt.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		if ( dto.tauxAnnuel != null )  conditionEmprunt.setTauxAnnuel(verificateur.verifierNombre(dto.tauxAnnuel, OBLIGATOIRE, null, 0d, 100d));
		if ( dto.capitalEmprunteEnCentimes != null ) conditionEmprunt.setCapitalEmprunteEnCentimes(verificateur.verifierNombre(dto.capitalEmprunteEnCentimes, OBLIGATOIRE, null, 100L, null));
		if ( dto.duree != null ) conditionEmprunt.setDuree(verificateur.verifierNombre(dto.duree, OBLIGATOIRE, null, 1, null));
		if ( dto.codeTypePeriodeEcheances != null ) conditionEmprunt.setTypePeriodeEcheances(verificateur.verifierTypePeriode(dto.codeTypePeriodeEcheances, FACULTATIF, TypePeriode.MENSUEL));
		if ( dto.numeroPremiereEcheance != null )  conditionEmprunt.setNumeroPremiereEcheance(verificateur.verifierNombre(dto.numeroPremiereEcheance, FACULTATIF, 1, 1, null));
		if ( dto.datePremiereEcheance != null )  conditionEmprunt.setDatePremiereEcheance(verificateur.verifierDate(dto.datePremiereEcheance, OBLIGATOIRE, null));
		if ( dto.montantFraisFixesEcheanceEnCentimes != null ) conditionEmprunt.setMontantFraisFixesEcheanceEnCentimes(verificateur.verifierNombre(dto.montantFraisFixesEcheanceEnCentimes, OBLIGATOIRE, null, 0L, null));
		if ( dto.montantTotalEcheanceEnCentimes != null ) conditionEmprunt.setMontantTotalEcheanceEnCentimes(verificateur.verifierNombre(dto.montantTotalEcheanceEnCentimes, OBLIGATOIRE, null, 0L, null));
	}

	private ConditionEmprunt rechercherParNumeroPremiereEcheance(Emprunt emprunt, int numeroSequence) {

		for ( ConditionEmprunt conditionEmpruntExistante : emprunt.getConditionsEmprunt() ) {
			if ( numeroSequence == conditionEmpruntExistante.getNumeroPremiereEcheance() ) {
				return conditionEmpruntExistante;
			}
		}
		
		return null;
	}
	
}
