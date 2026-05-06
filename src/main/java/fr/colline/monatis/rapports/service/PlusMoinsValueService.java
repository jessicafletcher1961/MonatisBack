package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypePeriode;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
class PlusMoinsValueService {


	@Autowired private SoldeService soldeService;
    @Autowired private CompteInterneService compteInterneService;
	@Autowired private OperationService operationService;
	
	PlusMoinsValueTypeFonctionnementLigne rechercherPlusMoinsValueTypeFonctionnementLigne(
			List<CompteInterne> comptesInternes,
			TypeFonctionnement typeFonctionnement,
			Banque banque,
			Titulaire titulaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {
		
		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);
		
		List<PlusMoinsValueCompteInterneLigne> lignesCompteInterne = new ArrayList<PlusMoinsValueCompteInterneLigne>();
		PlusMoinsValueTypeFonctionnementPeriode[] cumulsTypeFonctionnement = new PlusMoinsValueTypeFonctionnementPeriode[nombrePeriodes];
		
		for ( CompteInterne compteInterne : compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement) ) {
			
			if ( banque != null && (compteInterne.getBanque() == null || ! compteInterne.getBanque().getId().equals(banque.getId())) ) {
				continue;
			}
			
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
					
					cumulTypeFonctionnementPeriode.setMontantSoldeInitialEnCentimes(cumulCompteInternePeriode.getMontantSoldeInitialEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantOperationsEnCentimes(cumulCompteInternePeriode.getMontantOperationsEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantPlusMoinsValueNetteEnCentimes(cumulCompteInternePeriode.getMontantPlusMoinsValueNetteEnCentimes());
					cumulTypeFonctionnementPeriode.setTauxPlusMoinsValueNette(cumulCompteInternePeriode.getTauxPlusMoinsValueNette());
					cumulTypeFonctionnementPeriode.setMontantSoldeFinalEnCentimes(cumulCompteInternePeriode.getMontantSoldeFinalEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantFraisEnCentimes(cumulCompteInternePeriode.getMontantFraisEnCentimes());
					cumulTypeFonctionnementPeriode.setTauxFrais(cumulCompteInternePeriode.getTauxFrais());
					
					cumulsTypeFonctionnement[numeroPeriode] = cumulTypeFonctionnementPeriode;
				}
				else {
					cumulTypeFonctionnementPeriode = cumulsTypeFonctionnement[numeroPeriode];
					
					cumulTypeFonctionnementPeriode.setMontantSoldeInitialEnCentimes(cumulTypeFonctionnementPeriode.getMontantSoldeInitialEnCentimes() + cumulCompteInternePeriode.getMontantSoldeInitialEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantOperationsEnCentimes(cumulTypeFonctionnementPeriode.getMontantOperationsEnCentimes() + cumulCompteInternePeriode.getMontantOperationsEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantPlusMoinsValueNetteEnCentimes(cumulTypeFonctionnementPeriode.getMontantPlusMoinsValueNetteEnCentimes() + cumulCompteInternePeriode.getMontantPlusMoinsValueNetteEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantSoldeFinalEnCentimes(cumulTypeFonctionnementPeriode.getMontantSoldeFinalEnCentimes() + cumulCompteInternePeriode.getMontantSoldeFinalEnCentimes());
					cumulTypeFonctionnementPeriode.setMontantFraisEnCentimes(cumulTypeFonctionnementPeriode.getMontantFraisEnCentimes() + cumulCompteInternePeriode.getMontantFraisEnCentimes());

					Double tauxPlusMoinsValueNette = null;
					Long soldeDebutAvecOperations = cumulTypeFonctionnementPeriode.getMontantSoldeInitialEnCentimes() + cumulTypeFonctionnementPeriode.getMontantOperationsEnCentimes();
					Long montantPlusMoinsValueNetteEnCentimes = cumulTypeFonctionnementPeriode.getMontantPlusMoinsValueNetteEnCentimes();
					if ( soldeDebutAvecOperations != 0 ) {
						tauxPlusMoinsValueNette = (double) (100.00 * montantPlusMoinsValueNetteEnCentimes / soldeDebutAvecOperations);
					}
					cumulTypeFonctionnementPeriode.setTauxPlusMoinsValueNette(tauxPlusMoinsValueNette);

					Double tauxFrais = null;
					Long soldeFinPeriode = cumulTypeFonctionnementPeriode.getMontantSoldeFinalEnCentimes();
					Long montantFraisEnCentimes = cumulTypeFonctionnementPeriode.getMontantFraisEnCentimes();
					if ( soldeFinPeriode != 0 ) {
						tauxFrais = (double) (100.00 * montantFraisEnCentimes / soldeFinPeriode);
					}
					cumulTypeFonctionnementPeriode.setTauxFrais(tauxFrais);
				}
			}
		}

		PlusMoinsValueTypeFonctionnementLigne etat = new PlusMoinsValueTypeFonctionnementLigne();

		etat.setTypeFonctionnement(typeFonctionnement);
		etat.setLignesCompteInterne(lignesCompteInterne);

		if ( lignesCompteInterne.isEmpty() ) {
			
			// Aucun compte n'a été sélectionné pour ce type de fonctionnement, on doit créer une ligne de périodes "à vide"
			
			int numeroPeriode = 0;
			if ( typePeriode != null ) {
				LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
				while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
						LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
						
						PlusMoinsValueTypeFonctionnementPeriode cumulTypeFonctionnementPeriode = new PlusMoinsValueTypeFonctionnementPeriode();
						cumulTypeFonctionnementPeriode.setDateDebutPeriode(dateDebutPeriode);
						cumulTypeFonctionnementPeriode.setDateFinPeriode(dateFinPeriode);
						cumulTypeFonctionnementPeriode.setMontantSoldeInitialEnCentimes(0L);
						cumulTypeFonctionnementPeriode.setMontantOperationsEnCentimes(0L);
						cumulTypeFonctionnementPeriode.setMontantPlusMoinsValueNetteEnCentimes(0L);
						cumulTypeFonctionnementPeriode.setTauxPlusMoinsValueNette(null);
						cumulTypeFonctionnementPeriode.setMontantSoldeFinalEnCentimes(0L);
						cumulTypeFonctionnementPeriode.setMontantFraisEnCentimes(0L);
						cumulTypeFonctionnementPeriode.setTauxFrais(0D);
						cumulsTypeFonctionnement[numeroPeriode++] = cumulTypeFonctionnementPeriode;

						dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
				}
			}
			else {
				PlusMoinsValueTypeFonctionnementPeriode cumulTypeFonctionnementPeriode = new PlusMoinsValueTypeFonctionnementPeriode();
				
				cumulTypeFonctionnementPeriode.setDateDebutPeriode(dateDebutEtat);
				cumulTypeFonctionnementPeriode.setDateFinPeriode(dateFinEtat);
				cumulTypeFonctionnementPeriode.setMontantSoldeInitialEnCentimes(0L);
				cumulTypeFonctionnementPeriode.setMontantOperationsEnCentimes(0L);
				cumulTypeFonctionnementPeriode.setMontantPlusMoinsValueNetteEnCentimes(0L);
				cumulTypeFonctionnementPeriode.setTauxPlusMoinsValueNette(null);
				cumulTypeFonctionnementPeriode.setMontantSoldeFinalEnCentimes(0L);
				cumulTypeFonctionnementPeriode.setMontantFraisEnCentimes(0L);
				cumulTypeFonctionnementPeriode.setTauxFrais(0D);

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

			// On est sur une période qui est hors de la période de vie du compte, entièrement avant ou entièrement après
			
			PlusMoinsValueCompteInternePeriode plusMoinsValue = new PlusMoinsValueCompteInternePeriode();
			
			plusMoinsValue.setDateDebutPeriode(dateDebutPeriode);
			plusMoinsValue.setDateFinPeriode(dateFinPeriode);
			
			plusMoinsValue.setMontantSoldeInitialEnCentimes(soldeDebutPeriode);
			plusMoinsValue.setMontantOperationsEnCentimes(0L);
			plusMoinsValue.setMontantPlusMoinsValueNetteEnCentimes(0L);
			plusMoinsValue.setTauxPlusMoinsValueNette(null);
			plusMoinsValue.setMontantSoldeFinalEnCentimes(soldeFinPeriode);
			plusMoinsValue.setMontantFraisEnCentimes(0L);
			plusMoinsValue.setTauxFrais(null);
			
			return plusMoinsValue;
		}
		
		final Long nombreJoursPeriode = ChronoUnit.DAYS.between(dateDebutPeriode, dateFinPeriode) + 1L;

		List<Operation> operationsPeriode = operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(
				compteInterne, 
				dateDebutPeriode, 
				dateFinPeriode);
		
		// Recettes sur la période au prorata du nombre de jours entre la date de valeur de l'opération et
		// la fin de la période.
		// Si l'opération de recette a lieu le premier jour de la période, on considère que la durée du placement
		// sur la période est égale au nombre de jours de la période.
		// Si l'opération de recette a lieu le dernier jour de la période, on considère que la durée du placement
		// sur la période est de 1 jour.
		Long montantRecettesEnCentimes = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
				.filter((o) -> {return ! o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					Long nombreJoursOperation = ChronoUnit.DAYS.between(o.getDateValeur(), dateFinPeriode) + 1L;
					return o.getMontantEnCentimes() * nombreJoursOperation / nombreJoursPeriode;})
				.sum();
		
		// Dépenses sur la période au prorata du nombre de jours entre la date de valeur de l'opération et 
		// la fin de la période. 
		// Si l'opération de dépense a lieu le premier jour de la période, on considère que la durée du placement
		// sur la période est de 0 jour (la durée de "l'absence de placement" est égale au nombre de jours de la 
		// période).
		// Si l'opération de dépense a lieu le dernier jour de la période, on considère que la durée du placement
		// sur la période est égale au nombre de jours de la période - 1. (la durée de "l'absence de placement" 
		// est de 1 jour).
		Long montantDepensesEnCentimes = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return ! o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					Long nombreJoursOperation = ChronoUnit.DAYS.between(o.getDateValeur(), dateFinPeriode) + 1L;
					return o.getMontantEnCentimes() * nombreJoursOperation / nombreJoursPeriode;})
				.sum();
		
		// Quand l'ouverture du compte est située pendant la période étudiée, le solde initial du compte sera intégré 
		// aux recettes de la période
		boolean permierePeriodeDuCompte = compteInterne.getDateSoldeInitial().isAfter(dateDebutPeriode) 
				&& ! compteInterne.getDateSoldeInitial().isAfter(dateFinPeriode);
		if ( permierePeriodeDuCompte ) {
			montantRecettesEnCentimes += compteInterne.getMontantSoldeInitialEnCentimes();
		}
		
		// Le solde de début avec opérations est le montant du solde initial auquel on ajoute les recettes et dont 
		// on retranche les dépenses de la période, pondérées par la durée de l'investissement sur la période.  
		Long soldeDebutAvecOperations = soldeDebutPeriode + montantRecettesEnCentimes - montantDepensesEnCentimes;

		// Le montant de la plus value nette est calculée à partir des soldes de début et de fin  
		Long montantPlusMoinsValueNetteEnCentimes = soldeFinPeriode - soldeDebutAvecOperations;

		// Le taux de plus ou moins value nette calculée ne peut pas être calculé si le solde de début de période est à 0
		Double tauxPlusMoinsValueNette = null;
		if ( soldeDebutAvecOperations != 0 ) {
			tauxPlusMoinsValueNette = (double) (100.00 * montantPlusMoinsValueNetteEnCentimes / soldeDebutAvecOperations);
		}

		// Frais sur la période
		Long montantFraisEnCentimes = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		Double tauxFrais = null;
		if ( soldeFinPeriode != 0 ) {
			tauxFrais = (double) (100.00 * montantFraisEnCentimes / soldeFinPeriode);
		}
		
		PlusMoinsValueCompteInternePeriode plusMoinsValue = new PlusMoinsValueCompteInternePeriode();
		
		plusMoinsValue.setDateDebutPeriode(dateDebutPeriode);
		plusMoinsValue.setDateFinPeriode(dateFinPeriode);
		
		plusMoinsValue.setMontantSoldeInitialEnCentimes(soldeDebutPeriode);
		plusMoinsValue.setMontantOperationsEnCentimes(montantRecettesEnCentimes - montantDepensesEnCentimes);
		plusMoinsValue.setMontantPlusMoinsValueNetteEnCentimes(montantPlusMoinsValueNetteEnCentimes);
		plusMoinsValue.setTauxPlusMoinsValueNette(tauxPlusMoinsValueNette);
		plusMoinsValue.setMontantSoldeFinalEnCentimes(soldeFinPeriode);
		plusMoinsValue.setMontantFraisEnCentimes(montantFraisEnCentimes);
		plusMoinsValue.setTauxFrais(tauxFrais);
		
		return  plusMoinsValue;
	}
}
