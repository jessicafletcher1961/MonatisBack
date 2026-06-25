package fr.colline.monatis.rapports.controller;

import static fr.colline.monatis.rapports.RapportTestFixtures.banque;
import static fr.colline.monatis.rapports.RapportTestFixtures.beneficiaire;
import static fr.colline.monatis.rapports.RapportTestFixtures.categorie;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteExterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static fr.colline.monatis.rapports.RapportTestFixtures.operation;
import static fr.colline.monatis.rapports.RapportTestFixtures.operationLigne;
import static fr.colline.monatis.rapports.RapportTestFixtures.sousCategorie;
import static fr.colline.monatis.rapports.RapportTestFixtures.titulaire;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteInterneResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.SuiviBudgetPeriode;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

class RapportResponseMapperTest {

	@Test
	void mapperReleveCompteMappeEnteteOperationsEtMontantsEnEuros() throws ControllerException {
		Banque banque = banque(1L, "BANQUE");
		Titulaire titulaire = titulaire(2L, "ODILE");
		CompteInterne compte = compteInterne(1L, "COURANT");
		compte.setTypeFonctionnement(TypeFonctionnement.COURANT);
		compte.changerBanque(banque);
		compte.changerTitulaires(java.util.Set.of(titulaire));
		CompteExterne tiers = compteExterne(2L, "TIERS");
		ReleveCompte releve = new ReleveCompte();
		releve.setCompte(compte);
		releve.setDateDebutReleve(LocalDate.of(2026, 5, 1));
		releve.setDateFinReleve(LocalDate.of(2026, 5, 31));
		releve.setMontantSoldeDebutReleveEnCentimes(10_000L);
		releve.setMontantSoldeFinReleveEnCentimes(12_500L);
		releve.setMontantTotalOperationsRecetteEnCentimes(3_000L);
		releve.setMontantTotalOperationsDepenseEnCentimes(500L);
		releve.setMontantEcartEnCentimes(0L);
		releve.setOperationsRecette(List.of(operation(1L, TypeOperation.RECETTE, LocalDate.of(2026, 5, 4), 3_000L, compte, tiers)));
		releve.setOperationsDepense(List.of(operation(2L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 7), 500L, tiers, compte)));

		ReleveCompteResponseDto dto = RapportResponseDtoMapper.mapperReleveCompte(releve);

		assertInstanceOf(EnteteCompteInterneResponseDto.class, dto.enteteCompte);
		EnteteCompteInterneResponseDto entete = (EnteteCompteInterneResponseDto) dto.enteteCompte;
		assertEquals("COURANT", entete.identifiantCompte);
		assertEquals("COURANT", entete.codeTypeFonctionnement);
		assertEquals("Libelle BANQUE", entete.libelleBanque);
		assertEquals(List.of("Libelle ODILE"), entete.libellesTitulaires);
		assertEquals(100D, dto.montantSoldeDebutReleveEnEuros);
		assertEquals(125D, dto.montantSoldeFinReleveEnEuros);
		assertEquals(30D, dto.operationsRecette.get(0).montantEnEuros);
		assertEquals(-5D, dto.operationsDepense.get(0).montantEnEuros);
		assertEquals("TIERS", dto.operationsRecette.get(0).identifiantAutreCompte);
	}

	@Test
	void mapperEtatDepenseRecetteMappeCategoriesSousCategoriesEtSuiviBudget() {
		Categorie categorie = categorie(1L, "ALIM");
		SousCategorie sousCategorie = sousCategorie(2L, "COURSES", categorie);
		Beneficiaire beneficiaire = beneficiaire(3L, "MARCHE");
		SuiviBudgetPeriode suiviBudget = new SuiviBudgetPeriode();
		suiviBudget.setMontantBudgetEnCentimes(-3_000L);
		suiviBudget.setMontantExecutionEnCentimes(-3_500L);
		suiviBudget.setMontantVertEnCentimes(0L);
		suiviBudget.setMontantRougeEnCentimes(500L);
		suiviBudget.setTauxExecutionBudget(116.66D);
		DepenseRecettePeriode periode = new DepenseRecettePeriode();
		periode.setDateDebutPeriode(LocalDate.of(2026, 5, 1));
		periode.setDateFinPeriode(LocalDate.of(2026, 5, 31));
		periode.setMontantRecetteEnCentimes(1_000L);
		periode.setMontantDepenseEnCentimes(4_500L);
		periode.setSoldeDepenseRecetteEnCentimes(-3_500L);
		periode.setSuiviBudget(suiviBudget);
		DepenseRecetteSousCategorieLigne ligneSousCategorie = new DepenseRecetteSousCategorieLigne();
		ligneSousCategorie.setSousCategorie(sousCategorie);
		ligneSousCategorie.setCumulSousCategorie(new DepenseRecettePeriode[] { periode });
		DepenseRecetteCategorieLigne ligneCategorie = new DepenseRecetteCategorieLigne();
		ligneCategorie.setCategorie(categorie);
		ligneCategorie.setLignesSousCategorie(List.of(ligneSousCategorie));
		ligneCategorie.setCumulCategorie(new DepenseRecettePeriode[] { periode });
		EtatDepenseRecette etat = new EtatDepenseRecette();
		etat.setDateDebutEtat(LocalDate.of(2026, 5, 1));
		etat.setDateFinEtat(LocalDate.of(2026, 5, 31));
		etat.setTypePeriode(TypePeriode.MENSUEL);
		etat.setSousCategories(List.of(sousCategorie));
//		etat.setCategories(List.of(categorie));
		etat.setBeneficiaire(beneficiaire);
		etat.setLignesCategorie(List.of(ligneCategorie));
		etat.setCumulEtat(new DepenseRecettePeriode[] { periode });

		EtatDepenseRecetteResponseDto dto = RapportResponseDtoMapper.mapperEtatDepenseRecette(etat);

		assertEquals("MOIS", dto.typePeriode.code);
//		assertEquals("ALIM", dto.categories.get(0).nom);
		assertEquals("COURSES", dto.sousCategories.get(0).nom);
		assertEquals("MARCHE", dto.beneficiaire.nom);
		assertEquals(10D, dto.cumulEtat[0].montantRecetteEnEuros);
		assertEquals(45D, dto.cumulEtat[0].montantDepenseEnEuros);
		assertNotNull(dto.cumulEtat[0].suiviBudget);
		assertEquals(-30D, dto.cumulEtat[0].suiviBudget.montantBudgetEnEuros);
		assertEquals("SOLDE-", dto.cumulEtat[0].suiviBudget.typeBudget.code);
	}

	@Test
	void mapperReleveCompteRefuseUnTypeDeCompteNonPrisEnCharge() {
		ReleveCompte releve = new ReleveCompte();
		releve.setCompte(new CompteInconnu());
		releve.setOperationsRecette(List.of());
		releve.setOperationsDepense(List.of());

		assertThrows(ControllerException.class, () -> RapportResponseDtoMapper.mapperReleveCompte(releve));
	}

	@Test
	void mapperReleveCompteToPdfEcritUnPdfNonVide() throws ControllerException {
		CompteInterne compte = compteInterne(1L, "COURANT");
		ReleveCompte releve = new ReleveCompte();
		releve.setCompte(compte);
		ByteArrayOutputStream sortie = new ByteArrayOutputStream();

		RapportResponsePdfMapper.mapperReleveCompteToPdf(releve, new ByteArrayServletOutputStream(sortie));

		byte[] pdf = sortie.toByteArray();
		assertTrue(pdf.length > 100);
		assertEquals("%PDF", new String(pdf, 0, 4));
	}

	@Test
	void mapperReleveNonCategoriseMappeLesLignesOperation() throws ControllerException {
		Categorie categorie = categorie(1L, "ALIM");
		SousCategorie sousCategorie = sousCategorie(2L, "COURSES", categorie);
		Beneficiaire beneficiaire = beneficiaire(3L, "MARCHE");
		CompteInterne compte = compteInterne(1L, "COURANT");
		CompteExterne tiers = compteExterne(2L, "TIERS");
		Operation operation = operation(1L, TypeOperation.DEPENSE, LocalDate.of(2026, 5, 4), 1_200L, tiers, compte);
		var ligne = operationLigne(1, operation, 1_200L, sousCategorie, beneficiaire);
		var releve = new fr.colline.monatis.rapports.model.ReleveNonCategorise();
		releve.setDateDebutReleve(LocalDate.of(2026, 5, 1));
		releve.setDateFinReleve(LocalDate.of(2026, 5, 31));
		releve.setOperationsLignes(List.of(ligne));

		var dto = RapportResponseDtoMapper.mapperReleveNonCategorise(releve);

		assertEquals(1, dto.operationsLignes.size());
		assertEquals(12D, dto.operationsLignes.get(0).montantEnEuros);
		assertEquals("MARCHE", dto.operationsLignes.get(0).beneficiaires.get(0).nom);
		assertEquals("DEPENSE", dto.operationsLignes.get(0).typeOperation.code);
	}

	private static final class CompteInconnu extends Compte {
		@Override
		public fr.colline.monatis.typologies.model.TypeCompte getTypeCompte() {
			return fr.colline.monatis.typologies.model.TypeCompte.INTERNE;
		}
	}

	private static final class ByteArrayServletOutputStream extends ServletOutputStream {
		private final ByteArrayOutputStream delegate;

		private ByteArrayServletOutputStream(ByteArrayOutputStream delegate) {
			this.delegate = delegate;
		}

		@Override
		public void write(int b) throws IOException {
			delegate.write(b);
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
		}
	}
}
