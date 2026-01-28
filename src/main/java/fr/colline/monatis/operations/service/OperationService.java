package fr.colline.monatis.operations.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.erreurs.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationFonctionnelleErreur;
import fr.colline.monatis.operations.OperationTechniqueErreur;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.repository.OperationLigneRepository;
import fr.colline.monatis.operations.repository.OperationRepository;

@Service
public class OperationService {

    private final CompteTechniqueService compteTechniqueService;

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteExterneService compteExterneService;
	@Autowired private OperationRepository operationRepository;
	@Autowired private OperationLigneRepository operationLigneRepository;

    OperationService(CompteTechniqueService compteTechniqueService) {
        this.compteTechniqueService = compteTechniqueService;
    }

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

	/**
	 * Recherche des opération de dépense du compte indiqué entre date début (incluse)
	 * et date fin (incluse).</br>
	 * Comprends les opérations réelles et les opérations techniques.</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationsDepenseParCompteIdEntreDateDebutEtDateFin(
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
	public List<Operation> rechercherOperationsDepenseParCompteIdJusqueDateFin(Long compteId, LocalDate dateFin) throws ServiceException {

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
	public List<Operation> rechercherOperationsDepenseParCompteIdDepuisDateDebut(Long compteId, LocalDate dateDebut) throws ServiceException {

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
	 * Comprends les opérations réelles et les opérations techniques.</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationsRecetteParCompteIdEntreDateDebutEtDateFin(
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
	public List<Operation> rechercherOperationsRecetteParCompteIdJusqueDateFin(Long compteId, LocalDate dateFin) throws ServiceException {

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
	public List<Operation> rechercherOperationsRecetteParCompteIdDepuisDateDebut(Long compteId, LocalDate dateDebut) throws ServiceException {

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

	public List<Operation> rechercherOperationsParCompteId(Long compteId) throws ServiceException {

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
	public List<Operation> rechercherOperationsParCompteIdEntreDateDebutEtDateFin(Long compteId, LocalDate dateDebut,LocalDate dateFin) throws ServiceException {
		
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
	public List<Operation> rechercherOperationsParCompteIdJusqueDateFin(Long compteId, LocalDate dateFin) throws ServiceException {

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
	public List<Operation> rechercherOperationsParCompteIdDepuisDateDebut(Long compteId, LocalDate dateDebut) throws ServiceException {

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

		if ( ! determinerComptesCompatiblesEnDepense(typeOperation).contains(compteDepense) ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_DEPENSE_INCOMPATIBLES,
					typeOperation.getCode(), 
					compteDepense.getIdentifiant());
		}
	}

	private void verifierCompatibiliteEnRecette(
			TypeOperation typeOperation, 
			Compte compteRecette) throws ServiceException {

		if ( ! determinerComptesCompatiblesEnRecette(typeOperation).contains(compteRecette) ) {
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

	private Set<Compte> determinerComptesCompatiblesEnDepense(TypeOperation typeOperation) throws ServiceException {

		Set<Compte> resultat = new HashSet<>();

		switch(typeOperation) {

		case TRANSFERT:
		case DEPOT:
		case INVESTISSEMENT:
		case DEPENSE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT));
			break;
		case RETRAIT:
		case LIQUIDATION:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER));
			break;
		case VENTE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.BIEN));
			break;
		case RECETTE:
		case ACHAT:
			resultat.addAll(compteExterneService.rechercherTous());
			break;
		case TECHNIQUE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER));
			resultat.addAll(compteTechniqueService.rechercherTous());
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}

		return resultat;
	}

	private Set<Compte> determinerComptesCompatiblesEnRecette(TypeOperation typeOperation) throws ServiceException {

		Set<Compte> resultat = new HashSet<>();

		switch(typeOperation) {

		case TRANSFERT:
		case RETRAIT:
		case LIQUIDATION:
		case RECETTE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT));
			break;
		case DEPOT:
		case INVESTISSEMENT:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER));
			break;
		case ACHAT:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.BIEN));
			break;
		case DEPENSE:
		case VENTE:
			resultat.addAll(compteExterneService.rechercherTous());
			break;
		case TECHNIQUE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER));
			resultat.addAll(compteTechniqueService.rechercherTous());
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}

		return resultat;
	}

}
