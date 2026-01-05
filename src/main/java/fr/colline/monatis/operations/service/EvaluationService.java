package fr.colline.monatis.operations.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.EvaluationTechniqueErreur;
import fr.colline.monatis.operations.model.Evaluation;
import fr.colline.monatis.operations.repository.EvaluationRepository;

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
	
//	public void propagerImpactModificationOperation(Operation operation) throws ServiceException {
//		
//		if ( CompteInterne.class.isAssignableFrom(operation.getCompteDepense().getClass()) ) {
//			
//			Evaluation prochaineEvaluation = rechercherProchaineParCompteInterneIdDepuisDateCible(
//					operation.getCompteDepense().getId(),
//					operation.getDateValeur());
//			
//			if ( prochaineEvaluation != null ) {
//				creerOuModifierOperationPlusMoinsSolde(prochaineEvaluation);
//			}
//		}
//		
//		if ( CompteInterne.class.isAssignableFrom(operation.getCompteRecette().getClass()) ) {
//			
//			Evaluation prochaineEvaluation = rechercherProchaineParCompteInterneIdDepuisDateCible(
//					operation.getCompteRecette().getId(),
//					operation.getDateValeur());
//			
//			if ( prochaineEvaluation != null ) {
//				creerOuModifierOperationPlusMoinsSolde(prochaineEvaluation);
//			}
//		}
//	}

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

		return evaluation;
	}

	private Evaluation controlerEtPreparerPourModification(Evaluation evaluation) throws ServiceException {

		return evaluation;
	}

	private Evaluation controlerEtPreparerPourSuppression(Evaluation evaluation) throws ServiceException  {

		return evaluation;
	}

//	private void propagerImpactModificationEvaluation(Evaluation evaluation) throws ServiceException {
//		
//		Evaluation prochaineEvaluation = rechercherProchaineParCompteInterneIdDepuisDateCible(
//				evaluation.getCompteInterne().getId(),
//				evaluation.getDateSolde().plus(1, ChronoUnit.DAYS));
//
//		if ( prochaineEvaluation != null ) {
//			creerOuModifierOperationPlusMoinsSolde(prochaineEvaluation);
//		}
//	}
//
//	private Evaluation creerOuModifierOperationPlusMoinsSolde(Evaluation evaluation) throws ServiceException {
//
//		if ( evaluation == null ) return null;
//		
//		CompteInterne compteInterne = evaluation.getCompteInterne();
//		Operation operationPlusMoinsSolde = evaluation.getOperationPlusMoinsSolde();
//		
//		// Calcul du nouveau montant de l'opération de plus ou moins solde 
//		Long ancienneValeurOperation;
//		if ( operationPlusMoinsSolde != null ) {
//			if ( operationPlusMoinsSolde.getTypeOperation() == TypeOperation.PLUS_SOLDE ) {
//				ancienneValeurOperation = operationPlusMoinsSolde.getMontantEnCentimes();
//			}
//			else {
//				ancienneValeurOperation = 0 - operationPlusMoinsSolde.getMontantEnCentimes();
//			}
//		}
//		else {
//			ancienneValeurOperation = 0L;
//		}
//		Long soldeActuelEnCentimes = operationService.rechercherSolde(compteInterne, evaluation.getDateSolde()) - ancienneValeurOperation;
//		Long nouvelleValeurOperation = evaluation.getMontantSoldeEnCentimes() - soldeActuelEnCentimes;
//		
//		// Détermination des informations de l'opération de plus ou moins solde
//		TypeOperation typeOperation;
//		Compte compteRecette;
//		Compte compteDepense;
//		if ( nouvelleValeurOperation >= 0 ) {
//			typeOperation = TypeOperation.PLUS_SOLDE;
//			compteRecette = evaluation.getCompteInterne();
//			compteDepense = evaluation.getCompteTechnique();
//		}
//		else {
//			typeOperation = TypeOperation.MOINS_SOLDE;
//			compteRecette = evaluation.getCompteTechnique();
//			compteDepense = evaluation.getCompteInterne();
//		}
//		String libelle;
//		if ( evaluation.getLibelle() == null ) {
//			if ( evaluation.getCompteInterne().getTypeFonctionnement() == TypeFonctionnement.COURANT ) {
//				libelle = "Solde du compte '" + compteInterne.getIdentifiant() + "' fixé à la valeur " + evaluation.getMontantSoldeEnCentimes() + " en date du " + evaluation.getDateSolde();
//			}
//			else if ( evaluation.getCompteInterne().getTypeFonctionnement() == TypeFonctionnement.EPARGNE ) {
//				libelle = "Solde du compte '" + compteInterne.getIdentifiant() + "' fixé à la valeur " + evaluation.getMontantSoldeEnCentimes() + " après le versement des intérêts et/ou des dividendes en date du " + evaluation.getDateSolde();
//			}
//			else {
//				libelle = "Valeur estimative du bien '" + compteInterne.getIdentifiant() + "' fixée à " + evaluation.getMontantSoldeEnCentimes() + " en date du " + evaluation.getDateSolde();
//			}
//		}
//		else {
//			libelle = evaluation.getLibelle();
//		}
//
//		// Création ou mise à jour de l'opération de plus ou moins solde
//		if ( operationPlusMoinsSolde == null ) {
//
//			operationPlusMoinsSolde = new Operation(
//					null, 
//					typeOperation,
//					libelle,
//					evaluation.getDateSolde(), 
//					Math.abs(nouvelleValeurOperation), 
//					compteRecette, 
//					compteDepense,
//					new OperationLigne(
//							0,
//							libelle,
//							evaluation.getDateSolde(),
//							Math.abs(nouvelleValeurOperation),
//							null));
//			
//			operationPlusMoinsSolde = operationService.creerOperation(operationPlusMoinsSolde);
//		}
//		else
//		{
//			
//			operationPlusMoinsSolde.setTypeOperation(typeOperation);
//			operationPlusMoinsSolde.setDateValeur(evaluation.getDateSolde());
//			operationPlusMoinsSolde.setLibelle(libelle);
//			operationPlusMoinsSolde.setCompteDepense(compteDepense);
//			operationPlusMoinsSolde.setCompteRecette(compteRecette);
//			operationPlusMoinsSolde.setMontantEnCentimes(Math.abs(nouvelleValeurOperation));
//			
//			Set<OperationLigne> lignes = new HashSet<OperationLigne>();
//			lignes.add(new OperationLigne(
//							0,
//							libelle,
//							evaluation.getDateSolde(),
//							Math.abs(nouvelleValeurOperation),
//							null));
//			operationPlusMoinsSolde.changerLignes(lignes);
//			
//			operationPlusMoinsSolde = operationService.modifierOperation(operationPlusMoinsSolde);
//		}
//
//		evaluation.setOperationPlusMoinsSolde(operationPlusMoinsSolde);
//		
//		return operationPlusMoinsSolde;
//	}

}
