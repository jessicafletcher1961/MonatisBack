package fr.colline.monatis.budgets.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budgets.BudgetFonctionnelleErreur;
import fr.colline.monatis.budgets.BudgetTechniqueErreur;
import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.model.TypePeriode;
import fr.colline.monatis.budgets.repository.BudgetRepository;
import fr.colline.monatis.erreurs.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
public class BudgetService {

	@Autowired private BudgetRepository budgetRepository;

	public Budget rechercherParId(Long budgetId) throws ServiceException {

		try {
			Optional<Budget> optional = budgetRepository.findById(budgetId);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_PAR_ID,
					budgetId );
		}
	}

	public boolean isExistantParId(Long budgetId) throws ServiceException {

		try {
			return this.budgetRepository.existsById(budgetId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.EXISTENCE_PAR_ID,
					budgetId);
		}
	}

	public ArrayList<Budget> rechercherParReferenceId(Long referenceId) throws ServiceException {

		try {
			return budgetRepository.findByReferenceIdOrderByDateFinDesc(referenceId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_HISTORIQUE_PAR_REFERENCE_ID,
					referenceId);
		}
	}

	public boolean isExistantParReferenceId(Long referenceId) throws ServiceException {

		try {
			return budgetRepository.existsByReferenceId(referenceId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.EXISTENCE_HISTORIQUE_PAR_REFERENCE_ID,
					referenceId);
		}
	}

	public Budget rechercherParReferenceIdEtDateCible(Long referenceId, LocalDate dateCible) throws ServiceException {

		try {
			Optional<Budget> optional = budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThan(referenceId, dateCible, dateCible);
			return optional.isEmpty() ? null : optional.get(); 
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_PAR_REFERENCE_ID_ET_DATE_CIBLE,
					referenceId,
					dateCible);
		}
	}

	public boolean isExistantParReferenceIdEtDateCible(Long referenceId, LocalDate dateCible) throws ServiceException {

		try {
			return budgetRepository.existsByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThan(referenceId, dateCible, dateCible);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.EXISTENCE_PAR_REFERENCE_ID_ET_DATE_CIBLE,
					referenceId,
					dateCible);
		}
	}

	public Budget rechercherDernierParReferenceId(Long referenceId) throws ServiceException {

		try {
			Optional<Budget> optional = budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(referenceId);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_HISTORIQUE_PAR_REFERENCE_ID,
					referenceId);
		}
	}

	public List<Budget> rechercherTous() throws ServiceException {

		try {
			return budgetRepository.findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					BudgetTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public List<Budget> rechercherTous(Sort tri) throws ServiceException {

		try {
			return budgetRepository.findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					BudgetTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public void supprimerTous() throws ServiceException {

		try {
			budgetRepository.deleteAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					BudgetTechniqueErreur.SUPPRESSION_TOUS);
		}
	}

	public Budget creerBudget(Budget budget) throws ServiceException {

		budget = controlerEtPreparerPourCreation(budget);

		return enregistrer(budget);
	}

	public Budget reconduireBudget(Budget budget) throws ServiceException {

		budget = controlerEtPreparerPourReconduction(budget);

		return enregistrer(budget);
	}

	public Budget modifierBudget(Budget budget) throws ServiceException {

		budget = controlerEtPreparerPourModification(budget);

		return enregistrer(budget);
	}

	public void supprimerBudget(Budget budget) throws ServiceException {

		budget = controlerEtPreparerPourSuppression(budget);

		supprimer(budget);
	}

	private Budget enregistrer(Budget budget) throws ServiceException {

		try {
			return budgetRepository.save(budget);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					BudgetTechniqueErreur.ENREGISTREMENT,
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom(),
					budget.getDateDebut(),
					budget.getDateFin());
		}
	}

	private void supprimer(Budget budget) throws ServiceException {

		try {
			budgetRepository.delete(budget);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					BudgetTechniqueErreur.SUPPRESSION,
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom(),
					budget.getDateDebut(),
					budget.getDateFin());
		}
	}

	private Budget controlerEtPreparerPourCreation(Budget budget) throws ServiceException {

		if ( budget.getTypePeriode() == TypePeriode.TECHNIQUE ) {
			throw new ServiceException(
					BudgetFonctionnelleErreur.CREATION_ET_RECONDUCTION_AUTOMATIQUE_IMPOSSIBLE,
					TypePeriode.TECHNIQUE.getLibelle());
		}

		LocalDate dateDebut = DateEtPeriodeUtils.recadrerDateDebutPeriode(budget.getTypePeriode(), budget.getDateDebut());
		LocalDate dateFin = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(budget.getTypePeriode(), budget.getDateDebut());

		budget.setDateDebut(dateDebut);
		budget.setDateFin(dateFin);

		return budget;
	}

	private Budget controlerEtPreparerPourReconduction(Budget budget) throws ServiceException {
		
		if ( budget.getTypePeriode() == TypePeriode.TECHNIQUE ) {
			throw new ServiceException(
					BudgetFonctionnelleErreur.CREATION_ET_RECONDUCTION_AUTOMATIQUE_IMPOSSIBLE,
					TypePeriode.TECHNIQUE.getLibelle());
		}

		Reference reference = budget.getReference();
		Budget dernierBudget = rechercherDernierParReferenceId(reference.getId());
		LocalDate dateDebut = DateEtPeriodeUtils.recadrerDateDebutPeriode(budget.getTypePeriode(), budget.getDateDebut());
		LocalDate dateFin = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(budget.getTypePeriode(), dateDebut);

		if ( dateDebut.isBefore(dernierBudget.getDateFin())) {

			// Chevauchement avec la dernière période enregistrée

			throw new ServiceException(
					BudgetFonctionnelleErreur.CHEVAUCHEMENT_PERIODES, 
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom(),
					dernierBudget.getTypePeriode().getCode(),
					dernierBudget.getDateFin(),
					budget.getTypePeriode().getCode(),
					dateDebut);
		}

		if ( ! dernierBudget.getDateFin().equals(dateDebut) ) {

			// Il y a un "trou" entre le dernier budget enregistré et le nouveau budget : on crée un budget technique intermédiaire

			long montantMensuelDernierBudget = calculerMontantParMois(dernierBudget);
			long nombreDeMoisBudgetIntermediaire = DateEtPeriodeUtils.calculerNombreMoisEntreDeuxDates(dernierBudget.getDateFin(), dateDebut);
			Budget budgetIntermediaire = new Budget(
					budget.getReference(),
					TypePeriode.TECHNIQUE,
					dernierBudget.getDateFin(),
					dateDebut,
					montantMensuelDernierBudget * nombreDeMoisBudgetIntermediaire); 
			enregistrer(budgetIntermediaire);
		}

		budget.setDateDebut(dateDebut);
		budget.setDateFin(dateFin);

		return budget;
	}

	private Budget controlerEtPreparerPourModification(Budget budget) {

		return budget;
	}

	private Budget controlerEtPreparerPourSuppression(Budget budget) throws ServiceException {

		Reference reference = budget.getReference();

		ArrayList<Budget> historique = rechercherParReferenceId(reference.getId());
		for ( Budget dernierBudget : historique ) {

			// Suppression des budgets postérieurs au budget à supprimer

			if ( budget.getId().equals(dernierBudget.getId()) ) {
				break;
			}
			supprimer(dernierBudget);
		}

		return budget;
	}

	private long calculerMontantParMois(Budget budget) {

		long nombreMoisBudget = DateEtPeriodeUtils.calculerNombreMoisEntreDeuxDates(budget.getDateDebut(), budget.getDateFin());

		return Math.round((float) budget.getMontantEnCentimes() / nombreMoisBudget);
	}
	
	public long estimerMontantBudget(TypePeriode typePeriode, Budget budgetReference) throws ServiceException {
		
		long montantParMois = calculerMontantParMois(budgetReference);
		
		long nombreMois;
		switch ( typePeriode ) {
		case ANNUEL:
			nombreMois = 12;
			break;
		case SEMESTRIEL:
			nombreMois = 6;
			break;
		case TRIMESTRIEL:
			nombreMois = 3;
			break;
		case BIMESTRIEL:
			nombreMois = 2;
			break;
		case MENSUEL:
			nombreMois = 1;
			break;
		default:
			throw new ServiceException(
					GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypePeriode.class.getSimpleName(),
					typePeriode.getCode(),
					typePeriode.getLibelle());
		}
		
		return montantParMois * nombreMois;
	}
}