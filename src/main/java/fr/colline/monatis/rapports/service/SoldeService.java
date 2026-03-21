package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;

@Service
class SoldeService {

	@Autowired private OperationService operationService;
	@Autowired private EvaluationService evaluationService;
	
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
		Long montantTotalRecetteEnCentimes;
		Long montantTotalDepenseEnCentimes;

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {

			CompteInterne compteInterne = (CompteInterne) compte;

			//
			// On élimine les cas pour lesquels la date de solde demandée est antérieure à la date de solde initial
			//
			
			if ( dateSolde.isBefore(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) ) {
				// On doit ignorer les éventuelles opérations antérieures à la veille de la date 
				// du solde initial ; le solde initial est réputé "apparaître" la veille de la date
				// de solde initial, au soir
				return 0L;
			}

			if ( dateSolde.equals(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) ) {
				// Le solde de la veille de la première journée du compte (date du solde initial - 1 jour) est le
				// montant de solde initial indiqué au niveau du compte
				return compteInterne.getMontantSoldeInitialEnCentimes();
			}

			//
			// A partir de maintenant, la date de solde recherchée est égale ou postérieure à la date de solde initial
			//
			
			// On recherche la plus récente évaluation du compte qui est après la date de solde initial et avant la date de solde recherchée.
			Evaluation evaluation = evaluationService.rechercherDerniereParCompteInterneEntreDateDebutEtDateFin(compteInterne, compteInterne.getDateSoldeInitial(), dateSolde);    
				
			// Si cette évaluation est trouvée, le solde initial est égal au montant de l'évaluation, et les opérations à prendre en compte 
			// sont celles situées entre le lendemain de la date de l'évaluation de la date de solde recherchée
			List<Operation> operations;
			if ( evaluation != null ) {
				soldeInitial = evaluation.getMontantSoldeEnCentimes();
				operations = operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compteInterne, evaluation.getDateSolde().plus(1, ChronoUnit.DAYS), dateSolde);
			}
			else {
				soldeInitial = compteInterne.getMontantSoldeInitialEnCentimes();
				operations = operationService.rechercherOperationsParCompteEntreDateDebutEtDateFin(compteInterne, compteInterne.getDateSoldeInitial(), dateSolde);
			}
			
			montantTotalDepenseEnCentimes = operations
					.stream()
					.filter((o) -> {return o.getCompteDepense().getId().equals(compteInterne.getId());})
					.mapToLong((o) -> {
						return o.getMontantEnCentimes();})
					.sum();
			
			montantTotalRecetteEnCentimes = operations
					.stream()
					.filter((o) -> {return o.getCompteRecette().getId().equals(compteInterne.getId());})
					.mapToLong((o) -> {
						return o.getMontantEnCentimes();})
					.sum();
				
		}
		else {

			soldeInitial = 0L;

			montantTotalDepenseEnCentimes = operationService.rechercherOperationsDepenseParCompteJusqueDateFin(compte, dateSolde)
					.stream()
					.mapToLong((o) -> {
						return o.getMontantEnCentimes();})
					.sum();

			montantTotalRecetteEnCentimes = operationService.rechercherOperationsRecetteParCompteJusqueDateFin(compte, dateSolde)
					.stream()
					.mapToLong((o) -> {
						return o.getMontantEnCentimes();})
					.sum();
		}

		return soldeInitial
				+ montantTotalRecetteEnCentimes
				- montantTotalDepenseEnCentimes;
	}
}
