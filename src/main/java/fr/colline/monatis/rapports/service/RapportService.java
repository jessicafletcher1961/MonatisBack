package fr.colline.monatis.rapports.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.RapportCompteInterne;

@Service public class RapportService {

	@Autowired private OperationService operationService;
	
	public RapportCompteInterne calculerRapportCompteInterne(
			CompteInterne compteInterne, 
			ZonedDateTime dateDebut, 
			ZonedDateTime dateFin) throws ServiceException {

		RapportCompteInterne rapport = new RapportCompteInterne();
		
		rapport.setCompteInterne(compteInterne);
		rapport.setDateSoldeInitial(dateDebut);
		rapport.setDateSoldeFinal(dateFin);
		
		// Recalcul du solde initial en fonction de la date début
		Long montantSoldeInitialEnCentimes;
		if ( compteInterne.getDateSoldeInitial().isBefore(dateDebut) ) {
			RapportCompteInterne rapportAnterieur = calculerRapportCompteInterne(
					compteInterne, 
					compteInterne.getDateSoldeInitial(), 
					dateDebut);
			montantSoldeInitialEnCentimes = 
					compteInterne.getMontantSoldeInitialEnCentimes() 
					+ rapportAnterieur.getMontantTotalRecetteEnCentimes() 
					- rapportAnterieur.getMontantTotalDepenseEnCentimes();
		} else {
			montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		}
		rapport.setMontantSoldeInitialEnCentimes(montantSoldeInitialEnCentimes);		

		List<Operation> operationsDepense = operationService.rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
				compteInterne.getId(),
				dateDebut,
				dateFin);
		rapport.setOperationsDepense(operationsDepense);

		// Calcul du montant des dépenses entre date début et date fin
		Long montantTotalDepenseEnCentimes = 0L;
		for ( Operation operation : operationsDepense) {
			montantTotalDepenseEnCentimes += operation.getMontantTotalEnCentimes();
		}
		rapport.setMontantTotalDepenseEnCentimes(montantTotalDepenseEnCentimes);
		
		List<Operation> operationsRecette = operationService.rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
				compteInterne.getId(),
				dateDebut,
				dateFin);
		rapport.setOperationsRecette(operationsRecette);

		// Calcul du montant des recettes entre date début et date fin
		Long montantTotalRecetteEnCentimes = 0L;
		for ( Operation operation : operationsRecette) {
			montantTotalRecetteEnCentimes += operation.getMontantTotalEnCentimes();
		}
		rapport.setMontantTotalRecetteEnCentimes(montantTotalRecetteEnCentimes);

		Long montantSoldeFinalEnCentimes = 
				montantSoldeInitialEnCentimes
				+ montantTotalRecetteEnCentimes
				- montantTotalDepenseEnCentimes;
		rapport.setMontantSoldeFinalEnCentimes(montantSoldeFinalEnCentimes);

		Long montantDeltaEnCentimes = 
				montantSoldeFinalEnCentimes 
				- montantSoldeInitialEnCentimes;
		rapport.setMontantDeltaEnCentimes(montantDeltaEnCentimes);
		
		return rapport;
	}
}
