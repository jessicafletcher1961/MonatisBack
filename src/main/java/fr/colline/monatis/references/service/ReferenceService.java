package fr.colline.monatis.references.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.ReferenceTechniqueErreur;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.references.repository.ReferenceRepository;

@Service
public abstract class ReferenceService<T extends Reference> {
	
	public T rechercherParId(Long id) throws ServiceException {

		Assert.notNull(id,() -> "L'ID pour la recherche d'une référence de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			Optional<T> optional = getRepository().findById(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ReferenceTechniqueErreur.RECHERCHE_PAR_ID,
					getTClass().getSimpleName(),
					id );
		}
	}

	public boolean isExistantParId(Long id) throws ServiceException {

		Assert.notNull(id,() -> "L'ID pour la vérification de l'existence d'une référence de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			return getRepository().existsById(id);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ReferenceTechniqueErreur.EXISTENCE_PAR_ID,
					getTClass().getSimpleName(),
					id );
		}
	}

	public T rechercherParNom(String nom) throws ServiceException {

		Assert.notNull(nom,() -> "Le NOM pour la recherche d'une référence de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			Optional<T> optional = getRepository().findByNom(nom);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ReferenceTechniqueErreur.RECHERCHE_PAR_NOM,
					getTClass().getSimpleName(),
					nom);
		}
	}
	
	public boolean isExistantParNom(String nom) throws ServiceException {

		Assert.notNull(nom,() -> "Le NOM pour la vérification de l'existence d'une référence de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			return getRepository().existsByNom(nom);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ReferenceTechniqueErreur.EXISTENCE_PAR_NOM,
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
					ReferenceTechniqueErreur.RECHERCHE_TOUS,
					getTClass().getSimpleName());
		}
	}

	public List<T> rechercherTous(Sort tri) throws ServiceException {

		Assert.notNull(tri,() -> "Le TRI pour la recherche des références de type '" + getTClass().getSimpleName() + "' est obligatoire");

		try {
			return getRepository().findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ReferenceTechniqueErreur.RECHERCHE_TOUS,
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
					ReferenceTechniqueErreur.SUPPRESSION_TOUS,
					getTClass().getSimpleName());
		}
	}

	public final T creerReference(T reference) throws ServiceException {

		Assert.notNull(reference,() -> "La REFERENCE à créer de type '" + getTClass().getSimpleName() + "' est obligatoire");

		reference = controlerEtPreparerPourCreation(reference);

		return enregistrer(reference);
	}

	public final T modifierReference(T reference) throws ServiceException {

		Assert.notNull(reference,() -> "La REFERENCE à modifier de type '" + getTClass().getSimpleName() + "' est obligatoire");

		reference = controlerEtPreparerPourModification(reference);
		
		return enregistrer(reference);
	}

	public final void supprimerReference(T reference) throws ServiceException {

		Assert.notNull(reference,() -> "La REFERENCE à supprimer de type '" + getTClass().getSimpleName() + "' est obligatoire");

		reference = controlerEtPreparerPourSuppression(reference);

		supprimer(reference);
	}

	protected T controlerEtPreparerPourCreation(T reference) throws ServiceException {

		return reference;
	}

	protected T controlerEtPreparerPourModification(T reference) throws ServiceException {

		return reference;
	}

	protected T controlerEtPreparerPourSuppression(T reference) throws ServiceException {

		return reference;
	}

	private T enregistrer(T reference) throws ServiceException {

		try {
			return this.getRepository().save(reference);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ReferenceTechniqueErreur.ENREGISTREMENT,
					getTClass().getSimpleName(),
					reference.getNom());
		}
	}

	private void supprimer(T reference) throws ServiceException {

		try {
			this.getRepository().delete(reference);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ReferenceTechniqueErreur.SUPPRESSION,
					getTClass().getSimpleName(),
					reference.getNom());
		}
	}

	public abstract Class<T> getTClass();
	public abstract ReferenceRepository<T> getRepository();
}
