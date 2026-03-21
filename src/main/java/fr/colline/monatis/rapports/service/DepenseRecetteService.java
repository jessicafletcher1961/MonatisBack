package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategoriePeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteSousCategoriePeriode;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.utils.DateEtPeriodeUtils;
import fr.colline.monatis.utils.TypePeriode;

@Service
class DepenseRecetteService {

	@Autowired private OperationService operationService;
	
	DepenseRecetteCategorieLigne rechercherDepenseRecetteCategorieLigne(
			List<SousCategorie> sousCategories,
			Categorie categorie, 
			Beneficiaire beneficiaire,
			LocalDate dateDebutEtat, 
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);
		
		List<DepenseRecetteSousCategorieLigne> lignesSousCategorie = new ArrayList<DepenseRecetteSousCategorieLigne>();
		DepenseRecetteCategoriePeriode[] cumulsCategorie = new DepenseRecetteCategoriePeriode[nombrePeriodes];

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
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				DepenseRecetteSousCategoriePeriode sousCategoriePeriode = ligneSousCategorie.getPeriodes()[numeroPeriode];
				DepenseRecetteCategoriePeriode categoriePeriode;
				if ( cumulsCategorie[numeroPeriode] == null ) {
					categoriePeriode = new DepenseRecetteCategoriePeriode();
					
					categoriePeriode.setDateDebutPeriode(sousCategoriePeriode.getDateDebutPeriode());
					categoriePeriode.setDateFinPeriode(sousCategoriePeriode.getDateFinPeriode());
					categoriePeriode.setMontantRecetteEnCentimes(sousCategoriePeriode.getMontantRecetteEnCentimes());
					categoriePeriode.setMontantDepenseEnCentimes(sousCategoriePeriode.getMontantDepenseEnCentimes());
					categoriePeriode.setSoldeDepenseRecetteEnCentimes(sousCategoriePeriode.getSoldeDepenseRecetteEnCentimes());
					
					cumulsCategorie[numeroPeriode] = categoriePeriode;
				}
				else {
					categoriePeriode = cumulsCategorie[numeroPeriode];
					
					categoriePeriode.setMontantRecetteEnCentimes(categoriePeriode.getMontantRecetteEnCentimes() + sousCategoriePeriode.getMontantRecetteEnCentimes());
					categoriePeriode.setMontantDepenseEnCentimes(categoriePeriode.getMontantDepenseEnCentimes() + sousCategoriePeriode.getMontantDepenseEnCentimes());
					categoriePeriode.setSoldeDepenseRecetteEnCentimes(categoriePeriode.getSoldeDepenseRecetteEnCentimes() + sousCategoriePeriode.getSoldeDepenseRecetteEnCentimes());
				}
			}
		}

		DepenseRecetteCategorieLigne etat = new DepenseRecetteCategorieLigne();

		etat.setCategorie(categorie);
		etat.setLignesSousCategorie(lignesSousCategorie);

		if ( lignesSousCategorie.isEmpty() ) {
			
			// Aucune sousCategorie n'a été sélectionnée pour cette categorie, on doit créer les périodes "à vide"
			
			int numeroPeriode = 0;
			if ( typePeriode != null ) {
				LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
				while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
						LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
						
						DepenseRecetteCategoriePeriode cumulPeriode = new DepenseRecetteCategoriePeriode();

						cumulPeriode.setDateDebutPeriode(dateDebutPeriode);
						cumulPeriode.setDateFinPeriode(dateFinPeriode);
						cumulPeriode.setMontantRecetteEnCentimes(0L);
						cumulPeriode.setMontantDepenseEnCentimes(0L);
						cumulPeriode.setSoldeDepenseRecetteEnCentimes(0L);
						
						cumulsCategorie[numeroPeriode++] = cumulPeriode;

						dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
				}
			}
			else {
				DepenseRecetteCategoriePeriode cumulPeriode = new DepenseRecetteCategoriePeriode();
				
				cumulPeriode.setDateDebutPeriode(dateDebutEtat);
				cumulPeriode.setDateFinPeriode(dateFinEtat);
				cumulPeriode.setMontantRecetteEnCentimes(0L);
				cumulPeriode.setMontantDepenseEnCentimes(0L);
				cumulPeriode.setSoldeDepenseRecetteEnCentimes(0L);

				cumulsCategorie[0] = cumulPeriode;
			}
		}
			
		etat.setCumuls(cumulsCategorie);
		
		return etat;
	}

	DepenseRecetteSousCategorieLigne rechercherDepenseRecetteSousCategorieLigne(
			SousCategorie sousCategorie,
			Beneficiaire beneficiaire, 
			LocalDate dateDebutEtat, 
			LocalDate dateFinEtat, 
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		DepenseRecetteSousCategoriePeriode[] periodes = new DepenseRecetteSousCategoriePeriode[nombrePeriodes];

		int numeroPeriode = 0;
		if ( typePeriode != null ) {
			LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateDebutEtat);
			while ( ! dateDebutPeriode.isAfter(dateFinEtat) ) {
				LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
				
				DepenseRecetteSousCategoriePeriode periode = rechercherDepenseRecetteSousCategoriePeriode(
						sousCategorie, 
						beneficiaire,
						dateDebutPeriode, 
						dateFinPeriode);
				periodes[numeroPeriode++] = periode;
				
				dateDebutPeriode = DateEtPeriodeUtils.rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
			}
		}
		else {
			DepenseRecetteSousCategoriePeriode periode = rechercherDepenseRecetteSousCategoriePeriode(
					sousCategorie,
					beneficiaire,
					dateDebutEtat, 
					dateFinEtat);
			periodes[0] = periode;
		}

		DepenseRecetteSousCategorieLigne etat = new DepenseRecetteSousCategorieLigne();

		etat.setSousCategorie(sousCategorie);
		etat.setPeriodes(periodes);
		
		return etat;	
	}

	DepenseRecetteSousCategoriePeriode rechercherDepenseRecetteSousCategoriePeriode(
			SousCategorie sousCategorie, 
			Beneficiaire beneficiaire, 
			LocalDate dateDebutPeriode, 
			LocalDate dateFinPeriode) throws ServiceException {
		
		List<OperationLigne> operationsLignes = operationService.rechercherOperationsLignesParSousCategorieIdEntreDateDebutEtDateFin(
				sousCategorie.getId(),
				dateDebutPeriode,
				dateFinPeriode)
				.stream()
				.filter((ol) -> {return beneficiaire == null || ol.getBeneficiaires().contains(beneficiaire);})
				.filter((ol) -> {return ol.getOperation().getTypeOperation() == TypeOperation.DEPENSE || ol.getOperation().getTypeOperation() == TypeOperation.RECETTE;})
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
		
		DepenseRecetteSousCategoriePeriode etat = new DepenseRecetteSousCategoriePeriode();

		etat.setDateDebutPeriode(dateDebutPeriode);
		etat.setDateFinPeriode(dateFinPeriode);
		etat.setMontantRecetteEnCentimes(montantRecetteEnCentimes);
		etat.setMontantDepenseEnCentimes(montantDepenseEnCentimes);
		etat.setSoldeDepenseRecetteEnCentimes(soldeDepenseRecetteEnCentimes);

		return etat;
	}
	
}
