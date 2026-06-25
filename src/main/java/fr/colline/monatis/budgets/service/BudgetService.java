package fr.colline.monatis.budgets.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budgets.BudgetFonctionnelleErreur;
import fr.colline.monatis.budgets.BudgetTechniqueErreur;
import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.repository.BudgetRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
public class BudgetService {

	@Autowired private BudgetRepository budgetRepository;

	public Budget rechercherParId(Long id) throws ServiceException {

		try {
			Optional<Budget> optional = budgetRepository.findById(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_PAR_ID,
					id );
		}
	}

	public boolean isExistantParId(Long id) throws ServiceException {

		try {
			return this.budgetRepository.existsById(id);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.EXISTENCE_PAR_ID,
					id);
		}
	}

	public Budget rechercherParCle(String cle) throws ServiceException {

		try {
			Optional<Budget> optional = budgetRepository.findByCle(cle);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL,
					cle);
		}
	}

	public boolean isExistantParCle(String cle) throws ServiceException {

		try {
			return budgetRepository.existsByCle(cle);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL,
					Budget.class.getSimpleName(),
					cle);
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

	public Budget rechercherParReferenceIdEtDateCible(Long referenceId, LocalDate dateCible) throws ServiceException {

		try {
			Optional<Budget> optional = budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
					referenceId, 
					dateCible, 
					dateCible);
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
	
	public List<Budget> rechercherParReferenceIdEntreDateDebutEtDateFin(Long referenceId, LocalDate dateDebut, LocalDate dateFin) throws ServiceException {

		try {
			return budgetRepository.findByReferenceIdAndDateRange(
					referenceId, 
					dateDebut, 
					dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					BudgetTechniqueErreur.RECHERCHE_PAR_REFERENCE_ID_ET_PERIODE,
					referenceId,
					dateDebut,
					dateFin);
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
			budget = budgetRepository.save(budget);
			if ( budget.getCle() == null ) {
				budget.setCle(String.format("BUDG-%010d", budget.getId()));
				budget = budgetRepository.save(budget);
			}
			return budget;
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

		LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(budget.getTypePeriode(), budget.getDateDebut());
		LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(budget.getTypePeriode(), dateDebutPeriode);

		verifierChevauchementPeriode(budget, dateDebutPeriode, dateFinPeriode);
		
		budget.setDateDebut(dateDebutPeriode);
		budget.setDateFin(dateFinPeriode);

		return budget;
	}

	private Budget controlerEtPreparerPourReconduction(Budget budgetAReconduire) throws ServiceException {
		
		LocalDate dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(budgetAReconduire.getTypePeriode(), budgetAReconduire.getDateDebut());
		LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(budgetAReconduire.getTypePeriode(), dateDebutPeriode);

		Budget budgetReconduit = new Budget();
		budgetReconduit.setCle(null);
		budgetReconduit.setReference(budgetAReconduire.getReference());
		budgetReconduit.setTypePeriode(budgetAReconduire.getTypePeriode());
		budgetReconduit.setDateDebut(dateDebutPeriode);
		budgetReconduit.setDateFin(dateFinPeriode);
		budgetReconduit.setLibelle(budgetAReconduire.getLibelle());
		budgetReconduit.setTypeBudget(budgetAReconduire.getTypeBudget());
		budgetReconduit.setMontantBudgetEnCentimes(budgetAReconduire.getMontantBudgetEnCentimes());

		verifierChevauchementPeriode(budgetReconduit, dateDebutPeriode, dateFinPeriode);
		
		return budgetReconduit;
	}

	private Budget controlerEtPreparerPourModification(Budget budget) throws ServiceException {

		LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(budget.getTypePeriode(), budget.getDateDebut());
		LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(budget.getTypePeriode(), dateDebutPeriode);

		verifierChevauchementPeriode(budget, dateDebutPeriode, dateFinPeriode);
		
		budget.setDateDebut(dateDebutPeriode);
		budget.setDateFin(dateFinPeriode);
		
		return budget;
	}

	private Budget controlerEtPreparerPourSuppression(Budget budget) throws ServiceException {

		return budget;
	}
	
	private void verifierChevauchementPeriode(Budget budget, LocalDate dateDebutPeriode, LocalDate dateFinPeriode) throws ServiceException {

		Reference reference = budget.getReference();
		
		if ( rechercherDernierParReferenceId(reference.getId()) != null ) {

			// Au moins un budget existe déjà
			
			// Vérification chevauchement avec une période antérieure
			Budget budgetChevauchementDebut = rechercherParReferenceIdEtDateCible(reference.getId(), dateDebutPeriode); 
			if ( budgetChevauchementDebut != null && !budgetChevauchementDebut.getId().equals(budget.getId())) {
				throw new ServiceException(
						BudgetFonctionnelleErreur.CHEVAUCHEMENT_PERIODE_PRECEDENTE, 
						reference.getClass().getSimpleName(),
						reference.getNom(),
						budgetChevauchementDebut.getTypePeriode().getCode(),
						budgetChevauchementDebut.getDateFin(),
						budget.getTypePeriode().getCode(),
						dateDebutPeriode);
			}
			
			// Vérification chevauchement avec une période postérieure
			Budget budgetChevauchementFin = rechercherParReferenceIdEtDateCible(reference.getId(), dateFinPeriode);
			if ( budgetChevauchementFin != null && !budgetChevauchementFin.getId().equals(budget.getId()) ) {
				throw new ServiceException(
						BudgetFonctionnelleErreur.CHEVAUCHEMENT_PERIODE_SUIVANTE, 
						reference.getClass().getSimpleName(),
						reference.getNom(),
						budgetChevauchementFin.getTypePeriode().getCode(),
						budgetChevauchementFin.getDateDebut(),
						budget.getTypePeriode().getCode(),
						dateFinPeriode);
			}
		}
	}

}