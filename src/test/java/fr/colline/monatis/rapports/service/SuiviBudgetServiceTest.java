package fr.colline.monatis.rapports.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.service.BudgetService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.rapports.model.composants.depense_recette.SuiviBudgetPeriode;
import fr.colline.monatis.typologies.model.TypeBudget;
import fr.colline.monatis.typologies.model.TypePeriode;

@ExtendWith(MockitoExtension.class)
class SuiviBudgetServiceTest {

	@Mock
	private BudgetService budgetService;

	@InjectMocks
	private SuiviBudgetService suiviBudgetService;

	@Test
	void calculerSuiviBudgetRetourneNullSiAucunBudgetNeCouvreLaPeriode() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		when(budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(7L, debut, fin)).thenReturn(List.of());

		SuiviBudgetPeriode suivi = suiviBudgetService.calculerSuiviBudget(7L, debut, fin, -2_000L);

		assertNull(suivi);
	}

	@Test
	void calculerSuiviBudgetCalculeLeSensLesEcartsEtLeTaux() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Budget budget = budget(TypeBudget.SOLDE_PREVU_NEGATIF, debut, fin, 3_000L);
		when(budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(7L, debut, fin)).thenReturn(List.of(budget));

		SuiviBudgetPeriode suivi = suiviBudgetService.calculerSuiviBudget(7L, debut, fin, -3_500L);

		assertEquals(debut, suivi.getDateDebut());
		assertEquals(fin, suivi.getDateFin());
		assertEquals(-3_000L, suivi.getMontantBudgetEnCentimes());
		assertEquals(-3_500L, suivi.getMontantExecutionEnCentimes());
		assertEquals(0L, suivi.getMontantVertEnCentimes());
		assertEquals(500L, suivi.getMontantRougeEnCentimes());
		assertEquals(116.666, suivi.getTauxExecutionBudget(), 0.001);
	}

	@Test
	void calculerSuiviBudgetProratiseLesBudgetsQuiDebordentDeLaPeriode() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 11);
		LocalDate fin = LocalDate.of(2026, 5, 20);
		Budget budget = budget(TypeBudget.SOLDE_PREVU_POSITIF, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 3_100L);
		when(budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(7L, debut, fin)).thenReturn(List.of(budget));

		SuiviBudgetPeriode suivi = suiviBudgetService.calculerSuiviBudget(7L, debut, fin, 800L);

		assertEquals(1_000L, suivi.getMontantBudgetEnCentimes());
		assertEquals(200L, suivi.getMontantRougeEnCentimes());
		assertEquals(0L, suivi.getMontantVertEnCentimes());
		assertEquals(80D, suivi.getTauxExecutionBudget());
	}

	@Test
	void calculerSuiviBudgetRetourneNullSiLesBudgetsNeCouvrentPasTouteLaPeriode() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Budget budget = budget(TypeBudget.SOLDE_PREVU_POSITIF, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), 1_500L);
		when(budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(7L, debut, fin)).thenReturn(List.of(budget));

		SuiviBudgetPeriode suivi = suiviBudgetService.calculerSuiviBudget(7L, debut, fin, 800L);

		assertNull(suivi);
	}

	@Test
	void cumulerSuiviBudgetIgnoreUnSuiviNull() {
		SuiviBudgetPeriode cumul = suivi(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 1_000L, 700L);

		SuiviBudgetPeriode resultat = suiviBudgetService.cumulerSuiviBudget(cumul, null);

		assertSame(cumul, resultat);
		assertEquals(1_000L, resultat.getMontantBudgetEnCentimes());
		assertEquals(700L, resultat.getMontantExecutionEnCentimes());
	}

	@Test
	void cumulerSuiviBudgetInitialisePuisCumuleLesMontants() {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		SuiviBudgetPeriode aCumuler = suivi(debut, fin, 1_000L, 1_200L);

		SuiviBudgetPeriode resultat = suiviBudgetService.cumulerSuiviBudget(null, aCumuler);

		assertEquals(debut, resultat.getDateDebut());
		assertEquals(fin, resultat.getDateFin());
		assertEquals(1_000L, resultat.getMontantBudgetEnCentimes());
		assertEquals(1_200L, resultat.getMontantExecutionEnCentimes());
		assertEquals(-200L, resultat.getMontantVertEnCentimes());
		assertEquals(0L, resultat.getMontantRougeEnCentimes());
		assertEquals(120D, resultat.getTauxExecutionBudget());
	}

	private static Budget budget(TypeBudget typeBudget, LocalDate dateDebut, LocalDate dateFin, Long montant) {
		Budget budget = new Budget();
		budget.setTypePeriode(TypePeriode.MENSUEL);
		budget.setTypeBudget(typeBudget);
		budget.setDateDebut(dateDebut);
		budget.setDateFin(dateFin);
		budget.setMontantBudgetEnCentimes(montant);
		return budget;
	}

	private static SuiviBudgetPeriode suivi(LocalDate debut, LocalDate fin, Long budget, Long execution) {
		SuiviBudgetPeriode suivi = new SuiviBudgetPeriode();
		suivi.setDateDebut(debut);
		suivi.setDateFin(fin);
		suivi.setMontantBudgetEnCentimes(budget);
		suivi.setMontantExecutionEnCentimes(execution);
		suivi.setMontantRougeEnCentimes(0L);
		suivi.setMontantVertEnCentimes(0L);
		suivi.setTauxExecutionBudget(null);
		return suivi;
	}
}
