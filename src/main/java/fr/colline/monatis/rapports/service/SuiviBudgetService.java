package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.service.BudgetService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.rapports.model.composants.depense_recette.SuiviBudgetPeriode;
import fr.colline.monatis.typologies.model.TypeBudget;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
class SuiviBudgetService {
	
	@Autowired private BudgetService budgetService;

	SuiviBudgetPeriode calculerSuiviBudget(
			Long referenceId, 
			LocalDate dateDebutPeriode, 
			LocalDate dateFinPeriode, 
			Long montantExecutionEnCentimes) throws ServiceException {

		SuiviBudgetPeriode suiviBudget = null;
		
		Long montantBudget = calculerMontantBudgetEntreDateDebutEtDateFin(referenceId, montantExecutionEnCentimes, dateDebutPeriode, dateFinPeriode);
		if ( montantBudget != null ) {
			
			Long montantDifference = montantBudget - montantExecutionEnCentimes;
			Long montantVert = montantDifference <= 0 ? montantDifference : 0;
			Long montantRouge = montantDifference > 0 ? montantDifference : 0;
			Double tauxExecution = montantBudget == 0L ? null : montantExecutionEnCentimes * 100.00 / montantBudget;
			
			suiviBudget = new SuiviBudgetPeriode();

			suiviBudget.setDateDebut(dateDebutPeriode);
			suiviBudget.setDateFin(dateFinPeriode);
			
			suiviBudget.setMontantBudgetEnCentimes(montantBudget);
			suiviBudget.setMontantExecutionEnCentimes(montantExecutionEnCentimes);
			suiviBudget.setMontantVertEnCentimes(montantVert);
			suiviBudget.setMontantRougeEnCentimes(montantRouge);
			suiviBudget.setTauxExecutionBudget(tauxExecution);
		}
				
		return suiviBudget;
	}
	
	SuiviBudgetPeriode initialiserSuiviBudget(LocalDate dateDebutPeriode, LocalDate dateFinPeriode) {

		SuiviBudgetPeriode suiviBudget = new SuiviBudgetPeriode();

		suiviBudget.setDateDebut(dateDebutPeriode);
		suiviBudget.setDateFin(dateFinPeriode);
		
		suiviBudget.setMontantBudgetEnCentimes(0L);
		suiviBudget.setMontantExecutionEnCentimes(0L);
		suiviBudget.setMontantRougeEnCentimes(0L);
		suiviBudget.setMontantVertEnCentimes(0L);
		suiviBudget.setTauxExecutionBudget(null);
		
		return suiviBudget;
	}
	
	SuiviBudgetPeriode cumulerSuiviBudget(SuiviBudgetPeriode suiviBudgetCumule, SuiviBudgetPeriode suiviBudgetACumuler) {
		
		if ( suiviBudgetACumuler != null ) {

			if ( suiviBudgetCumule == null ) {
				suiviBudgetCumule = initialiserSuiviBudget(suiviBudgetACumuler.getDateDebut(), suiviBudgetACumuler.getDateFin());
			}

			Long montantBudget = suiviBudgetCumule.getMontantBudgetEnCentimes() + suiviBudgetACumuler.getMontantBudgetEnCentimes();
			Long montantExecution = suiviBudgetCumule.getMontantExecutionEnCentimes() + suiviBudgetACumuler.getMontantExecutionEnCentimes();

			Long montantDifference = montantBudget - montantExecution;
			Long montantVert = montantDifference <= 0 ? montantDifference : 0;
			Long montantRouge = montantDifference > 0 ? montantDifference : 0;
			Double tauxExecution = montantBudget == 0L ? null : montantExecution * 100.00 / montantBudget;

			suiviBudgetCumule.setMontantBudgetEnCentimes(montantBudget);
			suiviBudgetCumule.setMontantExecutionEnCentimes(montantExecution);
			suiviBudgetCumule.setMontantRougeEnCentimes(montantRouge);
			suiviBudgetCumule.setMontantVertEnCentimes(montantVert);
			suiviBudgetCumule.setTauxExecutionBudget(tauxExecution);
		}
		
		return suiviBudgetCumule;
	}
	
	/**
	 * Calcule le montant budgeté entre les deux dates indiquées (incluses) pour la référence indiquée.<br>
	 * Au besoin, un découpage est fait basé sur le nombre de jours pour enlever ce qui déborde 
	 * avant et/ou après par rapport aux dates des budgets utilisés.<br>
	 * Si le montant du budget est positif, c'est qu'on attend majoritairement des revenus.<br> 
	 * S'il est négatif, c'est qu'on attend majoritairement des dépenses.<br>
	 * S'il est égal à 0, c'est que la somme des montants des budgets retenus est égale à 0.
	 * S'il est égal à null, c'est qu'on n'a pas pu calculer le montant budgeté sur la période indiquée.
	 * 
	 * @param referenceId
	 * @param dateDebutPeriode
	 * @param dateFinPeriode
	 * 
	 * @return le montant budgeté ou null si la période n'est pas entièrement couverte par des budgets.
	 * 
	 * @throws ServiceException 
	 */
	private Long calculerMontantBudgetEntreDateDebutEtDateFin(Long referenceId, Long montantExecutionEnCentimes, LocalDate dateDebutPeriode, LocalDate dateFinPeriode) throws ServiceException {
		
		List<Budget> budgets = budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(referenceId, dateDebutPeriode, dateFinPeriode);
		
		if ( budgets.isEmpty() ) {
			return null;
		}
		
		long nombreJoursBudgetTotal = 0L;
		Long montantBudgetTotal = 0L;

		for ( Budget budget : budgets ) {

			long nombreJoursBudget = DateEtPeriodeUtils.calculerNombreJoursEntreDeuxDates(
					budget.getDateDebut(),
					budget.getDateFin());
			int sensBudget = budget.getTypeBudget() == TypeBudget.SOLDE_PREVU_POSITIF ? 1 : -1;
			
			nombreJoursBudgetTotal += nombreJoursBudget;
			montantBudgetTotal += budget.getMontantBudgetEnCentimes() * sensBudget;
			
			if ( budget.getDateDebut().isBefore(dateDebutPeriode) ) {
				// on doit enlever le nombre de jours hors période et le montant correspondant (avant le début de la période)
				long nombreJoursInclus = DateEtPeriodeUtils.calculerNombreJoursEntreDeuxDates(
						dateDebutPeriode,
						budget.getDateFin());

				long nombreJoursExclus = nombreJoursBudget - nombreJoursInclus;
				nombreJoursBudgetTotal -= nombreJoursExclus;
				
				long montantExclu = Math.round((double) budget.getMontantBudgetEnCentimes() / nombreJoursBudget) * nombreJoursExclus;
				montantBudgetTotal -= montantExclu * sensBudget;
			}
			if ( budget.getDateFin().isAfter(dateFinPeriode) ) {
				// on doit enlever le nombre de jours hors période et le montant correspondant (après la fin de la période)
				long nombreJoursInclus = DateEtPeriodeUtils.calculerNombreJoursEntreDeuxDates(
						budget.getDateDebut(),
						dateFinPeriode);

				long nombreJoursExclus = nombreJoursBudget - nombreJoursInclus;
				nombreJoursBudgetTotal -= nombreJoursExclus;

				long montantExclu = Math.round((double) budget.getMontantBudgetEnCentimes() / nombreJoursBudget) * nombreJoursExclus;
				montantBudgetTotal -= montantExclu * sensBudget;
			}
		}

		long nombreJoursPeriode = DateEtPeriodeUtils.calculerNombreJoursEntreDeuxDates(dateDebutPeriode, dateFinPeriode);
		if ( nombreJoursPeriode != nombreJoursBudgetTotal ) {
			// La période n'est pas entièrement couverte par des budgets
			return null;
		}
		
		return montantBudgetTotal;
	}
}
