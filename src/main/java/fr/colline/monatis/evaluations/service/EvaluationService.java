package fr.colline.monatis.evaluations.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
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

	public Evaluation rechercherParCompteInterneIdEtDateSolde(Long compteInterneId, LocalDate dateSolde) throws ServiceException {
		
		try {
			Optional<Evaluation> optional = evaluationRepository.findByCompteInterneIdAndDateSolde(
					compteInterneId, 
					dateSolde);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_PAR_COMPTE_INTERNE_ID_ET_DATE_SOLDE,
					compteInterneId,
					dateSolde);
		}
		
	}
	
	/**
	 * Recherche la plus récente des évaluations telles que dateDebut <= evaluation.dateSole <= dateFin
	 * 
	 * @param compteInterne
	 * @param dateDebut date à partir de laquelle on effectue la recherche (comprise dans la recherche)
	 * @param dateFin date jusqu'à laquelle on effectue la recherche (comprise dans la recherche)
	 * @return l'évaluation trouvée ou null s'il n'y en a pas
	 * @throws ServiceException en cas de problème technique lors de l'exécution de la requête en base
	 */
	public Evaluation rechercherDerniereParCompteInterneEntreDateDebutEtDateFin(CompteInterne compteInterne, LocalDate dateDebut, LocalDate dateFin) throws ServiceException {

		LocalDate dateDebutRecherche = CompteInterneService.prendreDateSoldeInitialAuBesoin(compteInterne, dateDebut);
		LocalDate dateFinRecherche = CompteInterneService.prendreDateClotureAuBesoin(compteInterne, dateFin);

		try {
			Optional<Evaluation> optional = evaluationRepository.findFirstByCompteInterneIdAndDateSoldeBetweenOrderByDateSoldeDesc(
					compteInterne.getId(), 
					dateDebutRecherche,
					dateFinRecherche);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.RECHERCHE_DERNIERE_PAR_COMPTE_INTERNE_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteInterne.getIdentifiant(),
					dateDebut,
					dateFin);
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
				evaluation.setCle(String.format("EVAL-%010d", evaluation.getId()));
				evaluation = evaluationRepository.save(evaluation);
			}
			return evaluation;
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EvaluationTechniqueErreur.ENREGISTREMENT,
					evaluation.getCompteInterne().getIdentifiant(),
					evaluation.getDateSolde());
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
					evaluation.getCompteInterne().getIdentifiant(),
					evaluation.getDateSolde());
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

		Evaluation evaluation = rechercherParCompteInterneIdEtDateSolde(compteInterneId, dateSolde);
		
		if ( evaluation != null 
				&& ! evaluation.getId().equals(evaluationId) ) {
			throw new ServiceException(
					EvaluationFonctionnelleErreur.UNE_SEULE_EVALUATION_PAR_JOUR_PAR_COMPTE_INTERNE,
					evaluation.getCompteInterne().getIdentifiant(),
					evaluation.getDateSolde());
		}
	}
}
