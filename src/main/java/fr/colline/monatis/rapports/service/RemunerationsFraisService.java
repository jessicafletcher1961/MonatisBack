package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
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
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisCompteInterneLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisCompteInternePeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.utils.DateEtPeriodeUtils;
import fr.colline.monatis.utils.TypePeriode;

@Service
class RemunerationsFraisService {

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private OperationService operationService;

	RemunerationsFraisTypeFonctionnementLigne rechercherRemunerationsFraisTypeFonctionnementLigne(
			List<CompteInterne> comptesInternes, 
			TypeFonctionnement typeFonctionnement, 
			Titulaire titulaire,
			LocalDate dateDebutEtat, 
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);
		
		List<RemunerationsFraisCompteInterneLigne> lignesCompteInterne = new ArrayList<RemunerationsFraisCompteInterneLigne>();
		RemunerationsFraisTypeFonctionnementPeriode[] cumulsTypeFonctionnement = new RemunerationsFraisTypeFonctionnementPeriode[nombrePeriodes];

		for ( CompteInterne compteInterne : compteInterneService.rechercherParTypeFonctionnement(typeFonctionnement) ) {
			
			if ( titulaire != null && ! compteInterne.getTitulaires().contains(titulaire) ) {
				continue;
			}
			
			if ( comptesInternes != null && ! comptesInternes.isEmpty() && ! comptesInternes.contains(compteInterne) ) {
				continue;
			}
			
			RemunerationsFraisCompteInterneLigne ligneCompteInterne = rechercherRemunerationsFraisCompteInterneLigne(
					compteInterne,
					dateDebutEtat, 
					dateFinEtat,
					typePeriode);
			
			lignesCompteInterne.add(ligneCompteInterne);
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				RemunerationsFraisCompteInternePeriode comptePeriode = ligneCompteInterne.getPeriodes()[numeroPeriode];
				RemunerationsFraisTypeFonctionnementPeriode cumulPeriode;
				if ( cumulsTypeFonctionnement[numeroPeriode] == null ) {
					cumulPeriode = new RemunerationsFraisTypeFonctionnementPeriode();
					
					cumulPeriode.setDateDebutPeriode(comptePeriode.getDateDebutPeriode());
					cumulPeriode.setDateFinPeriode(comptePeriode.getDateFinPeriode());
					cumulPeriode.setMontantRemunerationsEnCentimes(comptePeriode.getMontantRemunerationsEnCentimes());
					cumulPeriode.setMontantFraisEnCentimes(comptePeriode.getMontantFraisEnCentimes());
					cumulPeriode.setSoldeRemunerationsFraisEnCentimes(comptePeriode.getSoldeRemunerationsFraisEnCentimes());
					
					cumulsTypeFonctionnement[numeroPeriode] = cumulPeriode;
				}
				else {
					cumulPeriode = cumulsTypeFonctionnement[numeroPeriode];
					cumulPeriode.setMontantRemunerationsEnCentimes(cumulPeriode.getMontantRemunerationsEnCentimes() + comptePeriode.getMontantRemunerationsEnCentimes());
					cumulPeriode.setMontantFraisEnCentimes(cumulPeriode.getMontantFraisEnCentimes() + comptePeriode.getMontantFraisEnCentimes());
					cumulPeriode.setSoldeRemunerationsFraisEnCentimes(cumulPeriode.getSoldeRemunerationsFraisEnCentimes() + comptePeriode.getSoldeRemunerationsFraisEnCentimes());
				}
			}
		}

		RemunerationsFraisTypeFonctionnementLigne etat = new RemunerationsFraisTypeFonctionnementLigne();

		etat.setTypeFonctionnement(typeFonctionnement);
		etat.setLignesCompteInterne(lignesCompteInterne);

		if ( lignesCompteInterne.isEmpty() ) {
			
			// Aucun compte n'a été sélectionné pour ce type de fonctionnement, on doit créer les périodes "à vide"
			
			int numeroPeriode = 0;
			if ( typePeriode != null ) {
				LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
				while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
						LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
						
						RemunerationsFraisTypeFonctionnementPeriode cumulPeriode = new RemunerationsFraisTypeFonctionnementPeriode();

						cumulPeriode.setDateDebutPeriode(dateDebutPeriode);
						cumulPeriode.setDateFinPeriode(dateFinPeriode);
						cumulPeriode.setMontantRemunerationsEnCentimes(0L);
						cumulPeriode.setMontantFraisEnCentimes(0L);
						cumulPeriode.setSoldeRemunerationsFraisEnCentimes(0L);
						
						cumulsTypeFonctionnement[numeroPeriode++] = cumulPeriode;

						dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
				}
			}
			else {
				RemunerationsFraisTypeFonctionnementPeriode cumulPeriode = new RemunerationsFraisTypeFonctionnementPeriode();
				
				cumulPeriode.setDateDebutPeriode(dateDebutEtat);
				cumulPeriode.setDateFinPeriode(dateFinEtat);
				cumulPeriode.setMontantRemunerationsEnCentimes(0L);
				cumulPeriode.setMontantFraisEnCentimes(0L);
				cumulPeriode.setSoldeRemunerationsFraisEnCentimes(0L);

				cumulsTypeFonctionnement[0] = cumulPeriode;
			}
		}
			
		etat.setCumuls(cumulsTypeFonctionnement);
		
		return etat;
	}
	
	RemunerationsFraisCompteInterneLigne rechercherRemunerationsFraisCompteInterneLigne(
			CompteInterne compteInterne,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		RemunerationsFraisCompteInternePeriode[] periodes = new RemunerationsFraisCompteInternePeriode[nombrePeriodes];

		int numeroPeriode = 0;
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
			while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				RemunerationsFraisCompteInternePeriode periode = rechercherRemunerationsFraisCompteInternePeriode(
						compteInterne, 
						dateDebutPeriode, 
						dateFinPeriode);
				periodes[numeroPeriode++] = periode;
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
		}
		else {
			RemunerationsFraisCompteInternePeriode compteInternePeriode = rechercherRemunerationsFraisCompteInternePeriode(
					compteInterne, 
					dateDebutEtat, 
					dateFinEtat);
			periodes[0] = compteInternePeriode;
		}

		RemunerationsFraisCompteInterneLigne etat = new RemunerationsFraisCompteInterneLigne();

		etat.setCompteInterne(compteInterne);
		etat.setPeriodes(periodes);
		
		return etat;	
	}

	RemunerationsFraisCompteInternePeriode rechercherRemunerationsFraisCompteInternePeriode(
			CompteInterne compteInterne,
			LocalDate dateDebutPeriode,
			LocalDate dateFinPeriode) throws ServiceException {
		
		List<Operation> operationsPeriode = operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compteInterne, dateDebutPeriode, dateFinPeriode);
		
		Long montantRemunerations;
		montantRemunerations = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		Long montantFrais;
		montantFrais = operationsPeriode
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
				.filter((o) -> {return o.getTypeOperation().isFluxTechnique();})
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();
		
		Long montantSolde = montantRemunerations - montantFrais;
				
		RemunerationsFraisCompteInternePeriode etat = new RemunerationsFraisCompteInternePeriode();
		
		etat.setDateDebutPeriode(dateDebutPeriode);
		etat.setDateFinPeriode(dateFinPeriode);
		
		etat.setMontantRemunerationsEnCentimes(montantRemunerations);
		etat.setMontantFraisEnCentimes(montantFrais);
		etat.setSoldeRemunerationsFraisEnCentimes(montantSolde);
		
		return etat;	
	}
}
