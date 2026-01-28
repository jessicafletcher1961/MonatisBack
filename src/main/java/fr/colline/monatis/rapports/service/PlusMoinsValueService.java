package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.PlusMoinsValue;

@Service
public class PlusMoinsValueService {

	@Autowired private SoldeService soldeService;
	@Autowired private OperationService operationService;

	PlusMoinsValue rechercherPlusMoinsValue (
			CompteInterne compteInterne,
			LocalDate dateDebutEvaluation,
			LocalDate dateFinEvaluation) throws ServiceException {

		LocalDate dateDebutOperation = dateDebutEvaluation;
		if ( dateDebutEvaluation.isBefore(compteInterne.getDateSoldeInitial()) ) {
			dateDebutOperation = compteInterne.getDateSoldeInitial();
		}
		LocalDate dateFinOperation = dateFinEvaluation;
		if ( compteInterne.getDateCloture() != null && dateFinOperation.isAfter(compteInterne.getDateCloture() ) ) {
			dateFinOperation = compteInterne.getDateCloture();
		}
		
		if ( dateFinEvaluation.isBefore(dateDebutOperation) ) {
			// On est sur une période qui ptécède l'ouverture du compte
			PlusMoinsValue plusMoinsValue = new PlusMoinsValue();
			plusMoinsValue.setCompteInterne(compteInterne);
			plusMoinsValue.setDateDebutEvaluation(dateDebutEvaluation);
			plusMoinsValue.setDateFinEvaluation(dateFinEvaluation);
			plusMoinsValue.setMontantSoldeInitialEnCentimes(0L);
			plusMoinsValue.setMontantSoldeFinalEnCentimes(0L);
			plusMoinsValue.setMontantReelEnCentimes(0L);
			plusMoinsValue.setMontantTechniqueEnCentimes(0L);
			plusMoinsValue.setMontantPlusMoinsValueEnCentimes(0L);
			plusMoinsValue.setMontantPlusMoinsValueEnPourcentage(null);
			return plusMoinsValue;
		}

		Long montantSoldeInitialEnCentimes = soldeService.rechercherSolde(compteInterne, dateDebutEvaluation.minus(1, ChronoUnit.DAYS));
		Long montantSoldeFinalEnCentimes = soldeService.rechercherSolde(compteInterne, dateFinEvaluation);

		// Calcul de la sommes des opérations effectuées dans la période et réparties entre mouvements réels
		// et mouvements techniques 
		Long montantReelEnCentimes = 0L;
		Long montantTechniqueEnCentimes = 0L;
		List<Operation> operationsRecette = operationService.rechercherOperationsRecetteParCompteIdEntreDateDebutEtDateFin(compteInterne.getId(), dateDebutOperation, dateFinOperation);
		for ( Operation operation : operationsRecette ) {
			if ( operation.getTypeOperation() == TypeOperation.TECHNIQUE ) {
				montantTechniqueEnCentimes += operation.getMontantEnCentimes();
			}
			else {
				montantReelEnCentimes += operation.getMontantEnCentimes();
			}
		}
		List<Operation> operationsDepense = operationService.rechercherOperationsDepenseParCompteIdEntreDateDebutEtDateFin(compteInterne.getId(), dateDebutOperation, dateFinOperation);
		for ( Operation operation : operationsDepense ) {
			if ( operation.getTypeOperation() == TypeOperation.TECHNIQUE ) {
				montantTechniqueEnCentimes -= operation.getMontantEnCentimes();
			}
			else {
				montantReelEnCentimes -= operation.getMontantEnCentimes();
			}
		}

		Long montantPlusMoinsValueEnCentimes = montantSoldeFinalEnCentimes - (montantSoldeInitialEnCentimes + montantReelEnCentimes);
		
		Float montantPlusMoinsValueEnPourcentage = null;
		if ( montantSoldeInitialEnCentimes != 0 ) {
			montantPlusMoinsValueEnPourcentage = (float) ((montantPlusMoinsValueEnCentimes * 100.00) / montantSoldeInitialEnCentimes);
		}

		PlusMoinsValue plusMoinsValue = new PlusMoinsValue();
		plusMoinsValue.setCompteInterne(compteInterne);
		plusMoinsValue.setDateDebutEvaluation(dateDebutEvaluation);
		plusMoinsValue.setDateFinEvaluation(dateFinEvaluation);
		plusMoinsValue.setMontantSoldeInitialEnCentimes(montantSoldeInitialEnCentimes);
		plusMoinsValue.setMontantSoldeFinalEnCentimes(montantSoldeFinalEnCentimes);
		plusMoinsValue.setMontantReelEnCentimes(montantReelEnCentimes);
		plusMoinsValue.setMontantTechniqueEnCentimes(montantTechniqueEnCentimes);
		plusMoinsValue.setMontantPlusMoinsValueEnCentimes(montantPlusMoinsValueEnCentimes);
		plusMoinsValue.setMontantPlusMoinsValueEnPourcentage(montantPlusMoinsValueEnPourcentage);

		return  plusMoinsValue;
	}

}
