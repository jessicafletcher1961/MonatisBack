package fr.colline.monatis.operations.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTiers;
import fr.colline.monatis.comptes.model.TypeFonctionnementCompte;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;
import fr.colline.monatis.exceptions.erreurs.ErreurTechnique;
import fr.colline.monatis.operations.model.DetailOperation;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.repository.OperationRepository;

@Service
public class OperationService {

	@Autowired private OperationRepository operationRepository;

	public Operation rechercherParId(Long operationId) throws ServiceException {

		if ( operationId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					Operation.class.getSimpleName());
		}

		try {
			Optional<Operation> optional = operationRepository.findById(operationId);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_OPERATION_PAR_ID,
					operationId );
		}
	}

	public boolean isExistantParId(Long operationId) throws ServiceException {

		if ( operationId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					Operation.class.getSimpleName());
		}

		try {
			return operationRepository.existsById(operationId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_OPERATION_PAR_ID,
					operationId);
		}
	}

	public Operation rechercherParNumero(String numero) throws ServiceException {

		if ( numero == null || numero.isBlank() ) {
			throw new ServiceException(
					ErreurProgrammation.NUMERO_NULL);
		}

		try {
			Optional<Operation> optional = operationRepository.findByNumero(numero);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_OPERATION_PAR_NUMERO,
					numero);
		}
	}

	public boolean isExistantParNumero(String numero) throws ServiceException {

		if ( numero == null || numero.isBlank() ) {
			throw new ServiceException(
					ErreurProgrammation.NUMERO_NULL);
		}

		try {
			return operationRepository.existsByNumero(numero);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_OPERATION_PAR_NUMERO,
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
					ErreurTechnique.TECH_RECHERCHE_OPERATION_TOUS);
		}
	}

	public List<Operation> rechercherTous(Sort tri) throws ServiceException {

		if ( tri == null ) {
			throw new ServiceException(
					ErreurProgrammation.TRI_NULL,
					Operation.class.getSimpleName());
		}

		try {
			return operationRepository.findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_OPERATION_TOUS);
		}
	}

	public void supprimerTous() throws ServiceException {

		try {
			operationRepository.deleteAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_SUPPRESSION_OPERATION_TOUS);
		}
	}

	public List<Operation> rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
			Long compteId,
			ZonedDateTime dateDebut, 
			ZonedDateTime dateFin) throws ServiceException {

		try {
			return operationRepository.findByCompteRecetteIdAndDateValeurBetween(
					compteId, dateDebut, dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteId,
					dateDebut,
					dateFin);
		}
	}

	public List<Operation> rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
			Long compteId,
			ZonedDateTime dateDebut, 
			ZonedDateTime dateFin) throws ServiceException {

		try {
			return operationRepository.findByCompteDepenseIdAndDateValeurBetween(
					compteId, dateDebut, dateFin);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
					compteId,
					dateDebut,
					dateFin);
		}
	}

	public Operation creerOperation(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

		operation = controlerEtPreparerPourCreation(operation);

		return enregistrer(operation);
	}

	public Operation modifierOperation(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

		operation = controlerEtPreparerPourModification(operation);

		return enregistrer(operation);
	}

	public void supprimerOperation(Long operationId) 
			throws ServiceException {

		if ( operationId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					Operation.class.getSimpleName());
		}

		Operation operation = controlerEtPreparerPourSuppression(operationId);

		supprimer(operation);
	}
	
	public Operation creerOperationAjustement(
			CompteInterne compteAjustable, 
			CompteInterne compteAjustement,
			ZonedDateTime dateAjustement,
			Long soldeApresAjustement) throws ServiceException {

		if ( compteAjustable == null 
				|| compteAjustement == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					CompteInterne.class.getSimpleName());
		}
		if ( dateAjustement == null ) {
			throw new ServiceException(
					ErreurProgrammation.DATE_AJUSTEMENT_NULL);
		}
		if ( soldeApresAjustement == null ) {
			throw new ServiceException(
					ErreurProgrammation.SOLDE_APRES_AJUSTEMENT_NULL);
		}
		
  		TypeFonctionnementCompte fonctionnementCompte = compteAjustable
				.getTypeCompteInterne()
				.getTypeFonctionnementCompte();
		if ( fonctionnementCompte != TypeFonctionnementCompte.COURANT ) {
			throw new ServiceException(
					ErreurFonctionnelle.COMPTE_INTERNE_AJUSTEMENT_INCOMPATIBLE, 
					compteAjustable.getIdentifiant(),
					fonctionnementCompte.getLibelle());
		}
		
		Long soldeAvantAjustement = calculerSolde(
				compteAjustable,
				dateAjustement);
		Long montantAjustementEnCentimes = soldeApresAjustement - soldeAvantAjustement;

		TypeOperation typeOperation;
		Compte compteRecette;
		Compte compteDepense;
		String libelle;
		if ( montantAjustementEnCentimes < 0 ) {
			compteRecette = compteAjustement;
			compteDepense = compteAjustable;
			libelle = String.format(
					"Diminution par ajustement du solde du compte %s (%s)", 
					compteDepense.getIdentifiant(),
					montantAjustementEnCentimes);
			typeOperation = TypeOperation.MOINS_AJUSTEMENT;
		}
		else {
			compteRecette = compteAjustable;
			compteDepense = compteAjustement;
			libelle = String.format(
					"Augmentation par ajustement du solde du compte %s (+%s)", 
					compteRecette.getIdentifiant(),
					montantAjustementEnCentimes);
			typeOperation = TypeOperation.PLUS_AJUSTEMENT;
		}
		
		Operation operation = new Operation();

		operation.setNumero(null);
		operation.setMontantTotalEnCentimes(Math.abs(montantAjustementEnCentimes));
		operation.setCompteRecette(compteRecette);
		operation.setCompteDepense(compteDepense);
		operation.setDateValeur(dateAjustement);
		operation.setLibelle(libelle);
		operation.setTypeOperation(typeOperation);
		
		DetailOperation detailOperation = new DetailOperation();
		operation.getDetailsOperation().add(detailOperation);
		detailOperation.setOperation(operation);
		
		detailOperation.setSequence(0);
		detailOperation.setDateComptabilisation(operation.getDateValeur());
		detailOperation.setLibelle(operation.getLibelle());
		detailOperation.setMontantDetailEnCentimes(operation.getMontantTotalEnCentimes());
		
		return creerOperation(operation);
	}

	public Operation creerOperationActualisation(
			CompteInterne compteActualisable, 
			CompteInterne compteActualisation,
			ZonedDateTime dateActualisation,
			Long soldeApresActualisation) throws ServiceException {

		if ( compteActualisable == null 
				|| compteActualisation == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					CompteInterne.class.getSimpleName());
		}
		if ( dateActualisation == null ) {
			throw new ServiceException(
					ErreurProgrammation.DATE_ACTUALISATION_NULL);
		}
		if ( soldeApresActualisation == null ) {
			throw new ServiceException(
					ErreurProgrammation.SOLDE_APRES_ACTUALISATION_NULL);
		}
		
  		TypeFonctionnementCompte fonctionnementCompte = compteActualisable
				.getTypeCompteInterne()
				.getTypeFonctionnementCompte();
		if ( fonctionnementCompte != TypeFonctionnementCompte.IMMOBILIER
				&& fonctionnementCompte != TypeFonctionnementCompte.MOBILIER
				&& fonctionnementCompte != TypeFonctionnementCompte.PLACEMENT ) {
			throw new ServiceException(
					ErreurFonctionnelle.COMPTE_INTERNE_ACTUALISATION_INCOMPATIBLE, 
					compteActualisable.getIdentifiant(),
					fonctionnementCompte.getLibelle());
		}
		
		Long soldeAvantActualisation = calculerSolde(
				compteActualisable,
				dateActualisation);
		Long montantActualisationEnCentimes = soldeApresActualisation - soldeAvantActualisation;

		TypeOperation typeOperation;
		Compte compteRecette;
		Compte compteDepense;
		String libelle;
		if ( montantActualisationEnCentimes < 0 ) {
			compteRecette = compteActualisation;
			compteDepense = compteActualisable;
			libelle = String.format(
					"Enregistrement d'une moins-value de la valeur du compte %s (%s)", 
					compteDepense.getIdentifiant(),
					montantActualisationEnCentimes);
			typeOperation = TypeOperation.MOINS_VALUE;
		}
		else {
			compteRecette = compteActualisable;
			compteDepense = compteActualisation;
			libelle = String.format(
					"Enregistrement d'une plus-value de la valeur du compte %s (+%s)", 
					compteRecette.getIdentifiant(),
					montantActualisationEnCentimes);
			typeOperation = TypeOperation.PLUS_VALUE;
		}
		
		Operation operation = new Operation();

		operation.setNumero(null);
		operation.setMontantTotalEnCentimes(Math.abs(montantActualisationEnCentimes));
		operation.setCompteRecette(compteRecette);
		operation.setCompteDepense(compteDepense);
		operation.setDateValeur(dateActualisation);
		operation.setLibelle(libelle);
		operation.setTypeOperation(typeOperation);
		
		DetailOperation detailOperation = new DetailOperation();
		operation.getDetailsOperation().add(detailOperation);
		detailOperation.setOperation(operation);

		detailOperation.setSequence(0);
		detailOperation.setDateComptabilisation(operation.getDateValeur());
		detailOperation.setLibelle(operation.getLibelle());
		detailOperation.setMontantDetailEnCentimes(operation.getMontantTotalEnCentimes());

		return creerOperation(operation);
	}

	public double rechercherPourcentagePlusOuMoinsValue (
			CompteInterne compteInterne,
			ZonedDateTime dateValeurActualisee,
			Long valeurActualisee) throws ServiceException {

		if ( compteInterne == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					CompteInterne.class.getSimpleName());
		}
		if ( dateValeurActualisee == null ) {
			throw new ServiceException(
					ErreurProgrammation.DATE_ACTUALISATION_NULL);
		}
		if ( valeurActualisee == null ) {
			throw new ServiceException(
					ErreurProgrammation.SOLDE_APRES_ACTUALISATION_NULL);
		}

		Long solde = calculerSolde(
				compteInterne, 
				dateValeurActualisee);

		double plusOuMoinsValue = (double) ((valeurActualisee - solde) * 100.0000 / valeurActualisee);

		return plusOuMoinsValue;
	}
	
	private Operation enregistrer(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

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
					ErreurTechnique.TECH_ENREGISTREMENT_OPERATION,
					operation.getNumero());
		}
	}

	private void supprimer(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

		try {
			operationRepository.delete(operation);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_SUPPRESSION_OPERATION,
					operation.getNumero());
		}
	}

	private Operation controlerEtPreparerPourCreation(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

		verifierOperationNonEnregistree(operation.getId());
		verifierNumeroValideEtUnique(operation.getId(), operation.getNumero());
		verifierCompatibiliteEnDepense(operation.getTypeOperation(), operation.getCompteDepense());
		verifierCompatibiliteEnRecette(operation.getTypeOperation(), operation.getCompteRecette());
		verifierListeDetailOperation(operation);

		return operation;
	}

	private Operation controlerEtPreparerPourModification(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

		verifierOperationEnregistree(operation.getId());
		verifierNumeroValideEtUnique(operation.getId(), operation.getNumero());
		verifierCompatibiliteEnDepense(operation.getTypeOperation(), operation.getCompteDepense());
		verifierCompatibiliteEnRecette(operation.getTypeOperation(), operation.getCompteRecette());
		verifierListeDetailOperation(operation);

		return operation;
	}

	private Operation controlerEtPreparerPourSuppression(Long operationId) throws ServiceException  {

		if ( operationId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					Operation.class.getSimpleName());
		}

		verifierOperationEnregistree(operationId);

		return rechercherParId(operationId);
	}

	private void verifierOperationEnregistree(Long operationId) throws ServiceException {

		if ( operationId == null || ! isExistantParId(operationId) ) {
			throw new ServiceException (
					ErreurFonctionnelle.OPERATION_NON_ENREGISTREE_PAR_ID,
					operationId );
		}
	}

	private void verifierOperationNonEnregistree(Long operationId) throws ServiceException {

		if ( operationId != null && isExistantParId(operationId) ) {
			throw new ServiceException (
					ErreurFonctionnelle.OPERATION_DEJA_ENREGISTREE_PAR_ID,
					operationId);
		}
	}

	private void verifierNumeroValideEtUnique(
			Long operationId,
			String operationNumero) throws ServiceException {

		if ( operationNumero == null ) {
			return;
		}
		
		// On ne fait la vérification que si un numéro est spécifié
		
		if ( operationNumero.isBlank() ) {
			throw new ServiceException (
					ErreurFonctionnelle.OPERATION_NUMERO_INVALIDE);
		}

		boolean isNumeroCreeOuModifie;
		boolean isNumeroDejaUtilise;

		if ( operationId == null ) {
			// En cours création
			isNumeroCreeOuModifie = true;
			isNumeroDejaUtilise = isExistantParNumero(operationNumero);
		}
		else {
			// En cours modification
			try {
				isNumeroCreeOuModifie = ! operationRepository.existsByNumeroAndId(operationNumero, operationId);
				isNumeroDejaUtilise = operationRepository.existsByNumeroAndIdNot(operationNumero, operationId);
			}
			catch ( Throwable t ) {
				throw new ServiceException (
						t,
						ErreurTechnique.TECH_EXISTANCE_OPERATION_PAR_NUMERO,
						operationNumero);
			}
		}

		if ( isNumeroCreeOuModifie && isNumeroDejaUtilise ) {
			throw new ServiceException (
					ErreurFonctionnelle.OPERATION_NUMERO_DEJA_UTILISE,
					operationNumero);
		}
	}

	private void verifierCompatibiliteEnDepense(
			TypeOperation typeOperation, 
			Compte compteDepense) throws ServiceException {

		if ( CompteTiers.class.isAssignableFrom(compteDepense.getClass()) ) {
			if ( typeOperation != TypeOperation.RECETTE ) {
				throw new ServiceException(
						ErreurFonctionnelle.OPERATION_TYPE_COMPTE_DEPENSE_INCOMPATIBLE,
						typeOperation.getLibelle(), 
						TypeFonctionnementCompte.TIERS.getLibelle());
			}
		}
		else if ( CompteInterne.class.isAssignableFrom(compteDepense.getClass()) ) {

			CompteInterne compteInterne = (CompteInterne) compteDepense;
			TypeFonctionnementCompte typeFonctionnementCompte = compteInterne.getTypeCompteInterne().getTypeFonctionnementCompte();
			switch (typeFonctionnementCompte) {
			case COURANT:
				if ( typeOperation != TypeOperation.TRANSFERT 
				&& typeOperation != TypeOperation.ACHAT
				&& typeOperation != TypeOperation.MOINS_AJUSTEMENT
				&& typeOperation != TypeOperation.DEPENSE) {
					throw new ServiceException(
							ErreurFonctionnelle.OPERATION_TYPE_COMPTE_DEPENSE_INCOMPATIBLE,
							typeOperation.getLibelle(), 
							typeFonctionnementCompte.getLibelle());
				}
				break;
			case CONTREPARTIE:
				if ( typeOperation != TypeOperation.PLUS_AJUSTEMENT 
				&& typeOperation != TypeOperation.PLUS_VALUE ) {
					throw new ServiceException(
							ErreurFonctionnelle.OPERATION_TYPE_COMPTE_DEPENSE_INCOMPATIBLE,
							typeOperation.getLibelle(), 
							typeFonctionnementCompte.getLibelle());
				}
				break;
			case IMMOBILIER:
			case MOBILIER:
			case PLACEMENT:
				if ( typeOperation != TypeOperation.VENTE 
				&& typeOperation != TypeOperation.MOINS_VALUE ) {
					throw new ServiceException(
							ErreurFonctionnelle.OPERATION_TYPE_COMPTE_DEPENSE_INCOMPATIBLE,
							typeOperation.getLibelle(), 
							typeFonctionnementCompte.getLibelle());
				}
				break;
			default:
				throw new ServiceException(
						ErreurProgrammation.TYPE_FONCTIONNEMENT_COMPTE_NON_GERE, 
						typeFonctionnementCompte.getCode(),
						typeFonctionnementCompte.getLibelle());
			}
		}
		else {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_CLASSE_NON_GEREE, 
					compteDepense.getClass().getSimpleName());
		}
	}

	private void verifierCompatibiliteEnRecette(
			TypeOperation typeOperation, 
			Compte compteRecette) throws ServiceException {

		if ( CompteTiers.class.isAssignableFrom(compteRecette.getClass()) ) {
			if ( typeOperation != TypeOperation.DEPENSE ) {
				throw new ServiceException(
						ErreurFonctionnelle.OPERATION_TYPE_COMPTE_RECETTE_INCOMPATIBLE,
						typeOperation.getLibelle(), 
						TypeFonctionnementCompte.TIERS.getLibelle());
			}
		}
		else if ( CompteInterne.class.isAssignableFrom(compteRecette.getClass()) ) {

			CompteInterne compteInterne = (CompteInterne) compteRecette;
			TypeFonctionnementCompte typeFonctionnementCompte = compteInterne.getTypeCompteInterne().getTypeFonctionnementCompte();
			switch (typeFonctionnementCompte) {
			case COURANT:
				if ( typeOperation != TypeOperation.TRANSFERT 
				&& typeOperation != TypeOperation.VENTE
				&& typeOperation != TypeOperation.PLUS_AJUSTEMENT
				&& typeOperation != TypeOperation.RECETTE) {
					throw new ServiceException(
							ErreurFonctionnelle.OPERATION_TYPE_COMPTE_RECETTE_INCOMPATIBLE,
							typeOperation.getLibelle(),
							typeFonctionnementCompte.getLibelle());
				}
				break;
			case CONTREPARTIE:
				if ( typeOperation != TypeOperation.MOINS_AJUSTEMENT 
				&& typeOperation != TypeOperation.MOINS_VALUE ) {
					throw new ServiceException(
							ErreurFonctionnelle.OPERATION_TYPE_COMPTE_RECETTE_INCOMPATIBLE,
							typeOperation.getLibelle(),
							typeFonctionnementCompte.getLibelle());
				}
				break;
			case PLACEMENT:
			case MOBILIER:
			case IMMOBILIER:
				if ( typeOperation != TypeOperation.ACHAT 
				&& typeOperation != TypeOperation.PLUS_VALUE ) {
					throw new ServiceException(
							ErreurFonctionnelle.OPERATION_TYPE_COMPTE_RECETTE_INCOMPATIBLE,
							typeOperation.getLibelle(),
							typeFonctionnementCompte.getLibelle());
				}
				break;
			default:
				throw new ServiceException(
						ErreurProgrammation.TYPE_FONCTIONNEMENT_COMPTE_NON_GERE, 
						typeFonctionnementCompte.getCode(),
						typeFonctionnementCompte.getLibelle());
			}
		}
		else {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_CLASSE_NON_GEREE, 
					compteRecette.getClass().getSimpleName());
		}
	}

	private void verifierListeDetailOperation(Operation operation) throws ServiceException {

		if ( operation == null ) {
			throw new ServiceException(
					ErreurProgrammation.OPERATION_NULL);
		}

		Set<DetailOperation> detailsOperation = operation.getDetailsOperation();

		if ( detailsOperation == null 
				|| detailsOperation.isEmpty() ) {
			throw new ServiceException(
					ErreurFonctionnelle.OPERATION_AU_MOINS_UN_DETAIL_OPERATION_REQUIS);
		}

		Set<Integer> sequences = new HashSet<>();
		Long montantTotalEnCentimes = 0L;
		for ( DetailOperation detailOperation : detailsOperation ) {
			if ( sequences.contains(detailOperation.getSequence()) ) {
				throw new ServiceException(
						ErreurFonctionnelle.OPERATION_LISTE_DETAIL_NUMERO_SEQUENCE_DUPLIQUEE,
						detailOperation.getSequence());
			}
			sequences.add(detailOperation.getSequence());
			montantTotalEnCentimes += detailOperation.getMontantDetailEnCentimes();
		}
		if ( ! montantTotalEnCentimes.equals(operation.getMontantTotalEnCentimes()) ) {
			throw new ServiceException(
					ErreurFonctionnelle.OPERATION_LISTE_DETAIL_SOMME_MONTANTS_ERRONEE,
					operation.getMontantTotalEnCentimes(), 
					montantTotalEnCentimes);
		}
	}

	private Long calculerSolde(
			CompteInterne compteInterne,
			ZonedDateTime dateFin) throws ServiceException {

		Long soldeInitial = compteInterne.getMontantSoldeInitialEnCentimes();
		
		// Calcul du montant des dépenses entre date début et date fin
		List<Operation> operationsDepense = rechercherOperationDepenseParCompteIdEntreDateDebutEtDateFin(
				compteInterne.getId(),
				compteInterne.getDateSoldeInitial(),
				dateFin);
		Long montantTotalDepenseEnCentimes = 0L;
		for ( Operation operation : operationsDepense) {
			if ( operation.getTypeOperation() == TypeOperation.MOINS_VALUE 
					|| operation.getTypeOperation() == TypeOperation.PLUS_VALUE ) {
				continue;
			}
			montantTotalDepenseEnCentimes += operation.getMontantTotalEnCentimes();
		}

		// Calcul du montant des recettes entre date début et date fin
		List<Operation> operationsRecette = rechercherOperationRecetteParCompteIdEntreDateDebutEtDateFin(
				compteInterne.getId(),
				compteInterne.getDateSoldeInitial(),
				dateFin);
		Long montantTotalRecetteEnCentimes = 0L;
		for ( Operation operation : operationsRecette) {
			if ( operation.getTypeOperation() == TypeOperation.MOINS_VALUE 
					|| operation.getTypeOperation() == TypeOperation.PLUS_VALUE ) {
				continue;
			}
			montantTotalRecetteEnCentimes += operation.getMontantTotalEnCentimes();
		}

		return soldeInitial
				+ montantTotalRecetteEnCentimes
				- montantTotalDepenseEnCentimes;
	}
}
