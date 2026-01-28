package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;

@Service
public class SoldeService {

	@Autowired private OperationService operationService;
	@Autowired private EvaluationService evaluationService;
	@Autowired private CompteTechniqueService compteTechniqueService;
	
	/**
	 * Recherche la somme de toutes les opération (recettes - dépense) pour n'importe quel type de compte jusqu'à
	 * la date de solde indiquée (opérations à cette date incluses).</br>
	 * S'il s'agit d'un compte interne, seules sont prises en compte les opération ultérieures à la date
	 * de solde initial (ou le solde enregistré par une évaluation), et le montant du solde initial (ou le solde enregistré 
	 * par une évaluation) est intégré.</br>
	 * S'il s'agit du'un compte interne et que la date de solde indiquée est antérieure à la date de solde initial, 
	 * le solde retourné est à 0.
	 * @param compte le compte
	 * @param dateSolde la date du solde recherché
	 * @return le solde du compte indiqué à la date indiquée
	 * @throws ServiceException
	 */
	Long rechercherSolde(
			Compte compte,
			LocalDate dateSolde) throws ServiceException {

		Long soldeInitial;
		Long montantTotalRecetteEnCentimes = 0L;
		Long montantTotalDepenseEnCentimes = 0L;

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {

			CompteInterne compteInterne = (CompteInterne) compte;

			if ( dateSolde.isBefore(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) ) {
				// On doit ignorer les éventuelles opérations antérieures à la veille de la date 
				// du solde initial ; le solde initial est réputé "apparaître" la veille de la date
				// de solde initial, au soir
				return 0L;
			}

			if ( dateSolde.equals(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) ) {
				// Le solde de la veille de la première journée du compte (date du solde initial) est le
				// montant de solde initial indiqué au niveau du compte
				return compteInterne.getMontantSoldeInitialEnCentimes();
			}

			// La date du solde recherchée est égale ou ultérieure à la date de solde initial

			LocalDate dateFinRecherche = dateSolde;
			if ( compteInterne.getDateCloture() != null && compteInterne.getDateCloture().isBefore(dateSolde) ) {
				dateFinRecherche = compteInterne.getDateCloture();
			}
			
			Evaluation derniereEvaluation = evaluationService.rechercherDerniereParCompteInterneIdJusqueDateCible(
					compteInterne.getId(), 
					dateFinRecherche);
			if ( derniereEvaluation != null && dateFinRecherche.equals(derniereEvaluation.getDateSolde()) ) {
				// Le solde au soir de la date recherchée déjà est enregistré dans une évaluation 
				return derniereEvaluation.getMontantSoldeEnCentimes();
			}

			LocalDate dateDebutRecherche;
			if ( derniereEvaluation == null || derniereEvaluation.getDateSolde().isBefore(compteInterne.getDateSoldeInitial()) ) {
				// On reprend toutes les opérations depuis la date de solde initial
				soldeInitial = compteInterne.getMontantSoldeInitialEnCentimes();
				dateDebutRecherche = compteInterne.getDateSoldeInitial();
			}
			else {
				// On reprend toutes les opérations à partir du lendemain du dernier solde enregistré
				soldeInitial = derniereEvaluation.getMontantSoldeEnCentimes();
				dateDebutRecherche = derniereEvaluation.getDateSolde().plus(1, ChronoUnit.DAYS);
			}

			// Calcul du montant des dépenses entre date de début déterminée ci dessus et date solde recherchée
			List<Operation> operationsDepense = operationService.rechercherOperationsDepenseParCompteIdEntreDateDebutEtDateFin(
					compteInterne.getId(),
					dateDebutRecherche,
					dateFinRecherche);
			for ( Operation operation : operationsDepense) {
				montantTotalDepenseEnCentimes += operation.getMontantEnCentimes();
			}

			// Calcul du montant des recettes entre date de début déterminée ci dessus et date solde recherchée
			List<Operation> operationsRecette = operationService.rechercherOperationsRecetteParCompteIdEntreDateDebutEtDateFin(
					compteInterne.getId(),
					dateDebutRecherche,
					dateFinRecherche);
			for ( Operation operation : operationsRecette) {
				montantTotalRecetteEnCentimes += operation.getMontantEnCentimes();
			}
		}
		else {

			soldeInitial = 0L;

			List<Operation> operationsDepense = operationService.rechercherOperationsDepenseParCompteIdJusqueDateFin(
					compte.getId(),
					dateSolde);
			for ( Operation operation : operationsDepense) {
				montantTotalDepenseEnCentimes += operation.getMontantEnCentimes();
			}

			List<Operation> operationsRecette = operationService.rechercherOperationsRecetteParCompteIdJusqueDateFin(
					compte.getId(),
					dateSolde);
			for ( Operation operation : operationsRecette) {
				montantTotalRecetteEnCentimes += operation.getMontantEnCentimes();
			}
		}

		return soldeInitial
				+ montantTotalRecetteEnCentimes
				- montantTotalDepenseEnCentimes;
	}

	Operation rechercherOperationVirtuelle(
			CompteInterne compteInterne,
			LocalDate dateDebut,
			LocalDate dateFin) throws ServiceException {

		CompteTechnique compteTechnique = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();
		
		LocalDate dateDebutRecherche = dateDebut;
		if ( dateDebut.isBefore(compteInterne.getDateSoldeInitial()) ) {
			dateDebutRecherche = compteInterne.getDateSoldeInitial();
		}
		LocalDate dateFinRecherche = dateFin;
		if ( compteInterne.getDateCloture() != null && compteInterne.getDateCloture().isBefore(dateFin) ) {
			dateFinRecherche = compteInterne.getDateCloture();
		}

		// On repart du dernier solde enregistré avant la date de début ou de la valeur du solde initial du compte
		Long soldeInitial = rechercherSolde(compteInterne, dateDebutRecherche.minus(1, ChronoUnit.DAYS));

		// Calcul du montant des dépenses réelles entre date de début déterminée ci dessus et date solde recherchée
		Long montantTotalDepenseEnCentimes = 0L;
		List<Operation> operationsDepense = operationService.rechercherOperationsDepenseParCompteIdEntreDateDebutEtDateFin(
				compteInterne.getId(),
				dateDebutRecherche,
				dateFinRecherche);
		for ( Operation operation : operationsDepense) {
			montantTotalDepenseEnCentimes += operation.getMontantEnCentimes();
		}

		// Calcul du montant des recettes réelles entre date de début déterminée ci dessus et date solde recherchée
		Long montantTotalRecetteEnCentimes = 0L;
		List<Operation> operationsRecette = operationService.rechercherOperationsRecetteParCompteIdEntreDateDebutEtDateFin(
				compteInterne.getId(),
				dateDebutRecherche,
				dateFinRecherche);
		for ( Operation operation : operationsRecette) {
			montantTotalRecetteEnCentimes += operation.getMontantEnCentimes();
		}

		Long soldeFinal = rechercherSolde(compteInterne, dateFinRecherche);

		Long montantOperationVirtuelle = soldeFinal - (soldeInitial + montantTotalRecetteEnCentimes - montantTotalDepenseEnCentimes);

		if ( montantOperationVirtuelle < 0) {
			// Generation d'une opération de diminution du solde
			return new Operation(
					null, 
					TypeOperation.VIRTUELLE_MOINS,
					"Constatation d'une différence négative entre le solde calculé et le solde évalué : montant de la différence ou constatation de moins-value selon le contexte",
					dateFinRecherche, 
					0 - montantOperationVirtuelle, 
					compteInterne, 
					compteTechnique,
					false,
					new OperationLigne(
							0,
							"Constatation d'une différence négative entre le solde calculé et le solde évalué : montant de la différence ou constatation de moins-value selon le contexte",
							dateFinRecherche,
							0 - montantOperationVirtuelle,
							null));
		}
		else if ( montantOperationVirtuelle == 0 ) {
			return new Operation(
					null, 
					TypeOperation.VIRTUELLE_ZERO,
					"Pas de différence entre le solde calculé et le solde évalué",
					dateFinRecherche, 
					0L, 
					compteTechnique,
					compteInterne, 
					false,
					new OperationLigne(
							0,
							"Pas de différence entre le solde calculé et le solde évalué",
							dateFinRecherche, 
							0L, 
							null));
		}
		else {
			// Generation d'une opération d'augmentation solde
			return new Operation(
					null, 
					TypeOperation.VIRTUELLE_PLUS,
					"Constatation d'une différence positive entre le solde calculé et le solde évalué : montant de la différence ou constatation de plus-value selon le contexte",
					dateFinRecherche, 
					montantOperationVirtuelle, 
					compteTechnique,
					compteInterne,
					false,
					new OperationLigne(
							0,
							"Constatation d'une différence positive entre le solde calculé et le solde évalué : montant de la différence ou constatation de plus-value selon le contexte",
							dateFinRecherche,
							montantOperationVirtuelle,
							null));
		}
	}
}
