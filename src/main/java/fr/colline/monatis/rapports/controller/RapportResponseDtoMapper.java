package fr.colline.monatis.rapports.controller;

import java.util.ArrayList;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.GeneriqueTechniqueErreur;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineCompteInterneLigneResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineCompteInternePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoinePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigneResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.bilan_patrimoine.EtatBilanPatrimoineResponseDto;
import fr.colline.monatis.rapports.controller.commun.BeneficiaireResponseDto;
import fr.colline.monatis.rapports.controller.commun.CategorieResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteExterneResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteInterneResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteTechniqueResponseDto;
import fr.colline.monatis.rapports.controller.commun.SousCategorieResponseDto;
import fr.colline.monatis.rapports.controller.commun.TitulaireResponseDto;
import fr.colline.monatis.rapports.controller.commun.TypeFonctionnementResponseDto;
import fr.colline.monatis.rapports.controller.commun.TypePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecetteCategorieLigneResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecetteCategoriePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecettePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecetteSousCategorieLigneResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.DepenseRecetteSousCategoriePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.depense_recette.EtatDepenseRecetteResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueCompteInterneLigneResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueCompteInternePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValuePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueTypeFonctionnementLigneResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueTypeFonctionnementPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteOperationResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.EtatRemunerationsFraisResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisCompteInterneLigneResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisCompteInternePeriodeResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisTypeFonctionnementLigneResponseDto;
import fr.colline.monatis.rapports.controller.remunerations_frais.RemunerationsFraisTypeFonctionnementPeriodeResponseDto;
import fr.colline.monatis.rapports.controller.resumes_comptes_internes.ResumeCompteInterneResponseDto;
import fr.colline.monatis.rapports.model.EtatBilanPatrimoine;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValue;
import fr.colline.monatis.rapports.model.EtatRemunerationsFrais;
import fr.colline.monatis.rapports.model.ReleveOperationCompte;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoinePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategoriePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategoriePeriode;
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
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.utils.TypePeriode;

public class RapportResponseDtoMapper {

	// --- Relevé de compte

	public static ReleveCompteResponseDto mapperReleveCompte(ReleveOperationCompte releve) throws ControllerException {

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

	// --- Liste des comptes avec solde

	public static ResumeCompteInterneResponseDto mapperResumeCompteInterne(ResumeCompteInterne resumeCompteInterne) throws ControllerException {
		
		ResumeCompteInterneResponseDto dto = new ResumeCompteInterneResponseDto();
		
		dto.compteInterne = mapperEnteteCompte(resumeCompteInterne.getCompteInterne());
		dto.dateSolde = resumeCompteInterne.getDateSolde();
		dto.montantSoldeEnEuros = (double) (resumeCompteInterne.getMontantSoldeEnCentimes() / 100.00);
		
		return dto;
	}
//	
//	public static EtatAvancementBudgetResponseDto mapperEtatAvancementBudget(
//			EtatAvancementBudget etatAvancement) {
//
//		EtatAvancementBudgetResponseDto dto = new EtatAvancementBudgetResponseDto();
//
//		dto.reference = SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto(etatAvancement.getSousCategorie());
//		dto.budget = BudgetResponseDtoMapper.mapperModelToResponseDto(etatAvancement.getBudget());
//
//		dto.montantBudgeteEnEuros = (float) (etatAvancement.getMontantBudgetEnCentimes() / 100.00);
//		dto.montantExecutionEnEuros = (float) (etatAvancement.getMontantExecutionEnCentimes() / 100.00);
//		dto.montantExecutionEnPourcentage = etatAvancement.getMontantExecutionEnPourcentage();
//		dto.resteADepenserEnEuros = (float) (etatAvancement.getResteADepenserEnCentimes() / 100.00);
//
//		dto.montantTotalLignesDepenseEnEuros = (float) (etatAvancement.getMontantTotalLignesDepenseEnCentimes() / 100.00);
//		dto.montantTotalLignesRecetteEnEuros = (float) (etatAvancement.getMontantTotalLignesRecetteEnCentimes() / 100.00);
//		dto.montantTotalLignesExcluesEnEuros = (float) (etatAvancement.getMontantTotalLignesExcluesEnCentimes() / 100.00);
//
//		return dto;
//	}
	
	// --- Dépenses et recettes


	public static EtatDepenseRecetteResponseDto mapperEtatDepenseRecette(EtatDepenseRecette etat) {

		EtatDepenseRecetteResponseDto dto = new EtatDepenseRecetteResponseDto();

		int nombrePeriodes = etat.getCumuls().length;

		dto.dateDebutEtat = etat.getDateDebutEtat();
		dto.dateFinEtat = etat.getDateFinEtat();
		dto.typePeriode = mapperTypePeriode(etat.getTypePeriode());
		
		dto.sousCategories = new ArrayList<SousCategorieResponseDto>();
		for ( SousCategorie sousCategorie : etat.getSousCategories() ) {
			dto.sousCategories.add(mapperSousCategorie(sousCategorie));
		}
		dto.categories = new ArrayList<CategorieResponseDto>();
		for ( Categorie categorie : etat.getCategories() ) {
			dto.categories.add(mapperCategorie(categorie));
		}
		dto.beneficiaire = mapperBeneficiaire(etat.getBeneficiaire());

		dto.lignesCategorie = new ArrayList<DepenseRecetteCategorieLigneResponseDto>();
		for ( DepenseRecetteCategorieLigne ligneCategorie : etat.getLignesCategorie() ) {
			dto.lignesCategorie.add(mapperDepenseRecetteCategorieLigne(ligneCategorie));
		}
		dto.cumuls = new DepenseRecettePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumuls[numeroPeriode] = mapperDepenseRecettePeriode(etat.getCumuls()[numeroPeriode]);
		}
		
		return dto;

	}

	private static DepenseRecettePeriodeResponseDto mapperDepenseRecettePeriode(
			DepenseRecettePeriode etat) {

		DepenseRecettePeriodeResponseDto dto = new DepenseRecettePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantRecetteEnEuros = (double) (etat.getMontantRecetteEnCentimes() / 100.00);
		dto.montantDepenseEnEuros = (double) (etat.getMontantDepenseEnCentimes() / 100.00);
		dto.soldeDepenseRecetteEnEuros = (double) (etat.getSoldeDepenseRecetteEnCentimes() / 100.00);
		
		return dto;
	}

	private static DepenseRecetteCategorieLigneResponseDto mapperDepenseRecetteCategorieLigne(
			DepenseRecetteCategorieLigne etat) {

		DepenseRecetteCategorieLigneResponseDto dto = new DepenseRecetteCategorieLigneResponseDto();

		int nombrePeriodes = etat.getCumuls().length;

		dto.categorie = mapperCategorie(etat.getCategorie());
		dto.lignesSousCategorie = new ArrayList<DepenseRecetteSousCategorieLigneResponseDto>();
		for ( DepenseRecetteSousCategorieLigne detail : etat.getLignesSousCategorie() ) {
			dto.lignesSousCategorie.add(mapperDepenseRecetteSousCategorieLigne(detail));
		}
		dto.cumuls = new DepenseRecetteCategoriePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.cumuls[numeroPeriode] = mapperDepenseRecetteCategoriePeriode(etat.getCumuls()[numeroPeriode]);
		}
		
		return dto;
	}

	private static DepenseRecetteCategoriePeriodeResponseDto mapperDepenseRecetteCategoriePeriode(
			DepenseRecetteCategoriePeriode etat) {

		DepenseRecetteCategoriePeriodeResponseDto dto = new DepenseRecetteCategoriePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantRecetteEnEuros = (double) (etat.getMontantRecetteEnCentimes() / 100.00);
		dto.montantDepenseEnEuros = (double) (etat.getMontantDepenseEnCentimes() / 100.00);
		dto.soldeDepenseRecetteEnEuros = (double) (etat.getSoldeDepenseRecetteEnCentimes() / 100.00);

		return dto;
	}

	private static DepenseRecetteSousCategorieLigneResponseDto mapperDepenseRecetteSousCategorieLigne(
			DepenseRecetteSousCategorieLigne etat) {

		DepenseRecetteSousCategorieLigneResponseDto dto = new DepenseRecetteSousCategorieLigneResponseDto();
		
		int nombrePeriodes = etat.getPeriodes().length;

		dto.sousCategorie = mapperSousCategorie(etat.getSousCategorie());
		dto.periodes = new DepenseRecetteSousCategoriePeriodeResponseDto[nombrePeriodes];
		for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
			dto.periodes[numeroPeriode] = mapperDepenseRecetteSousCategoriePeriode(etat.getPeriodes()[numeroPeriode]);
		}

		return dto;
	}
	
	private static DepenseRecetteSousCategoriePeriodeResponseDto mapperDepenseRecetteSousCategoriePeriode(
			DepenseRecetteSousCategoriePeriode etat) {
		
		DepenseRecetteSousCategoriePeriodeResponseDto dto = new DepenseRecetteSousCategoriePeriodeResponseDto();
		
		dto.dateDebutPeriode = etat.getDateDebutPeriode();
		dto.dateFinPeriode = etat.getDateFinPeriode();
		dto.montantRecetteEnEuros = (double) (etat.getMontantRecetteEnCentimes() / 100.00);
		dto.montantDepenseEnEuros = (double) (etat.getMontantDepenseEnCentimes() / 100.00);
		dto.soldeDepenseRecetteEnEuros = (double) (etat.getSoldeDepenseRecetteEnCentimes() / 100.00);

		
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
		dto.typesFonctionnements = new ArrayList<TypeFonctionnementResponseDto>();
		for ( TypeFonctionnement typeFonctionnement : etat.getTypesFonctionnements() ) {
			dto.typesFonctionnements.add(mapperTypeFonctionnement(typeFonctionnement));
		}
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

	private static PlusMoinsValuePeriodeResponseDto mapperPlusMoinsValuePeriode(PlusMoinsValuePeriode plusMoinsValuePeriode) {
		
		PlusMoinsValuePeriodeResponseDto dto = new PlusMoinsValuePeriodeResponseDto();
		
		dto.dateDebutPeriode = plusMoinsValuePeriode.getDateDebutPeriode();
		dto.dateFinPeriode = plusMoinsValuePeriode.getDateFinPeriode();
		dto.montantPlusMoinsValuePotentielleEnCentimes = (Double) (plusMoinsValuePeriode.getMontantPlusMoinsValuePotentielleEnCentimes() / 100.00);

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
		dto.montantPlusMoinsValuePotentielleEnCentimes = (Double) (etat.getMontantPlusMoinsValuePotentielleEnCentimes() / 100.00);
		
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
		dto.montantSoldeFinalEnEuros = (double) (etat.getMontantSoldeFinalEnCentimes() / 100.00);
		dto.montantMouvementEnEuros = (double) (etat.getMontantMouvementTransactionEnCentimes() / 100.00);
		dto.montantTechniqueEnEuros = (double) (etat.getMontantMouvementTechniqueEnCentimes() / 100.00);
		dto.montantPlusMoinsValuePotentielleEnEuros = (double) (etat.getMontantPlusMoinsValuePotentielleEnCentimes() / 100.00);
		dto.tauxPlusMoinsValuePotentielle = etat.getTauxPlusMoinsValuePotentielle();
		dto.montantPlusMoinsValueRealiseeEnEuros = (double) (etat.getMontantPlusValueRealiseeEnCentimes() / 100.00);
		
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
		dto.typesFonctionnements = new ArrayList<TypeFonctionnementResponseDto>();
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
		dto.typesFonctionnements = new ArrayList<TypeFonctionnementResponseDto>();
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

	private static TypePeriodeResponseDto mapperTypePeriode(TypePeriode typePeriode) {
		
		TypePeriodeResponseDto dto = new TypePeriodeResponseDto();

		if ( typePeriode != null ) {
			dto.code = typePeriode.getCode();
			dto.libelle = typePeriode.getLibelle();
		}
		else {
			dto.code = "INDEFINI";
			dto.libelle = "Aucune périodicité définie";
		}

		return dto;
	}

	private static TypeFonctionnementResponseDto mapperTypeFonctionnement(TypeFonctionnement typeFonctionnement) {

		TypeFonctionnementResponseDto dto = new TypeFonctionnementResponseDto();
		
		if ( typeFonctionnement != null ) {
			dto.code = typeFonctionnement.getCode();
			dto.libelle = typeFonctionnement.getLibelle();
		}
		else {
			dto.code = "INDEFINI";
			dto.libelle = "Aucun type de fonctionnement défini";
		}
		
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
			dto.libelle = "Catégorie indéterminée";
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
			dto.libelle = "Sous-catégorie indéterminée";
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
			dto.libelle = "Beneficiaire indéterminé";
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
			dto.libelle = "Titulaire indéterminé";
		}
		
		return dto;
	}
}
