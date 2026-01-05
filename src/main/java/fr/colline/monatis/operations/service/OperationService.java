package fr.colline.monatis.operations.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.erreurs.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationFonctionnelleErreur;
import fr.colline.monatis.operations.OperationTechniqueErreur;
import fr.colline.monatis.operations.model.Evaluation;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.repository.OperationLigneRepository;
import fr.colline.monatis.operations.repository.OperationRepository;

@Service
public class OperationService {

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteExterneService compteExterneService;
	@Autowired private CompteTechniqueService compteTechniqueService;
	@Autowired private EvaluationService evaluationService;

	@Autowired private OperationRepository operationRepository;
	@Autowired private OperationLigneRepository operationLigneRepository;

	public Operation rechercherParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la recherche d'une opération est obligatoire");

		try {
			Optional<Operation> optional = operationRepository.findById(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					OperationTechniqueErreur.RECHERCHE_PAR_ID,
					id );
		}
	}

	public boolean isExistantParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la vérification de l'existence d'une opération est obligatoire");

		try {
			return operationRepository.existsById(id);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					OperationTechniqueErreur.EXISTENCE_PAR_ID,
					id);
		}
	}

	public Operation rechercherParNumero(String numero) throws ServiceException {

		Assert.notNull(numero, () -> "Le NUMERO pour la recherche d'une opération est obligatoire");

		try {
			Optional<Operation> optional = operationRepository.findByNumero(numero);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					OperationTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL,
					numero);
		}
	}

	public boolean isExistantParNumero(String numero) throws ServiceException {

		Assert.notNull(numero, () -> "Le NUMERO pour la vérification de l'existence d'une opération est obligatoire");

		try {
			return operationRepository.existsByNumero(numero);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					OperationTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL,
					Operation.class.getSimpleName(),
					numero);
		}
	}

	public List<Operation> rechercherTous() throws ServiceException {

		try {
			return operationRepository.findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public List<Operation> rechercherTous(Sort tri) throws ServiceException {

		Assert.notNull(tri, () -> "Le TRI pour la recherche de toutes les opérations est obligatoire");

		try {
			return operationRepository.findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public void supprimerTous() throws ServiceException {

		try {
			operationRepository.deleteAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.SUPPRESSION_TOUS);
		}
	}

	public List<Compte> rechercherComptesCompatiblesEnDepense(TypeOperation typeOperation) throws ServiceException {

		List<Compte> resultat = new ArrayList<>();

		switch(typeOperation) {

		case TRANSFERT:
		case DEPOT:
		case ACHAT:
		case DEPENSE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT));
			break;
		case RETRAIT:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.EPARGNE));
			break;
		case VENTE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.PATRIMOINE));
			break;
		case RECETTE:
			resultat.addAll(compteExterneService.rechercherTous());
			break;
		case GAIN:
			resultat.addAll(compteTechniqueService.rechercherTous());
			break;
		case FRAIS:
			resultat.addAll(compteInterneService.rechercherTous());
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}

		Collections.sort(resultat, (o1, o2) -> {
			return o1.getIdentifiant().compareTo(o2.getIdentifiant());
		});

		return resultat;
	}

	public Set<Compte> rechercherComptesCompatiblesEnRecette(TypeOperation typeOperation) throws ServiceException {

		Set<Compte> resultat = new HashSet<>();

		switch(typeOperation) {

		case TRANSFERT:
		case RETRAIT:
		case VENTE:
		case RECETTE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT));
			break;
		case DEPOT:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.EPARGNE));
			break;
		case ACHAT:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.PATRIMOINE));
			break;
		case DEPENSE:
			resultat.addAll(compteExterneService.rechercherTous());
			break;
		case FRAIS:
			resultat.addAll(compteTechniqueService.rechercherTous());
			break;
		case GAIN:
			resultat.addAll(compteInterneService.rechercherTous());
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}

		return resultat;
	}

	/**
	 * Recherche des opération de dépense du compte indiqué entre date début (incluse)
	 * et date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
			Long compteId,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdAndDateValeurBetweenOrderByDateValeur(compteId, dateDebut, dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteId,
					dateDebut,
					dateFin);
		}
	}

	/**
	 * Recherche des opération de dépense du compte indiqué jusqu'à la date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationDepenseParCompteIdJusqueDateFin(Long compteId, LocalDate dateFin) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdAndDateValeurLessThanEqualOrderByDateValeur(
					compteId, 
					dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_JUSQUE_DATE_FIN,
					compteId,
					dateFin);
		}
	}

	/**
	 * Recherche des opération de dépense du compte indiqué depuis la date de début (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @return
	 */
	public List<Operation> rechercherOperationDepenseParCompteIdDepuisDateDebut(Long compteId, LocalDate dateDebut) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdAndDateValeurGreaterThanEqualOrderByDateValeur(
					compteId, 
					dateDebut);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_DEPUIS_DATE_DEBUT,
					compteId,
					dateDebut);
		}
	}

	/**
	 * Recherche des opération de recette du compte indiqué entre date début (incluse)
	 * et date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
			Long compteId,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {
		
		try {
			return operationRepository.findByCompteRecetteIdAndDateValeurBetweenOrderByDateValeur(compteId, dateDebut, dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteId,
					dateDebut,
					dateFin);
		}
	}

	/**
	 * Recherche des opération de recette du compte indiqué jusqu'à la date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationRecetteParCompteIdJusqueDateFin(Long compteId, LocalDate dateFin) throws ServiceException {

		try {
			return operationRepository.findByCompteRecetteIdAndDateValeurLessThanEqualOrderByDateValeur(
					compteId, 
					dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_AVANT_DATE_FIN,
					compteId,
					dateFin);
		}

	}

	/**
	 * Recherche des opération de recette du compte indiqué depuis la date de début (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @return
	 */
	public List<Operation> rechercherOperationRecetteParCompteIdDepuisDateDebut(Long compteId, LocalDate dateDebut) throws ServiceException {

		try {
			return operationRepository.findByCompteRecetteIdAndDateValeurGreaterThanEqualOrderByDateValeur(
					compteId, 
					dateDebut);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_DEPUIS_DATE_DEBUT,
					compteId,
					dateDebut);
		}
	}

	/**
	 * Recherche de toutes les opération du compte indiqué.</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId1
	 * @param compteId2
	 * @return
	 */
	public List<Operation> rechercherOperationParCompteId(Long compteId) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdOrCompteRecetteIdOrderByDateValeur(
					compteId, 
					compteId);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_COMPTE_ID,
					compteId);
		}
	}
	
	/**
	 * Recherche de toutes les opérations du compte indiqué entre la date de début (incluse)
	 * et date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 * @throws ServiceException 
	 */
	public List<Operation> rechercherOperationParCompteIdEntreDateDebutEtDateFin(Long compteId, LocalDate dateDebut,LocalDate dateFin) throws ServiceException {
		
		try {
			return operationRepository.findByCompteDepenseIdOrCompteRecetteIdAndDateValeurBetweenOrderByDateValeur(
					compteId, 
					compteId,
					dateDebut,
					dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteId,
					dateDebut,
					dateFin);
		}
	}

	/**
	 * Recherche de toutes les opération du compte indiqué jusqu'à la date de fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationParCompteIdJusqueDateFin(Long compteId, LocalDate dateFin) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdOrCompteRecetteIdAndDateValeurLessThanEqualOrderByDateValeur(
					compteId, 
					compteId, 
					dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_COMPTE_ID_JUSQUE_DATE_FIN,
					compteId,
					dateFin);
		}
	}

	/**
	 * Recherche de toutes les opération du compte indiqué depuis la date de début (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @return
	 */
	public List<Operation> rechercherOperationParCompteIdDepuisDateDebut(Long compteId, LocalDate dateDebut) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdOrCompteRecetteIdAndDateValeurGreaterThanEqualOrderByDateValeur(
					compteId, 
					compteId, 
					dateDebut);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_COMPTE_ID_DEPUIS_DATE_DEBUT,
					compteId,
					dateDebut);
		}
	}

	public List<OperationLigne> rechercherOperationsLignesParSousCategorieIdEntreDateDebutEtDateFin(Long sousCategorieId, LocalDate dateDebut, LocalDate dateFin) throws ServiceException {

		try {
			return operationLigneRepository.findBySousCategorieIdAndDateComptabilisationBetweenOrderByDateComptabilisation(
					sousCategorieId,
					dateDebut,
					dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_LIGNE_PAR_REFERENCE_ID_JUSQUE_DATE_FIN,
					sousCategorieId,
					dateFin);
		}
	}

	/**
	 * Recherche la somme de toutes les opération (recettes - dépense) pour n'importe quel type de compte jusqu'à
	 * la date de solde indiquée (opérations à cette date incluses).</br>
	 * S'il s'agit d'un compte interne, seules sont prises en compte les opération ultérieures à la date
	 * de solde initial (ou le solde enregistré par une évaluation), et le montant du solde initial (ou le solde enregistré par une évaluation) est intégré.</br>
	 * S'il s'agit du'un compte interne et que la date de solde indiquée est antérieure à la date de solde initial, 
	 * le solde retourné est à 0.
	 * @param compte le compte
	 * @param dateSolde la date du solde recherché
	 * @return le solde du compte indiqué à la date indiquée
	 * @throws ServiceException
	 */
	public Long rechercherSolde(
			Compte compte,
			LocalDate dateSolde) throws ServiceException {

		Long soldeInitial;
		Long montantTotalRecetteEnCentimes = 0L;
		Long montantTotalDepenseEnCentimes = 0L;

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {

			CompteInterne compteInterne = (CompteInterne) compte;

			if ( dateSolde.isBefore(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) ) {
				// On doit ignorer les éventuelles opérations antérieures à la date 
				// du solde initial
				return compteInterne.getMontantSoldeInitialEnCentimes();
			}

			if ( dateSolde.equals(compteInterne.getDateSoldeInitial().minus(1, ChronoUnit.DAYS)) ) {
				// Le solde de la veille de la première journée du compte (date du solde initial) est le
				// montant de solde initial indiqué au niveau du compte
				return compteInterne.getMontantSoldeInitialEnCentimes();
			}

			// La date du solde rechéerchée est égale ou ultérieure à la date de première journée du compte
			
			Evaluation derniereEvaluation = evaluationService.rechercherDerniereParCompteInterneIdJusqueDateCible(compteInterne.getId(), dateSolde);
			if ( derniereEvaluation != null && dateSolde.equals(derniereEvaluation.getDateSolde()) ) {
				// Le solde au soir de la date recherchée déjà est enregistré dans une évaluation 
				return derniereEvaluation.getMontantSoldeEnCentimes();
			}

			LocalDate dateDebut;
			if ( derniereEvaluation == null || derniereEvaluation.getDateSolde().isBefore(compteInterne.getDateSoldeInitial()) ) {
				// On reprend toutes les opérations depuis la date de solde initial
				soldeInitial = compteInterne.getMontantSoldeInitialEnCentimes();
				dateDebut = compteInterne.getDateSoldeInitial();
			}
			else {
				// On reprend toutes les opérations à partir du lendemain du dernier solde enregistré
				soldeInitial = derniereEvaluation.getMontantSoldeEnCentimes();
				dateDebut = derniereEvaluation.getDateSolde().plus(1, ChronoUnit.DAYS);
			}

			// Calcul du montant des dépenses entre date de début déterminée ci dessus et date solde recherchée
			List<Operation> operationsDepense = rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
					compteInterne.getId(),
					dateDebut,
					dateSolde);
			ajouterOperationVirtuellesDepenseEntreDateDebutEtDateFin(
					operationsDepense, 
					compteInterne.getId(), 
					dateDebut, 
					dateSolde);
			for ( Operation operation : operationsDepense) {
				montantTotalDepenseEnCentimes += operation.getMontantEnCentimes();
			}

			// Calcul du montant des recettes entre date de début déterminée ci dessus et date solde recherchée
			List<Operation> operationsRecette = rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
					compteInterne.getId(),
					dateDebut,
					dateSolde);
			ajouterOperationVirtuelleRecetteEntreDateDebutEtDateFin(
					operationsRecette, 
					compteInterne.getId(), 
					dateDebut, 
					dateSolde);
			for ( Operation operation : operationsRecette) {
				montantTotalRecetteEnCentimes += operation.getMontantEnCentimes();
			}
		}
		else {

			soldeInitial = 0L;

			List<Operation> operationsDepense = rechercherOperationDepenseParCompteIdJusqueDateFin(
					compte.getId(),
					dateSolde);
			for ( Operation operation : operationsDepense) {
				montantTotalDepenseEnCentimes += operation.getMontantEnCentimes();
			}

			List<Operation> operationsRecette = rechercherOperationRecetteParCompteIdJusqueDateFin(
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

	public void ajouterOperationVirtuellesDepenseEntreDateDebutEtDateFin(
			List<Operation> operations,
			Long compteId, 
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		List<Evaluation> evaluations = evaluationService.rechercherParCompteInterneIdEntreDateDebutEtDateFin(compteId, dateDebut, dateFin);
		
		if ( ! evaluations.isEmpty() ) {

			for ( Evaluation evaluation : evaluations ) {

				Long montantSoldeAvantEvaluationEnCentimes = rechercherSolde(evaluation.getCompteInterne(), evaluation.getDateSolde().minus(1, ChronoUnit.DAYS));
				
				Long montantOperationsReellesDepenseEnCentimes = 0L;
				List<Operation> operationsReellesDepense = rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
						compteId, 
						evaluation.getDateSolde(), 
						evaluation.getDateSolde());
				for ( Operation operation : operationsReellesDepense ) {
					montantOperationsReellesDepenseEnCentimes += operation.getMontantEnCentimes();
				}
				Long montantOperationsReellesRecetteEnCentimes = 0L;
				List<Operation> operationsReellesRecette = rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
						compteId, 
						evaluation.getDateSolde(), 
						evaluation.getDateSolde());
				for ( Operation operation : operationsReellesRecette ) {
					montantOperationsReellesRecetteEnCentimes += operation.getMontantEnCentimes();
				}
				
				Long monantOperationsReellesDuJour = montantOperationsReellesRecetteEnCentimes - montantOperationsReellesDepenseEnCentimes;
				
				Long montantOperationVirtuelle = evaluation.getMontantSoldeEnCentimes() - (montantSoldeAvantEvaluationEnCentimes + monantOperationsReellesDuJour);
				
				if ( montantOperationVirtuelle < 0) {
					// Generation d'uns opération de moins solde
					operations.add(new Operation(
							"VIRTUEL", 
							TypeOperation.MOINS_SOLDE,
							"Operation d'ajustement virtuelle (diminution)",
							evaluation.getDateSolde(), 
							0 - montantOperationVirtuelle, 
							evaluation.getCompteTechnique(),
							evaluation.getCompteInterne(), 
							new OperationLigne(
									0,
									"Operation d'ajustement virtuelle (diminution)",
									evaluation.getDateSolde(),
									0 - montantOperationVirtuelle,
									null)));
				}
			}
			Collections.sort(operations, (o1, o2) -> {return o1.getDateValeur().compareTo(o2.getDateValeur());});
		}
	}

	public void ajouterOperationVirtuelleRecetteEntreDateDebutEtDateFin(
			List<Operation> operations,
			Long compteId, 
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		List<Evaluation> evaluations = evaluationService.rechercherParCompteInterneIdEntreDateDebutEtDateFin(compteId, dateDebut, dateFin);
		
		if ( ! evaluations.isEmpty() ) {

			for ( Evaluation evaluation : evaluations ) {

				Long montantSoldeAvantEvaluationEnCentimes = rechercherSolde(evaluation.getCompteInterne(), evaluation.getDateSolde().minus(1, ChronoUnit.DAYS));
				
				Long montantOperationsReellesDepenseEnCentimes = 0L;
				List<Operation> operationsReellesDepense = rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
						compteId, 
						evaluation.getDateSolde(), 
						evaluation.getDateSolde());
				for ( Operation operation : operationsReellesDepense ) {
					montantOperationsReellesDepenseEnCentimes += operation.getMontantEnCentimes();
				}
				Long montantOperationsReellesRecetteEnCentimes = 0L;
				List<Operation> operationsReellesRecette = rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
						compteId, 
						evaluation.getDateSolde(), 
						evaluation.getDateSolde());
				for ( Operation operation : operationsReellesRecette ) {
					montantOperationsReellesRecetteEnCentimes += operation.getMontantEnCentimes();
				}
				Long monantOperationsReellesDuJour = montantOperationsReellesRecetteEnCentimes - montantOperationsReellesDepenseEnCentimes;
				
				Long montantOperationVirtuelle = evaluation.getMontantSoldeEnCentimes() - (montantSoldeAvantEvaluationEnCentimes + monantOperationsReellesDuJour);
				
				if ( montantOperationVirtuelle > 0) {
					// Generation d'uns opération de plus solde
					operations.add(new Operation(
							"VIRTUEL", 
							TypeOperation.PLUS_SOLDE,
							"Operation d'ajustement virtuelle (augmentation)",
							evaluation.getDateSolde(), 
							montantOperationVirtuelle, 
							evaluation.getCompteTechnique(),
							evaluation.getCompteInterne(), 
							new OperationLigne(
									0,
									"Operation d'ajustement virtuelle (augmentation)",
									evaluation.getDateSolde(),
									montantOperationVirtuelle,
									null)));
				}
			}
			Collections.sort(operations, (o1, o2) -> {return o1.getDateValeur().compareTo(o2.getDateValeur());});
		}
	}

	public Operation creerOperation(Operation operation) throws ServiceException {

		Assert.notNull(operation, () -> "L'OPERATION à créer est obligatoire");

		operation = controlerEtPreparerPourCreation(operation);

		return enregistrer(operation);
	}

	public Operation modifierOperation(Operation operation) throws ServiceException {

		Assert.notNull(operation, () -> "L'OPERATION à modifier est obligatoire");

		operation = controlerEtPreparerPourModification(operation);

		return enregistrer(operation);
	}

	public void supprimerOperation(Operation operation) throws ServiceException {

		Assert.notNull(operation, () -> "L'OPERATION à supprimer est obligatoire");

		operation = controlerEtPreparerPourSuppression(operation);

		supprimer(operation);
	}

	private Operation enregistrer(Operation operation) throws ServiceException {

		try {
			operation = operationRepository.save(operation);
			if ( operation.getNumero() == null ) {
				operation.setNumero(String.format("AUTO-%010d", operation.getId()));
				operation = operationRepository.save(operation);
			}
			return operation;
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.ENREGISTREMENT,
					operation.getNumero());
		}
	}

	private void supprimer(Operation operation) throws ServiceException {

		try {
			operationRepository.delete(operation);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.SUPPRESSION,
					operation.getNumero());
		}
	}

	private Operation controlerEtPreparerPourCreation(Operation operation) throws ServiceException {

		verifierCompatibiliteEnDepense(operation.getTypeOperation(), operation.getCompteDepense());
		verifierCompatibiliteEnRecette(operation.getTypeOperation(), operation.getCompteRecette());
		verifierListeOperationDetail(operation.getLignes(), operation.getMontantEnCentimes());

		return operation;
	}

	private Operation controlerEtPreparerPourModification(Operation operation) throws ServiceException {

		verifierCompatibiliteEnDepense(operation.getTypeOperation(), operation.getCompteDepense());
		verifierCompatibiliteEnRecette(operation.getTypeOperation(), operation.getCompteRecette());
		verifierListeOperationDetail(operation.getLignes(), operation.getMontantEnCentimes());

		return operation;
	}

	private Operation controlerEtPreparerPourSuppression(Operation operation) throws ServiceException  {

		return operation;
	}

	private void verifierCompatibiliteEnDepense(
			TypeOperation typeOperation, 
			Compte compteDepense) throws ServiceException {

		if ( ! rechercherComptesCompatiblesEnDepense(typeOperation).contains(compteDepense) ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_DEPENSE_INCOMPATIBLES,
					typeOperation.getCode(), 
					compteDepense.getIdentifiant());
		}
	}

	private void verifierCompatibiliteEnRecette(
			TypeOperation typeOperation, 
			Compte compteRecette) throws ServiceException {

		if ( ! rechercherComptesCompatiblesEnRecette(typeOperation).contains(compteRecette) ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_RECETTE_INCOMPATIBLES,
					typeOperation.getCode(), 
					compteRecette.getIdentifiant());
		}
	}

	private void verifierListeOperationDetail(Set<OperationLigne> operationDetails, Long montantOperationEnCentimes) throws ServiceException {

		if ( operationDetails == null || operationDetails.isEmpty() ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.OPERATION_AU_MOINS_UN_DETAIL_REQUIS);
		}

		Set<Integer> numerosDetails = new HashSet<>();
		Long sommeMontantDetailEnCentimes = 0L;
		for ( OperationLigne operationDetail : operationDetails ) {
			
			if ( numerosDetails.contains(operationDetail.getNumeroLigne()) ) {
				throw new ServiceException(
						OperationFonctionnelleErreur.OPERATION_LISTE_DETAIL_NUMERO_DUPLIQUE,
						operationDetail.getNumeroLigne());
			}
			numerosDetails.add(operationDetail.getNumeroLigne());
			sommeMontantDetailEnCentimes += operationDetail.getMontantEnCentimes();
		}

		if ( ! sommeMontantDetailEnCentimes.equals(montantOperationEnCentimes) ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.OPERATION_LISTE_DETAIL_SOMME_MONTANTS_ERRONEE, 
					sommeMontantDetailEnCentimes,
					montantOperationEnCentimes);
		}
	}
}
