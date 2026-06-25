package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.SuiviBudgetPeriode;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
class DepenseRecetteService {

	@Autowired private OperationService operationService;
	@Autowired private SuiviBudgetService suiviBudgetService;
	
	List<DepenseRecetteCategorieLigne> rechercherDepenseRecetteCategorieLignes(
			List<SousCategorie> sousCategories,
			Beneficiaire beneficiaire,
			LocalDate dateDebutEtat, 
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		// On établit les catégories que l'état va contenir. Elles seront triées sur l'identifiant.
		TreeMap<String, Categorie> categories = new TreeMap<String, Categorie>();
		for ( SousCategorie sousCategorie : sousCategories ) {
			Categorie categorie = sousCategorie.getCategorie();
			categories.put(categorie.getNom(), categorie);
		}
		
		List<DepenseRecetteCategorieLigne> lignesCategorie = new ArrayList<DepenseRecetteCategorieLigne>();
		
		for ( Categorie categorie : categories.values() ) {
			
			List<DepenseRecetteSousCategorieLigne> lignesSousCategorie = new ArrayList<DepenseRecetteSousCategorieLigne>();
			DepenseRecettePeriode[] cumulsCategorie = initialiserPeriodes(dateDebutEtat, dateFinEtat, typePeriode);

			for ( SousCategorie sousCategorie : categorie.getSousCategories() ) {
				
				if ( sousCategories != null && ! sousCategories.isEmpty() && ! sousCategories.contains(sousCategorie) ) {
					continue;
				}
				
				DepenseRecetteSousCategorieLigne ligneSousCategorie = rechercherDepenseRecetteSousCategorieLigne(
						sousCategorie,
						beneficiaire,
						dateDebutEtat, 
						dateFinEtat,
						typePeriode);
				
				lignesSousCategorie.add(ligneSousCategorie);
				cumulsCategorie = cumulerPeriodes(cumulsCategorie, ligneSousCategorie.getCumulSousCategorie());
			}
			
			DepenseRecetteCategorieLigne ligneCategorie = new DepenseRecetteCategorieLigne();

			ligneCategorie.setCategorie(categorie);
			ligneCategorie.setLignesSousCategorie(lignesSousCategorie);
			ligneCategorie.setCumulCategorie(cumulsCategorie);

			lignesCategorie.add(ligneCategorie);
		}

		return lignesCategorie;
	}
	
	DepenseRecetteSousCategorieLigne rechercherDepenseRecetteSousCategorieLigne(
			SousCategorie sousCategorie,
			Beneficiaire beneficiaire, 
			LocalDate dateDebutEtat, 
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		DepenseRecettePeriode[] cumulsSousCategorie = initialiserPeriodes(dateDebutEtat, dateFinEtat, typePeriode);

		int numeroPeriode = 0;
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
			while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				
				DepenseRecettePeriode periode = rechercherDepenseRecetteSousCategoriePeriode(
						sousCategorie, 
						beneficiaire,
						dateDebutPeriode, 
						dateFinPeriode);
				cumulsSousCategorie[numeroPeriode++] = periode;
				
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
		}
		else {
			DepenseRecettePeriode periode = rechercherDepenseRecetteSousCategoriePeriode(
					sousCategorie,
					beneficiaire,
					dateDebutEtat, 
					dateFinEtat);
			cumulsSousCategorie[0] = periode;
		}

		DepenseRecetteSousCategorieLigne etat = new DepenseRecetteSousCategorieLigne();

		etat.setSousCategorie(sousCategorie);
		etat.setCumulSousCategorie(cumulsSousCategorie);
		
		return etat;	
	}
	
	DepenseRecettePeriode rechercherDepenseRecetteSousCategoriePeriode(
			SousCategorie sousCategorie, 
			Beneficiaire beneficiaire, 
			LocalDate dateDebutPeriode, 
			LocalDate dateFinPeriode) throws ServiceException {
		
		List<OperationLigne> operationsLignes = operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(
				sousCategorie.getId(),
				beneficiaire == null ? null : beneficiaire.getId(),
				dateDebutPeriode,
				dateFinPeriode)
				.toList();
		
		Long montantDepenseEnCentimes = operationsLignes
				.stream()
				.filter((ol) -> {return ol.getOperation().getTypeOperation() == TypeOperation.DEPENSE;})
				.mapToLong((ol) -> {return ol.getMontantEnCentimes();})
				.sum();
		Long montantRecetteEnCentimes = operationsLignes
				.stream()
				.filter((ol) -> {return ol.getOperation().getTypeOperation() == TypeOperation.RECETTE;})
				.mapToLong((ol) -> {return ol.getMontantEnCentimes();})
				.sum();
		
		Long soldeDepenseRecetteEnCentimes = montantRecetteEnCentimes - montantDepenseEnCentimes; 
		
		SuiviBudgetPeriode suiviBudget = suiviBudgetService.calculerSuiviBudget(sousCategorie.getId(), dateDebutPeriode, dateFinPeriode, soldeDepenseRecetteEnCentimes);
				
		DepenseRecettePeriode etat = new DepenseRecettePeriode();

		etat.setDateDebutPeriode(dateDebutPeriode);
		etat.setDateFinPeriode(dateFinPeriode);
		etat.setMontantRecetteEnCentimes(montantRecetteEnCentimes);
		etat.setMontantDepenseEnCentimes(montantDepenseEnCentimes);
		etat.setSoldeDepenseRecetteEnCentimes(soldeDepenseRecetteEnCentimes);
		etat.setSuiviBudget(suiviBudget);
		
		return etat;
	}
	
	DepenseRecettePeriode[] initialiserPeriodes(LocalDate dateDebutEtat, LocalDate dateFinEtat, TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);
		
		DepenseRecettePeriode[] periodes = new DepenseRecettePeriode[nombrePeriodes];
		
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				
				DepenseRecettePeriode periode = new DepenseRecettePeriode();
				periode.setDateDebutPeriode(dateDebutPeriode);
				periode.setDateFinPeriode(dateFinPeriode);
				periode.setMontantDepenseEnCentimes(0L);
				periode.setMontantRecetteEnCentimes(0L);
				periode.setSoldeDepenseRecetteEnCentimes(0L);
				periode.setSuiviBudget(null);
				periodes[numeroPeriode] = periode;
				
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
		}
		else {
			DepenseRecettePeriode periode = new DepenseRecettePeriode();
			periode.setDateDebutPeriode(dateDebutEtat);
			periode.setDateFinPeriode(dateFinEtat);
			periode.setMontantDepenseEnCentimes(0L);
			periode.setMontantRecetteEnCentimes(0L);
			periode.setSoldeDepenseRecetteEnCentimes(0L);
			periode.setSuiviBudget(null);
			periodes[0] = periode;
		}

		return periodes;
	}
	
	DepenseRecettePeriode[] cumulerPeriodes(DepenseRecettePeriode[] periodesCumulees, DepenseRecettePeriode[] periodesACumuler) {

		for ( int numeroPeriode = 0 ; numeroPeriode < periodesCumulees.length ; numeroPeriode++ ) {
			DepenseRecettePeriode periodeCumulee = periodesCumulees[numeroPeriode];
			DepenseRecettePeriode periodeACumuler = periodesACumuler[numeroPeriode];
			
			Long montantDepenseCumule = periodeCumulee.getMontantDepenseEnCentimes() + periodeACumuler.getMontantDepenseEnCentimes();
			Long montantRecetteCumule = periodeCumulee.getMontantRecetteEnCentimes() + periodeACumuler.getMontantRecetteEnCentimes();
			Long soldeDepenseRecetteCumule = periodeCumulee.getSoldeDepenseRecetteEnCentimes() + periodeACumuler.getSoldeDepenseRecetteEnCentimes();

			periodeCumulee.setMontantDepenseEnCentimes(montantDepenseCumule);
			periodeCumulee.setMontantRecetteEnCentimes(montantRecetteCumule);
			periodeCumulee.setSoldeDepenseRecetteEnCentimes(soldeDepenseRecetteCumule);
			periodeCumulee.setSuiviBudget(suiviBudgetService.cumulerSuiviBudget(periodeCumulee.getSuiviBudget(), periodeACumuler.getSuiviBudget()));
		}
		
		return periodesCumulees;
	}

}
