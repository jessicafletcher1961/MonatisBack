package fr.colline.monatis.budgets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.colline.monatis.budgets.BudgetFonctionnelleErreur;
import fr.colline.monatis.budgets.BudgetTechniqueErreur;
import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.repository.BudgetRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.typologies.model.TypeBudget;
import fr.colline.monatis.typologies.model.TypePeriode;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

	@Mock
	private BudgetRepository budgetRepository;

	@InjectMocks
	private BudgetService budgetService;

	private Reference reference;

	@BeforeEach
	void setUp() {
		reference = new Categorie("ALIMENTATION", "Alimentation");
		reference.setId(11L);
	}

	@Test
	void rechercherParIdRetourneLeBudgetTrouve() throws ServiceException {
		Budget budget = budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
		when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

		Budget resultat = budgetService.rechercherParId(1L);

		assertSame(budget, resultat);
		verify(budgetRepository).findById(1L);
	}

	@Test
	void rechercherParIdRetourneNullSiAucunBudget() throws ServiceException {
		when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

		Budget resultat = budgetService.rechercherParId(1L);

		assertNull(resultat);
	}

	@Test
	void rechercherParIdTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findById(1L)).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.rechercherParId(1L));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.RECHERCHE_PAR_ID, exception.getErreur());
		assertEquals(1L, exception.getValues()[0]);
	}

	@Test
	void isExistantParIdRetourneLeResultatDuRepository() throws ServiceException {
		when(budgetRepository.existsById(1L)).thenReturn(true);

		boolean resultat = budgetService.isExistantParId(1L);

		assertTrue(resultat);
		verify(budgetRepository).existsById(1L);
	}

	@Test
	void isExistantParIdTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.existsById(1L)).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.isExistantParId(1L));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.EXISTENCE_PAR_ID, exception.getErreur());
		assertEquals(1L, exception.getValues()[0]);
	}

	@Test
	void rechercherParCleRetourneLeBudgetTrouve() throws ServiceException {
		Budget budget = budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
		when(budgetRepository.findByCle("BUDG-0000000001")).thenReturn(Optional.of(budget));

		Budget resultat = budgetService.rechercherParCle("BUDG-0000000001");

		assertSame(budget, resultat);
		verify(budgetRepository).findByCle("BUDG-0000000001");
	}

	@Test
	void rechercherParCleRetourneNullSiAucunBudget() throws ServiceException {
		when(budgetRepository.findByCle("BUDG-0000000001")).thenReturn(Optional.empty());

		Budget resultat = budgetService.rechercherParCle("BUDG-0000000001");

		assertNull(resultat);
	}

	@Test
	void rechercherParCleTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findByCle("BUDG-0000000001")).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.rechercherParCle("BUDG-0000000001"));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL, exception.getErreur());
		assertEquals("BUDG-0000000001", exception.getValues()[0]);
	}

	@Test
	void isExistantParCleRetourneLeResultatDuRepository() throws ServiceException {
		when(budgetRepository.existsByCle("BUDG-0000000001")).thenReturn(false);

		boolean resultat = budgetService.isExistantParCle("BUDG-0000000001");

		assertFalse(resultat);
		verify(budgetRepository).existsByCle("BUDG-0000000001");
	}

	@Test
	void isExistantParCleTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.existsByCle("BUDG-0000000001")).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.isExistantParCle("BUDG-0000000001"));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL, exception.getErreur());
		assertEquals(Budget.class.getSimpleName(), exception.getValues()[0]);
		assertEquals("BUDG-0000000001", exception.getValues()[1]);
	}

	@Test
	void rechercherTousRetourneLesBudgetsDuRepository() throws ServiceException {
		List<Budget> budgets = List.of(
				budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31)),
				budget(2L, "BUDG-0000000002", TypePeriode.MENSUEL, LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)));
		when(budgetRepository.findAll()).thenReturn(budgets);

		List<Budget> resultat = budgetService.rechercherTous();

		assertSame(budgets, resultat);
		verify(budgetRepository).findAll();
	}

	@Test
	void rechercherTousTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findAll()).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.rechercherTous());

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.RECHERCHE_TOUS, exception.getErreur());
	}

	@Test
	void supprimerTousDelegueAuRepository() throws ServiceException {
		budgetService.supprimerTous();

		verify(budgetRepository).deleteAll();
	}

	@Test
	void supprimerTousTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		doThrow(cause).when(budgetRepository).deleteAll();

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.supprimerTous());

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.SUPPRESSION_TOUS, exception.getErreur());
	}

	@Test
	void rechercherParReferenceIdEtDateCibleRetourneLeBudgetTrouve() throws ServiceException {
		LocalDate dateCible = LocalDate.of(2026, 5, 15);
		Budget budget = budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(11L, dateCible, dateCible))
				.thenReturn(Optional.of(budget));

		Budget resultat = budgetService.rechercherParReferenceIdEtDateCible(11L, dateCible);

		assertSame(budget, resultat);
	}

	@Test
	void rechercherParReferenceIdEtDateCibleRetourneNullSiAucunBudget() throws ServiceException {
		LocalDate dateCible = LocalDate.of(2026, 5, 15);
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(11L, dateCible, dateCible))
				.thenReturn(Optional.empty());

		Budget resultat = budgetService.rechercherParReferenceIdEtDateCible(11L, dateCible);

		assertNull(resultat);
	}

	@Test
	void rechercherParReferenceIdEtDateCibleTransformeUneErreurTechniqueEnServiceException() {
		LocalDate dateCible = LocalDate.of(2026, 5, 15);
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(11L, dateCible, dateCible))
				.thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.rechercherParReferenceIdEtDateCible(11L, dateCible));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.RECHERCHE_PAR_REFERENCE_ID_ET_DATE_CIBLE, exception.getErreur());
		assertEquals(11L, exception.getValues()[0]);
		assertEquals(dateCible, exception.getValues()[1]);
	}

	@Test
	void rechercherParReferenceIdEntreDateDebutEtDateFinRetourneLesBudgetsDuRepository() throws ServiceException {
		LocalDate dateDebut = LocalDate.of(2026, 5, 1);
		LocalDate dateFin = LocalDate.of(2026, 6, 30);
		List<Budget> budgets = List.of(
				budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31)),
				budget(2L, "BUDG-0000000002", TypePeriode.MENSUEL, LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)));
		when(budgetRepository.findByReferenceIdAndDateRange(11L, dateDebut, dateFin)).thenReturn(budgets);

		List<Budget> resultat = budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(11L, dateDebut, dateFin);

		assertSame(budgets, resultat);
	}

	@Test
	void rechercherParReferenceIdEntreDateDebutEtDateFinTransformeUneErreurTechniqueEnServiceException() {
		LocalDate dateDebut = LocalDate.of(2026, 5, 1);
		LocalDate dateFin = LocalDate.of(2026, 6, 30);
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findByReferenceIdAndDateRange(11L, dateDebut, dateFin)).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class,
				() -> budgetService.rechercherParReferenceIdEntreDateDebutEtDateFin(11L, dateDebut, dateFin));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.RECHERCHE_PAR_REFERENCE_ID_ET_PERIODE, exception.getErreur());
		assertEquals(11L, exception.getValues()[0]);
		assertEquals(dateDebut, exception.getValues()[1]);
		assertEquals(dateFin, exception.getValues()[2]);
	}

	@Test
	void rechercherDernierParReferenceIdRetourneLeDernierBudgetTrouve() throws ServiceException {
		Budget budget = budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(11L)).thenReturn(Optional.of(budget));

		Budget resultat = budgetService.rechercherDernierParReferenceId(11L);

		assertSame(budget, resultat);
	}

	@Test
	void rechercherDernierParReferenceIdRetourneNullSiAucunBudget() throws ServiceException {
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(11L)).thenReturn(Optional.empty());

		Budget resultat = budgetService.rechercherDernierParReferenceId(11L);

		assertNull(resultat);
	}

	@Test
	void rechercherDernierParReferenceIdTransformeUneErreurTechniqueEnServiceException() {
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(11L)).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.rechercherDernierParReferenceId(11L));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.RECHERCHE_HISTORIQUE_PAR_REFERENCE_ID, exception.getErreur());
		assertEquals(11L, exception.getValues()[0]);
	}

	@Test
	void creerBudgetRecadreLaPeriodeEtGenereLaCleApresCreation() throws ServiceException {
		Budget budget = budget(null, null, TypePeriode.MENSUEL, LocalDate.of(2026, 5, 15), null);
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.empty());
		preparerSauvegardeAvecId(42L);

		Budget resultat = budgetService.creerBudget(budget);

		assertSame(budget, resultat);
		assertEquals(LocalDate.of(2026, 5, 1), resultat.getDateDebut());
		assertEquals(LocalDate.of(2026, 5, 31), resultat.getDateFin());
		assertEquals("BUDG-0000000042", resultat.getCle());
		verify(budgetRepository, times(2)).save(budget);
	}

	@Test
	void creerBudgetConserveLaCleExistanteEtSauvegardeUneSeuleFois() throws ServiceException {
		Budget budget = budget(42L, "BUDG-EXISTANT", TypePeriode.TRIMESTRIEL, LocalDate.of(2026, 5, 15), null);
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.empty());
		when(budgetRepository.save(budget)).thenReturn(budget);

		Budget resultat = budgetService.creerBudget(budget);

		assertSame(budget, resultat);
		assertEquals(LocalDate.of(2026, 4, 1), resultat.getDateDebut());
		assertEquals(LocalDate.of(2026, 6, 30), resultat.getDateFin());
		assertEquals("BUDG-EXISTANT", resultat.getCle());
		verify(budgetRepository).save(budget);
	}

	@Test
	void creerBudgetRefuseUnChevauchementAvecLeDebutDePeriode() {
		Budget budget = budget(null, null, TypePeriode.MENSUEL, LocalDate.of(2026, 5, 15), null);
		Budget budgetExistant = budget(1L, "BUDG-0000000001", TypePeriode.TRIMESTRIEL, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 6, 30));
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.of(budgetExistant));
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
				reference.getId(), LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 1))).thenReturn(Optional.of(budgetExistant));

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.creerBudget(budget));

		assertSame(BudgetFonctionnelleErreur.CHEVAUCHEMENT_PERIODE_PRECEDENTE, exception.getErreur());
		verify(budgetRepository, never()).save(any(Budget.class));
	}

	@Test
	void creerBudgetRefuseUnChevauchementAvecLaFinDePeriode() {
		Budget budget = budget(null, null, TypePeriode.MENSUEL, LocalDate.of(2026, 5, 15), null);
		Budget dernierBudget = budget(1L, "BUDG-0000000001", TypePeriode.MENSUEL, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30));
		Budget budgetExistant = budget(2L, "BUDG-0000000002", TypePeriode.TRIMESTRIEL, LocalDate.of(2026, 5, 20), LocalDate.of(2026, 7, 31));
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.of(dernierBudget));
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
				reference.getId(), LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 1))).thenReturn(Optional.empty());
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
				reference.getId(), LocalDate.of(2026, 5, 31), LocalDate.of(2026, 5, 31))).thenReturn(Optional.of(budgetExistant));

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.creerBudget(budget));

		assertSame(BudgetFonctionnelleErreur.CHEVAUCHEMENT_PERIODE_SUIVANTE, exception.getErreur());
		verify(budgetRepository, never()).save(any(Budget.class));
	}

	@Test
	void modifierBudgetIgnoreSonPropreChevauchementEtRecadreLaPeriode() throws ServiceException {
		Budget budget = budget(42L, "BUDG-0000000042", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 15), LocalDate.of(2026, 5, 20));
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.of(budget));
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
				reference.getId(), LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 1))).thenReturn(Optional.of(budget));
		when(budgetRepository.findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
				reference.getId(), LocalDate.of(2026, 5, 31), LocalDate.of(2026, 5, 31))).thenReturn(Optional.of(budget));
		when(budgetRepository.save(budget)).thenReturn(budget);

		Budget resultat = budgetService.modifierBudget(budget);

		assertSame(budget, resultat);
		assertEquals(LocalDate.of(2026, 5, 1), resultat.getDateDebut());
		assertEquals(LocalDate.of(2026, 5, 31), resultat.getDateFin());
		verify(budgetRepository).save(budget);
	}

	@Test
	void reconduireBudgetCreeLaPeriodeSuivanteSansModifierLeBudgetOriginal() throws ServiceException {
		Budget budgetAReconduire = budget(42L, "BUDG-0000000042", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 15), LocalDate.of(2026, 5, 31));
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.empty());
		preparerSauvegardeAvecId(43L);

		Budget resultat = budgetService.reconduireBudget(budgetAReconduire);

		assertEquals(42L, budgetAReconduire.getId());
		assertEquals("BUDG-0000000042", budgetAReconduire.getCle());
		assertEquals(LocalDate.of(2026, 6, 1), resultat.getDateDebut());
		assertEquals(LocalDate.of(2026, 6, 30), resultat.getDateFin());
		assertEquals("BUDG-0000000043", resultat.getCle());
		assertSame(reference, resultat.getReference());
		assertEquals(TypePeriode.MENSUEL, resultat.getTypePeriode());
		assertEquals(TypeBudget.SOLDE_PREVU_NEGATIF, resultat.getTypeBudget());
		assertEquals(120000L, resultat.getMontantBudgetEnCentimes());
		assertEquals("Budget de test", resultat.getLibelle());
		verify(budgetRepository, times(2)).save(any(Budget.class));
	}

	@Test
	void supprimerBudgetControlePuisSupprimeLeBudget() throws ServiceException {
		Budget budget = budget(42L, "BUDG-0000000042", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));

		budgetService.supprimerBudget(budget);

		verify(budgetRepository).delete(budget);
	}

	@Test
	void supprimerBudgetTransformeUneErreurTechniqueEnServiceException() {
		Budget budget = budget(42L, "BUDG-0000000042", TypePeriode.MENSUEL, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
		RuntimeException cause = new RuntimeException("base indisponible");
		doThrow(cause).when(budgetRepository).delete(budget);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.supprimerBudget(budget));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.SUPPRESSION, exception.getErreur());
		assertEquals(Categorie.class.getSimpleName(), exception.getValues()[0]);
		assertEquals("ALIMENTATION", exception.getValues()[1]);
		assertEquals(LocalDate.of(2026, 5, 1), exception.getValues()[2]);
		assertEquals(LocalDate.of(2026, 5, 31), exception.getValues()[3]);
	}

	@Test
	void creerBudgetTransformeUneErreurTechniqueDeSauvegardeEnServiceException() {
		Budget budget = budget(null, null, TypePeriode.MENSUEL, LocalDate.of(2026, 5, 15), null);
		RuntimeException cause = new RuntimeException("base indisponible");
		when(budgetRepository.findFirstByReferenceIdOrderByDateFinDesc(reference.getId())).thenReturn(Optional.empty());
		when(budgetRepository.save(budget)).thenThrow(cause);

		ServiceException exception = assertThrows(ServiceException.class, () -> budgetService.creerBudget(budget));

		assertSame(cause, exception.getCause());
		assertSame(BudgetTechniqueErreur.ENREGISTREMENT, exception.getErreur());
		assertEquals(Categorie.class.getSimpleName(), exception.getValues()[0]);
		assertEquals("ALIMENTATION", exception.getValues()[1]);
		assertEquals(LocalDate.of(2026, 5, 1), exception.getValues()[2]);
		assertEquals(LocalDate.of(2026, 5, 31), exception.getValues()[3]);
	}

	private void preparerSauvegardeAvecId(Long id) {
		when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> {
			Budget budget = invocation.getArgument(0);
			if (budget.getId() == null) {
				budget.setId(id);
			}
			return budget;
		});
	}

	private Budget budget(Long id, String cle, TypePeriode typePeriode, LocalDate dateDebut, LocalDate dateFin) {
		Budget budget = new Budget();
		budget.setId(id);
		budget.setCle(cle);
		budget.setReference(reference);
		budget.setTypePeriode(typePeriode);
		budget.setDateDebut(dateDebut);
		budget.setDateFin(dateFin);
		budget.setTypeBudget(TypeBudget.SOLDE_PREVU_NEGATIF);
		budget.setMontantBudgetEnCentimes(120000L);
		budget.setLibelle("Budget de test");
		return budget;
	}
}
