package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.utils.DateEtPeriodeUtils;
import fr.colline.monatis.utils.TypePeriode;

@Service
class PlusMoinsValueService {

    private final CompteInterneService compteInterneService;

	@Autowired private SoldeService soldeService;
	@Autowired private OperationService operationService;
//	@Autowired private EvaluationService evaluationService;

//	private class PlusMoinsValuePeriodeEvaluation {
//	
//		LocalDate dateSolde;
//		Long montantSoldeNet;
//		double tauxPlusMoinsValuePotentielle;
//	}

    PlusMoinsValueService(CompteInterneService compteInterneService) {
        this.compteInterneService = compteInterneService;
    }
	
	PlusMoinsValueTypeFonctionnementLigne rechercherPlusMoinsValueTypeFonctionnementLigne(
			List<CompteInterne> comptesInternes,
			TypeFonctionnement typeFonctionnement,
			Titulaire titulaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {
		
		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);
		
		List<PlusMoinsValueCompteInterneLigne> lignesCompteInterne = new ArrayList<PlusMoinsValueCompteInterneLigne>();
		PlusMoinsValueTypeFonctionnementPeriode[] cumulsTypeFonctionnement = new PlusMoinsValueTypeFonctionnementPeriode[nombrePeriodes];
		
		for ( CompteInterne compteInterne : compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement) ) {
			
			if ( titulaire != null && ! compteInterne.getTitulaires().contains(titulaire) ) {
				continue;
			}
			
			if ( comptesInternes != null && ! comptesInternes.isEmpty() && ! comptesInternes.contains(compteInterne) ) {
				continue;
			}

			PlusMoinsValueCompteInterneLigne ligneCompteInterne = rechercherPlusMoinsValueCompteInterneLigne(
					compteInterne,
					dateDebutEtat,
					dateFinEtat,
					typePeriode);
			
			lignesCompteInterne.add(ligneCompteInterne);
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				PlusMoinsValueCompteInternePeriode cumulCompteInternePeriode = ligneCompteInterne.getPeriodes()[numeroPeriode];
				PlusMoinsValueTypeFonctionnementPeriode cumulTypeFonctionnementPeriode;
				if ( cumulsTypeFonctionnement[numeroPeriode] == null ) {
					cumulTypeFonctionnementPeriode = new PlusMoinsValueTypeFonctionnementPeriode();
					cumulTypeFonctionnementPeriode.setDateDebutPeriode(cumulCompteInternePeriode.getDateDebutPeriode());
					cumulTypeFonctionnementPeriode.setDateFinPeriode(cumulCompteInternePeriode.getDateFinPeriode());
					cumulTypeFonctionnementPeriode.setMontantPlusMoinsValuePotentielleEnCentimes(cumulCompteInternePeriode.getMontantPlusMoinsValuePotentielleEnCentimes());
					cumulsTypeFonctionnement[numeroPeriode] = cumulTypeFonctionnementPeriode;
				}
				else {
					cumulTypeFonctionnementPeriode = cumulsTypeFonctionnement[numeroPeriode];
					cumulTypeFonctionnementPeriode.setMontantPlusMoinsValuePotentielleEnCentimes(cumulTypeFonctionnementPeriode.getMontantPlusMoinsValuePotentielleEnCentimes() + cumulCompteInternePeriode.getMontantPlusMoinsValuePotentielleEnCentimes());
				}
			}
		}

		PlusMoinsValueTypeFonctionnementLigne etat = new PlusMoinsValueTypeFonctionnementLigne();

		etat.setTypeFonctionnement(typeFonctionnement);
		etat.setLignesCompteInterne(lignesCompteInterne);

		if ( lignesCompteInterne.isEmpty() ) {
			
			// Aucun compte n'a été sélectionné pour ce type de fonctionnement, on doit créer les périodes "à vide"
			
			int numeroPeriode = 0;
			if ( typePeriode != null ) {
				LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
				while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
						LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
						
						PlusMoinsValueTypeFonctionnementPeriode cumulTypeFonctionnementPeriode = new PlusMoinsValueTypeFonctionnementPeriode();
						cumulTypeFonctionnementPeriode.setDateDebutPeriode(dateDebutPeriode);
						cumulTypeFonctionnementPeriode.setDateFinPeriode(dateFinPeriode);
						cumulTypeFonctionnementPeriode.setMontantPlusMoinsValuePotentielleEnCentimes(0L);
						cumulsTypeFonctionnement[numeroPeriode++] = cumulTypeFonctionnementPeriode;

						dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
				}
			}
			else {
				PlusMoinsValueTypeFonctionnementPeriode cumulTypeFonctionnementPeriode = new PlusMoinsValueTypeFonctionnementPeriode();
				
				cumulTypeFonctionnementPeriode.setDateDebutPeriode(dateDebutEtat);
				cumulTypeFonctionnementPeriode.setDateFinPeriode(dateFinEtat);
				cumulTypeFonctionnementPeriode.setMontantPlusMoinsValuePotentielleEnCentimes(0L);
				cumulsTypeFonctionnement[0] = cumulTypeFonctionnementPeriode;
			}
		}
			
		etat.setCumulsPeriodes(cumulsTypeFonctionnement);
		
		return etat;
	}
	
	PlusMoinsValueCompteInterneLigne rechercherPlusMoinsValueCompteInterneLigne(
			CompteInterne compteInterne,
			LocalDate dateDebutEtat, 
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		PlusMoinsValueCompteInternePeriode[] periodes = new PlusMoinsValueCompteInternePeriode[nombrePeriodes];

		int numeroPeriode = 0;
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
			while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				PlusMoinsValueCompteInternePeriode periode = rechercherPlusMoinsValueCompteInternePeriode(
						compteInterne, 
						dateDebutPeriode, 
						dateFinPeriode);
				periodes[numeroPeriode++] = periode;
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
		}
		else {
			PlusMoinsValueCompteInternePeriode compteInternePeriode = rechercherPlusMoinsValueCompteInternePeriode(
					compteInterne, 
					dateDebutEtat, 
					dateFinEtat);
			periodes[0] = compteInternePeriode;
		}

		PlusMoinsValueCompteInterneLigne etat = new PlusMoinsValueCompteInterneLigne();

		etat.setCompteInterne(compteInterne);
		etat.setPeriodes(periodes);
		
		return etat;
	}

	PlusMoinsValueCompteInternePeriode rechercherPlusMoinsValueCompteInternePeriode (
			CompteInterne compteInterne,
			LocalDate dateDebutPeriode,
			LocalDate dateFinPeriode) throws ServiceException {
		
		Long soldeDebutPeriode = soldeService.rechercherSolde(compteInterne, dateDebutPeriode.minus(1, ChronoUnit.DAYS));
		Long soldeFinPeriode = soldeService.rechercherSolde(compteInterne, dateFinPeriode);
		
		if ( dateFinPeriode.isBefore(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) 
				|| (compteInterne.getDateCloture() != null && dateDebutPeriode.isAfter(compteInterne.getDateCloture())) ) {

			// On est sur une période qui est hors de la période de vie du compte
			
			PlusMoinsValueCompteInternePeriode plusMoinsValue = new PlusMoinsValueCompteInternePeriode();
			
			plusMoinsValue.setDateDebutPeriode(dateDebutPeriode);
			plusMoinsValue.setDateFinPeriode(dateFinPeriode);
			plusMoinsValue.setMontantSoldeInitialEnCentimes(soldeDebutPeriode);
			plusMoinsValue.setMontantSoldeFinalEnCentimes(soldeFinPeriode);
			plusMoinsValue.setMontantMouvementTransactionEnCentimes(0L);
			plusMoinsValue.setMontantMouvementTechniqueEnCentimes(0L);
			plusMoinsValue.setMontantPlusValueRealiseeEnCentimes(0L);
			plusMoinsValue.setMontantPlusMoinsValuePotentielleEnCentimes(0L);
			plusMoinsValue.setTauxPlusMoinsValuePotentielle(0D);
			
			return plusMoinsValue;
		}

		List<Operation> operationsPeriode = operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(
				compteInterne, 
				dateDebutPeriode, 
				dateFinPeriode);

		// Rémunération - frais sur la période
		Long montantMouvementTechniqueEnCentimes = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		montantMouvementTechniqueEnCentimes -= operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		// Recettes - dépenses sur la période
		Long montantMouvementTransactionEnCentimes = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTransaction();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		montantMouvementTransactionEnCentimes -= operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTransaction();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		// Quand l'ouverture du compte est située dans la période étudiée, le solde initial du compte sera considéré comme une recette pour ce compte
		boolean permierePeriodeDuCompte = compteInterne.getDateSoldeInitial().isAfter(dateDebutPeriode) 
				&& ! compteInterne.getDateSoldeInitial().isAfter(dateFinPeriode);
		if ( permierePeriodeDuCompte ) {
			montantMouvementTransactionEnCentimes += compteInterne.getMontantSoldeInitialEnCentimes();
		}
		
//		// Réalisation des plus ou moins values sur les dépenses faites dana la période (les ventes/liquidations/retraits)
//		// On calcule le montant de plus ou moins value réalisée pour chaque dépense à l'aide du taux le plus récent connu à sa date de valeur
//		List<PlusMoinsValuePeriodeEvaluation> historiqueCompte = etablirHistoriqueEvaluationsPlusMoinsValue(compteInterne, compteInterne.getDateSoldeInitial(), operationsPeriode);
//		Long montantPlusMoinsValueRealiseeEnCentimes = operationsPeriode
//				.stream()
//				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
//				.filter((o) -> {return o.getTypeOperation().isFluxTransaction();})
//				.filter((o) -> {return ! o.getDateValeur().isBefore(dateDebutPeriode) && ! o.getDateValeur().isAfter(dateFinPeriode);})
//				.mapToLong((o) -> {
//					try {
//						PlusMoinsValuePeriodeEvaluation periodeApplicable = rechercherPlusMoinsValuePeriodeEvaluationApplicable(o.getDateValeur(), historiqueCompte);
//						Long montantPlusValueRealisee = Math.round(
//								(double) ((periodeApplicable.tauxPlusMoinsValuePotentielle * o.getMontantEnCentimes()) / 100.00));
//						return montantPlusValueRealisee;
//					} catch (ServiceException e) {
//						e.printStackTrace();
//						return 0l;
//					}})
//				.sum();
		// TODO
		Long montantPlusMoinsValueRealiseeEnCentimes = 0L;
		
		// Le solde brut est le montant du solde initial auquel on ajoute les recettes et dont on retranche les dépenses sur la période, pour leur montant total.  
		// Ne comporte pas les opérations de rémunération et de frais
		Long soldeBrut = soldeDebutPeriode + montantMouvementTransactionEnCentimes;
		
		// Le solde net est le solde brut duquel on retranche le montant des plus ou moins values réalisées
		Long soldeNet = soldeBrut - montantPlusMoinsValueRealiseeEnCentimes;

		// Le montant de la plus value calculée embarque les montants des rémunérations, des frais, des plus-values réalisées et de l'écart entre le solde calculé et le solde enregistré
		Long montantPlusMoinsValuePotentielleEnCentimes = soldeFinPeriode - soldeNet;

		// Le taux de plus ou moins value calculé tient compte des rémunérations et frais de la période, y compris des montants des plus-values réalisée
		double tauxPlusMoinsValuePotentielle;
		if ( soldeNet != 0 ) {
			tauxPlusMoinsValuePotentielle = (double) (100.00 * montantPlusMoinsValuePotentielleEnCentimes / soldeNet);
		}
		else {
			tauxPlusMoinsValuePotentielle = 0;
		}
		
		PlusMoinsValueCompteInternePeriode plusMoinsValue = new PlusMoinsValueCompteInternePeriode();
		
		plusMoinsValue.setDateDebutPeriode(dateDebutPeriode);
		plusMoinsValue.setDateFinPeriode(dateFinPeriode);
		plusMoinsValue.setMontantSoldeInitialEnCentimes(soldeDebutPeriode);
		plusMoinsValue.setMontantSoldeFinalEnCentimes(soldeFinPeriode);
		plusMoinsValue.setMontantMouvementTransactionEnCentimes(montantMouvementTransactionEnCentimes);
		plusMoinsValue.setMontantMouvementTechniqueEnCentimes(montantMouvementTechniqueEnCentimes);
		plusMoinsValue.setMontantPlusMoinsValuePotentielleEnCentimes(montantPlusMoinsValuePotentielleEnCentimes);
		plusMoinsValue.setTauxPlusMoinsValuePotentielle(tauxPlusMoinsValuePotentielle);
		plusMoinsValue.setMontantPlusValueRealiseeEnCentimes(montantPlusMoinsValueRealiseeEnCentimes);

		return  plusMoinsValue;
	}
//
//	private PlusMoinsValuePeriodeEvaluation rechercherPlusMoinsValuePeriodeEvaluationApplicable(
//			LocalDate dateCible, 
//			List<PlusMoinsValuePeriodeEvaluation> periodes) throws ServiceException {
//	
//		PlusMoinsValuePeriodeEvaluation plusMoinsValue = null;
//		for ( PlusMoinsValuePeriodeEvaluation periode : periodes ) {
//			if ( periode.dateSolde.isAfter(dateCible) ) {
//				break;
//			}
//			plusMoinsValue = periode;
//		}
//		
//		return plusMoinsValue;
//	}
//	
//	private List<PlusMoinsValuePeriodeEvaluation> etablirHistoriqueEvaluationsPlusMoinsValue(
//			CompteInterne compteInterne, 
//			LocalDate dateDebutPeriode, 
//			List<Operation> operationsPeriode) throws ServiceException {
//
//		List<PlusMoinsValuePeriodeEvaluation> periodes = new ArrayList<PlusMoinsValueService.PlusMoinsValuePeriodeEvaluation>();
//		
//		// On recherche une évaluation précédant le début de la période
//		Evaluation derniereEvaluation = evaluationService.rechercherDerniereParCompteInterneIdJusqueDateCible(
//				compteInterne.getId(), 
//				dateDebutPeriode.minus(1, ChronoUnit.DAYS));
//		if ( derniereEvaluation == null ) {
//			derniereEvaluation = new Evaluation();
//			derniereEvaluation.setCompteInterne(compteInterne);
//			if ( compteInterne.getDateSoldeInitial().isAfter(dateDebutPeriode) ) {
//				derniereEvaluation.setDateSolde(dateDebutPeriode.minus(1, ChronoUnit.DAYS));
//				derniereEvaluation.setMontantSoldeEnCentimes(0l);
//			}
//			else {
//				derniereEvaluation.setDateSolde(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS));
//				derniereEvaluation.setMontantSoldeEnCentimes(compteInterne.getMontantSoldeInitialEnCentimes());
//			}
//		}
//
//		PlusMoinsValuePeriodeEvaluation periode = new PlusMoinsValuePeriodeEvaluation();
//		periode.dateSolde = derniereEvaluation.getDateSolde();
//		periode.montantSoldeNet = derniereEvaluation.getMontantSoldeEnCentimes();
//		periode.tauxPlusMoinsValuePotentielle = 0;
//		periodes.add(periode);
//		
//		List<Evaluation> evaluations = evaluationService.rechercherParCompteInterneIdDepuisDateDebut(
//				compteInterne.getId(), 
//				dateDebutPeriode);
//
//		if ( ! evaluations.isEmpty()) {
//			
//			for ( Evaluation evaluation : evaluations ) {
//				
//				Long mouvementsTotaux = calculerTotalMouvementTransaction(compteInterne.getId(), periode.dateSolde.plus(1, ChronoUnit.DAYS), evaluation.getDateSolde(), operationsPeriode);
//				Long mouvementsPlusMoinsValueRealisee;
//				if ( periode.tauxPlusMoinsValuePotentielle != 0 ) {
//					// TODO
////					mouvementsPlusMoinsValueRealisee = Math.round(
////							(double) (mouvementsDepense * 100.00) / (100.00 + periode.tauxPlusMoinsValuePotentielle));
//					mouvementsPlusMoinsValueRealisee = 0L;
//				}
//				else {
//					mouvementsPlusMoinsValueRealisee = 0l;
//				}
//
//				Long soldeNet = (periode.montantSoldeNet + (mouvementsTotaux - mouvementsPlusMoinsValueRealisee));
//				double tauxPlusMoinsValuePotentielle;
//				if ( soldeNet != 0 ) {
//					tauxPlusMoinsValuePotentielle = (double) (100.00 * (evaluation.getMontantSoldeEnCentimes() - soldeNet) / soldeNet);
//				}
//				else {
//					tauxPlusMoinsValuePotentielle = 0;
//				}
//				
//				periode = new PlusMoinsValuePeriodeEvaluation();
//				periode.dateSolde = evaluation.getDateSolde();
//				periode.montantSoldeNet = soldeNet;
//				periode.tauxPlusMoinsValuePotentielle = tauxPlusMoinsValuePotentielle;
//				periodes.add(periode);
//			}
//		}
//		
//		return periodes;
//	}
}
