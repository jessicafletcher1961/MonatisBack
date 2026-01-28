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
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValues;
import fr.colline.monatis.rapports.model.HistoriquePlusMoinsValues;
import fr.colline.monatis.rapports.model.ListeResumeCompteInterne;
import fr.colline.monatis.rapports.model.PlusMoinsValue;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
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
	
	@Autowired private SoldeService soldeService;
	@Autowired private PlusMoinsValueService plusMoinsValueService;
	
	public ReleveCompte rechercherReleveCompte(
			Compte compte,
			LocalDate dateDebut,
			LocalDate dateFin) throws ServiceException {
		
		ReleveCompte releve = new ReleveCompte();
		
		releve.setCompte(compte);
		releve.setDateDebutReleve(dateDebut);
		releve.setDateFinReleve(dateFin);

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;

			// Tout à 0 si la date de fin du relevé se situe avant la date du solde initial du compte
			if ( dateFin.isBefore(compteInterne.getDateSoldeInitial()) ) {
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
				// opérations réelles effectuées jusqu'à la veille de la date de début du relévé
	 			releve.setMontantSoldeDebutReleveEnCentimes(soldeService.rechercherSolde(compte, dateDebutReleve.minus(1L, ChronoUnit.DAYS)));
			}
			
			releve.setMontantSoldeFinReleveEnCentimes(soldeService.rechercherSolde(compte, dateFin));
			
			Operation operationVirtuelle = soldeService.rechercherOperationVirtuelle(compteInterne, dateDebutReleve, dateFin);
			
			List<Operation> operationsRecette = operationService.rechercherOperationsRecetteParCompteIdEntreDateDebutEtDateFin(
					compte.getId(), 
					dateDebutReleve, 
					dateFin);
			if ( operationVirtuelle.getTypeOperation() == TypeOperation.VIRTUELLE_PLUS ) {
				operationsRecette.add(operationVirtuelle);
			}
			releve.setOperationsRecette(operationsRecette);
			
			List<Operation> operationsDepense = operationService.rechercherOperationsDepenseParCompteIdEntreDateDebutEtDateFin(
					compte.getId(), 
					dateDebutReleve, 
					dateFin);
			if ( operationVirtuelle.getTypeOperation() == TypeOperation.VIRTUELLE_MOINS ) {
				operationsDepense.add(operationVirtuelle);
			}
			releve.setOperationsDepense(operationsDepense);

		}
		else {
			
			// Ce n'est pas un compte interne -> on prend les opérations antérieures à la date de début comme solde initial

			releve.setMontantSoldeDebutReleveEnCentimes(soldeService.rechercherSolde(compte, dateDebut.minus(1L, ChronoUnit.DAYS)));
			releve.setMontantSoldeFinReleveEnCentimes(soldeService.rechercherSolde(compte, dateFin));
			releve.setOperationsRecette(operationService.rechercherOperationsRecetteParCompteIdEntreDateDebutEtDateFin(compte.getId(), dateDebut, dateFin));
			releve.setOperationsDepense(operationService.rechercherOperationsDepenseParCompteIdEntreDateDebutEtDateFin(compte.getId(), dateDebut, dateFin));
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
		
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutHistorique);
			while ( ! dateDebutPeriode.isAfter(dateFinHistorique) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				historique.getPlusMoinsValues().add(plusMoinsValueService.rechercherPlusMoinsValue(compteInterne, dateDebutPeriode, dateFinPeriode));
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
			Collections.sort(
					historique.getPlusMoinsValues(), 
					(o1, o2) -> { return o1.getDateDebutEvaluation().compareTo(o2.getDateDebutEvaluation());});
		}
		else {
			historique.getPlusMoinsValues().add(plusMoinsValueService.rechercherPlusMoinsValue(compteInterne, dateDebutHistorique, dateFinHistorique));
		}
	
		return historique;
	}

	public List<EtatPlusMoinsValues> rechercherEtatsPlusMoinsValue(
			TypePeriode typePeriode,
			LocalDate dateCible) throws ServiceException, ControllerException {

		List<EtatPlusMoinsValues> etats = new ArrayList<>();
		
		LocalDate dateDebutEvaluation = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateCible);
		LocalDate dateFinEvaluation = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutEvaluation);
		
		Sort tri = Sort.by("typeFonctionnement", "identifiant");
		List<CompteInterne> comptesInternes = compteInterneService.rechercherTous(tri);
		for ( CompteInterne compteInterne : comptesInternes ) {

			if ( compteInterne.getDateCloture() != null 
					&& compteInterne.getDateCloture().isBefore(dateDebutEvaluation) ) {
				continue;
			}

			if ( compteInterne.getDateSoldeInitial().isAfter(dateFinEvaluation) ) {
				continue;
			}
			
			PlusMoinsValue plusMoinsValue = plusMoinsValueService.rechercherPlusMoinsValue(
					compteInterne, 
					dateDebutEvaluation, 
					dateFinEvaluation);

			if ( plusMoinsValue.getMontantPlusMoinsValueEnCentimes() != 0 ) {
				EtatPlusMoinsValues etat = new EtatPlusMoinsValues();
				etat.setCompteInterne(compteInterne);
				etat.setPlusMoinsValue(plusMoinsValue);
				etats.add(etat);
			}
		}
		
		return etats;
	}
	
	public ListeResumeCompteInterne rechercherListeResumeCompteInterne(LocalDate dateSolde) throws ServiceException {
		
		ListeResumeCompteInterne liste = new ListeResumeCompteInterne();
		
		for ( TypeFonctionnement typeFonctionnement : TypeFonctionnement.values() ) {

			for ( CompteInterne compteInterne : compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement)) {
			
				if ( compteInterne.getDateSoldeInitial().isAfter(dateSolde) ) {
					continue;
				}

				ResumeCompteInterne resume = new ResumeCompteInterne();
				resume.setCompteInterne(compteInterne);
				resume.setMontantSoldeEnCentimes(soldeService.rechercherSolde(compteInterne, dateSolde));
				resume.setDateSolde(dateSolde); 
				liste.getMap().get(typeFonctionnement).add(resume);
			}
		}
		
		return liste;
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
