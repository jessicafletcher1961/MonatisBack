package fr.colline.monatis.operations.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.exceptions.GeneriqueTechniqueErreur;
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

	public record CaracteristiqueCompatibiliteCompte(TypeCompte typeCompte, TypeFonctionnement typeFonctionnement) {};
	
	@Autowired private OperationRepository operationRepository;
	@Autowired private OperationLigneRepository operationLigneRepository;

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteExterneService compteExterneService;
    @Autowired private CompteTechniqueService compteTechniqueService;

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
	 * Recherche toutes les opérations du compte indiqué entre une date début (incluse)
	 * et une date fin (incluse).</br>
	 * Pour les comptes internes, la recherche est bornée par la date de solde initial du compte et
	 * la date de clôture du compte (s'il y en a une).<br>
	 * Trié par date de valeur de l'opération.
	 * 
	 * @param compte
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 * @throws ServiceException
	 */
	public List<Operation> rechercherOperationsParCompteEntreDateDebutEtDateFin(
			Compte compte,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		LocalDate dateDebutRecherche = recalculerDateDebutCompte(compte, dateDebut); 
		LocalDate dateFinRecherche = recalculerDateFinCompte(compte, dateFin);
		
		try {
			return operationRepository.findByCompteDepenseIdOrCompteRecetteIdOrderByDateValeur(
					compte.getId(), 
					compte.getId())
					.filter((o) -> {return dateDebutRecherche == null || ! o.getDateValeur().isBefore(dateDebutRecherche);})
					.filter((o) -> {return dateFinRecherche == null || ! o.getDateValeur().isAfter(dateFinRecherche);})
					.toList();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_COMPTE_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compte.getIdentifiant(),
					dateDebut,
					dateFin);
		}
	}
	
	public List<Operation> rechercherOperationsParCompteJusqueDateFin(
			Compte compte,
			LocalDate dateFin) throws ServiceException {

		return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, null, dateFin);
	}

	
	public List<Operation> rechercherOperationsParCompteDepuisDateDebut(
			Compte compte,
			LocalDate dateDebut) throws ServiceException {

		return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, dateDebut, null);
	}
	
	public List<Operation> rechercherDernieresParCompte(Compte compte, LocalDate dateFin, long limite) throws ServiceException {
		
		return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, null, dateFin)
				.stream()
				.sorted((o1, o2) -> {return o2.getDateValeur().compareTo(o1.getDateValeur());})
				.limit(limite)
				.toList();
	}

	/**
	 * Recherche des opération de dépense du compte indiqué entre date début (incluse)
	 * et date fin (incluse).</br>
	 * Comprends les opérations de dépense réelles et les opérations techniques (de frais).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compte
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationsDepenseParCompteEntreDateDebutEtDateFin(
			Compte compte,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, dateDebut, dateFin)
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compte.getId());})
				.toList();
	}

	/**
	 * Recherche des opération de dépense du compte indiqué jusqu'à la date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compte
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationsDepenseParCompteJusqueDateFin(
			Compte compte, 
			LocalDate dateFin) throws ServiceException {

		return rechercherOperationsParCompteJusqueDateFin(compte, dateFin)
				.stream()
				.filter((o) -> {return o.getCompteDepense().getId().equals(compte.getId());})
				.toList();

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
	public List<Operation> rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(
			Compte compte,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, dateDebut, dateFin)
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compte.getId());})
				.toList();

	}

	/**
	 * Recherche des opération de recette du compte indiqué jusqu'à la date fin (incluse).</br>
	 * Trié par date de valeur de l'opération.
	 * @param compteId
	 * @param dateFin
	 * @return
	 */
	public List<Operation> rechercherOperationsRecetteParCompteJusqueDateFin(
			Compte compte, 
			LocalDate dateFin) throws ServiceException {

		return rechercherOperationsParCompteJusqueDateFin(compte, dateFin)
				.stream()
				.filter((o) -> {return o.getCompteRecette().getId().equals(compte.getId());})
				.toList();
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
					OperationTechniqueErreur.RECHERCHE_OPERATION_LIGNE_PAR_REFERENCE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					sousCategorieId,
					dateDebut,
					dateFin);
		}
	}

	public List<Compte> rechercherComptesCompatiblesDepense(TypeOperation typeOperation) throws ServiceException {

		CaracteristiqueCompatibiliteCompte caracteristiqueCompteRequiseDepense = determinerCaracteristiqueCompatibiliteCompteRequiseDepense(typeOperation);

		return determinerListeComptes(caracteristiqueCompteRequiseDepense);
	}

	public List<Compte> rechercherComptesCompatiblesRecette(TypeOperation typeOperation) throws ServiceException {

		CaracteristiqueCompatibiliteCompte caracteristiqueCompteRequiseRecette = determinerCaracteristiqueCompatibiliteCompteRequiseRecette(typeOperation);

		return determinerListeComptes(caracteristiqueCompteRequiseRecette);
	}
	
	public List<TypeOperation> rechercherTypesOperationCompatiblesDepense(Compte compte) throws ServiceException {
		
		List<TypeOperation> resultat = new ArrayList<TypeOperation>();
		
		for ( TypeOperation typeOperation : TypeOperation.values() ) {

			if ( isCompatibiliteDepense(compte, typeOperation) ) {
				resultat.add(typeOperation);
			}
		}
		
		return resultat;
	}
	
	public List<TypeOperation> rechercherTypesOperationCompatiblesRecette(Compte compte) throws ServiceException {

		List<TypeOperation> resultat = new ArrayList<TypeOperation>();
		
		for ( TypeOperation typeOperation : TypeOperation.values() ) {

			if ( isCompatibiliteRecette(compte, typeOperation) ) {
				resultat.add(typeOperation);
			}
		}
		
		return resultat;
	}
	
	public boolean verifierCompatibiliteDepense(Compte compte, TypeOperation typeOperation) throws ServiceException {
	
		return isCompatibiliteDepense(compte, typeOperation);
	}

	
	public boolean verifierCompatibiliteRecette(Compte compte, TypeOperation typeOperation) throws ServiceException {
	
		return isCompatibiliteRecette(compte, typeOperation);
	}

//
//	public List<Compte> rechercherComptesCompatiblesNonTechniques(TypeOperation typeOperation) throws ServiceException {
//
//		List<Compte> comptes = new ArrayList<Compte>();
//
//		if ( typeOperation.isFluxTechnique() ) {
//			
//			comptes.addAll(determinerComptesCompatiblesRecette(typeOperation)
//					.stream()
//					.filter((c) -> {return c.getTypeCompte() != TypeCompte.TECHNIQUE;})
//					.toList());
//			comptes.addAll(determinerComptesCompatiblesDepense(typeOperation)
//					.stream()
//					.filter((c) -> {return c.getTypeCompte() != TypeCompte.TECHNIQUE;})
//					.toList());
//		}
//		return comptes
//				.stream()
//				.sorted((c1,c2)->{return c1.getIdentifiant().compareTo(c2.getIdentifiant());})
//				.toList();
//
//	}
//	
//	public CompteTechnique determinerCompteTechniqueRemunerationsEtFrais(TypeFonctionnement typeFonctionnement) throws ServiceException {
//		
//		switch (typeFonctionnement ) {
//		case COURANT:
//			return compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisCourant();
//		case FINANCIER:
//			return compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisFinancier();
//		case BIEN:
//			return compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisBien();
//		default:
//			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
//					TypeFonctionnement.class.getSimpleName(),
//					typeFonctionnement.getCode(),
//					typeFonctionnement.getLibelle());
//		}
//	}
	
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

		verifierCompatibiliteDepense(operation.getTypeOperation(), operation.getCompteDepense());
		verifierCompatibiliteRecette(operation.getTypeOperation(), operation.getCompteRecette());
		verifierDateCloture(operation.getDateValeur(), operation.getCompteDepense());
		verifierDateCloture(operation.getDateValeur(), operation.getCompteRecette());
		verifierListeOperationDetail(operation.getLignes(), operation.getMontantEnCentimes());

		return operation;
	}

	private Operation controlerEtPreparerPourModification(Operation operation) throws ServiceException {

		verifierCompatibiliteDepense(operation.getTypeOperation(), operation.getCompteDepense());
		verifierCompatibiliteRecette(operation.getTypeOperation(), operation.getCompteRecette());
		verifierDateCloture(operation.getDateValeur(), operation.getCompteDepense());
		verifierDateCloture(operation.getDateValeur(), operation.getCompteRecette());
		verifierListeOperationDetail(operation.getLignes(), operation.getMontantEnCentimes());

		return operation;
	}

	private Operation controlerEtPreparerPourSuppression(Operation operation) throws ServiceException  {

		return operation;
	}

	private void verifierCompatibiliteDepense(
			TypeOperation typeOperation, 
			Compte compteDepense) throws ServiceException {

		if ( ! isCompatibiliteDepense(compteDepense, typeOperation) ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_DEPENSE_INCOMPATIBLES,
					typeOperation.getCode(), 
					compteDepense.getIdentifiant());
		}
	}

	private void verifierCompatibiliteRecette(
			TypeOperation typeOperation, 
			Compte compteRecette) throws ServiceException {

		if ( ! isCompatibiliteRecette(compteRecette, typeOperation) ) {
			throw new ServiceException(
					OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_RECETTE_INCOMPATIBLES,
					typeOperation.getCode(), 
					compteRecette.getIdentifiant());
		}
	}

	private void verifierDateCloture(
			LocalDate dateValeur, 
			Compte compte) throws ServiceException {
		
		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			CompteInterne compteInterne = (CompteInterne) compte;
			if ( compteInterne.getDateCloture() != null && dateValeur.isAfter(compteInterne.getDateCloture()) ) {
				throw new ServiceException(
						OperationFonctionnelleErreur.DATE_VALEUR_APRES_CLOTURE_COMPTE,
						dateValeur, 
						compteInterne.getIdentifiant(),
						compteInterne.getDateCloture());
			}
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

	/**
	 * Si le compte n'est pas un compte interne, retourne la date indiquée.</br>
	 * Si le compte est un compte interne :<ul>
	 *  <li>si la date passée est à null, retourne la date du solde initial.</li>
	 *  <li>si la date passée n'est pas à null, retourne  soit la date indiquée, soit la date de
	 *  	solde initial si celle-ci est après la date indiquée.</li></ul> 
	 *  
	 * @param compte
	 * @param dateDebut
	 * @return
	 */
	private LocalDate recalculerDateDebutCompte(Compte compte, LocalDate dateDebut) {
	
		LocalDate dateDebutCompte;
		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;
			
			// La date de début pour la prise en compte des opération est au minimum la date du solde initial.
			// En effet, toutes les opérations jusqu'à la veille du jour de la "création" du compte doivent
			// être ignorées
			dateDebutCompte = compteInterne.getDateSoldeInitial();
			if ( dateDebut != null && dateDebutCompte.isBefore(dateDebut) ) {
				dateDebutCompte = dateDebut;
			}
		}
		else {
			
			dateDebutCompte = dateDebut;
		}

		return dateDebutCompte;
	}


	/**
	 * Si le compte n'est pas un compte interne, retourne la date indiquée.</br>
	 * Si le compte est un compte interne :<ul>
	 *  <li>si la date passée est à null, retourne la date de clôture (qui peut être à null).</li>
	 *  <li>si la date passée n'est pas à null, retourne  soit la date indiquée, soit la date de
	 *  	clôture du compte si elle existe et qu'elle est avant la date indiquée.</li></ul> 
	 *  
	 * @param compte
	 * @param dateDebut
	 * @return
	 */
	private LocalDate recalculerDateFinCompte(Compte compte, LocalDate dateFin) {
	
		LocalDate dateFinCompte;
		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;
			
			// La date de fin pour la prise en compte des opération est au maximum la date de clôture.
			// En effet, toutes les opérations à partir de cette date doivent être ignorées (normalement,
			// il ne peut pas y en avoir mais sait-on jamais)
			dateFinCompte = compteInterne.getDateCloture();
			if ( dateFin != null && (dateFinCompte == null || dateFinCompte.isAfter(dateFin)) ) {
				dateFinCompte = dateFin;
			}
		}
		else {
			
			dateFinCompte = dateFin;
		}

		return dateFinCompte;
	}
	
	private List<Compte> determinerListeComptes(CaracteristiqueCompatibiliteCompte caracteristiqueCompatibiliteCompte) throws ServiceException {
		
		List<Compte> resultat = new ArrayList<>();
		
		switch (caracteristiqueCompatibiliteCompte.typeCompte) {
		case INTERNE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(caracteristiqueCompatibiliteCompte.typeFonctionnement));
			break;
		case EXTERNE:	
			resultat.addAll(compteExterneService.rechercherTous());
			break;
		case TECHNIQUE:
			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFrais());
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeCompte.class.getSimpleName(),
					caracteristiqueCompatibiliteCompte.typeCompte.getCode(),
					caracteristiqueCompatibiliteCompte.typeCompte.getLibelle());
		}
		
		return resultat;
	}
	
	private boolean isCompatibiliteDepense(Compte compte, TypeOperation typeOperation) throws ServiceException {
		
		CaracteristiqueCompatibiliteCompte caracteristiqueCompte = determinerCaracteristiqueCompatibiliteCompte(compte);
		CaracteristiqueCompatibiliteCompte caracteristiqueTypeOperation = determinerCaracteristiqueCompatibiliteCompteRequiseDepense(typeOperation);
		
		return caracteristiqueCompte.equals(caracteristiqueTypeOperation);
	}

	private boolean isCompatibiliteRecette(Compte compte, TypeOperation typeOperation) throws ServiceException {
		
		CaracteristiqueCompatibiliteCompte caracteristiqueCompte = determinerCaracteristiqueCompatibiliteCompte(compte);
		CaracteristiqueCompatibiliteCompte caracteristiqueTypeOperation = determinerCaracteristiqueCompatibiliteCompteRequiseRecette(typeOperation);
		
		return caracteristiqueCompte.equals(caracteristiqueTypeOperation);
	}
	
	private CaracteristiqueCompatibiliteCompte determinerCaracteristiqueCompatibiliteCompte(Compte compte) {

		TypeCompte typeCompte = compte.getTypeCompte();
		TypeFonctionnement typeFonctionnement = null;
		
		if ( typeCompte == TypeCompte.INTERNE ) {
			CompteInterne compteInterne = (CompteInterne) compte;
			typeFonctionnement = compteInterne.getTypeFonctionnement();
		}
		
		return new CaracteristiqueCompatibiliteCompte(typeCompte, typeFonctionnement);
	}

	private CaracteristiqueCompatibiliteCompte determinerCaracteristiqueCompatibiliteCompteRequiseDepense(TypeOperation typeOperation) throws ServiceException {

		CaracteristiqueCompatibiliteCompte resultat;
		
		switch(typeOperation) {

		case TRANSFERT:
		case DEPOT:
		case INVESTISSEMENT:
		case DEPENSE:
		case FRAIS_COMPTE_COURANT:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.INTERNE, TypeFonctionnement.COURANT);
			break;
		case RETRAIT:
		case LIQUIDATION:
		case FRAIS_COMPTE_FINANCIER:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.INTERNE, TypeFonctionnement.FINANCIER);
			break;
		case VENTE:
		case FRAIS_COMPTE_BIEN:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.INTERNE, TypeFonctionnement.BIEN);
			break;
		case RECETTE:
		case ACHAT:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.EXTERNE, null);
			break;
		case REMUNERATION_COMPTE_COURANT:
		case REMUNERATION_COMPTE_FINANCIER:
		case REMUNERATION_COMPTE_BIEN:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.TECHNIQUE, null);
			break; 
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}
		
		return resultat;
	}

	private CaracteristiqueCompatibiliteCompte determinerCaracteristiqueCompatibiliteCompteRequiseRecette(TypeOperation typeOperation) throws ServiceException {

		CaracteristiqueCompatibiliteCompte resultat;
		switch(typeOperation) {

		case TRANSFERT:
		case RETRAIT:
		case LIQUIDATION:
		case RECETTE:
		case REMUNERATION_COMPTE_COURANT:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.INTERNE, TypeFonctionnement.COURANT);
			break;
		case DEPOT:
		case INVESTISSEMENT:
		case REMUNERATION_COMPTE_FINANCIER:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.INTERNE, TypeFonctionnement.FINANCIER);
			break;
		case ACHAT:
		case REMUNERATION_COMPTE_BIEN:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.INTERNE, TypeFonctionnement.BIEN);
			break;
		case DEPENSE:
		case VENTE:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.EXTERNE, null);
			break;
		case FRAIS_COMPTE_COURANT:
		case FRAIS_COMPTE_FINANCIER:
		case FRAIS_COMPTE_BIEN:
			resultat = new CaracteristiqueCompatibiliteCompte(TypeCompte.TECHNIQUE, null);
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}

		return resultat;
	}
	
//	private List<Compte> determinerComptesCompatiblesDepense(TypeOperation typeOperation) throws ServiceException {
//
//		List<Compte> resultat = new ArrayList<>();
//
//		switch(typeOperation) {
//
//		case TRANSFERT:
//		case DEPOT:
//		case INVESTISSEMENT:
//		case DEPENSE:
//		case FRAIS_COMPTE_COURANT:
//			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT));
//			break;
//		case RETRAIT:
//		case LIQUIDATION:
//		case FRAIS_COMPTE_FINANCIER:
//			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER));
//			break;
//		case VENTE:
//		case FRAIS_COMPTE_BIEN:
//			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.BIEN));
//			break;
//		case RECETTE:
//		case ACHAT:
//			resultat.addAll(compteExterneService.rechercherTous());
//			break;
//		case REMUNERATION_COMPTE_COURANT:
//			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisCourant());
//			break;
//		case REMUNERATION_COMPTE_FINANCIER:
//			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisFinancier());
//			break;
//		case REMUNERATION_COMPTE_BIEN:
//			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisBien());
//			break;
//		default:
//			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
//					TypeOperation.class.getSimpleName(),
//					typeOperation.getCode(),
//					typeOperation.getLibelle());
//		}
//
//		return resultat;
//	}
	
//	private List<Compte> determinerComptesCompatiblesRecette(TypeOperation typeOperation) throws ServiceException {
//
//		List<Compte> resultat = new ArrayList<>();
//
//		switch(typeOperation) {
//
//		case TRANSFERT:
//		case RETRAIT:
//		case LIQUIDATION:
//		case RECETTE:
//		case REMUNERATION_COMPTE_COURANT:
//			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.COURANT));
//			break;
//		case DEPOT:
//		case INVESTISSEMENT:
//		case REMUNERATION_COMPTE_FINANCIER:
//			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.FINANCIER));
//			break;
//		case ACHAT:
//		case REMUNERATION_COMPTE_BIEN:
//			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(TypeFonctionnement.BIEN));
//			break;
//		case DEPENSE:
//		case VENTE:
//			resultat.addAll(compteExterneService.rechercherTous());
//			break;
//		case FRAIS_COMPTE_COURANT:
//			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisCourant());
//			break;
//		case FRAIS_COMPTE_FINANCIER:
//			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisFinancier());
//			break;
//		case FRAIS_COMPTE_BIEN:
//			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechniqueRemunerationsEtFraisBien());
//			break;
//		default:
//			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
//					TypeOperation.class.getSimpleName(),
//					typeOperation.getCode(),
//					typeOperation.getLibelle());
//		}
//
//		return resultat;
//	}

}
