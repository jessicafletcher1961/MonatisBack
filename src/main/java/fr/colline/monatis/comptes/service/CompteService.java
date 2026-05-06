package fr.colline.monatis.comptes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.CompteFonctionnelleErreur;
import fr.colline.monatis.comptes.CompteTechniqueErreur;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.repository.CompteRepository;
import fr.colline.monatis.exceptions.ServiceException;

@Service
public abstract class CompteService<T extends Compte> {
	
	public T rechercherParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la recherche d'un compte de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			Optional<T> optional = getRepository().findById(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					CompteTechniqueErreur.RECHERCHE_PAR_ID,
					getTClass().getSimpleName(),
					id );
		}
	}

	public boolean isExistantParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la vérification de l'existence d'un compte de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			return this.getRepository().existsById(id);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					CompteTechniqueErreur.EXISTENCE_PAR_ID,
					getTClass().getSimpleName(),
					id);
		}
	}

	public T rechercherParIdentifiant(String identifiant) throws ServiceException {

		Assert.notNull(identifiant, () -> "L'IDENTIFIANT pour la recherche d'un compte de type '" + getTClass().getSimpleName() + "' est obligatoire");
		
		try {
			Optional<T> optional = getRepository().findByIdentifiant(identifiant);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					CompteTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL,
					getTClass().getSimpleName(),
					identifiant);
		}
	}

	public boolean isExistantParIdentifiant(String identifiant) throws ServiceException {

		Assert.notNull(identifiant, () -> "L'IDENTIFIANT pour la vérification de l'existence d'un compte de type '" + getTClass().getSimpleName() + "' est obligatoire");
		
		try {
			return this.getRepository().existsByIdentifiant(identifiant);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					CompteTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL,
					getTClass().getSimpleName(),
					identifiant);
		}
	}

	public List<T> rechercherTous() throws ServiceException{

		try {
			return getRepository().findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					CompteTechniqueErreur.RECHERCHE_TOUS,
					getTClass().getSimpleName());
		}
	}

	public void supprimerTous() throws ServiceException {

		try {
			getRepository().deleteAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					CompteTechniqueErreur.SUPPRESSION_TOUS,
					getTClass().getSimpleName());
		}
	}

	public final T creerCompte(T compte) throws ServiceException {

		Assert.notNull(compte, () -> "Le COMPTE à créer de type '" + getTClass().getSimpleName() + "' est obligatoire");

		compte = controlerEtPreparerPourCreation(compte);

		return enregistrer(compte);
	}

	public final T modifierCompte(T compte) throws ServiceException {

		Assert.notNull(compte, () -> "Le COMPTE à modifier de type '" + getTClass().getSimpleName() + "' est obligatoire");

		compte = controlerEtPreparerPourModification(compte);

		return enregistrer(compte);
	}

	public final void supprimerCompte(T compte) throws ServiceException {

		Assert.notNull(compte, () -> "Le COMPTE à supprimer de type '" + getTClass().getSimpleName() + "' est obligatoire");

		compte = controlerEtPreparerPourSuppression(compte);

		supprimer(compte);
	}

	protected T controlerEtPreparerPourCreation(T compte) throws ServiceException {

		return compte;
	}
	
	protected T controlerEtPreparerPourModification(T compte) throws ServiceException {

		return compte;
	}

	protected T controlerEtPreparerPourSuppression(T compte) throws ServiceException {

		verifierAbsenceOperationAssociee(compte.getId());
		
		return compte;
	}

	private T enregistrer(T compte) throws ServiceException {

		try {
			return getRepository().save(compte);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					CompteTechniqueErreur.ENREGISTREMENT,
					compte.getIdentifiant());
		}
	}

	private void supprimer(T compte) throws ServiceException {

		try {
			this.getRepository().delete(compte);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					CompteTechniqueErreur.SUPPRESSION,
					compte.getIdentifiant());
		}
	}
	
	private void verifierAbsenceOperationAssociee(Long id) throws ServiceException {
	
		int nombreOperationAssociee;
		try {
			nombreOperationAssociee = getRepository().compterOperationByCompteId(id);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					CompteTechniqueErreur.COMPTAGE_USAGE_OPERATION_PAR_ID,
					getTClass().getSimpleName(),
					id);
		}
		
		if ( nombreOperationAssociee > 0 ) {
			throw new ServiceException (
					CompteFonctionnelleErreur.SUPPRESSION_COMPTE_AVEC_OPERATION,
					id,
					nombreOperationAssociee);
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
	 * @param dateCible
	 * @return
	 */
	public static LocalDate prendreDateSoldeInitialAuBesoin(Compte compte, LocalDate dateCible) {
	
		if ( dateCible != null && CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;
			LocalDate dateSoldeInitial = compteInterne.getDateSoldeInitial();
			
			// La date de début pour la prise en compte des opération est au minimum la date du solde initial.
			// En effet, toutes les opérations jusqu'à la veille du jour de la "création" du compte doivent
			// être ignorées
			if ( dateCible.isBefore(dateSoldeInitial) ) {
				dateCible = dateSoldeInitial;
			}
		}

		return dateCible;
	}

	/**
	 * Si le compte n'est pas un compte interne, retourne la date indiquée.</br>
	 * Si le compte est un compte interne :<ul>
	 *  <li>si la date passée est à null, retourne la date de clôture (qui peut être à null).</li>
	 *  <li>si la date passée n'est pas à null, retourne  soit la date indiquée, soit la date de
	 *  	clôture du compte si elle existe et qu'elle est avant la date indiquée.</li></ul> 
	 *  
	 * @param compte
	 * @param dateFin
	 * @return
	 */
	public static LocalDate prendreDateClotureAuBesoin(Compte compte, LocalDate dateCible) {
	
		if ( dateCible != null && CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;
			LocalDate dateCloture = compteInterne.getDateCloture();
			
			// La date de fin pour la prise en compte des opération est au maximum la date de clôture.
			// En effet, toutes les opérations à partir de cette date doivent être ignorées (normalement,
			// il ne peut pas y en avoir mais sait-on jamais)
			if ( dateCloture != null && dateCible.isAfter(dateCloture) ) {
				dateCible = dateCloture;
			}
		}

		return dateCible;
	}

	public static boolean isDateCibleVisible(Compte compte, LocalDate dateCible) {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			
			CompteInterne compteInterne = (CompteInterne) compte;
			LocalDate dateSoldeInitial = compteInterne.getDateSoldeInitial();
			LocalDate dateCloture = compteInterne.getDateCloture();
			
			if ( dateCible == null
					|| dateSoldeInitial.isAfter(dateCible) 
					|| (dateCloture != null && dateCloture.isBefore(dateCible)) ) {
				return false;
			}
		}

		return true;
	}
	
	public abstract Class<T> getTClass();
	public abstract CompteRepository<T> getRepository();
}
