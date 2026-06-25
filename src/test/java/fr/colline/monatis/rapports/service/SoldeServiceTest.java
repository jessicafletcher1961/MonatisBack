package fr.colline.monatis.rapports.service;

import static fr.colline.monatis.rapports.RapportTestFixtures.compteExterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.evaluation;
import static fr.colline.monatis.rapports.RapportTestFixtures.operation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.typologies.model.TypeOperation;

@ExtendWith(MockitoExtension.class)
class SoldeServiceTest {

	@Mock
	private OperationService operationService;

	@Mock
	private EvaluationService evaluationService;

	@InjectMocks
	private SoldeService soldeService;

	@Test
	void rechercherSoldeRetourneZeroAvantLaVeilleDuSoldeInitial() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");

		Long solde = soldeService.rechercherSolde(compte, LocalDate.of(2026, 1, 8));

		assertEquals(0L, solde);
		verifyNoInteractions(operationService, evaluationService);
	}

	@Test
	void rechercherSoldeRetourneLeSoldeInitialLaVeilleDuSoldeInitial() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");

		Long solde = soldeService.rechercherSolde(compte, LocalDate.of(2026, 1, 9));

		assertEquals(10_000L, solde);
		verifyNoInteractions(operationService, evaluationService);
	}

	@Test
	void rechercherSoldeCompteInterneSansEvaluationAdditionneRecettesEtDepenses() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne externe = compteExterne(2L, "TIERS");
		LocalDate dateSolde = LocalDate.of(2026, 1, 31);
		Operation recette = operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 1, 12), 3_000L, compte, externe);
		Operation depense = operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 1, 15), 2_000L, externe, compte);

		when(evaluationService.rechercherDerniereParCompteInterneEntreDateDebutEtDateFin(compte, compte.getDateSoldeInitial(), dateSolde))
				.thenReturn(null);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, compte.getDateSoldeInitial(), dateSolde))
				.thenReturn(List.of(recette, depense));

		Long solde = soldeService.rechercherSolde(compte, dateSolde);

		assertEquals(11_000L, solde);
	}

	@Test
	void rechercherSoldeCompteInterneAvecEvaluationRepartDeLaDerniereEvaluation() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne externe = compteExterne(2L, "TIERS");
		LocalDate dateSolde = LocalDate.of(2026, 1, 31);
		Evaluation evaluation = evaluation(compte, LocalDate.of(2026, 1, 20), 20_000L);
		Operation recette = operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 1, 22), 500L, compte, externe);
		Operation depense = operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 1, 25), 300L, externe, compte);

		when(evaluationService.rechercherDerniereParCompteInterneEntreDateDebutEtDateFin(compte, compte.getDateSoldeInitial(), dateSolde))
				.thenReturn(evaluation);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, LocalDate.of(2026, 1, 21), dateSolde))
				.thenReturn(List.of(recette, depense));

		Long solde = soldeService.rechercherSolde(compte, dateSolde);

		assertEquals(20_200L, solde);
		verify(operationService, never()).rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, compte.getDateSoldeInitial(), dateSolde);
	}

	@Test
	void rechercherSoldeCompteExterneAdditionneLesOperationsJusquaLaDate() throws ServiceException {
		CompteExterne compte = compteExterne(1L, "EXTERNE");
		CompteInterne interne = compteInterne(2L, "COURANT");
		LocalDate dateSolde = LocalDate.of(2026, 1, 31);
		Operation recette = operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 1, 12), 1_500L, compte, interne);
		Operation depense = operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 1, 15), 700L, interne, compte);

		when(operationService.rechercherOperationsDepenseParCompteJusqueDateFin(compte, dateSolde)).thenReturn(List.of(depense));
		when(operationService.rechercherOperationsRecetteParCompteJusqueDateFin(compte, dateSolde)).thenReturn(List.of(recette));

		Long solde = soldeService.rechercherSolde(compte, dateSolde);

		assertEquals(800L, solde);
		verifyNoInteractions(evaluationService);
	}
}
