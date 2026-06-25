package fr.colline.monatis.rapports.service;

import static fr.colline.monatis.rapports.RapportTestFixtures.beneficiaire;
import static fr.colline.monatis.rapports.RapportTestFixtures.categorie;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteExterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.operation;
import static fr.colline.monatis.rapports.RapportTestFixtures.operationLigne;
import static fr.colline.monatis.rapports.RapportTestFixtures.sousCategorie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.SuiviBudgetPeriode;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;

@ExtendWith(MockitoExtension.class)
class DepenseRecetteServiceTest {

	@Mock
	private OperationService operationService;

	@Mock
	private SuiviBudgetService suiviBudgetService;

	@InjectMocks
	private DepenseRecetteService depenseRecetteService;

	@Test
	void rechercherDepenseRecetteSousCategoriePeriodeAdditionneDepensesRecettesEtSuiviBudget() throws ServiceException {
		Categorie categorie = categorie(1L, "ALIM");
		SousCategorie sousCategorie = sousCategorie(7L, "COURSES", categorie);
		Beneficiaire beneficiaire = beneficiaire(8L, "MARCHE");
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Operation depense = operation(1L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 3), 1_000L, tiers, compte);
		Operation recette = operation(2L, TypeOperation.RECETTE, LocalDate.of(2026, 5, 8), 2_500L, compte, tiers);
		OperationLigne ligneDepense = operationLigne(1, depense, 1_000L, sousCategorie, beneficiaire);
		OperationLigne ligneRecette = operationLigne(1, recette, 2_500L, sousCategorie, beneficiaire);
		SuiviBudgetPeriode suiviBudget = new SuiviBudgetPeriode();

		when(operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(7L, 8L, debut, fin))
				.thenReturn(Stream.of(ligneDepense, ligneRecette));
		when(suiviBudgetService.calculerSuiviBudget(7L, debut, fin, 1_500L)).thenReturn(suiviBudget);

		DepenseRecettePeriode resultat = depenseRecetteService.rechercherDepenseRecetteSousCategoriePeriode(sousCategorie, beneficiaire, debut, fin);

		assertEquals(debut, resultat.getDateDebutPeriode());
		assertEquals(fin, resultat.getDateFinPeriode());
		assertEquals(2_500L, resultat.getMontantRecetteEnCentimes());
		assertEquals(1_000L, resultat.getMontantDepenseEnCentimes());
		assertEquals(1_500L, resultat.getSoldeDepenseRecetteEnCentimes());
		assertSame(suiviBudget, resultat.getSuiviBudget());
	}

	@Test
	void initialiserPeriodesCreeUnePeriodeParMoisCadre() throws ServiceException {
		DepenseRecettePeriode[] periodes = depenseRecetteService.initialiserPeriodes(
				LocalDate.of(2026, 5, 15),
				LocalDate.of(2026, 6, 2),
				TypePeriode.MENSUEL);

		assertEquals(2, periodes.length);
		assertEquals(LocalDate.of(2026, 5, 1), periodes[0].getDateDebutPeriode());
		assertEquals(LocalDate.of(2026, 5, 31), periodes[0].getDateFinPeriode());
		assertEquals(LocalDate.of(2026, 6, 1), periodes[1].getDateDebutPeriode());
		assertEquals(LocalDate.of(2026, 6, 30), periodes[1].getDateFinPeriode());
		assertEquals(0L, periodes[0].getMontantDepenseEnCentimes());
		assertEquals(0L, periodes[1].getMontantRecetteEnCentimes());
	}

	@Test
	void cumulerPeriodesAdditionneLesMontantsEtLeSuiviBudget() {
		DepenseRecettePeriode cumulee = periode(100L, 50L, 50L);
		DepenseRecettePeriode aCumuler = periode(300L, 80L, 220L);
		SuiviBudgetPeriode suiviCumule = new SuiviBudgetPeriode();
		SuiviBudgetPeriode suiviACumuler = new SuiviBudgetPeriode();
		SuiviBudgetPeriode suiviResultat = new SuiviBudgetPeriode();
		cumulee.setSuiviBudget(suiviCumule);
		aCumuler.setSuiviBudget(suiviACumuler);
		when(suiviBudgetService.cumulerSuiviBudget(suiviCumule, suiviACumuler)).thenReturn(suiviResultat);

		DepenseRecettePeriode[] resultat = depenseRecetteService.cumulerPeriodes(
				new DepenseRecettePeriode[] { cumulee },
				new DepenseRecettePeriode[] { aCumuler });

		assertEquals(400L, resultat[0].getMontantRecetteEnCentimes());
		assertEquals(130L, resultat[0].getMontantDepenseEnCentimes());
		assertEquals(270L, resultat[0].getSoldeDepenseRecetteEnCentimes());
		assertSame(suiviResultat, resultat[0].getSuiviBudget());
	}

	// TODO Revoir ce test
//	@Test
//	void rechercherDepenseRecetteCategorieLigneFiltreLesSousCategoriesSelectionnees() throws ServiceException {
//		Categorie categorie = categorie(1L, "ALIM");
//		SousCategorie retenue = sousCategorie(7L, "COURSES", categorie);
//		SousCategorie ignoree = sousCategorie(8L, "RESTO", categorie);
//		LocalDate debut = LocalDate.of(2026, 5, 1);
//		LocalDate fin = LocalDate.of(2026, 5, 31);
//		when(operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(7L, null, debut, fin)).thenReturn(Stream.empty());
//		when(suiviBudgetService.calculerSuiviBudget(7L, debut, fin, 0L)).thenReturn(null);
//
//		DepenseRecetteCategorieLigne resultat = depenseRecetteService.rechercherDepenseRecetteCategorieLigne(
//				List.of(retenue),
//				categorie,
//				null,
//				debut,
//				fin,
//				null);
//
//		assertEquals(categorie, resultat.getCategorie());
//		assertEquals(1, resultat.getLignesSousCategorie().size());
//		DepenseRecetteSousCategorieLigne ligne = resultat.getLignesSousCategorie().get(0);
//		assertSame(retenue, ligne.getSousCategorie());
//		assertEquals(1, resultat.getCumulCategorie().length);
//		assertEquals(0L, resultat.getCumulCategorie()[0].getSoldeDepenseRecetteEnCentimes());
//		assertEquals("RESTO", ignoree.getNom());
//	}

	private static DepenseRecettePeriode periode(Long recette, Long depense, Long solde) {
		DepenseRecettePeriode periode = new DepenseRecettePeriode();
		periode.setDateDebutPeriode(LocalDate.of(2026, 5, 1));
		periode.setDateFinPeriode(LocalDate.of(2026, 5, 31));
		periode.setMontantRecetteEnCentimes(recette);
		periode.setMontantDepenseEnCentimes(depense);
		periode.setSoldeDepenseRecetteEnCentimes(solde);
		return periode;
	}
}
