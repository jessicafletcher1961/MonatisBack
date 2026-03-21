package fr.colline.monatis.references.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;
import fr.colline.monatis.exceptions.erreurs.ErreurTechnique;
import fr.colline.monatis.model.references.Reference;
import fr.colline.monatis.references.repository.ReferenceRepository;

public abstract class ReferenceService<T extends Reference> {

	public T rechercherParId(Long referenceId) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		try {
			Optional<T> optional = getRepository().findById(referenceId);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_REFERENCE_PAR_ID,
					getTClass().getSimpleName(),
					referenceId );
		}
	}

	public boolean isExistantParId(Long referenceId) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		try {
			return getRepository().existsById(referenceId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_REFERENCE_PAR_ID,
					getTClass().getSimpleName(),
					referenceId );
		}
	}

	public T rechercherParNom(String nom) throws ServiceException {

		if ( nom == null || nom.isBlank() ) {
			throw new ServiceException(
					ErreurProgrammation.NOM_NULL,
					getTClass().getSimpleName());
		}

		try {
			Optional<T> optional = getRepository().findByNom(nom);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_REFERENCE_PAR_NOM,
					getTClass().getSimpleName(),
					nom);
		}
	}
	
	public boolean isExistantParNom(String nom) throws ServiceException {

		if ( nom == null || nom.isBlank() ) {
			throw new ServiceException(
					ErreurProgrammation.NOM_NULL,
					getTClass().getSimpleName());
		}

		try {
			return getRepository().existsByNom(nom);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_REFERENCE_PAR_NOM,
					getTClass().getSimpleName(),
					nom);
		}
	}

	public List<T> rechercherTous() throws ServiceException {

		try {
			return getRepository().findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_REFERENCE_TOUS,
					getTClass().getSimpleName());
		}
	}

	public List<T> rechercherTous(Sort tri) throws ServiceException {

		if ( tri == null ) {
			throw new ServiceException(
					ErreurProgrammation.TRI_NULL,
					getTClass().getSimpleName());
		}

		try {
			return getRepository().findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_REFERENCE_TOUS,
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
					ErreurTechnique.TECH_SUPPRESSION_REFERENCE_TOUS,
					getTClass().getSimpleName());
		}
	}

	public final T creerReference(T reference) throws ServiceException {

		if ( reference == null ) {
			throw new ServiceException(
					ErreurProgrammation.REFERENCE_NULL,
					getTClass().getSimpleName());
		}

		reference = controlerEtPreparerPourCreation(reference);

		return enregistrer(reference);
	}

	public final T modifierReference(T reference) throws ServiceException {

		if ( reference == null ) {
			throw new ServiceException(
					ErreurProgrammation.REFERENCE_NULL,
					getTClass().getSimpleName());
		}

		reference = controlerEtPreparerPourModification(reference);
		
		return enregistrer(reference);
	}

	public final void supprimerReference(Long referenceId) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		T reference = controlerEtPreparerPourSuppression(referenceId);

		supprimer(reference);
	}

	protected T enregistrer(T reference) throws ServiceException {

		if ( reference == null ) {
			throw new ServiceException(
					ErreurProgrammation.REFERENCE_NULL,
					getTClass().getSimpleName());
		}

		try {
			return this.getRepository().save(reference);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_ENREGISTREMENT_REFERENCE,
					getTClass().getSimpleName(),
					reference.getNom());
		}
	}

	protected void supprimer(T reference) throws ServiceException {

		if ( reference == null ) {
			throw new ServiceException(
					ErreurProgrammation.REFERENCE_NULL,
					getTClass().getSimpleName());
		}

		try {
			this.getRepository().delete(reference);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_SUPPRESSION_REFERENCE,
					getTClass().getSimpleName(),
					reference.getNom());
		}
	}

	protected T controlerEtPreparerPourCreation(T reference) throws ServiceException {

		if ( reference == null ) {
			throw new ServiceException(
					ErreurProgrammation.REFERENCE_NULL,
					getTClass().getSimpleName());
		}

		verifierReferenceNonEnregistree(reference.getId());
		verifierNomValideEtUnique(reference.getId(), reference.getNom());
		
		return reference;
	}

	protected T controlerEtPreparerPourModification(T reference) throws ServiceException {

		if ( reference == null ) {
			throw new ServiceException(
					ErreurProgrammation.REFERENCE_NULL,
					getTClass().getSimpleName());
		}

		verifierReferenceEnregistree(reference.getId());
		verifierNomValideEtUnique(reference.getId(), reference.getNom());
		
		return reference;
	}

	protected T controlerEtPreparerPourSuppression(Long referenceId) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		verifierReferenceEnregistree(referenceId);

		return rechercherParId(referenceId);
	}

	private void verifierReferenceEnregistree(Long referenceId) throws ServiceException {
		
		if ( referenceId == null || ! isExistantParId(referenceId) ) {
			throw new ServiceException (
					ErreurFonctionnelle.REFERENCE_NON_ENREGISTREE_PAR_ID,
					getTClass().getSimpleName(),
					referenceId);
		}
	}

	private void verifierReferenceNonEnregistree(Long referenceId) throws ServiceException {
	
		if ( referenceId != null && isExistantParId(referenceId) ) {
			throw new ServiceException (
					ErreurFonctionnelle.REFERENCE_DEJA_ENREGISTREE_PAR_ID,
					getTClass().getSimpleName(),
					referenceId);
		}
	}
	
	private void verifierNomValideEtUnique(
			Long referenceId, 
			String referenceNom) throws ServiceException {

		if ( referenceNom == null || referenceNom.isBlank() ) {
			throw new ServiceException (
					ErreurFonctionnelle.REFERENCE_NOM_INVALIDE,
					getTClass().getSimpleName());
		}
		
		boolean isNomCreeOuModifie;
		boolean isNomDejaUtilise;
		
		if ( referenceId == null ) {
			// En cours création
			isNomCreeOuModifie = true;
			isNomDejaUtilise = isExistantParNom(referenceNom);
		}
		else {
			try {
				// En cours modification
				isNomCreeOuModifie = ! getRepository().existsByNomAndId(referenceNom, referenceId);
				isNomDejaUtilise = getRepository().existsByNomAndIdNot(referenceNom, referenceId);
			}
			catch ( Throwable t ) {
				throw new ServiceException (
						t,
						ErreurTechnique.TECH_EXISTANCE_REFERENCE_PAR_NOM,
						referenceNom);
			}
		}

		if ( isNomCreeOuModifie && isNomDejaUtilise ) {
			throw new ServiceException (
					ErreurFonctionnelle.REFERENCE_NOM_DEJA_UTILISE,
					getTClass().getSimpleName(),
					referenceNom);
		}
	}
	
	protected abstract Class<T> getTClass();
	protected abstract ReferenceRepository<T> getRepository();
}
