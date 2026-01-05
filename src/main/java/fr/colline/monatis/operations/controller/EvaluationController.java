package fr.colline.monatis.operations.controller;

import java.time.LocalDate;
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

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.controller.request.EvaluationCreationRequestDto;
import fr.colline.monatis.operations.controller.request.EvaluationModificationRequestDto;
import fr.colline.monatis.operations.controller.response.EvaluationDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.EvaluationSimpleResponseDto;
import fr.colline.monatis.operations.model.Evaluation;
import fr.colline.monatis.operations.service.EvaluationService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/evaluations")
@Transactional
public class EvaluationController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private EvaluationService evaluationService;
	
	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteTechniqueService compteTechniqueService;
	
	@GetMapping("/all")
	public List<EvaluationSimpleResponseDto> getAllEvaluation() throws ServiceException, ControllerException {

		List<EvaluationSimpleResponseDto> resultat = new ArrayList<>();
		
		List<CompteInterne> comptesInternes = compteInterneService.rechercherTous(Sort.by("identifiant"));
		for ( CompteInterne compteInterne : comptesInternes ) {
			List<Evaluation> evaluations = evaluationService.rechercherParCompteInterneId(compteInterne.getId());
			for ( Evaluation evaluation : evaluations ) {
				resultat.add(EvaluationResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation));
			}
		}

		return resultat;
	}

	@GetMapping("/get/{cle}")
	public EvaluationDetailedResponseDto getEvaluationParNumero(@PathVariable String cle) throws ControllerException, ServiceException {

		Evaluation evaluation = verificateur.verifierEvaluation(
				cle, 
				OBLIGATOIRE);

		return EvaluationResponseDtoMapper.mapperModelToDetailedResponseDto(evaluation);
	}

	@PostMapping("/new")
	public EvaluationDetailedResponseDto creerEvaluation(@RequestBody EvaluationCreationRequestDto dto) throws ControllerException, ServiceException {

		Evaluation evaluation = new Evaluation();
		evaluation = mapperCreationRequestDtoToModel(dto, evaluation);
		evaluation = evaluationService.creerEvaluation(evaluation);
		return EvaluationResponseDtoMapper.mapperModelToDetailedResponseDto(evaluation);
	}

	@PutMapping("/mod/{cle}")
	public EvaluationDetailedResponseDto modifierEvaluation(
			@PathVariable String cle, 
			@RequestBody EvaluationModificationRequestDto dto) throws ControllerException, ServiceException {

		Evaluation evaluation = verificateur.verifierEvaluation(cle, OBLIGATOIRE);
		evaluation = mapperModificationRequestDtoToModel(dto, evaluation);
		evaluation = evaluationService.modifierEvaluation(evaluation);
		return EvaluationResponseDtoMapper.mapperModelToDetailedResponseDto(evaluation);
	}

	@DeleteMapping("/del/{cle}")
	public void supprimerEvaluation(@PathVariable String cle) throws ControllerException, ServiceException {

		Evaluation evaluation = verificateur.verifierEvaluation(cle, OBLIGATOIRE);
		evaluationService.supprimerEvaluation(evaluation);
	}

	private Evaluation mapperCreationRequestDtoToModel(
			EvaluationCreationRequestDto dto, 
			Evaluation evaluation) throws ControllerException, ServiceException {

		evaluation.setCle(verificateur.verifierCleValideEtUnique(dto.cle, evaluation.getId(), FACULTATIF));
		evaluation.setCompteInterne((CompteInterne) verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, dto.identifiantCompteInterne, OBLIGATOIRE));
		evaluation.setDateSolde(verificateur.verifierDate(dto.dateSolde, FACULTATIF, LocalDate.now()));
		evaluation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		evaluation.setMontantSoldeEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantSoldeEnCentimes, OBLIGATOIRE, null));

		evaluation.setCompteTechnique(compteTechniqueService.rechercherOuCreerCompteTechniqueOperationsReevaluation());

		return evaluation;
	}

	private Evaluation mapperModificationRequestDtoToModel(
			EvaluationModificationRequestDto dto, 
			Evaluation evaluation) throws ControllerException, ServiceException {

		if ( dto.cle != null ) evaluation.setCle(verificateur.verifierCleValideEtUnique(dto.cle, evaluation.getId(), OBLIGATOIRE));
		if ( dto.identifiantCompteInterne != null ) evaluation.setCompteInterne((CompteInterne) verificateur.verifierCompteEtTypeCompte(TypeCompte.INTERNE, dto.identifiantCompteInterne, OBLIGATOIRE));
		if ( dto.dateSolde != null ) evaluation.setDateSolde(verificateur.verifierDate(dto.dateSolde, OBLIGATOIRE, null));
 		if ( dto.libelle != null ) evaluation.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		if ( dto.montantSoldeEnCentimes != null ) evaluation.setMontantSoldeEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantSoldeEnCentimes, OBLIGATOIRE, null));

		return evaluation;
	}
	
}
