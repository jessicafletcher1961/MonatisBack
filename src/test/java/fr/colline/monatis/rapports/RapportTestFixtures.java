package fr.colline.monatis.rapports;

import java.time.LocalDate;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;

public final class RapportTestFixtures {

	private RapportTestFixtures() {
	}

	public static Banque banque(Long id, String nom) {
		Banque banque = new Banque(nom, "Libelle " + nom);
		banque.setId(id);
		return banque;
	}

	public static Titulaire titulaire(Long id, String nom) {
		Titulaire titulaire = new Titulaire(nom, "Libelle " + nom);
		titulaire.setId(id);
		return titulaire;
	}

	public static Beneficiaire beneficiaire(Long id, String nom) {
		Beneficiaire beneficiaire = new Beneficiaire(nom, "Libelle " + nom);
		beneficiaire.setId(id);
		return beneficiaire;
	}

	public static Categorie categorie(Long id, String nom) {
		Categorie categorie = new Categorie(nom, "Libelle " + nom);
		categorie.setId(id);
		return categorie;
	}

	public static SousCategorie sousCategorie(Long id, String nom, Categorie categorie) {
		SousCategorie sousCategorie = new SousCategorie(nom, "Libelle " + nom, categorie);
		sousCategorie.setId(id);
		return sousCategorie;
	}

	public static CompteInterne compteInterne(Long id, String identifiant) {
		CompteInterne compte = new CompteInterne();
		compte.setId(id);
		compte.setIdentifiant(identifiant);
		compte.setLibelle("Libelle " + identifiant);
		compte.setTypeFonctionnement(TypeFonctionnement.COURANT);
		compte.setDateSoldeInitial(LocalDate.of(2026, 1, 10));
		compte.setMontantSoldeInitialEnCentimes(10_000L);
		return compte;
	}

	public static CompteExterne compteExterne(Long id, String identifiant) {
		CompteExterne compte = new CompteExterne();
		compte.setId(id);
		compte.setIdentifiant(identifiant);
		compte.setLibelle("Libelle " + identifiant);
		return compte;
	}

	public static CompteTechnique compteTechnique(Long id, String identifiant) {
		CompteTechnique compte = new CompteTechnique();
		compte.setId(id);
		compte.setIdentifiant(identifiant);
		compte.setLibelle("Libelle " + identifiant);
		return compte;
	}

	public static Operation operation(
			Long id,
			TypeOperation typeOperation,
			LocalDate dateValeur,
			Long montantEnCentimes,
			Compte compteRecette,
			Compte compteDepense) {

		Operation operation = new Operation();
		operation.setId(id);
		operation.setNumero("OP-" + id);
		operation.setLibelle("Operation " + id);
		operation.setTypeOperation(typeOperation);
		operation.setDateValeur(dateValeur);
		operation.setMontantEnCentimes(montantEnCentimes);
		operation.setCompteRecette(compteRecette);
		operation.setCompteDepense(compteDepense);
		operation.setPointee(false);
		return operation;
	}

	public static OperationLigne operationLigne(
			int numeroLigne,
			Operation operation,
			Long montantEnCentimes,
			SousCategorie sousCategorie,
			Beneficiaire... beneficiaires) {

		OperationLigne ligne = new OperationLigne();
		ligne.setNumeroLigne(numeroLigne);
		ligne.setLibelle("Ligne " + numeroLigne);
		ligne.setDateComptabilisation(operation.getDateValeur());
		ligne.setMontantEnCentimes(montantEnCentimes);
		ligne.setOperation(operation);
		ligne.setSousCategorie(sousCategorie);
		for (Beneficiaire beneficiaire : beneficiaires) {
			ligne.getBeneficiaires().add(beneficiaire);
		}
		return ligne;
	}

	public static Evaluation evaluation(CompteInterne compte, LocalDate dateSolde, Long montantSoldeEnCentimes) {
		Evaluation evaluation = new Evaluation();
		evaluation.setId(1L);
		evaluation.setCle("EVAL-0000000001");
		evaluation.setCompteInterne(compte);
		evaluation.setDateSolde(dateSolde);
		evaluation.setMontantSoldeEnCentimes(montantSoldeEnCentimes);
		evaluation.setLibelle("Evaluation");
		return evaluation;
	}
}
