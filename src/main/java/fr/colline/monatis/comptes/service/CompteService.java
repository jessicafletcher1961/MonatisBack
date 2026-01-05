package fr.colline.monatis.comptes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.CompteFonctionnelleErreur;
import fr.colline.monatis.comptes.CompteTechniqueErreur;
import fr.colline.monatis.comptes.model.Compte;
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

	public List<T> rechercherTous(Sort tri) throws ServiceException {

		Assert.notNull(tri, () -> "Le TRI pour la recherche de tous les comptes de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			return getRepository().findAll(tri);
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
					CompteTechniqueErreur.COMPTAGE_USAGE_PAR_ID,
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
	
	public abstract Class<T> getTClass();
	public abstract CompteRepository<T> getRepository();
}
