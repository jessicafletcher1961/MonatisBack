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
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.utils.DateEtPeriodeUtils;
import fr.colline.monatis.utils.TypePeriode;

@Service
class BilanPatrimoineService {

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private SoldeService soldeService;
	@Autowired private OperationService operationService;
	
	BilanPatrimoineTypeFonctionnementLigne rechercherBilanPatrimoineTypeFonctionnementLigne(
			List<CompteInterne> comptesInternes,
			TypeFonctionnement typeFonctionnement,
			Titulaire titulaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);
		
		List<BilanPatrimoineCompteInterneLigne> lignesCompteInterne = new ArrayList<BilanPatrimoineCompteInterneLigne>();
		Long montantSoldeInitialEnCentimes = 0L;
		BilanPatrimoineTypeFonctionnementPeriode[] cumulsTypeFonctionnement = new BilanPatrimoineTypeFonctionnementPeriode[nombrePeriodes];

		for ( CompteInterne compteInterne : compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement) ) {
			
			if ( titulaire != null && ! compteInterne.getTitulaires().contains(titulaire) ) {
				continue;
			}
			
			if ( comptesInternes != null && ! comptesInternes.isEmpty() && ! comptesInternes.contains(compteInterne) ) {
				continue;
			}
			
			BilanPatrimoineCompteInterneLigne ligneCompteInterne = rechercherBilanPatrimoineCompteInterneLigne(
					compteInterne,
					dateDebutEtat, 
					dateFinEtat,
					typePeriode);
			
			lignesCompteInterne.add(ligneCompteInterne);
			montantSoldeInitialEnCentimes += ligneCompteInterne.getMontantSoldeInitialEnCentimes();
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				BilanPatrimoineCompteInternePeriode comptePeriode = ligneCompteInterne.getPeriodes()[numeroPeriode];
				BilanPatrimoineTypeFonctionnementPeriode cumulPeriode;
				if ( cumulsTypeFonctionnement[numeroPeriode] == null ) {
					cumulPeriode = new BilanPatrimoineTypeFonctionnementPeriode();
					
					cumulPeriode.setDateDebutPeriode(comptePeriode.getDateDebutPeriode());
					cumulPeriode.setDateFinPeriode(comptePeriode.getDateFinPeriode());
					cumulPeriode.setMontantSoldeFinalEnCentimes(comptePeriode.getMontantSoldeFinalEnCentimes());
					cumulPeriode.setMontantTotalRecetteEnCentimes(comptePeriode.getMontantTotalRecetteEnCentimes());
					cumulPeriode.setMontantTotalDepenseEnCentimes(comptePeriode.getMontantTotalDepenseEnCentimes());
					cumulPeriode.setSoldeTotalTechniqueEnCentimes(comptePeriode.getSoldeTotalTechniqueEnCentimes());
					cumulPeriode.setMontantEcartNonJustifieEnCentimes(comptePeriode.getMontantEcartNonJustifieEnCentimes());
					
					cumulsTypeFonctionnement[numeroPeriode] = cumulPeriode;
				}
				else {
					cumulPeriode = cumulsTypeFonctionnement[numeroPeriode];
					cumulPeriode.setMontantSoldeFinalEnCentimes(cumulPeriode.getMontantSoldeFinalEnCentimes() + comptePeriode.getMontantSoldeFinalEnCentimes());
					cumulPeriode.setMontantTotalRecetteEnCentimes(cumulPeriode.getMontantTotalRecetteEnCentimes() + comptePeriode.getMontantTotalRecetteEnCentimes());
					cumulPeriode.setMontantTotalDepenseEnCentimes(cumulPeriode.getMontantTotalDepenseEnCentimes() + comptePeriode.getMontantTotalDepenseEnCentimes());
					cumulPeriode.setSoldeTotalTechniqueEnCentimes(cumulPeriode.getSoldeTotalTechniqueEnCentimes() + comptePeriode.getSoldeTotalTechniqueEnCentimes());
					cumulPeriode.setMontantEcartNonJustifieEnCentimes(cumulPeriode.getMontantEcartNonJustifieEnCentimes() + comptePeriode.getMontantEcartNonJustifieEnCentimes());
				}
			}
		}

		BilanPatrimoineTypeFonctionnementLigne etat = new BilanPatrimoineTypeFonctionnementLigne();

		etat.setTypeFonctionnement(typeFonctionnement);
		etat.setLignesCompteInterne(lignesCompteInterne);
		etat.setMontantSoldeInitialEnCentimes(montantSoldeInitialEnCentimes);

		if ( lignesCompteInterne.isEmpty() ) {
			
			// Aucun compte n'a été sélectionné pour ce type de fonctionnement, on doit créer les périodes "à vide"
			
			int numeroPeriode = 0;
			if ( typePeriode != null ) {
				LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
				while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
						LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
						
						BilanPatrimoineTypeFonctionnementPeriode cumulPeriode = new BilanPatrimoineTypeFonctionnementPeriode();

						cumulPeriode.setDateDebutPeriode(dateDebutPeriode);
						cumulPeriode.setDateFinPeriode(dateFinPeriode);
						cumulPeriode.setMontantSoldeFinalEnCentimes(0L);
						cumulPeriode.setMontantTotalRecetteEnCentimes(0L);
						cumulPeriode.setMontantTotalDepenseEnCentimes(0L);
						cumulPeriode.setSoldeTotalTechniqueEnCentimes(0L);
						cumulPeriode.setMontantEcartNonJustifieEnCentimes(0L);
						
						cumulsTypeFonctionnement[numeroPeriode++] = cumulPeriode;

						dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
				}
			}
			else {
				BilanPatrimoineTypeFonctionnementPeriode cumulPeriode = new BilanPatrimoineTypeFonctionnementPeriode();
				
				cumulPeriode.setDateDebutPeriode(dateDebutEtat);
				cumulPeriode.setDateFinPeriode(dateFinEtat);
				cumulPeriode.setMontantSoldeFinalEnCentimes(0L);
				cumulPeriode.setMontantTotalRecetteEnCentimes(0L);
				cumulPeriode.setMontantTotalDepenseEnCentimes(0L);
				cumulPeriode.setSoldeTotalTechniqueEnCentimes(0L);
				cumulPeriode.setMontantEcartNonJustifieEnCentimes(0L);

				cumulsTypeFonctionnement[0] = cumulPeriode;
			}
		}
			
		etat.setCumulsPeriodes(cumulsTypeFonctionnement);
		
		return etat;
	}

	BilanPatrimoineCompteInterneLigne rechercherBilanPatrimoineCompteInterneLigne(
			CompteInterne compteInterne,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		BilanPatrimoineCompteInternePeriode[] periodes = new BilanPatrimoineCompteInternePeriode[nombrePeriodes];

		int numeroPeriode = 0;
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
			while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				BilanPatrimoineCompteInternePeriode periode = rechercherBilanPatrimoineCompteInternePeriode(
						compteInterne, 
						dateDebutPeriode, 
						dateFinPeriode);
				periodes[numeroPeriode++] = periode;
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
		}
		else {
			BilanPatrimoineCompteInternePeriode compteInternePeriode = rechercherBilanPatrimoineCompteInternePeriode(
					compteInterne, 
					dateDebutEtat, 
					dateFinEtat);
			periodes[0] = compteInternePeriode;
		}

		BilanPatrimoineCompteInterneLigne etat = new BilanPatrimoineCompteInterneLigne();

		etat.setCompteInterne(compteInterne);
		etat.setMontantSoldeInitialEnCentimes(periodes[0].getMontantSoldeInitialEnCentimes());
		etat.setPeriodes(periodes);
		
		return etat;
	}

	BilanPatrimoineCompteInternePeriode rechercherBilanPatrimoineCompteInternePeriode(
			CompteInterne compteInterne,
			LocalDate dateDebutPeriode,
			LocalDate dateFinPeriode) throws ServiceException {
		
		List<Operation> operationsPeriode = operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compteInterne, dateDebutPeriode, dateFinPeriode);
		
		Long montantTotalRecette;
		montantTotalRecette = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTransaction();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		Long montantTotalDepense;
		montantTotalDepense = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTransaction();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		Long soldeTotalTechnique = 0L;
		soldeTotalTechnique = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		soldeTotalTechnique -= operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		Long soldeDebutPeriode = soldeService.rechercherSolde(compteInterne, dateDebutPeriode.minus(1,ChronoUnit.DAYS));
		Long soldeFinPeriode = soldeService.rechercherSolde(compteInterne, dateFinPeriode);
		Long montantEcartNonJustifie = soldeFinPeriode - (soldeDebutPeriode + montantTotalRecette - montantTotalDepense + soldeTotalTechnique);
		
		BilanPatrimoineCompteInternePeriode etat = new BilanPatrimoineCompteInternePeriode();
		
		etat.setDateDebutPeriode(dateDebutPeriode);
		etat.setDateFinPeriode(dateFinPeriode);
		
		etat.setMontantSoldeInitialEnCentimes(soldeDebutPeriode);
		
		etat.setMontantSoldeFinalEnCentimes(soldeFinPeriode);
		etat.setMontantTotalRecetteEnCentimes(montantTotalRecette);
		etat.setMontantTotalDepenseEnCentimes(montantTotalDepense);
		etat.setSoldeTotalTechniqueEnCentimes(soldeTotalTechnique);
		etat.setMontantEcartNonJustifieEnCentimes(montantEcartNonJustifie);
		
		return etat;
	}

}
