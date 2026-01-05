package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budgets.model.TypePeriode;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.RapportControleErreur;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValues;
import fr.colline.monatis.rapports.model.HistoriquePlusMoinsValues;
import fr.colline.monatis.rapports.model.PlusMoinsValue;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

/**
 * Liste des rapports
 * <ul><b>relevé de compte</b> : liste de toutes les opérations ayant affecté ce compte en dépense comme en recette, entre deux dates.
 * Les opérations enregistrées avant la date de solde initial sont ignorées.</ul> 
 */
@Service
public class RapportService {

	@Autowired private OperationService operationService;
	@Autowired private CompteInterneService compteInterneService;
	
	public ReleveCompte rechercherReleveCompte(
			Compte compte,
			LocalDate dateDebut,
			LocalDate dateFin) throws ServiceException {
		
		ReleveCompte releve = new ReleveCompte();
		
		releve.setCompte(compte);
		releve.setDateFinReleve(dateFin);

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;

			// Tout à 0 si la date de fin du relevé se situe avant la date du solde initial du compte
			if ( dateFin.isBefore(compteInterne.getDateSoldeInitial()) ) {
				releve.setDateDebutReleve(dateDebut);
				releve.setMontantSoldeDebutReleveEnCentimes(0L);
				releve.setMontantSoldeFinReleveEnCentimes(0L);
				releve.setMontantTotalOperationsRecetteEnCentimes(0L);
				releve.setMontantTotalOperationsDepenseEnCentimes(0L);
				releve.setOperationsRecette(new ArrayList<>());
				releve.setOperationsDepense(new ArrayList<>());
				return releve;
			}

			// On considère que la date de début du relevé doit être égale ou
			// supérieure à la date du solde initial du compte
			LocalDate dateDebutReleve = dateDebut;
			if ( dateDebut.isBefore(compteInterne.getDateSoldeInitial()) ) {
				dateDebutReleve = compteInterne.getDateSoldeInitial();
			}
			releve.setDateDebutReleve(dateDebutReleve);
			
			// Calcul du solde initial à présenter sur le relevé
			if ( dateDebutReleve.equals(compteInterne.getDateSoldeInitial()) ) {
				// Si la date de début du relevé est égale la date de solde initial du compte, 
				// le solde initial du relevé est égal au solde initial du compte
				releve.setMontantSoldeDebutReleveEnCentimes(compteInterne.getMontantSoldeInitialEnCentimes());
			}
			else {
				// Si la date de début du relevé est postérieure à la date du solde initial du compte,
				// le solde initial du relevé est égal au solde initial du compte auquel on ajoute toutes les 
				// opérations effectuées jusqu'à la veille de la date de début du relévé
	 			releve.setMontantSoldeDebutReleveEnCentimes(operationService.rechercherSolde(compte, dateDebutReleve.minus(1L, ChronoUnit.DAYS)));
			}
			
			releve.setMontantSoldeFinReleveEnCentimes(operationService.rechercherSolde(compte, dateFin));
			releve.setOperationsRecette(operationService.rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(compte.getId(), dateDebutReleve, dateFin));
			releve.setOperationsDepense(operationService.rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(compte.getId(), dateDebutReleve, dateFin));

			operationService.ajouterOperationVirtuelleRecetteEntreDateDebutEtDateFin(releve.getOperationsRecette(), compte.getId(), dateDebutReleve, dateFin);
			operationService.ajouterOperationVirtuellesDepenseEntreDateDebutEtDateFin(releve.getOperationsDepense(), compte.getId(), dateDebutReleve, dateFin);
		}
		else {
			
			// Ce n'est pas un compte interne -> on prend les opérations antérieures à la date de début comme solde initial
			
			releve.setMontantSoldeDebutReleveEnCentimes(operationService.rechercherSolde(compte, dateDebut.minus(1L, ChronoUnit.DAYS)));
			releve.setMontantSoldeFinReleveEnCentimes(operationService.rechercherSolde(compte, dateFin));
			releve.setOperationsRecette(operationService.rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(compte.getId(), dateDebut, dateFin));
			releve.setOperationsDepense(operationService.rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(compte.getId(), dateDebut, dateFin));
		}

		Long montantOperationsRecetteEnCentimes = 0L;
		for ( Operation operation : releve.getOperationsRecette() ) {
			montantOperationsRecetteEnCentimes += operation.getMontantEnCentimes();
		}
		releve.setMontantTotalOperationsRecetteEnCentimes(montantOperationsRecetteEnCentimes);
		
		Long montantOperationsDepenseEnCentimes = 0L;
		for ( Operation operation : releve.getOperationsDepense() ) {
			montantOperationsDepenseEnCentimes += operation.getMontantEnCentimes();
		}
		releve.setMontantTotalOperationsDepenseEnCentimes(montantOperationsDepenseEnCentimes);
		
		return releve;
	}

	public HistoriquePlusMoinsValues rechercherHistoriquePlusMoinsValue(
			CompteInterne compteInterne,
			LocalDate dateDebutHistorique,
			LocalDate dateFinHistorique,
			TypePeriode typePeriode) throws ServiceException, ControllerException {

		HistoriquePlusMoinsValues historique = new HistoriquePlusMoinsValues();

		historique.setCompteInterne(compteInterne);
		historique.setPlusMoinsValues(new ArrayList<PlusMoinsValue>());
		
		if ( dateDebutHistorique == null 
				|| dateDebutHistorique.isBefore(compteInterne.getDateSoldeInitial()) ) {
			dateDebutHistorique = compteInterne.getDateSoldeInitial();
		}
		if ( dateFinHistorique == null ) {
			dateFinHistorique = LocalDate.now();
		}
		
		if ( dateFinHistorique.isBefore(dateDebutHistorique) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFinHistorique, 
					dateDebutHistorique);
		}
		
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutHistorique);
			while ( ! dateDebutPeriode.isAfter(dateFinHistorique) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				historique.getPlusMoinsValues().add(rechercherPlusMoinsValue(compteInterne, dateDebutPeriode, dateFinPeriode));
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
			Collections.sort(
					historique.getPlusMoinsValues(), 
					(o1, o2) -> { return o1.getDateDebutEvaluation().compareTo(o2.getDateDebutEvaluation());});
		}
		else {
			historique.getPlusMoinsValues().add(rechercherPlusMoinsValue(compteInterne, dateDebutHistorique, dateFinHistorique));
		}
	
		return historique;
	}

	public List<EtatPlusMoinsValues> rechercherEtatsPlusMoinsValue(
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat,
			TypePeriode typePeriode) throws ServiceException, ControllerException {

		List<EtatPlusMoinsValues> etats = new ArrayList<>();
		
		if ( dateFinEtat == null ) {
			dateFinEtat = LocalDate.now();
		}
		
		LocalDate dateDebutEvaluation;
		LocalDate dateFinEvaluation;
		
		Sort tri = Sort.by("typeFonctionnement", "identifiant");
		List<CompteInterne> comptesInternes = compteInterneService.rechercherTous(tri);
		for ( CompteInterne compteInterne : comptesInternes ) {

			dateDebutEvaluation = dateDebutEtat;
			if ( dateDebutEvaluation == null 
					|| dateDebutEvaluation.isBefore(compteInterne.getDateSoldeInitial()) ) {
				dateDebutEvaluation = compteInterne.getDateSoldeInitial();
			}
			
			dateFinEvaluation = dateFinEtat;
			if ( dateFinEvaluation == null ) {
				dateFinEvaluation = LocalDate.now();
			}
			
			if ( dateFinEvaluation.isBefore(dateDebutEvaluation) ) {
				throw new ControllerException(
						RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
						dateFinEvaluation, 
						dateDebutEvaluation);
			}
			
			if ( typePeriode != null ) {
				dateDebutEvaluation = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateFinEvaluation);
				dateFinEvaluation = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutEvaluation);
			}
			
			PlusMoinsValue plusMoinsValue = rechercherPlusMoinsValue(
					compteInterne, 
					dateDebutEvaluation, 
					dateFinEvaluation);

			if ( plusMoinsValue.getMontantPlusMoinsValueEnPourcentage() != null ) {

				EtatPlusMoinsValues etat = new EtatPlusMoinsValues();
				etat.setCompteInterne(compteInterne);
				etat.setPlusMoinsValue(plusMoinsValue);
				
				etats.add(etat);
			}
		}
		
		return etats;
	}
	
	private PlusMoinsValue rechercherPlusMoinsValue (
			CompteInterne compteInterne,
			LocalDate dateDebutEvaluation,
			LocalDate dateFinEvaluation) throws ServiceException {
	
		Long montantSoldeAvantDebutEvaluationEnCentimes = operationService.rechercherSolde(compteInterne, dateDebutEvaluation.minus(1, ChronoUnit.DAYS));

		// Calcul de la somme des opérations virtuelles de réévaluation enregistrées dans la période
		Long montantReevaluationEnCentimes = 0L;
		List<Operation> operationsVirtuelles = new ArrayList<Operation>();
		operationService.ajouterOperationVirtuelleRecetteEntreDateDebutEtDateFin(operationsVirtuelles, compteInterne.getId(), dateDebutEvaluation, dateFinEvaluation);
		operationService.ajouterOperationVirtuellesDepenseEntreDateDebutEtDateFin(operationsVirtuelles, compteInterne.getId(), dateDebutEvaluation, dateFinEvaluation);
		for ( Operation operation : operationsVirtuelles ) {
			if ( operation.getTypeOperation() == TypeOperation.PLUS_SOLDE ) {
				montantReevaluationEnCentimes += operation.getMontantEnCentimes();
			}
			else if ( operation.getTypeOperation() == TypeOperation.MOINS_SOLDE ) {
				montantReevaluationEnCentimes -= operation.getMontantEnCentimes();
			}
		}
		
		// Calcul de la sommes des opérations effectuées dans la période et réparties entre mouvements
		// (achat / vente) et rémunérations (frais / gains) 
		Long montantMouvementsEnCentimes = 0L;
		Long montantRemunerationEnCentimes = 0L;
		List<Operation> operationsRecette = operationService.rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(compteInterne.getId(), dateDebutEvaluation, dateFinEvaluation);
		for ( Operation operation : operationsRecette ) {
			if ( operation.getTypeOperation() == TypeOperation.GAIN ) {
				montantRemunerationEnCentimes += operation.getMontantEnCentimes();
			}
			else {
				montantMouvementsEnCentimes += operation.getMontantEnCentimes();
			}
		}
		List<Operation> operationsDepense = operationService.rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(compteInterne.getId(), dateDebutEvaluation, dateFinEvaluation);
		for ( Operation operation : operationsDepense ) {
			if ( operation.getTypeOperation() == TypeOperation.FRAIS ) {
				montantRemunerationEnCentimes -= operation.getMontantEnCentimes();
			}
			else {
				montantMouvementsEnCentimes -= operation.getMontantEnCentimes();
			}
		}
		
		Long montantSoldeFinalEnCentimes = operationService.rechercherSolde(compteInterne, dateFinEvaluation);
		
		Float montantPlusMoinsValueEnPourcentage = null;
		if ( montantSoldeAvantDebutEvaluationEnCentimes != 0 ) {
			Long baseCalculEnCentimes = montantSoldeAvantDebutEvaluationEnCentimes 
					+ montantMouvementsEnCentimes;
			montantPlusMoinsValueEnPourcentage = (float) (((montantSoldeFinalEnCentimes - baseCalculEnCentimes) * 100.00) / montantSoldeAvantDebutEvaluationEnCentimes);
		}

		PlusMoinsValue plusMoinsValue = new PlusMoinsValue();
		plusMoinsValue.setCompteInterne(compteInterne);
		plusMoinsValue.setDateDebutEvaluation(dateDebutEvaluation);
		plusMoinsValue.setDateFinEvaluation(dateFinEvaluation);
		plusMoinsValue.setMontantSoldeInitialEnCentimes(montantSoldeAvantDebutEvaluationEnCentimes);
		plusMoinsValue.setMontantSoldeFinalEnCentimes(montantSoldeFinalEnCentimes);
		plusMoinsValue.setMontantReevaluationEnCentimes(montantReevaluationEnCentimes);
		plusMoinsValue.setMontantsGainsEtFraisEnCentimes(montantRemunerationEnCentimes);
		plusMoinsValue.setMontantMouvementsEnCentimes(montantMouvementsEnCentimes);
		plusMoinsValue.setMontantPlusMoinsValueEnPourcentage(montantPlusMoinsValueEnPourcentage);
		
		return  plusMoinsValue;
	}

//
//	public EtatAvancementBudget rechercherEtatAvancementBudgetParSousCategorie(
//			SousCategorie sousCategorie,
//			Budget budget) throws ServiceException {
//
//		LocalDate dateDebut = budget.getDateDebut();
//		LocalDate dateFin = budget.getDateFin().minus(1, ChronoUnit.DAYS);
//
//		List<OperationLigne> lignes = operationService.rechercherOperationsLignesParSousCategorieIdEntreDateDebutEtDateFin(
//				sousCategorie.getId(),
//				dateDebut,
//				dateFin);
//
//		EtatAvancementBudget etatAvancementBudget = new EtatAvancementBudget();
//
//		etatAvancementBudget.setSousCategorie(sousCategorie);
//		etatAvancementBudget.setBudget(budget);
//		etatAvancementBudget.setDateDebutEtat(dateDebut);
//		etatAvancementBudget.setDateFinEtat(dateFin);
//		
//		etatAvancementBudget.setLignesDepense(new ArrayList<>());
//		etatAvancementBudget.setLignesRecette(new ArrayList<>());
//		etatAvancementBudget.setLignesExclues(new ArrayList<>());
//		Long montantTotalDepenseEnCentimes = 0L;
//		Long montantTotalRecetteEnCentimes = 0L;
//		Long montantTotalExcluesEnCentimes = 0L;
//		for ( OperationLigne ligne : lignes ) {
//			if ( ligne.getOperation().getTypeOperation() == TypeOperation.DEPENSE ) {
//				montantTotalDepenseEnCentimes += ligne.getMontantEnCentimes();
//				etatAvancementBudget.getLignesDepense().add(ligne);
//			}
//			else if ( ligne.getOperation().getTypeOperation() == TypeOperation.RECETTE ) {
//				montantTotalRecetteEnCentimes += ligne.getMontantEnCentimes();
//				etatAvancementBudget.getLignesRecette().add(ligne);
//			}
//			else
//			{
//				montantTotalExcluesEnCentimes += ligne.getMontantEnCentimes();
//				etatAvancementBudget.getLignesExclues().add(ligne);
//			}
//		}
//		etatAvancementBudget.setMontantTotalLignesExcluesEnCentimes(montantTotalExcluesEnCentimes);
//		etatAvancementBudget.setMontantTotalLignesRecetteEnCentimes(montantTotalRecetteEnCentimes);
//		etatAvancementBudget.setMontantTotalLignesDepenseEnCentimes(montantTotalDepenseEnCentimes);
//		
//		Long montantExecutionEnCentimes = montantTotalDepenseEnCentimes - montantTotalRecetteEnCentimes;
//		Long montantBudgeteEnCentimes = budget.getMontantEnCentimes();
//		Float montantExecutionEnPourcentage = null;
//		if ( montantBudgeteEnCentimes != 0 ) {
//			montantExecutionEnPourcentage = (float) ((montantExecutionEnCentimes * 100.00) / montantBudgeteEnCentimes);
//		}
//		etatAvancementBudget.setMontantBudgetEnCentimes(montantBudgeteEnCentimes);
//		etatAvancementBudget.setMontantExecutionEnCentimes(montantExecutionEnCentimes);
//		etatAvancementBudget.setMontantExecutionEnPourcentage(montantExecutionEnPourcentage);
//
//		etatAvancementBudget.setResteADepenserEnCentimes(montantBudgeteEnCentimes - montantExecutionEnCentimes);
//
//		return etatAvancementBudget;
//	}

}
