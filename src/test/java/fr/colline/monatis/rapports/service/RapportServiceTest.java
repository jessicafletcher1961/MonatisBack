package fr.colline.monatis.rapports.service;

import static fr.colline.monatis.rapports.RapportTestFixtures.categorie;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteExterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.operation;
import static fr.colline.monatis.rapports.RapportTestFixtures.operationLigne;
import static fr.colline.monatis.rapports.RapportTestFixtures.sousCategorie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
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
import fr.colline.monatis.rapports.model.EtatBilanPatrimoine;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValue;
import fr.colline.monatis.rapports.model.EtatRemunerationsFrais;
import fr.colline.monatis.rapports.model.ReleveNonCategorise;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.model.ReleveSousCategorie;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;

@ExtendWith(MockitoExtension.class)
class RapportServiceTest {

	@Mock
	private OperationService operationService;

	@Mock
	private SoldeService soldeService;

	@Mock
	private PlusMoinsValueService plusMoinsValueService;

	@Mock
	private DepenseRecetteService depenseRecetteService;

	@Mock
	private RemunerationsFraisService remunerationsFraisService;

	@Mock
	private BilanPatrimoineService bilanPatrimoineService;

	@InjectMocks
	private RapportService rapportService;

	@Test
	void rechercherReleveOperationCompteCalculeLesTotauxEtLEcart() throws ServiceException {
		CompteExterne compte = compteExterne(1L, "EXTERNE");
		CompteInterne interne = compteInterne(2L, "COURANT");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Operation recette = operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 5, 10), 2_000L, compte, interne);
		Operation depense = operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 12), 700L, interne, compte);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(1_000L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(2_500L);
		when(operationService.rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(compte, debut, fin)).thenReturn(List.of(recette));
		when(operationService.rechercherOperationsDepenseParCompteEntreDateDebutEtDateFin(compte, debut, fin)).thenReturn(List.of(depense));

		ReleveCompte releve = rapportService.rechercherReleveOperationCompte(compte, debut, fin);

		assertSame(compte, releve.getCompte());
		assertEquals(2_000L, releve.getMontantTotalOperationsRecetteEnCentimes());
		assertEquals(700L, releve.getMontantTotalOperationsDepenseEnCentimes());
		assertEquals(200L, releve.getMontantEcartEnCentimes());
		assertEquals(List.of(recette), releve.getOperationsRecette());
		assertEquals(List.of(depense), releve.getOperationsDepense());
	}

	@Test
	void rechercherReleveOperationCompteRetourneUnReleveVideHorsVieDuCompteInterne() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		LocalDate debut = LocalDate.of(2025, 12, 1);
		LocalDate fin = LocalDate.of(2025, 12, 31);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2025, 11, 30))).thenReturn(0L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(0L);

		ReleveCompte releve = rapportService.rechercherReleveOperationCompte(compte, debut, fin);

		assertEquals(0L, releve.getMontantTotalOperationsRecetteEnCentimes());
		assertEquals(0L, releve.getMontantTotalOperationsDepenseEnCentimes());
		assertEquals(0, releve.getOperationsRecette().size());
		assertEquals(0, releve.getOperationsDepense().size());
		verify(operationService, never()).rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(compte, debut, fin);
	}

	@Test
	void rechercherRelevesParCategorieDeleguentAuxOperations() throws ServiceException {
		Categorie categorie = categorie(1L, "ALIM");
		SousCategorie sousCategorie = sousCategorie(2L, "COURSES", categorie);
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		Operation operation = operation(1L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 2), 1_000L, tiers, compte);
		OperationLigne ligne = operationLigne(1, operation, 1_000L, sousCategorie);
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		when(operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(null, null, debut, fin)).thenReturn(Stream.of(ligne));
		when(operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(2L, null, debut, fin)).thenReturn(Stream.of(ligne));

		ReleveNonCategorise nonCategorise = rapportService.rechercherReleveOperationNonCategorise(debut, fin);
		ReleveSousCategorie parSousCategorie = rapportService.rechercherReleveOperationSousCategorie(sousCategorie, debut, fin);

		assertEquals(List.of(ligne), nonCategorise.getOperationsLignes());
		assertSame(sousCategorie, parSousCategorie.getSousCategorie());
		assertEquals(List.of(ligne), parSousCategorie.getOperationsLignes());
	}

	@Test
	void rechercherResumeCompteInterneAssocieLeCompteLaDateEtLeSolde() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		LocalDate dateSolde = LocalDate.of(2026, 5, 31);
		when(soldeService.rechercherSolde(compte, dateSolde)).thenReturn(12_345L);

		ResumeCompteInterne resume = rapportService.rechercherResumeCompteInterne(compte, dateSolde);

		assertSame(compte, resume.getCompteInterne());
		assertEquals(dateSolde, resume.getDateSolde());
		assertEquals(12_345L, resume.getMontantSoldeEnCentimes());
	}

//	@Test
//	void rechercherEtatDepenseRecetteCumuleLesLignesCategorie() throws ServiceException {
//		Categorie categorie = categorie(1L, "ALIM");
//		LocalDate debut = LocalDate.of(2026, 5, 1);
//		LocalDate fin = LocalDate.of(2026, 5, 31);
//		DepenseRecettePeriode periodeInitiale = depenseRecettePeriode(0L, 0L, 0L);
//		DepenseRecettePeriode periodeCategorie = depenseRecettePeriode(1_000L, 300L, 700L);
//		DepenseRecettePeriode periodeCumulee = depenseRecettePeriode(1_000L, 300L, 700L);
//		DepenseRecettePeriode[] cumulInitial = new DepenseRecettePeriode[] { periodeInitiale };
//		DepenseRecetteCategorieLigne ligne = new DepenseRecetteCategorieLigne();
//		ligne.setCategorie(categorie);
//		ligne.setLignesSousCategorie(List.of());
//		ligne.setCumulCategorie(new DepenseRecettePeriode[] { periodeCategorie });
//		when(depenseRecetteService.initialiserPeriodes(debut, fin, TypePeriode.MENSUEL)).thenReturn(cumulInitial);
//		when(depenseRecetteService.rechercherDepenseRecetteCategorieLigne(List.of(), categorie, null, debut, fin, TypePeriode.MENSUEL)).thenReturn(ligne);
//		when(depenseRecetteService.cumulerPeriodes(cumulInitial, ligne.getCumulCategorie()))
//				.thenReturn(new DepenseRecettePeriode[] { periodeCumulee });
//
//		EtatDepenseRecette etat = rapportService.rechercherEtatDepenseRecette(List.of(), List.of(categorie), null, debut, fin, TypePeriode.MENSUEL);
//
//		assertEquals(List.of(categorie), etat.getCategories());
//		assertEquals(List.of(ligne), etat.getLignesCategorie());
//		assertSame(periodeCumulee, etat.getCumulEtat()[0]);
//	}

	@Test
	void rechercherEtatPlusMoinsValueExposeLesLignesEtLesCumuls() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		PlusMoinsValueTypeFonctionnementLigne ligne = plusMoinsValueLigne(TypeFonctionnement.COURANT, 10_000L, 500L, 300L, 10_800L, 100L);
		when(plusMoinsValueService.rechercherPlusMoinsValueTypeFonctionnementLigne(List.of(), TypeFonctionnement.COURANT, null, null, debut, fin, null))
				.thenReturn(ligne);

		EtatPlusMoinsValue etat = rapportService.rechercherEtatPlusMoinsValue(
				List.of(),
				List.of(TypeFonctionnement.COURANT),
				null,
				null,
				debut,
				fin,
				null);

		assertEquals(List.of(TypeFonctionnement.COURANT), etat.getTypesFonctionnements());
		assertEquals(List.of(ligne), etat.getLignesTypeFonctionnement());
		assertEquals(10_000L, etat.getCumuls()[0].getMontantSoldeInitialEnCentimes());
		assertEquals(500L, etat.getCumuls()[0].getMontantOperationsEnCentimes());
		assertEquals(300L, etat.getCumuls()[0].getMontantPlusMoinsValueNetteEnCentimes());
		assertEquals(10_800L, etat.getCumuls()[0].getMontantSoldeFinalEnCentimes());
		assertEquals(100L, etat.getCumuls()[0].getMontantFraisEnCentimes());
	}

	@Test
	void rechercherEtatRemunerationsFraisCumuleLesTypesDeFonctionnement() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		RemunerationsFraisTypeFonctionnementLigne ligneCourant = remunerationLigne(TypeFonctionnement.COURANT, 1_000L, 200L, 800L);
		RemunerationsFraisTypeFonctionnementLigne ligneFinancier = remunerationLigne(TypeFonctionnement.FINANCIER, 300L, 50L, 250L);
		when(remunerationsFraisService.rechercherRemunerationsFraisTypeFonctionnementLigne(List.of(), TypeFonctionnement.COURANT, null, debut, fin, null))
				.thenReturn(ligneCourant);
		when(remunerationsFraisService.rechercherRemunerationsFraisTypeFonctionnementLigne(List.of(), TypeFonctionnement.FINANCIER, null, debut, fin, null))
				.thenReturn(ligneFinancier);

		EtatRemunerationsFrais etat = rapportService.rechercherEtatRemunerationsFrais(
				List.of(),
				List.of(TypeFonctionnement.COURANT, TypeFonctionnement.FINANCIER),
				null,
				debut,
				fin,
				null);

		assertEquals(2, etat.getLignesTypeFonctionnement().size());
		assertEquals(1_300L, etat.getCumuls()[0].getMontantRemunerationsEnCentimes());
		assertEquals(250L, etat.getCumuls()[0].getMontantFraisEnCentimes());
		assertEquals(1_050L, etat.getCumuls()[0].getSoldeRemunerationsFraisEnCentimes());
	}

	@Test
	void rechercherEtatBilanPatrimoineCumuleLesTypesDeFonctionnement() throws ServiceException {
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		BilanPatrimoineTypeFonctionnementLigne ligneCourant = bilanLigne(TypeFonctionnement.COURANT, 10_000L, 11_000L, 1_000L, 500L, 100L, 400L);
		BilanPatrimoineTypeFonctionnementLigne ligneFinancier = bilanLigne(TypeFonctionnement.FINANCIER, 20_000L, 21_000L, 2_000L, 700L, 200L, 500L);
		when(bilanPatrimoineService.rechercherBilanPatrimoineTypeFonctionnementLigne(List.of(), TypeFonctionnement.COURANT, null, debut, fin, null))
				.thenReturn(ligneCourant);
		when(bilanPatrimoineService.rechercherBilanPatrimoineTypeFonctionnementLigne(List.of(), TypeFonctionnement.FINANCIER, null, debut, fin, null))
				.thenReturn(ligneFinancier);

		EtatBilanPatrimoine etat = rapportService.rechercherEtatBilanPatrimoine(
				List.of(),
				List.of(TypeFonctionnement.COURANT, TypeFonctionnement.FINANCIER),
				null,
				debut,
				fin,
				null);

		assertEquals(30_000L, etat.getMontantSoldeInitialEnCentimes());
		assertEquals(32_000L, etat.getCumuls()[0].getMontantSoldeFinalEnCentimes());
		assertEquals(3_000L, etat.getCumuls()[0].getMontantTotalRecetteEnCentimes());
		assertEquals(1_200L, etat.getCumuls()[0].getMontantTotalDepenseEnCentimes());
		assertEquals(300L, etat.getCumuls()[0].getSoldeTotalTechniqueEnCentimes());
		assertEquals(900L, etat.getCumuls()[0].getMontantEcartNonJustifieEnCentimes());
	}

	private static DepenseRecettePeriode depenseRecettePeriode(Long recette, Long depense, Long solde) {
		DepenseRecettePeriode periode = new DepenseRecettePeriode();
		periode.setDateDebutPeriode(LocalDate.of(2026, 5, 1));
		periode.setDateFinPeriode(LocalDate.of(2026, 5, 31));
		periode.setMontantRecetteEnCentimes(recette);
		periode.setMontantDepenseEnCentimes(depense);
		periode.setSoldeDepenseRecetteEnCentimes(solde);
		return periode;
	}

	private static RemunerationsFraisTypeFonctionnementLigne remunerationLigne(
			TypeFonctionnement typeFonctionnement,
			Long remunerations,
			Long frais,
			Long solde) {

		RemunerationsFraisTypeFonctionnementPeriode periode = new RemunerationsFraisTypeFonctionnementPeriode();
		periode.setDateDebutPeriode(LocalDate.of(2026, 5, 1));
		periode.setDateFinPeriode(LocalDate.of(2026, 5, 31));
		periode.setMontantRemunerationsEnCentimes(remunerations);
		periode.setMontantFraisEnCentimes(frais);
		periode.setSoldeRemunerationsFraisEnCentimes(solde);
		RemunerationsFraisTypeFonctionnementLigne ligne = new RemunerationsFraisTypeFonctionnementLigne();
		ligne.setTypeFonctionnement(typeFonctionnement);
		ligne.setLignesCompteInterne(List.of());
		ligne.setCumuls(new RemunerationsFraisTypeFonctionnementPeriode[] { periode });
		return ligne;
	}

	private static PlusMoinsValueTypeFonctionnementLigne plusMoinsValueLigne(
			TypeFonctionnement typeFonctionnement,
			Long soldeInitial,
			Long operations,
			Long plusMoinsValue,
			Long soldeFinal,
			Long frais) {

		PlusMoinsValueTypeFonctionnementPeriode periode = new PlusMoinsValueTypeFonctionnementPeriode();
		periode.setDateDebutPeriode(LocalDate.of(2026, 5, 1));
		periode.setDateFinPeriode(LocalDate.of(2026, 5, 31));
		periode.setMontantSoldeInitialEnCentimes(soldeInitial);
		periode.setMontantOperationsEnCentimes(operations);
		periode.setMontantPlusMoinsValueNetteEnCentimes(plusMoinsValue);
		periode.setTauxPlusMoinsValueNette(2.857D);
		periode.setMontantSoldeFinalEnCentimes(soldeFinal);
		periode.setMontantFraisEnCentimes(frais);
		periode.setTauxFrais(0.925D);
		PlusMoinsValueTypeFonctionnementLigne ligne = new PlusMoinsValueTypeFonctionnementLigne();
		ligne.setTypeFonctionnement(typeFonctionnement);
		ligne.setLignesCompteInterne(List.of());
		ligne.setCumulsPeriodes(new PlusMoinsValueTypeFonctionnementPeriode[] { periode });
		return ligne;
	}

	private static BilanPatrimoineTypeFonctionnementLigne bilanLigne(
			TypeFonctionnement typeFonctionnement,
			Long soldeInitial,
			Long soldeFinal,
			Long recette,
			Long depense,
			Long technique,
			Long ecart) {

		BilanPatrimoineTypeFonctionnementPeriode periode = new BilanPatrimoineTypeFonctionnementPeriode();
		periode.setDateDebutPeriode(LocalDate.of(2026, 5, 1));
		periode.setDateFinPeriode(LocalDate.of(2026, 5, 31));
		periode.setMontantSoldeFinalEnCentimes(soldeFinal);
		periode.setMontantTotalRecetteEnCentimes(recette);
		periode.setMontantTotalDepenseEnCentimes(depense);
		periode.setSoldeTotalTechniqueEnCentimes(technique);
		periode.setMontantEcartNonJustifieEnCentimes(ecart);
		BilanPatrimoineTypeFonctionnementLigne ligne = new BilanPatrimoineTypeFonctionnementLigne();
		ligne.setTypeFonctionnement(typeFonctionnement);
		ligne.setLignesCompteInterne(List.of());
		ligne.setMontantSoldeInitialEnCentimes(soldeInitial);
		ligne.setCumulsPeriodes(new BilanPatrimoineTypeFonctionnementPeriode[] { periode });
		return ligne;
	}
}
