package fr.colline.monatis.rapports.service;

import static fr.colline.monatis.rapports.RapportTestFixtures.banque;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteExterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.operation;
import static fr.colline.monatis.rapports.RapportTestFixtures.titulaire;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;

@ExtendWith(MockitoExtension.class)
class PlusMoinsValueServiceTest {

	@Mock
	private SoldeService soldeService;

	@Mock
	private CompteInterneService compteInterneService;

	@Mock
	private OperationService operationService;

	@InjectMocks
	private PlusMoinsValueService plusMoinsValueService;

	@Test
	void rechercherPlusMoinsValueCompteInternePeriodeIgnoreOperationsAvantVieCompte() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "AVANT");
		compte.setDateSoldeInitial(LocalDate.of(2026, 6, 15));
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(0L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(0L);

		PlusMoinsValueCompteInternePeriode resultat = plusMoinsValueService.rechercherPlusMoinsValueCompteInternePeriode(
				compte,
				debut,
				fin);

		assertEquals(debut, resultat.getDateDebutPeriode());
		assertEquals(fin, resultat.getDateFinPeriode());
		assertEquals(0L, resultat.getMontantSoldeInitialEnCentimes());
		assertEquals(0L, resultat.getMontantOperationsEnCentimes());
		assertEquals(0L, resultat.getMontantPlusMoinsValueNetteEnCentimes());
		assertNull(resultat.getTauxPlusMoinsValueNette());
		assertEquals(0L, resultat.getMontantSoldeFinalEnCentimes());
		assertEquals(0L, resultat.getMontantFraisEnCentimes());
		assertNull(resultat.getTauxFrais());
		verifyNoInteractions(operationService);
	}

	@Test
	void rechercherPlusMoinsValueCompteInternePeriodeIgnoreOperationsApresClotureCompte() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "CLOS");
		compte.setDateCloture(LocalDate.of(2026, 4, 30));
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(12_000L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(12_000L);

		PlusMoinsValueCompteInternePeriode resultat = plusMoinsValueService.rechercherPlusMoinsValueCompteInternePeriode(
				compte,
				debut,
				fin);

		assertEquals(12_000L, resultat.getMontantSoldeInitialEnCentimes());
		assertEquals(0L, resultat.getMontantOperationsEnCentimes());
		assertEquals(0L, resultat.getMontantPlusMoinsValueNetteEnCentimes());
		assertNull(resultat.getTauxPlusMoinsValueNette());
		assertEquals(12_000L, resultat.getMontantSoldeFinalEnCentimes());
		assertEquals(0L, resultat.getMontantFraisEnCentimes());
		assertNull(resultat.getTauxFrais());
		verifyNoInteractions(operationService);
	}

	@Test
	void rechercherPlusMoinsValueCompteInternePeriodeAjouteSoldeInitialQuandCompteOuvreDansPeriode() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "OUVERTURE");
		compte.setDateSoldeInitial(LocalDate.of(2026, 5, 6));
		compte.setMontantSoldeInitialEnCentimes(2_000L);
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 10);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(0L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(2_500L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, debut, fin)).thenReturn(List.of());

		PlusMoinsValueCompteInternePeriode resultat = plusMoinsValueService.rechercherPlusMoinsValueCompteInternePeriode(
				compte,
				debut,
				fin);

		assertEquals(0L, resultat.getMontantSoldeInitialEnCentimes());
		assertEquals(2_000L, resultat.getMontantOperationsEnCentimes());
		assertEquals(500L, resultat.getMontantPlusMoinsValueNetteEnCentimes());
		assertEquals(25D, resultat.getTauxPlusMoinsValueNette());
		assertEquals(2_500L, resultat.getMontantSoldeFinalEnCentimes());
		assertEquals(0L, resultat.getMontantFraisEnCentimes());
		assertEquals(0D, resultat.getTauxFrais());
	}

	@Test
	void rechercherPlusMoinsValueCompteInternePeriodeNeCalculePasLesTauxQuandLesBasesSontNulles() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "ZERO");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Operation frais = operation(1L, TypeOperation.FRAIS_COMPTE_COURANT, LocalDate.of(2026, 5, 12), 200L, tiers, compte);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(0L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(0L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, debut, fin)).thenReturn(List.of(frais));

		PlusMoinsValueCompteInternePeriode resultat = plusMoinsValueService.rechercherPlusMoinsValueCompteInternePeriode(
				compte,
				debut,
				fin);

		assertEquals(0L, resultat.getMontantOperationsEnCentimes());
		assertEquals(0L, resultat.getMontantPlusMoinsValueNetteEnCentimes());
		assertNull(resultat.getTauxPlusMoinsValueNette());
		assertEquals(0L, resultat.getMontantSoldeFinalEnCentimes());
		assertEquals(200L, resultat.getMontantFraisEnCentimes());
		assertNull(resultat.getTauxFrais());
	}

	@Test
	void rechercherPlusMoinsValueCompteInterneLigneDecoupeLesPeriodesMensuellesCompletes() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "PERIODES");
		LocalDate debutEtat = LocalDate.of(2026, 5, 15);
		LocalDate finEtat = LocalDate.of(2026, 6, 2);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(1_000L);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 5, 31))).thenReturn(1_100L);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 6, 30))).thenReturn(1_200L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(
				compte,
				LocalDate.of(2026, 5, 1),
				LocalDate.of(2026, 5, 31))).thenReturn(List.of());
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(
				compte,
				LocalDate.of(2026, 6, 1),
				LocalDate.of(2026, 6, 30))).thenReturn(List.of());

		PlusMoinsValueCompteInterneLigne resultat = plusMoinsValueService.rechercherPlusMoinsValueCompteInterneLigne(
				compte,
				debutEtat,
				finEtat,
				TypePeriode.MENSUEL);

		assertSame(compte, resultat.getCompteInterne());
		assertEquals(2, resultat.getPeriodes().length);
		assertEquals(LocalDate.of(2026, 5, 1), resultat.getPeriodes()[0].getDateDebutPeriode());
		assertEquals(LocalDate.of(2026, 5, 31), resultat.getPeriodes()[0].getDateFinPeriode());
		assertEquals(100L, resultat.getPeriodes()[0].getMontantPlusMoinsValueNetteEnCentimes());
		assertEquals(LocalDate.of(2026, 6, 1), resultat.getPeriodes()[1].getDateDebutPeriode());
		assertEquals(LocalDate.of(2026, 6, 30), resultat.getPeriodes()[1].getDateFinPeriode());
		assertEquals(100L, resultat.getPeriodes()[1].getMontantPlusMoinsValueNetteEnCentimes());
	}

	@Test
	void rechercherPlusMoinsValueTypeFonctionnementFiltreParBanqueTitulaireEtComptesSelectionnes() throws ServiceException {
		Banque banqueSelectionnee = banque(1L, "BANQUE");
		Banque autreBanque = banque(2L, "AUTRE");
		Titulaire titulaireSelectionne = titulaire(1L, "TITULAIRE");
		Titulaire autreTitulaire = titulaire(2L, "AUTRE");
		CompteInterne conserve = compteInterne(1L, "CONSERVE");
		conserve.changerBanque(banqueSelectionnee);
		conserve.getTitulaires().add(titulaireSelectionne);
		CompteInterne mauvaisBanque = compteInterne(2L, "MAUVAISE_BANQUE");
		mauvaisBanque.changerBanque(autreBanque);
		mauvaisBanque.getTitulaires().add(titulaireSelectionne);
		CompteInterne mauvaisTitulaire = compteInterne(3L, "MAUVAIS_TITULAIRE");
		mauvaisTitulaire.changerBanque(banqueSelectionnee);
		mauvaisTitulaire.getTitulaires().add(autreTitulaire);
		CompteInterne horsSelection = compteInterne(4L, "HORS_SELECTION");
		horsSelection.changerBanque(banqueSelectionnee);
		horsSelection.getTitulaires().add(titulaireSelectionne);
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		when(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT))
				.thenReturn(List.of(conserve, mauvaisBanque, mauvaisTitulaire, horsSelection));
		when(soldeService.rechercherSolde(conserve, LocalDate.of(2026, 4, 30))).thenReturn(10_000L);
		when(soldeService.rechercherSolde(conserve, fin)).thenReturn(10_500L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(conserve, debut, fin)).thenReturn(List.of());

		PlusMoinsValueTypeFonctionnementLigne resultat = plusMoinsValueService.rechercherPlusMoinsValueTypeFonctionnementLigne(
				List.of(conserve),
				TypeFonctionnement.COURANT,
				banqueSelectionnee,
				titulaireSelectionne,
				debut,
				fin,
				null);

		assertEquals(TypeFonctionnement.COURANT, resultat.getTypeFonctionnement());
		assertEquals(1, resultat.getLignesCompteInterne().size());
		assertSame(conserve, resultat.getLignesCompteInterne().get(0).getCompteInterne());
		assertEquals(10_000L, resultat.getCumulsPeriodes()[0].getMontantSoldeInitialEnCentimes());
		assertEquals(0L, resultat.getCumulsPeriodes()[0].getMontantOperationsEnCentimes());
		assertEquals(500L, resultat.getCumulsPeriodes()[0].getMontantPlusMoinsValueNetteEnCentimes());
		assertEquals(10_500L, resultat.getCumulsPeriodes()[0].getMontantSoldeFinalEnCentimes());
		verify(operationService).rechercherOperationsParCompteEntreDateDebutEtDateFin(conserve, debut, fin);
		verifyNoMoreInteractions(operationService);
	}

	@Test
	void rechercherPlusMoinsValueTypeFonctionnementAgregeLesComptesEtRecalculeLesTaux() throws ServiceException {
		CompteInterne compte1 = compteInterne(1L, "COMPTE_1");
		CompteInterne compte2 = compteInterne(2L, "COMPTE_2");
		CompteExterne tiers = compteExterne(99L, "TIERS");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Operation frais1 = operation(1L, TypeOperation.FRAIS_COMPTE_COURANT, LocalDate.of(2026, 5, 12), 100L, tiers, compte1);
		Operation frais2 = operation(2L, TypeOperation.FRAIS_COMPTE_COURANT, LocalDate.of(2026, 5, 14), 200L, tiers, compte2);
		when(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT)).thenReturn(List.of(compte1, compte2));
		when(soldeService.rechercherSolde(compte1, LocalDate.of(2026, 4, 30))).thenReturn(1_000L);
		when(soldeService.rechercherSolde(compte1, fin)).thenReturn(1_100L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte1, debut, fin)).thenReturn(List.of(frais1));
		when(soldeService.rechercherSolde(compte2, LocalDate.of(2026, 4, 30))).thenReturn(2_000L);
		when(soldeService.rechercherSolde(compte2, fin)).thenReturn(2_600L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte2, debut, fin)).thenReturn(List.of(frais2));

		PlusMoinsValueTypeFonctionnementLigne resultat = plusMoinsValueService.rechercherPlusMoinsValueTypeFonctionnementLigne(
				List.of(),
				TypeFonctionnement.COURANT,
				null,
				null,
				debut,
				fin,
				null);

		PlusMoinsValueTypeFonctionnementPeriode cumul = resultat.getCumulsPeriodes()[0];
		assertEquals(2, resultat.getLignesCompteInterne().size());
		assertEquals(3_000L, cumul.getMontantSoldeInitialEnCentimes());
		assertEquals(0L, cumul.getMontantOperationsEnCentimes());
		assertEquals(700L, cumul.getMontantPlusMoinsValueNetteEnCentimes());
		assertEquals(23.333D, cumul.getTauxPlusMoinsValueNette(), 0.001D);
		assertEquals(3_700L, cumul.getMontantSoldeFinalEnCentimes());
		assertEquals(300L, cumul.getMontantFraisEnCentimes());
		assertEquals(8.108D, cumul.getTauxFrais(), 0.001D);
	}

	@Test
	void rechercherPlusMoinsValueTypeFonctionnementCreeDesCumulsMensuelsVidesApresFiltres() throws ServiceException {
		Banque banqueSelectionnee = banque(1L, "BANQUE");
		Banque autreBanque = banque(2L, "AUTRE");
		CompteInterne filtre = compteInterne(1L, "FILTRE");
		filtre.changerBanque(autreBanque);
		when(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT)).thenReturn(List.of(filtre));

		PlusMoinsValueTypeFonctionnementLigne resultat = plusMoinsValueService.rechercherPlusMoinsValueTypeFonctionnementLigne(
				List.of(),
				TypeFonctionnement.COURANT,
				banqueSelectionnee,
				null,
				LocalDate.of(2026, 5, 15),
				LocalDate.of(2026, 6, 2),
				TypePeriode.MENSUEL);

		assertEquals(0, resultat.getLignesCompteInterne().size());
		assertEquals(2, resultat.getCumulsPeriodes().length);
		assertEquals(LocalDate.of(2026, 5, 1), resultat.getCumulsPeriodes()[0].getDateDebutPeriode());
		assertEquals(LocalDate.of(2026, 5, 31), resultat.getCumulsPeriodes()[0].getDateFinPeriode());
		assertEquals(LocalDate.of(2026, 6, 1), resultat.getCumulsPeriodes()[1].getDateDebutPeriode());
		assertEquals(LocalDate.of(2026, 6, 30), resultat.getCumulsPeriodes()[1].getDateFinPeriode());
		assertEquals(0L, resultat.getCumulsPeriodes()[1].getMontantPlusMoinsValueNetteEnCentimes());
		assertNull(resultat.getCumulsPeriodes()[1].getTauxPlusMoinsValueNette());
		assertEquals(0D, resultat.getCumulsPeriodes()[1].getTauxFrais());
		verifyNoInteractions(soldeService, operationService);
	}
}
