package fr.colline.monatis.evaluations.controller;

import java.time.LocalDate;
import java.util.Comparator;
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

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.typologies.model.TypeCompte;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/evaluations")
@Transactional
public class EvaluationController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private EvaluationService evaluationService;
	
	@GetMapping("/all")
	public List<EvaluationResponseDto> getAllEvaluation() throws ServiceException, ControllerException {

		return evaluationService.rechercherTous()
				.stream()
				.sorted((e1, e2) -> {return e1.getCle().compareTo(e2.getCle());})
				.map((e) -> {return EvaluationResponseDtoMapper.mapperModelToBasicResponseDto(e);})
				.toList();
	}

	@GetMapping("/get/{cle}")
	public EvaluationResponseDto getEvaluationParCle(@PathVariable String cle) throws ControllerException, ServiceException {

		Evaluation evaluation = verificateur.verifierEvaluation(
				cle, 
				OBLIGATOIRE);
		return EvaluationResponseDtoMapper.mapperModelToDetailedResponseDto(evaluation);
	}

	@PostMapping("/new")
	public EvaluationResponseDto creerEvaluation(@RequestBody EvaluationCreationRequestDto dto) throws ControllerException, ServiceException {

		Evaluation evaluation = new Evaluation();
		evaluation = mapperCreationRequestDtoToModel(dto, evaluation);
		evaluation = evaluationService.creerEvaluation(evaluation);
		return EvaluationResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation);
	}

	@PutMapping("/mod/{cle}")
	public EvaluationResponseDto modifierEvaluation(
			@PathVariable String cle, 
			@RequestBody EvaluationModificationRequestDto dto) throws ControllerException, ServiceException {

		Evaluation evaluation = verificateur.verifierEvaluation(cle, OBLIGATOIRE);
		evaluation = mapperModificationRequestDtoToModel(dto, evaluation);
		evaluation = evaluationService.modifierEvaluation(evaluation);
		return EvaluationResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation);
	}

	@DeleteMapping("/del/{cle}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerEvaluation(@PathVariable String cle) throws ControllerException, ServiceException {

		Evaluation evaluation = verificateur.verifierEvaluation(cle, OBLIGATOIRE);
		evaluationService.supprimerEvaluation(evaluation);
	}

	@PostMapping("/selection")
	public List<EvaluationResponseDto>selectionnerEvaluation(
			@RequestBody EvaluationSelectionRequestDto requestDto) throws ServiceException, ControllerException {

		final String cle = verificateur.standardiserIdentifiantFonctionnel(requestDto.cleContient);
		final String libelle = verificateur.verifierLibelle(requestDto.libelleContient, FACULTATIF, null);
		final Compte compte = verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, requestDto.identifiantCompteInterne, FACULTATIF);
		final LocalDate avantLe = verificateur.verifierDate(requestDto.avantLe, FACULTATIF, null);
		
		return evaluationService.rechercherTous()
				.stream()
				.filter((e) -> {return cle == null 
						|| e.getCle().contains(cle);})
				.filter((e) -> {return libelle == null 
						|| e.getLibelle().toUpperCase().contains(libelle.toUpperCase());})
				.filter((e) -> {return compte == null 
						|| e.getCompteInterne().getId().equals(compte.getId());})
				.filter((e) -> {return avantLe == null
						|| e.getDateSolde().isBefore(avantLe);})
				.sorted(Comparator.comparing(Evaluation::getDateSolde, Comparator.reverseOrder()))
				.map((o) -> {return EvaluationResponseDtoMapper.mapperModelToBasicResponseDto(o);})
				.toList();
	}

	private Evaluation mapperCreationRequestDtoToModel(
			EvaluationCreationRequestDto requestDto, 
			Evaluation evaluation) throws ControllerException, ServiceException {

		evaluation.setCle(verificateur.verifierCleEvaluationValideEtUnique(requestDto.cle, evaluation.getId(), FACULTATIF));
		evaluation.setCompteInterne((CompteInterne) verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, requestDto.identifiantCompteInterne, OBLIGATOIRE));
		evaluation.setDateSolde(verificateur.verifierDate(requestDto.dateSolde, FACULTATIF, LocalDate.now()));
		evaluation.setLibelle(verificateur.verifierLibelle(requestDto.libelle, FACULTATIF, null));
		evaluation.setMontantSoldeEnCentimes(verificateur.verifierMontantEnCentimes(requestDto.montantSoldeEnCentimes, OBLIGATOIRE, null));

		return evaluation;
	}

	private Evaluation mapperModificationRequestDtoToModel(
			EvaluationModificationRequestDto dto, 
			Evaluation evaluation) throws ControllerException, ServiceException {

		if ( dto.cle != null ) evaluation.setCle(verificateur.verifierCleEvaluationValideEtUnique(dto.cle, evaluation.getId(), OBLIGATOIRE));
		if ( dto.identifiantCompteInterne != null ) evaluation.setCompteInterne((CompteInterne) verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, dto.identifiantCompteInterne, OBLIGATOIRE));
		if ( dto.dateSolde != null ) evaluation.setDateSolde(verificateur.verifierDate(dto.dateSolde, OBLIGATOIRE, null));
 		if ( dto.libelle != null ) evaluation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		if ( dto.montantSoldeEnCentimes != null ) evaluation.setMontantSoldeEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantSoldeEnCentimes, OBLIGATOIRE, null));

		return evaluation;
	}
	
}
