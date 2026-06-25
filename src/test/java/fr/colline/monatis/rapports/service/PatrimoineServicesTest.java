package fr.colline.monatis.rapports.service;

import static fr.colline.monatis.rapports.RapportTestFixtures.compteExterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.operation;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementLigne;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;

@ExtendWith(MockitoExtension.class)
class PatrimoineServicesTest {

	@Mock
	private CompteInterneService compteInterneService;

	@Mock
	private OperationService operationService;

	@Mock
	private SoldeService soldeService;

	@InjectMocks
	private RemunerationsFraisService remunerationsFraisService;

	@InjectMocks
	private BilanPatrimoineService bilanPatrimoineService;

	@InjectMocks
	private PlusMoinsValueService plusMoinsValueService;

	@Test
	void remunerationsFraisCompteInternePeriodeIsoleLesFluxTechniques() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Operation remuneration = operation(1L, TypeOperation.REMUNERATION_COMPTE_COURANT, LocalDate.of(2026, 5, 4), 1_000L, compte, tiers);
		Operation frais = operation(2L, TypeOperation.FRAIS_COMPTE_COURANT, LocalDate.of(2026, 5, 8), 250L, tiers, compte);
		Operation nonTechnique = operation(3L, TypeOperation.RECETTE, LocalDate.of(2026, 5, 10), 9_999L, compte, tiers);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, debut, fin))
				.thenReturn(List.of(remuneration, frais, nonTechnique));

		RemunerationsFraisCompteInternePeriode resultat = remunerationsFraisService.rechercherRemunerationsFraisCompteInternePeriode(compte, debut, fin);

		assertEquals(1_000L, resultat.getMontantRemunerationsEnCentimes());
		assertEquals(250L, resultat.getMontantFraisEnCentimes());
		assertEquals(750L, resultat.getSoldeRemunerationsFraisEnCentimes());
	}

	@Test
	void remunerationsFraisTypeFonctionnementCreeDesCumulsVidesSansCompteSelectionne() throws ServiceException {
		when(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT)).thenReturn(List.of());

		RemunerationsFraisTypeFonctionnementLigne resultat = remunerationsFraisService.rechercherRemunerationsFraisTypeFonctionnementLigne(
				List.of(),
				TypeFonctionnement.COURANT,
				null,
				LocalDate.of(2026, 5, 15),
				LocalDate.of(2026, 6, 2),
				TypePeriode.MENSUEL);

		assertEquals(TypeFonctionnement.COURANT, resultat.getTypeFonctionnement());
		assertEquals(0, resultat.getLignesCompteInterne().size());
		assertEquals(2, resultat.getCumuls().length);
		assertEquals(LocalDate.of(2026, 5, 1), resultat.getCumuls()[0].getDateDebutPeriode());
		assertEquals(0L, resultat.getCumuls()[1].getSoldeRemunerationsFraisEnCentimes());
	}

	@Test
	void bilanPatrimoineCompteInternePeriodeCalculeSoldesOperationsEtEcart() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 31);
		Operation recette = operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 5, 4), 1_000L, compte, tiers);
		Operation depense = operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 8), 400L, tiers, compte);
		Operation techniqueRecette = operation(3L, TypeOperation.REMUNERATION_COMPTE_COURANT, LocalDate.of(2026, 5, 10), 100L, compte, tiers);
		Operation techniqueDepense = operation(4L, TypeOperation.FRAIS_COMPTE_COURANT, LocalDate.of(2026, 5, 11), 60L, tiers, compte);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, debut, fin))
				.thenReturn(List.of(recette, depense, techniqueRecette, techniqueDepense));
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(10_000L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(10_700L);

		BilanPatrimoineCompteInternePeriode resultat = bilanPatrimoineService.rechercherBilanPatrimoineCompteInternePeriode(compte, debut, fin);

		assertEquals(10_000L, resultat.getMontantSoldeInitialEnCentimes());
		assertEquals(10_700L, resultat.getMontantSoldeFinalEnCentimes());
		assertEquals(1_000L, resultat.getMontantTotalRecetteEnCentimes());
		assertEquals(400L, resultat.getMontantTotalDepenseEnCentimes());
		assertEquals(40L, resultat.getSoldeTotalTechniqueEnCentimes());
		assertEquals(60L, resultat.getMontantEcartNonJustifieEnCentimes());
	}

	@Test
	void bilanPatrimoineTypeFonctionnementCreeDesCumulsVidesSansCompteSelectionne() throws ServiceException {
		when(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER)).thenReturn(List.of());

		BilanPatrimoineTypeFonctionnementLigne resultat = bilanPatrimoineService.rechercherBilanPatrimoineTypeFonctionnementLigne(
				List.of(),
				TypeFonctionnement.FINANCIER,
				null,
				LocalDate.of(2026, 5, 1),
				LocalDate.of(2026, 5, 31),
				null);

		assertEquals(TypeFonctionnement.FINANCIER, resultat.getTypeFonctionnement());
		assertEquals(0L, resultat.getMontantSoldeInitialEnCentimes());
		assertEquals(1, resultat.getCumulsPeriodes().length);
		assertEquals(0L, resultat.getCumulsPeriodes()[0].getMontantEcartNonJustifieEnCentimes());
	}

	@Test
	void plusMoinsValueCompteInternePeriodePondereLesFluxEtCalculeLesTaux() throws ServiceException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		LocalDate debut = LocalDate.of(2026, 5, 1);
		LocalDate fin = LocalDate.of(2026, 5, 10);
		Operation recette = operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 5, 1), 1_000L, compte, tiers);
		Operation depense = operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 6), 500L, tiers, compte);
		Operation frais = operation(3L, TypeOperation.FRAIS_COMPTE_COURANT, LocalDate.of(2026, 5, 7), 100L, tiers, compte);
		when(soldeService.rechercherSolde(compte, LocalDate.of(2026, 4, 30))).thenReturn(10_000L);
		when(soldeService.rechercherSolde(compte, fin)).thenReturn(11_200L);
		when(operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, debut, fin))
				.thenReturn(List.of(recette, depense, frais));

		PlusMoinsValueCompteInternePeriode resultat = plusMoinsValueService.rechercherPlusMoinsValueCompteInternePeriode(compte, debut, fin);

		assertEquals(10_000L, resultat.getMontantSoldeInitialEnCentimes());
		assertEquals(750L, resultat.getMontantOperationsEnCentimes());
		assertEquals(450L, resultat.getMontantPlusMoinsValueNetteEnCentimes());
		assertEquals(4.186, resultat.getTauxPlusMoinsValueNette(), 0.001);
		assertEquals(11_200L, resultat.getMontantSoldeFinalEnCentimes());
		assertEquals(100L, resultat.getMontantFraisEnCentimes());
		assertEquals(0.892, resultat.getTauxFrais(), 0.001);
	}

	@Test
	void plusMoinsValueTypeFonctionnementCreeDesCumulsVidesSansCompteSelectionne() throws ServiceException {
		when(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.BIEN)).thenReturn(List.of());

		PlusMoinsValueTypeFonctionnementLigne resultat = plusMoinsValueService.rechercherPlusMoinsValueTypeFonctionnementLigne(
				List.of(),
				TypeFonctionnement.BIEN,
				null,
				null,
				LocalDate.of(2026, 5, 1),
				LocalDate.of(2026, 5, 31),
				null);

		assertEquals(TypeFonctionnement.BIEN, resultat.getTypeFonctionnement());
		assertEquals(0, resultat.getLignesCompteInterne().size());
		assertEquals(1, resultat.getCumulsPeriodes().length);
		assertEquals(0L, resultat.getCumulsPeriodes()[0].getMontantSoldeFinalEnCentimes());
	}
}
