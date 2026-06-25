package fr.colline.monatis.rapports.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.emprunts.model.ConditionEmprunt;
import fr.colline.monatis.emprunts.model.Echeance;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.GeneriqueTechniqueErreur;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineCompteInterneLigneResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineCompteInternePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoinePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigneResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.EtatBilanPatrimoineResponseDto;
import fr.colline.monatis.rapports.controller.commun.BanqueResponseDto;
import fr.colline.monatis.rapports.controller.commun.BeneficiaireResponseDto;
import fr.colline.monatis.rapports.controller.commun.CategorieResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteExterneResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteInterneResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteTechniqueResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteEmpruntResponseDto;
import fr.colline.monatis.rapports.controller.commun.SousCategorieResponseDto;
import fr.colline.monatis.rapports.controller.commun.TitulaireResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecetteCategorieLigneResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecettePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecetteSousCategorieLigneResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.SuiviBudgetResponseDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierConditionEmpruntResponseDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierCumulsResponseDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierLigneResponseDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.echeancier.EcheancierResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueCompteInterneLigneResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueCompteInternePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValuePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueTypeFonctionnementLigneResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueTypeFonctionnementPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteOperationResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.controller.releve_non_categorise.ReleveNonCategoriseOperationLigneResponseDto;
import fr.colline.monatis.rapports.controller.releve_non_categorise.ReleveNonCategoriseResponseDto;
import fr.colline.monatis.rapports.controller.releve_sous_categorie.ReleveSousCategorieOperationLigneResponseDto;
import fr.colline.monatis.rapports.controller.releve_sous_categorie.ReleveSousCategorieResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.EtatRemunerationsFraisResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisCompteInterneLigneResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisCompteInternePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisTypeFonctionnementLigneResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisTypeFonctionnementPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.resumes_comptes_internes.ResumeCompteInterneResponseDto;
import fr.colline.monatis.rapports.model.Echeancier;
import fr.colline.monatis.rapports.model.EtatBilanPatrimoine;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValue;
import fr.colline.monatis.rapports.model.EtatRemunerationsFrais;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.model.ReleveNonCategorise;
import fr.colline.monatis.rapports.model.ReleveSousCategorie;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoinePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.SuiviBudgetPeriode;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierConditionEmpruntLigne;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierCumuls;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierEcheanceLigne;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierLigne;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierPeriodeLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValuePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisPeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.controller.TypeOperationResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;
import fr.colline.monatis.typologies.model.TypeBudget;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;

public class RapportResponseDtoMapper {

	// --- Relevé de compte

	public static ReleveCompteResponseDto mapperReleveCompte(ReleveCompte releve) throws ControllerException {

		ReleveCompteResponseDto dto = new ReleveCompteResponseDto();

		dto.enteteCompte = mapperEnteteCompte(releve.getCompte());

		dto.dateDebutReleve = releve.getDateDebutReleve();
		dto.dateFinReleve = releve.getDateFinReleve();
		dto.montantSoldeDebutReleveEnEuros = (double) (releve.getMontantSoldeDebutReleveEnCentimes() / 100.00);
		dto.montantSoldeFinReleveEnEuros = (double) (releve.getMontantSoldeFinReleveEnCentimes() / 100.00);
		dto.montantTotalOperationsRecetteEnEuros = (double) (releve.getMontantTotalOperationsRecetteEnCentimes() / 100.00);
		dto.montantTotalOperationsDepenseEnEuros = (double) (releve.getMontantTotalOperationsDepenseEnCentimes() / 100.00);
		dto.montantEcartEnEuros = (double) (releve.getMontantEcartEnCentimes() / 100.00);

		dto.operationsRecette = new ArrayList<ReleveCompteOperationResponseDto>();
		for ( Operation operation : releve.getOperationsRecette() ) {
			dto.operationsRecette.add(mapperOperationRecette(operation));
		}
		dto.operationsDepense = new ArrayList<ReleveCompteOperationResponseDto>();
		for ( Operation operation : releve.getOperationsDepense() ) {
			dto.operationsDepense.add(mapperOperationDepense(operation));
		}

		return dto;
	}

	private static ReleveCompteOperationResponseDto mapperOperationRecette(Operation operation) {

		ReleveCompteOperationResponseDto dto = new ReleveCompteOperationResponseDto();

		dto.numero = operation.getNumero();
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnEuros = (double) (operation.getMontantEnCentimes() / 100.00);
		dto.libelle = operation.getLibelle();
		dto.identifiantAutreCompte = operation.getCompteDepense().getIdentifiant();
		dto.libelleAutreCompte = operation.getCompteDepense().getLibelle();
		dto.codeTypeAutreCompte = operation.getCompteDepense().getTypeCompte().getCode();

		return dto;
	}

	private static ReleveCompteOperationResponseDto mapperOperationDepense(Operation operation) {

		ReleveCompteOperationResponseDto dto = new ReleveCompteOperationResponseDto();

		dto.numero = operation.getNumero();
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnEuros = 0 - (double) (operation.getMontantEnCentimes() / 100.00);
		dto.libelle = operation.getLibelle();
		dto.identifiantAutreCompte = operation.getCompteRecette().getIdentifiant();
		dto.libelleAutreCompte = operation.getCompteRecette().getLibelle();
		dto.codeTypeAutreCompte = operation.getCompteRecette().getTypeCompte().getCode();

		return dto;
	}
	
	// --- Liste des lignes d'opération non catégorisées
	
	public static ReleveNonCategoriseResponseDto mapperReleveNonCategorise(ReleveNonCategorise releve) throws ControllerException {
		
		ReleveNonCategoriseResponseDto dto = new ReleveNonCategoriseResponseDto();
		
		dto.dateDebutReleve = releve.getDateDebutReleve();
		dto.dateFinReleve = releve.getDateFinReleve();

		dto.operationsLignes = new ArrayList<ReleveNonCategoriseOperationLigneResponseDto>();
		for ( OperationLigne operationLigne : releve.getOperationsLignes() ) {
			dto.operationsLignes.add(mapperOperationLigneNonCategorisee(operationLigne));
		}
		
		return dto;
	}

	private static ReleveNonCategoriseOperationLigneResponseDto mapperOperationLigneNonCategorisee(OperationLigne operationLigne) throws ControllerException {
		
		ReleveNonCategoriseOperationLigneResponseDto dto = new ReleveNonCategoriseOperationLigneResponseDto();
		
		dto.dateComptabilisation = operationLigne.getDateComptabilisation();
		dto.libelle = operationLigne.getLibelle();
		dto.montantEnEuros = (double) (operationLigne.getMontantEnCentimes() / 100.00);
		dto.numeroOperation = operationLigne.getOperation().getNumero();
		dto.numeroLigne = operationLigne.getNumeroLigne();
		dto.typeOperation = mapperTypeOperation(operationLigne.getOperation().getTypeOperation());
		dto.compteDepense = mapperEnteteCompte(operationLigne.getOperation().getCompteDepense());
		dto.compteRecette = mapperEnteteCompte(operationLigne.getOperation().getCompteRecette());

		dto.beneficiaires = new ArrayList<BeneficiaireResponseDto>();
		for ( Beneficiaire beneficiaire : operationLigne.getBeneficiaires() ) {
			dto.beneficiaires.add(mapperBeneficiaire(beneficiaire));
		}
		
		return dto;
	}
	
	// --- Liste des opérations (lignes) pour une sous-categorie donnée
	
	public static ReleveSousCategorieResponseDto mapperReleveSousCategorie(ReleveSousCategorie releve) throws ControllerException {

		ReleveSousCategorieResponseDto dto = new ReleveSousCategorieResponseDto();
		
		dto.dateDebutReleve = releve.getDateDebutReleve();
		dto.dateFinReleve = releve.getDateFinReleve();

		dto.operationsLignes = new ArrayList<ReleveSousCategorieOperationLigneResponseDto>();
		for ( OperationLigne operationLigne : releve.getOperationsLignes() ) {
			dto.operationsLignes.add(mapperOperationLigneSousCategorisee(operationLigne));
		}

		return dto;
	}

	private static ReleveSousCategorieOperationLigneResponseDto mapperOperationLigneSousCategorisee(
			OperationLigne operationLigne) throws ControllerException {
		
		ReleveSousCategorieOperationLigneResponseDto dto = new ReleveSousCategorieOperationLigneResponseDto();
		
		dto.dateComptabilisation = operationLigne.getDateComptabilisation();
		dto.libelle = operationLigne.getLibelle();
		dto.montantEnEuros = (double) (operationLigne.getMontantEnCentimes() / 100.00);
		dto.numeroOperation = operationLigne.getOperation().getNumero();
		dto.numeroLigne = operationLigne.getNumeroLigne();
		dto.typeOperation = mapperTypeOperation(operationLigne.getOperation().getTypeOperation());
		dto.compteDepense = mapperEnteteCompte(operationLigne.getOperation().getCompteDepense());
		dto.compteRecette = mapperEnteteCompte(operationLigne.getOperation().getCompteRecette());

		dto.beneficiaires = new ArrayList<BeneficiaireResponseDto>();
		for ( Beneficiaire beneficiaire : operationLigne.getBeneficiaires() ) {
			dto.beneficiaires.add(mapperBeneficiaire(beneficiaire));
		}

		return dto;
	}

	// --- Liste des comptes avec solde

	public static ResumeCompteInterneResponseDto mapperResumeCompteInterne(ResumeCompteInterne resumeCompteInterne) throws ControllerException {
		
		ResumeCompteInterneResponseDto dto = new ResumeCompteInterneResponseDto();
		
		dto.compteInterne = mapperEnteteCompte(resumeCompteInterne.getCompteInterne());
		dto.dateSolde = resumeCompteInterne.getDateSolde();
		dto.montantSoldeEnEuros = (double) (resumeCompteInterne.getMontantSoldeEnCentimes() / 100.00);
		
		return dto;
	}

	// Echeancier
	
	public static EcheancierResponseDto mapperEcheancier(Echeancier etat) {
		
		EcheancierResponseDto dto = new EcheancierResponseDto();
		
		dto.emprunt = mapperEnteteEmprunt(etat.getEmprunt());
		dto.cumulFinal = mapperEcheancierCumuls(etat.getCumulFinal());
		dto.lignes = new ArrayList<EcheancierLigneResponseDto>();
		for ( EcheancierLigne ligne : etat.getLignes() ) {
			if ( EcheancierConditionEmpruntLigne.class.isAssignableFrom(ligne.getClass()) ) dto.lignes.add(mapperEcheancierLigne((EcheancierConditionEmpruntLigne) ligne));
			else if ( EcheancierPeriodeLigne.class.isAssignableFrom(ligne.getClass()) ) dto.lignes.add(mapperEcheancierLigne((EcheancierPeriodeLigne) ligne));
			else if ( EcheancierEcheanceLigne.class.isAssignableFrom(ligne.getClass()) ) dto.lignes.add(mapperEcheancierLigne((EcheancierEcheanceLigne) ligne));
		}
		dto.cumulDateCible = mapperEcheancierCumuls(etat.getCumulDateCible());
		
		return dto;
	}

	private static EcheancierLigneResponseDto mapperEcheancierLigne(EcheancierConditionEmpruntLigne ligne) {
		
		EcheancierConditionEmpruntResponseDto dto = new EcheancierConditionEmpruntResponseDto();
		
		ConditionEmprunt conditionEmprunt = ligne.getConditionEmprunt();
		
		dto.libelle = ligne.getConditionEmprunt().getLibelle();
		dto.numeroEcheanceApplicabilite = conditionEmprunt.getNumeroPremiereEcheance();
		dto.montantPretEnEuros = (double) (conditionEmprunt.getCapitalEmprunteEnCentimes() / 100.00);
		dto.dureePret = conditionEmprunt.getDuree();
		dto.periodiciteEcheances = mapperTypePeriode(conditionEmprunt.getTypePeriodeEcheances());
		dto.tauxAnnuel = conditionEmprunt.getTauxAnnuel();

		EcheancierCumuls cumuls = ligne.getCumuls();

		dto.dateDebutApplicabilite = cumuls.getDatePremiereEcheance();
		dto.dateFinApplicabilite = cumuls.getDateDerniereEcheance();
		dto.nombreEcheancesApplicabilite = cumuls.getNombreEcheances();
		dto.cumuls = mapperEcheancierCumuls(cumuls);
		
		return dto;
	}

	private static EcheancierLigneResponseDto mapperEcheancierLigne(EcheancierPeriodeLigne ligne) {

		EcheancierPeriodeResponseDto dto = new EcheancierPeriodeResponseDto();
		
		dto.dateDebutPeriode = ligne.getDateDebutPeriode();
		dto.dateFinPeriode = ligne.getDateFinPeriode();

		EcheancierCumuls cumuls = ligne.getCumuls();

		dto.datePremiereEcheancePeriode = cumuls.getDatePremiereEcheance();
		dto.dateDerniereEcheancePeriode = cumuls.getDateDerniereEcheance();
		dto.nombreEcheancesPeriode = cumuls.getNombreEcheances();
		dto.cumuls = mapperEcheancierCumuls(cumuls);

		return dto;
	}

	private static EcheancierLigneResponseDto mapperEcheancierLigne(EcheancierEcheanceLigne ligne) {
	
		EcheancierEcheanceResponseDto dto = new EcheancierEcheanceResponseDto();
		
		Echeance echeance = ligne.getEcheance();
		
		dto.rang = echeance.getNumeroEcheance();
		dto.dateEcheance = echeance.getDateEcheance();
		dto.montantARecouvrerEnEuros = (double) (echeance.getMontantPaiementEnCentimes() / 100.00);
		dto.capitalAmortiEnEuros = (double) (echeance.getPartCapitalEnCentimes() / 100.00);
		dto.partInteretsEnEuros = (double) (echeance.getPartInteretsEnCentimes() / 100.00);
		dto.capitalRestantDuEnEuros = (double) (echeance.getCapitalEmprunteRestantDuEnCentimes() / 100.00);
		dto.partFraisFixesEnEuros = (double) (echeance.getPartFraisFixesEnCentimes() / 100.00);
		
		return dto;
	}

	private static EcheancierCumulsResponseDto mapperEcheancierCumuls(EcheancierCumuls cumuls) {
		
		EcheancierCumulsResponseDto dto = new EcheancierCumulsResponseDto();

		dto.cumulMontantPaiementsEnEuros = (double) (cumuls.getCumulMontantPaiement() / 100.00);
		dto.cumulPartCapitalEnEuros = (double) (cumuls.getCumulPartCapital() / 100.00);
		dto.cumulPartInteretsEnEuros = (double) (cumuls.getCumulPartInterets() / 100.00);
		dto.cumulPartFraisFixesEnEuros = (double) (cumuls.getCumulPartFraisFixes() / 100.00);
		dto.capitalEmpruntDejaRembourseEnEuros = (double) (cumuls.getCapitalEmprunteDejaRembourse() / 100.00);
		dto.capitalEmpruntRestantDuEnEuros = (double) (cumuls.getCapitalEmprunteRestantDu() / 100.00);

		return dto;
	}
	
	// --- Dépenses et recettes

	public static EtatDepenseRecetteResponseDto mapperEtatDepenseRecette(EtatDepenseRecette etat) {

		EtatDepenseRecetteResponseDto dto = new EtatDepenseRecetteResponseDto();

		int nombrePeriodes = etat.getCumulEtat().length;

		dto.dateDebutEtat = etat.getDateDebutEtat();
		dto.dateFinEtat = etat.getDateFinEtat();
		dto.typePeriode = mapperTypePeriode(etat.getTypePeriode());
		dto.sousCategories = new ArrayList<SousCategorieResponseDto>();
		for ( SousCategorie sousCategorie : etat.getSousCategories() ) {
			dto.sousCategories.add(mapperSousCategorie(sousCategorie));
		}
		dto.beneficiaire = mapperBeneficiaire(etat.getBeneficiaire());	

		dto.lignesCategorie = new ArrayList<DepenseRecetteCategorieLigneResponseDto>();
		for ( DepenseRecetteCategorieLigne ligneCategorie : etat.getLignesCategorie() ) {
			dto.lignesCategorie.add(mapperDepenseRecetteCategorieLigne(ligneCategorie));
		}
		dto.cumulEtat = new DepenseRecettePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumulEtat[numeroPeriode] = mapperDepenseRecettePeriode(etat.getCumulEtat()[numeroPeriode]);
		}
		
		return dto;
	}

	private static DepenseRecetteCategorieLigneResponseDto mapperDepenseRecetteCategorieLigne(
			DepenseRecetteCategorieLigne ligneCategorie) {

		DepenseRecetteCategorieLigneResponseDto dto = new DepenseRecetteCategorieLigneResponseDto();

		int nombrePeriodes = ligneCategorie.getCumulCategorie().length;

		dto.categorie = mapperCategorie(ligneCategorie.getCategorie());
		dto.lignesSousCategorie = new ArrayList<DepenseRecetteSousCategorieLigneResponseDto>();
		for ( DepenseRecetteSousCategorieLigne ligneSousCategorie : ligneCategorie.getLignesSousCategorie() ) {
			dto.lignesSousCategorie.add(mapperDepenseRecetteSousCategorieLigne(ligneSousCategorie));
		}
		dto.cumulCategorie = new DepenseRecettePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumulCategorie[numeroPeriode] = mapperDepenseRecettePeriode(ligneCategorie.getCumulCategorie()[numeroPeriode]);
		}
		
		return dto;
	}

	private static DepenseRecetteSousCategorieLigneResponseDto mapperDepenseRecetteSousCategorieLigne(
			DepenseRecetteSousCategorieLigne ligneSousCategorie) {

		DepenseRecetteSousCategorieLigneResponseDto dto = new DepenseRecetteSousCategorieLigneResponseDto();
		
		int nombrePeriodes = ligneSousCategorie.getCumulSousCategorie().length;

		dto.sousCategorie = mapperSousCategorie(ligneSousCategorie.getSousCategorie());
		dto.cumulSousCategorie = new DepenseRecettePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumulSousCategorie[numeroPeriode] = mapperDepenseRecettePeriode(ligneSousCategorie.getCumulSousCategorie()[numeroPeriode]);
		}

		return dto;
	}
	
	private static DepenseRecettePeriodeResponseDto mapperDepenseRecettePeriode(
			DepenseRecettePeriode cumulPeriode) {
		
		DepenseRecettePeriodeResponseDto dto = new DepenseRecettePeriodeResponseDto();
		
		dto.dateDebutPeriode = cumulPeriode.getDateDebutPeriode();
		dto.dateFinPeriode = cumulPeriode.getDateFinPeriode();
		
		dto.montantRecetteEnEuros = (double) (cumulPeriode.getMontantRecetteEnCentimes() / 100.00);
		dto.montantDepenseEnEuros = (double) (cumulPeriode.getMontantDepenseEnCentimes() / 100.00);
		dto.soldeDepenseRecetteEnEuros = (double) (cumulPeriode.getSoldeDepenseRecetteEnCentimes() / 100.00);
		
		if ( cumulPeriode.getSuiviBudget() != null ) {
			dto.suiviBudget = mapperSuiviBudget(cumulPeriode.getSuiviBudget());
		}
		
		return dto;
	}
	
	private static SuiviBudgetResponseDto mapperSuiviBudget(SuiviBudgetPeriode suiviBudget) {

		SuiviBudgetResponseDto dto = null;
		
		if ( suiviBudget != null ) {

			dto = new SuiviBudgetResponseDto();

			dto.montantBudgetEnEuros = (double) (suiviBudget.getMontantBudgetEnCentimes() / 100.00);
			dto.montantExecutionEnEuros = (double) (suiviBudget.getMontantExecutionEnCentimes() / 100.00);
			dto.montantVertEnEuros = (double) (suiviBudget.getMontantVertEnCentimes() / 100.00);
			dto.montantRougeEnEuros = (double) (suiviBudget.getMontantRougeEnCentimes() / 100.00);
			dto.tauxExecutionBudget = suiviBudget.getTauxExecutionBudget();
			dto.typeBudget = mapperTypeBudget(dto.montantBudgetEnEuros > 0 ? TypeBudget.SOLDE_PREVU_POSITIF : TypeBudget.SOLDE_PREVU_NEGATIF);
		}

		return dto;
	}
	
	// --- Plus et moins values

	public static EtatPlusMoinsValueResponseDto mapperEtatPlusMoinsValue(EtatPlusMoinsValue etat) throws ControllerException {

		EtatPlusMoinsValueResponseDto dto = new EtatPlusMoinsValueResponseDto();
		
		int nombrePeriodes = etat.getCumuls().length;

		dto.dateDebutEtat = etat.getDateDebutEtat();
		dto.dateFinEtat = etat.getDateFinEtat();
		dto.typePeriode = mapperTypePeriode(etat.getTypePeriode());
		dto.comptesInternes = new ArrayList<EnteteCompteResponseDto>();
		for ( CompteInterne compteInterne : etat.getComptesInternes() ) {
			dto.comptesInternes.add(mapperEnteteCompte(compteInterne));
		}
		dto.typesFonctionnements = new ArrayList<>();
		for ( TypeFonctionnement typeFonctionnement : etat.getTypesFonctionnements() ) {
			dto.typesFonctionnements.add(mapperTypeFonctionnement(typeFonctionnement));
		}
		dto.banque = mapperBanque(etat.getBanque());
		dto.titulaire = mapperTitulaire(etat.getTitulaire());

		dto.lignesTypeFonctionnement = new ArrayList<PlusMoinsValueTypeFonctionnementLigneResponseDto>();
		for ( PlusMoinsValueTypeFonctionnementLigne ligneTypeFonctionnement : etat.getLignesTypeFonctionnement() ) {
			dto.lignesTypeFonctionnement.add(mapperPlusMoinsValueTypeFonctionnementLigne(ligneTypeFonctionnement));
		}
		dto.cumuls = new PlusMoinsValuePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumuls[numeroPeriode] = mapperPlusMoinsValuePeriode(etat.getCumuls()[numeroPeriode]);
		}
		
		return dto;	
	}

	private static PlusMoinsValuePeriodeResponseDto mapperPlusMoinsValuePeriode(PlusMoinsValuePeriode etat) {
		
		PlusMoinsValuePeriodeResponseDto dto = new PlusMoinsValuePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		
		dto.montantPlusMoinsValueNetteEnEuros = (double) (etat.getMontantPlusMoinsValueNetteEnCentimes() / 100.00);
		dto.tauxPlusMoinsValueNette = etat.getTauxPlusMoinsValueNette();
		dto.montantFraisEnEuros = (double) (etat.getMontantFraisEnCentimes() / 100.00);
		dto.tauxFrais = etat.getTauxFrais();

		return dto;
	}

	private static PlusMoinsValueTypeFonctionnementLigneResponseDto mapperPlusMoinsValueTypeFonctionnementLigne(PlusMoinsValueTypeFonctionnementLigne etat) throws ControllerException {
		
		PlusMoinsValueTypeFonctionnementLigneResponseDto dto = new PlusMoinsValueTypeFonctionnementLigneResponseDto();

		int nombrePeriodes = etat.getCumulsPeriodes().length;

		dto.typeFonctionnement = mapperTypeFonctionnement(etat.getTypeFonctionnement());
		
		dto.lignesCompteInterne = new ArrayList<PlusMoinsValueCompteInterneLigneResponseDto>();
		for ( PlusMoinsValueCompteInterneLigne ligneCompteInterne : etat.getLignesCompteInterne() ) {
			dto.lignesCompteInterne.add(mapperPlusMoinsValueCompteInterneLigne(ligneCompteInterne));
		}
		dto.cumulsPeriodes = new PlusMoinsValueTypeFonctionnementPeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumulsPeriodes[numeroPeriode] = mapperPlusMoinsValue(etat.getCumulsPeriodes()[numeroPeriode]);
		}
		
		return dto;
	}

	private static PlusMoinsValueTypeFonctionnementPeriodeResponseDto mapperPlusMoinsValue(PlusMoinsValueTypeFonctionnementPeriode etat) {
			
		PlusMoinsValueTypeFonctionnementPeriodeResponseDto dto = new PlusMoinsValueTypeFonctionnementPeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		
		dto.montantPlusMoinsValueNetteEnEuros = (double) (etat.getMontantPlusMoinsValueNetteEnCentimes() / 100.00);
		dto.tauxPlusMoinsValueNette = etat.getTauxPlusMoinsValueNette();
		dto.montantFraisEnEuros = (double) (etat.getMontantFraisEnCentimes() / 100.00);
		dto.tauxFrais = etat.getTauxFrais();
		
		return dto;
	}

	private static PlusMoinsValueCompteInterneLigneResponseDto mapperPlusMoinsValueCompteInterneLigne(PlusMoinsValueCompteInterneLigne etat) throws ControllerException {

		PlusMoinsValueCompteInterneLigneResponseDto dto = new PlusMoinsValueCompteInterneLigneResponseDto();

		int nombrePeriodes = etat.getPeriodes().length;

		dto.compteInterne = mapperEnteteCompte(etat.getCompteInterne());
		dto.periodes = new PlusMoinsValueCompteInternePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.periodes[numeroPeriode] = mapperPlusMoinsValueCompteInternePeriode(etat.getPeriodes()[numeroPeriode]);
		}
		
		return dto;
	}

	private static PlusMoinsValueCompteInternePeriodeResponseDto mapperPlusMoinsValueCompteInternePeriode(PlusMoinsValueCompteInternePeriode etat) {

		PlusMoinsValueCompteInternePeriodeResponseDto dto = new PlusMoinsValueCompteInternePeriodeResponseDto();

		dto.dateDebutperiode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		
		dto.montantSoldeInitialEnEuros = (double) (etat.getMontantSoldeInitialEnCentimes() / 100.00);
		dto.montantOperationsEnEuros = (double) (etat.getMontantOperationsEnCentimes() / 100.00);
		dto.montantPlusMoinsValueNetteEnEuros = (double) (etat.getMontantPlusMoinsValueNetteEnCentimes() / 100.00);
		dto.tauxPlusMoinsValueNette = etat.getTauxPlusMoinsValueNette();
		dto.montantSoldeFinalEnEuros = (double) (etat.getMontantSoldeFinalEnCentimes() / 100.00);
		dto.montantFraisEnEuros = (double) (etat.getMontantFraisEnCentimes() / 100.00);
		dto.tauxFrais = etat.getTauxFrais();
		
		return dto;
	}

	// --- Rémunérations et frais des comptes

	public static EtatRemunerationsFraisResponseDto mapperEtatRemunerationsFrais(EtatRemunerationsFrais etat) throws ControllerException {

		EtatRemunerationsFraisResponseDto dto = new EtatRemunerationsFraisResponseDto();
		
		int nombrePeriodes = etat.getCumuls().length;

		// Paramètres de l'état
		dto.dateDebutEtat = etat.getDateDebutEtat();
		dto.dateFinEtat = etat.getDateFinEtat();
		dto.typePeriode = mapperTypePeriode(etat.getTypePeriode());
		dto.comptesInternes = new ArrayList<EnteteCompteResponseDto>();
		for ( CompteInterne compteInterne : etat.getComptesInternes() ) {
			dto.comptesInternes.add(mapperEnteteCompte(compteInterne));
		}
		dto.typesFonctionnements = new ArrayList<TypologieResponseDto>();
		for ( TypeFonctionnement typeFonctionnement : etat.getTypesFonctionnements() ) {
			dto.typesFonctionnements.add(mapperTypeFonctionnement(typeFonctionnement));
		}
		dto.titulaire = mapperTitulaire(etat.getTitulaire());

		// L'état produit
		dto.lignesTypeFonctionnement = new ArrayList<RemunerationsFraisTypeFonctionnementLigneResponseDto>();
		for ( RemunerationsFraisTypeFonctionnementLigne ligneTypeFonctionnement : etat.getLignesTypeFonctionnement() ) {
			dto.lignesTypeFonctionnement.add(mapperRemunerationsFraisTypeFonctionnementLigne(ligneTypeFonctionnement));
		}
		dto.cumuls = new RemunerationsFraisPeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumuls[numeroPeriode] = mapperRemunerationsFraisPeriode(etat.getCumuls()[numeroPeriode]);
		}
		
		return dto;
	}

	private static RemunerationsFraisPeriodeResponseDto mapperRemunerationsFraisPeriode(RemunerationsFraisPeriode etat) {
			
			RemunerationsFraisPeriodeResponseDto dto = new RemunerationsFraisPeriodeResponseDto();
			
			dto.dateDebutPeriode = etat.getDateDebutPeriode();
			dto.dateFinPeriode = etat.getDateFinPeriode();
			dto.montantRemunerationsEnEuros = (double) (etat.getMontantRemunerationsEnCentimes() / 100.00);
			dto.montantFraisEnEuros = (double) (etat.getMontantFraisEnCentimes() / 100.00);
			dto.soldeRemunerationsFraisEnEuros = (double) (etat.getSoldeRemunerationsFraisEnCentimes() / 100.00);
			
			return dto;
	}

	private static RemunerationsFraisTypeFonctionnementLigneResponseDto mapperRemunerationsFraisTypeFonctionnementLigne(
			RemunerationsFraisTypeFonctionnementLigne etat) throws ControllerException {

		RemunerationsFraisTypeFonctionnementLigneResponseDto dto = new RemunerationsFraisTypeFonctionnementLigneResponseDto();

		int nombrePeriodes = etat.getCumuls().length;

		dto.typeFonctionnement = mapperTypeFonctionnement(etat.getTypeFonctionnement());

		dto.lignesCompteInterne = new ArrayList<RemunerationsFraisCompteInterneLigneResponseDto>();
		for ( RemunerationsFraisCompteInterneLigne detail : etat.getLignesCompteInterne() ) {
			dto.lignesCompteInterne.add(mapperRemunerationsFraisCompteInterneLigne(detail));
		}
		dto.cumulsPeriodes = new RemunerationsFraisTypeFonctionnementPeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumulsPeriodes[numeroPeriode] = mapperRemunerationsFraisTypeFonctionnementPeriode(etat.getCumuls()[numeroPeriode]);
		}
		
		return dto;
	}

	private static RemunerationsFraisTypeFonctionnementPeriodeResponseDto mapperRemunerationsFraisTypeFonctionnementPeriode(
			RemunerationsFraisTypeFonctionnementPeriode etat) {

		RemunerationsFraisTypeFonctionnementPeriodeResponseDto dto = new RemunerationsFraisTypeFonctionnementPeriodeResponseDto();

		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantRemunerationsEnEuros = (double) (etat.getMontantRemunerationsEnCentimes() / 100.00);
		dto.montantFraisEnEuros = (double) (etat.getMontantFraisEnCentimes() / 100.00);
		dto.soldeRemunerationsFraisEnEuros = (double) (etat.getSoldeRemunerationsFraisEnCentimes() / 100.00);
		
		return dto;
	}

	private static RemunerationsFraisCompteInterneLigneResponseDto mapperRemunerationsFraisCompteInterneLigne(
			RemunerationsFraisCompteInterneLigne etat) throws ControllerException {

		RemunerationsFraisCompteInterneLigneResponseDto dto = new RemunerationsFraisCompteInterneLigneResponseDto();

		int nombrePeriodes = etat.getPeriodes().length;

		dto.compteInterne = mapperEnteteCompte(etat.getCompteInterne());
		dto.periodes = new RemunerationsFraisCompteInternePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.periodes[numeroPeriode] = mapperRemunerationsFraisCompteInternePeriode(etat.getPeriodes()[numeroPeriode]);
		}
		
		return dto;
	}

	private static RemunerationsFraisCompteInternePeriodeResponseDto mapperRemunerationsFraisCompteInternePeriode(RemunerationsFraisCompteInternePeriode etat) throws ControllerException {
		
		RemunerationsFraisCompteInternePeriodeResponseDto dto = new RemunerationsFraisCompteInternePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantRemunerationsEnEuros = (double) (etat.getMontantRemunerationsEnCentimes() / 100.00);
		dto.montantFraisEnEuros = (double) (etat.getMontantFraisEnCentimes() / 100.00);
		dto.soldeRemunerationsFraisEnEuros = (double) (etat.getSoldeRemunerationsFraisEnCentimes() / 100.00);
		
		return dto;
	}

	// --- Bilan de patrimoine

	public static EtatBilanPatrimoineResponseDto mapperEtatBilanPatrimoine(EtatBilanPatrimoine etat) throws ControllerException {
		
		EtatBilanPatrimoineResponseDto dto = new EtatBilanPatrimoineResponseDto();

		int nombrePeriodes = etat.getCumuls().length;

		dto.dateDebutEtat = etat.getDateDebutEtat();
		dto.dateFinEtat = etat.getDateFinEtat();
		dto.typePeriode = mapperTypePeriode(etat.getTypePeriode());
		dto.comptesInternes = new ArrayList<EnteteCompteResponseDto>();
		for ( CompteInterne compteInterne : etat.getComptesInternes() ) {
			dto.comptesInternes.add(mapperEnteteCompte(compteInterne));
		}
		dto.typesFonctionnements = new ArrayList<>();
		for ( TypeFonctionnement typeFonctionnement : etat.getTypesFonctionnements() ) {
			dto.typesFonctionnements.add(mapperTypeFonctionnement(typeFonctionnement));
		}
		dto.titulaire = mapperTitulaire(etat.getTitulaire());

		dto.lignesTypeFonctionnement = new ArrayList<BilanPatrimoineTypeFonctionnementLigneResponseDto>();
		for ( BilanPatrimoineTypeFonctionnementLigne ligneTypeFonctionnement : etat.getLignesTypeFonctionnement() ) {
			dto.lignesTypeFonctionnement.add(mapperBilanPatrimoineTypeFonctionnementLigne(ligneTypeFonctionnement));
		}
		dto.montantSoldeInitialEnEuros = (double) (etat.getMontantSoldeInitialEnCentimes() / 100.00);
		dto.cumuls = new BilanPatrimoinePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumuls[numeroPeriode] = mapperBilanPatrimoinePeriode(etat.getCumuls()[numeroPeriode]);
		}
		
		return dto;
	}

	private static BilanPatrimoinePeriodeResponseDto mapperBilanPatrimoinePeriode(
			BilanPatrimoinePeriode etat) {
		
		BilanPatrimoinePeriodeResponseDto dto = new BilanPatrimoinePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantSoldeFinalEnEuros = (double) (etat.getMontantSoldeFinalEnCentimes() / 100.00);
		dto.montantTotalRecetteEnEuros = (double) (etat.getMontantTotalRecetteEnCentimes() / 100.00);
		dto.montantTotalDepenseEnEuros = (double) (etat.getMontantTotalDepenseEnCentimes() / 100.00);
		dto.soldeTotalTechniqueEnEuros = (double) (etat.getSoldeTotalTechniqueEnCentimes() / 100.00);
		dto.montantEcartNonJustifieEnEuros = (double) (etat.getMontantEcartNonJustifieEnCentimes() / 100.00);
		
		return dto;
	}

	private static BilanPatrimoineTypeFonctionnementLigneResponseDto mapperBilanPatrimoineTypeFonctionnementLigne(
			BilanPatrimoineTypeFonctionnementLigne etat) throws ControllerException {

		BilanPatrimoineTypeFonctionnementLigneResponseDto dto = new BilanPatrimoineTypeFonctionnementLigneResponseDto();

		int nombrePeriodes = etat.getCumulsPeriodes().length;

		dto.typeFonctionnement = mapperTypeFonctionnement(etat.getTypeFonctionnement());
		
		dto.lignesCompteInterne = new ArrayList<BilanPatrimoineCompteInterneLigneResponseDto>();
		for ( BilanPatrimoineCompteInterneLigne detail : etat.getLignesCompteInterne() ) {
			dto.lignesCompteInterne.add(mapperBilanPatrimoineCompteInterneLigne(detail));
		}
		dto.montantSoldeInitialEnEuros = (double) (etat.getMontantSoldeInitialEnCentimes() / 100.00);
		dto.cumulsPeriodes = new BilanPatrimoineTypeFonctionnementPeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumulsPeriodes[numeroPeriode] = mapperBilanPatrimoineTypeFonctionnementPeriode(etat.getCumulsPeriodes()[numeroPeriode]);
		}
		
		return dto;
	}

	private static BilanPatrimoineTypeFonctionnementPeriodeResponseDto mapperBilanPatrimoineTypeFonctionnementPeriode(
			BilanPatrimoineTypeFonctionnementPeriode etat) {

		BilanPatrimoineTypeFonctionnementPeriodeResponseDto dto = new BilanPatrimoineTypeFonctionnementPeriodeResponseDto();

		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantSoldeFinalEnEuros = (double) (etat.getMontantSoldeFinalEnCentimes() / 100.00);
		dto.montantTotalRecetteEnEuros = (double) (etat.getMontantTotalRecetteEnCentimes() / 100.00);
		dto.montantTotalDepenseEnEuros = (double) (etat.getMontantTotalDepenseEnCentimes() / 100.00);
		dto.soldeTotalTechniqueEnEuros = (double) (etat.getSoldeTotalTechniqueEnCentimes() / 100.00);
		dto.montantEcartNonJustifieEnEuros = (double) (etat.getMontantEcartNonJustifieEnCentimes() / 100.00);
		
		return dto;
	}

	private static BilanPatrimoineCompteInterneLigneResponseDto mapperBilanPatrimoineCompteInterneLigne(
			BilanPatrimoineCompteInterneLigne etat) throws ControllerException {
		
		BilanPatrimoineCompteInterneLigneResponseDto dto = new BilanPatrimoineCompteInterneLigneResponseDto();

		int nombrePeriodes = etat.getPeriodes().length;

		dto.compteInterne = mapperEnteteCompte(etat.getCompteInterne());
		dto.montantSoldeInitialEnEuros = (double) (etat.getMontantSoldeInitialEnCentimes() / 100.00);
		dto.periodes = new BilanPatrimoineCompteInternePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.periodes[numeroPeriode] = mapperBilanPatrimoineCompteInternePeriode(etat.getPeriodes()[numeroPeriode]);
		}
		
		return dto;
	}

	private static BilanPatrimoineCompteInternePeriodeResponseDto mapperBilanPatrimoineCompteInternePeriode(
			BilanPatrimoineCompteInternePeriode etat) throws ControllerException {
		
		BilanPatrimoineCompteInternePeriodeResponseDto dto = new BilanPatrimoineCompteInternePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();

		dto.montantSoldeInitialEnEuros = (double) (etat.getMontantSoldeInitialEnCentimes() / 100.00);
		dto.montantSoldeFinalEnEuros = (double) (etat.getMontantSoldeFinalEnCentimes() / 100.00);
		dto.montantTotalRecetteEnEuros = (double) (etat.getMontantTotalRecetteEnCentimes() / 100.00);
		dto.montantTotalDepenseEnEuros = (double) (etat.getMontantTotalDepenseEnCentimes() / 100.00);
		dto.soldeTotalTechniqueEnEuros = (double) (etat.getSoldeTotalTechniqueEnCentimes() / 100.00);
		dto.montantEcartNonJustifieEnEuros = (double) (etat.getMontantEcartNonJustifieEnCentimes() / 100.00);

		return dto;
	}

	// --- Divers réutilisables

	private static EnteteCompteResponseDto mapperEnteteCompte(Compte compte) throws ControllerException {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {

			EnteteCompteInterneResponseDto dto = new EnteteCompteInterneResponseDto();

			dto.identifiantCompte = compte.getIdentifiant();
			dto.libelleCompte = compte.getLibelle();
			dto.codeTypeCompte = compte.getTypeCompte().getCode();
			dto.libelleTypeCompte = compte.getTypeCompte().getLibelle();

			CompteInterne compteInterne = (CompteInterne) compte;

			dto.codeTypeFonctionnement = compteInterne.getTypeFonctionnement().getCode();
			dto.libelleBanque = compteInterne.getBanque() == null ? null : compteInterne.getBanque().getLibelle();
			if ( compteInterne.getTitulaires() != null && !compteInterne.getTitulaires().isEmpty() ) {
				dto.libellesTitulaires = new ArrayList<>();
				for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
					dto.libellesTitulaires.add(titulaire.getLibelle());
				}
			}
			dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
			dto.montantSoldeInitialEnEuros = (double) (compteInterne.getMontantSoldeInitialEnCentimes() / 100.00);

			return dto;
		}
		else if ( CompteExterne.class.isAssignableFrom(compte.getClass()) ) {

			EnteteCompteExterneResponseDto dto = new EnteteCompteExterneResponseDto();

			dto.identifiantCompte = compte.getIdentifiant();
			dto.libelleCompte = compte.getLibelle();
			dto.codeTypeCompte = compte.getTypeCompte().getCode();
			dto.libelleTypeCompte = compte.getTypeCompte().getLibelle();

			return dto;
		}
		else if ( CompteTechnique.class.isAssignableFrom(compte.getClass()) ) {

			EnteteCompteTechniqueResponseDto dto = new EnteteCompteTechniqueResponseDto();

			dto.identifiantCompte = compte.getIdentifiant();
			dto.libelleCompte = compte.getLibelle();
			dto.codeTypeCompte = compte.getTypeCompte().getCode();
			dto.libelleTypeCompte = compte.getTypeCompte().getLibelle();

			return dto;
		}
		else {
			throw new ControllerException(
					GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE,
					compte.getClass().getSimpleName());
		}
	}

	private static EnteteEmpruntResponseDto mapperEnteteEmprunt(Emprunt emprunt) {
		
		EnteteEmpruntResponseDto dto = new EnteteEmpruntResponseDto();
		
		dto.momentCalcul = LocalDateTime.now();
		if ( emprunt.getCompteInterne() != null && emprunt.getCompteInterne().getBanque() != null ) {
			dto.banque = mapperBanque(emprunt.getCompteInterne().getBanque());
		}
		dto.cle = emprunt.getCle();
		dto.libelle = emprunt.getLibelle();
		dto.montantPretEnEuros = (double) (emprunt.getConditionEmpruntInitiale().getCapitalEmprunteEnCentimes() / 100);
		dto.dureePret = emprunt.getConditionEmpruntInitiale().getDuree();
		dto.periodiciteEcheances = mapperTypePeriode(emprunt.getConditionEmpruntInitiale().getTypePeriodeEcheances());

		return dto;
	}
	
	private static CategorieResponseDto mapperCategorie(Categorie categorie) {

		CategorieResponseDto dto = new CategorieResponseDto();
		
		if ( categorie != null ) {
			dto.nom = categorie.getNom();
			dto.libelle = categorie.getLibelle();
		}
		else {
			dto.nom = "INDETERMINEE";
			dto.libelle = "Catégorie non spécifiée";
		}
		
		return dto;
	}

	private static SousCategorieResponseDto mapperSousCategorie(SousCategorie sousCategorie) {

		SousCategorieResponseDto dto = new SousCategorieResponseDto();
		
		if ( sousCategorie != null ) {
			dto.nom = sousCategorie.getNom();
			dto.libelle = sousCategorie.getLibelle();
		}
		else {
			dto.nom = "INDETERMINEE";
			dto.libelle = "Sous-catégorie non spécifiée";
		}
		
		return dto;
	}

	private static BeneficiaireResponseDto mapperBeneficiaire(Beneficiaire beneficiaire) {

		BeneficiaireResponseDto dto = new BeneficiaireResponseDto();
		
		if ( beneficiaire != null ) {
			dto.nom = beneficiaire.getNom();
			dto.libelle = beneficiaire.getLibelle();
		}
		else {
			dto.nom = "INDETERMINE";
			dto.libelle = "Beneficiaire non spécifié";
		}
		
		return dto;
	}

	private static BanqueResponseDto mapperBanque(Banque banque) {
		
		BanqueResponseDto dto = new BanqueResponseDto();
		
		if ( banque != null ) {
			dto.nom = banque.getNom();
			dto.libelle = banque.getLibelle();
		}
		else {
			dto.nom = "INDETERMINEE";
			dto.libelle = "Banque non spécifiée";
		}
		
		return dto;
	}

	private static TitulaireResponseDto mapperTitulaire(Titulaire titulaire) {
		
		TitulaireResponseDto dto = new TitulaireResponseDto();
		
		if ( titulaire != null ) {
			dto.nom = titulaire.getNom();
			dto.libelle = titulaire.getLibelle();
		}
		else {
			dto.nom = "INDETERMINE";
			dto.libelle = "Titulaire non spécifié";
		}
		
		return dto;
	}
	
	private static TypologieResponseDto mapperTypePeriode(TypePeriode typePeriode) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		if ( typePeriode != null ) {
			dto.code = typePeriode.getCode();
			dto.libelle = typePeriode.getLibelle();
		}
		else {
			dto.code = "INDETERMINE";
			dto.libelle = "Type de période non spécifié";
		}
			
		return dto;
	}
	
	private static TypologieResponseDto mapperTypeFonctionnement(TypeFonctionnement typeFonctionnement) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		if ( typeFonctionnement != null ) {
			dto.code = typeFonctionnement.getCode();
			dto.libelle = typeFonctionnement.getLibelle();
		}
		else {
			dto.code = "INDETERMINE";
			dto.libelle = "Type de fonctionnement non spécifié";
		}
			
		return dto;
	}
	
	private static TypologieResponseDto mapperTypeOperation(TypeOperation typeOperation) {
		
		TypeOperationResponseDto dto = new TypeOperationResponseDto();
		
		if ( typeOperation != null ) {
			dto.code = typeOperation.getCode();
			dto.libelleCourt = typeOperation.getLibelleCourt();
			dto.libelle = typeOperation.getLibelle();
			dto.fluxTechnique = typeOperation.isFluxTechnique();
			dto.categorisable = typeOperation.isCategorisable();
		}
		else {
			dto.code = "INDETERMINE";
			dto.libelleCourt = "Indéterminé";
			dto.libelle = "Type d'opération non spécifié";
			dto.fluxTechnique = false;
			dto.categorisable = false;
			
		}
		
		return dto; 
	}
	
	private static TypologieResponseDto mapperTypeBudget(TypeBudget typeBudget) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		if ( typeBudget != null ) {
			dto.code = typeBudget.getCode();
			dto.libelle = typeBudget.getLibelle();
		}
		else {
			dto.code = "INDETERMINE";
			dto.libelle = "Type de budget non spécifié";
		}
			
		return dto;
	}
}
