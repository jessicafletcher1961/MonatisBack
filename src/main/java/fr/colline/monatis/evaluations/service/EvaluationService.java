package fr.colline.monatis.evaluations.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.evaluations.EvaluationFonctionnelleErreur;
import fr.colline.monatis.evaluations.EvaluationTechniqueErreur;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.repository.EvaluationRepository;
import fr.colline.monatis.exceptions.ServiceException;

@Service
public class EvaluationService {
	
	@Autowired private EvaluationRepository evaluationRepository;
	
	public Evaluation rechercherParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la recherche d'une évaluation est obligatoire");

		try {
			Optional<Evaluation> optional = evaluationRepository.findById(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_ID,
					id );
		}
	}

	public boolean isExistantParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la vérification de l'existence d'une évaluation est obligatoire");

		try {
			return evaluationRepository.existsById(id);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EvaluationTechniqueErreur.EXISTENCE_PAR_ID,
					id);
		}
	}

	public Evaluation rechercherParCle(String cle) throws ServiceException {

		Assert.notNull(cle, () -> "La CLE pour la recherche d'une évaluation est obligatoire");

		try {
			Optional<Evaluation> optional = evaluationRepository.findByCle(cle);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL,
					cle);
		}
	}

	public boolean isExistantParCle(String cle) throws ServiceException {

		Assert.notNull(cle, () -> "La CLE pour la vérification de l'existence d'une évaluation est obligatoire");

		try {
			return evaluationRepository.existsByCle(cle);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EvaluationTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL,
					Evaluation.class.getSimpleName(),
					cle);
		}
	}

	public List<Evaluation> rechercherTous() throws ServiceException {

		try {
			return evaluationRepository.findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public List<Evaluation> rechercherTous(Sort tri) throws ServiceException {

		Assert.notNull(tri, () -> "Le TRI pour la recherche de toutes les évaluations est obligatoire");

		try {
			return evaluationRepository.findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public void supprimerTous() throws ServiceException {

		try {
			evaluationRepository.deleteAll();
			
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.SUPPRESSION_TOUS);
		}
	}

	public List<Evaluation> rechercherParCompteInterneId(Long compteInterneId) throws ServiceException {

		try {
			return evaluationRepository.findByCompteInterneIdOrderByDateSolde(compteInterneId);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_COMPTE_INTERNE_ID,
					compteInterneId);
		}
	}

	public List<Evaluation> rechercherParCompteInterneIdEntreDateDebutEtDateFin(
			Long compteInterneId,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		try {
			return evaluationRepository.findByCompteInterneIdAndDateSoldeBetweenOrderByDateSolde(compteInterneId, dateDebut, dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_COMPTE_INTERNE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteInterneId,
					dateDebut,
					dateFin);
		}
	}
	
	public Evaluation rechercherPremiereParCompteInterneIdDepuisDateCible(Long compteInterneId, LocalDate dateCible) throws ServiceException {
		
		try {
			Optional<Evaluation> optional = evaluationRepository.findFirstByCompteInterneIdAndDateSoldeGreaterThanEqualOrderByDateSolde(
					compteInterneId, 
					dateCible);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_COMPTE_INTERNE_ID_DEPUIS_DATE_CIBLE,
					compteInterneId,
					dateCible);
		}
	}
	
	public Evaluation rechercherDerniereParCompteInterneIdJusqueDateCible(Long compteInterneId, LocalDate dateCible) throws ServiceException {
		
		try {
			Optional<Evaluation> optional = evaluationRepository.findFirstByCompteInterneIdAndDateSoldeLessThanEqualOrderByDateSoldeDesc(
					compteInterneId, 
					dateCible);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_COMPTE_INTERNE_ID_JUSQUE_DATE_CIBLE,
					compteInterneId,
					dateCible);
		}
	}
	
	public Evaluation creerEvaluation(Evaluation evaluation) throws ServiceException {

		Assert.notNull(evaluation, () -> "L'EVALUATION à créer est obligatoire");

		evaluation = controlerEtPreparerPourCreation(evaluation);

		return enregistrer(evaluation);
	}

	public Evaluation modifierEvaluation(Evaluation evaluation) throws ServiceException {

		Assert.notNull(evaluation, () -> "L'EVALUATION à modifier est obligatoire");

		evaluation = controlerEtPreparerPourModification(evaluation);

		return enregistrer(evaluation);
	}

	public void supprimerEvaluation(Evaluation evaluation) throws ServiceException {

		Assert.notNull(evaluation, () -> "L'EVALUATION à supprimer est obligatoire");

		evaluation = controlerEtPreparerPourSuppression(evaluation);

		supprimer(evaluation);
	}
	
	private Evaluation enregistrer(Evaluation evaluation) throws ServiceException {

		try {
			evaluation = evaluationRepository.save(evaluation);
			if ( evaluation.getCle() == null ) {
				evaluation.setCle(String.format("AUTO-%010d", evaluation.getId()));
				evaluation = evaluationRepository.save(evaluation);
			}
			return evaluation;
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.ENREGISTREMENT,
					evaluation.getCle());
		}
	}

	private void supprimer(Evaluation evaluation) throws ServiceException {

		try {
			evaluationRepository.delete(evaluation);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.SUPPRESSION,
					evaluation.getCle());
		}
	}

	private Evaluation controlerEtPreparerPourCreation(Evaluation evaluation) throws ServiceException {

		verifierAbsenceAutreEvaluationMemeJour(evaluation.getCompteInterne().getId(), evaluation.getId(), evaluation.getDateSolde());
		
		return evaluation;
	}

	private Evaluation controlerEtPreparerPourModification(Evaluation evaluation) throws ServiceException {

		verifierAbsenceAutreEvaluationMemeJour(evaluation.getCompteInterne().getId(), evaluation.getId(), evaluation.getDateSolde());
		
		return evaluation;
	}

	private Evaluation controlerEtPreparerPourSuppression(Evaluation evaluation) throws ServiceException  {

		return evaluation;
	}

	private void verifierAbsenceAutreEvaluationMemeJour(
			Long compteInterneId,
			Long evaluationId,
			LocalDate dateSolde) throws ServiceException {

		Evaluation evaluation = rechercherPremiereParCompteInterneIdDepuisDateCible(compteInterneId, dateSolde);
		
		if ( evaluation != null 
				&& evaluation.getDateSolde().equals(dateSolde) 
				&& ! evaluation.getId().equals(evaluationId) ) {
			throw new ServiceException(
					EvaluationFonctionnelleErreur.UNE_SEULE_EVALUATION_PAR_JOUR_PAR_COMPTE_INTERNE,
					evaluation.getCle(), 
					evaluation.getDateSolde());
		}
	}

}
