package fr.colline.monatis.comptes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.repository.CompteRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;
import fr.colline.monatis.exceptions.erreurs.ErreurTechnique;
import jakarta.annotation.Nullable;

public abstract class CompteService<T extends Compte> {

	public T rechercherParId(Long compteId) throws ServiceException {

		if ( compteId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		try {
			Optional<T> optional = getRepository().findById(compteId);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_COMPTE_PAR_ID,
					compteId,
					getTClass().getSimpleName() );
		}
	}

	public boolean isExistantParId(Long compteId) throws ServiceException {

		if ( compteId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		try {
			return this.getRepository().existsById(compteId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_COMPTE_PAR_ID,
					compteId,
					getTClass().getSimpleName() );
		}
	}

	public T rechercherParIdentifiant(String identifiant) throws ServiceException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ServiceException(
					ErreurProgrammation.IDENTIFIANT_NULL,
					getTClass().getSimpleName());
		}
		
		try {
			Optional<T> optional = getRepository().findByIdentifiant(identifiant);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_COMPTE_PAR_IDENTIFIANT,
					getTClass().getSimpleName(),
					identifiant);
		}
	}

	public boolean isExistantParIdentifiant(String identifiant) throws ServiceException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ServiceException(
					ErreurProgrammation.IDENTIFIANT_NULL,
					getTClass().getSimpleName());
		}
		
		try {
			return this.getRepository().existsByIdentifiant(identifiant);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_COMPTE_PAR_IDENTIFIANT,
					getTClass().getSimpleName(),
					identifiant );
		}
	}

	public List<T> rechercherTous() throws ServiceException{

		try {
			return getRepository().findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_COMPTE_TOUS,
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
					ErreurTechnique.TECH_RECHERCHE_COMPTE_TOUS,
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
					ErreurTechnique.TECH_SUPPRESSION_COMPTE_TOUS,
					getTClass().getSimpleName());
		}
	}

	public final T creerCompte(T compte) throws ServiceException {

		if ( compte == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					getTClass().getSimpleName());
		}

		compte = controlerEtPreparerPourCreation(compte);

		return enregistrer(compte);
	}

	public final T modifierCompte(T compte) throws ServiceException {

		if ( compte == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					getTClass().getSimpleName());
		}

		compte = controlerEtPreparerPourModification(compte);

		return enregistrer(compte);
	}

	public final void supprimerCompte(Long compteId) throws ServiceException {

		if ( compteId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		T compte = controlerEtPreparerPourSuppression(compteId);

		supprimer(compte);
	}

	protected T enregistrer(T compte) throws ServiceException {

		if ( compte == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					getTClass().getSimpleName());
		}
		
		try {
			return getRepository().save(compte);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_ENREGISTREMENT_COMPTE,
					getTClass().getSimpleName(),
					compte.getIdentifiant());
		}
	}

	protected void supprimer(T compte) throws ServiceException {

		if ( compte == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					getTClass().getSimpleName());
		}

		try {
			this.getRepository().delete(compte);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_SUPPRESSION_COMPTE,
					getTClass().getSimpleName(),
					compte.getIdentifiant());
		}
	}

	protected T controlerEtPreparerPourCreation(T compte) throws ServiceException {

		if ( compte == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					getTClass().getSimpleName());
		}
		
		verifierCompteNonEnregistre(compte.getId());
		verifierIdentifiantValideEtUnique(compte.getId(), compte.getIdentifiant());
		
		return compte;
	}
	
	protected T controlerEtPreparerPourModification(T compte) throws ServiceException {

		if ( compte == null ) {
			throw new ServiceException(
					ErreurProgrammation.COMPTE_NULL,
					getTClass().getSimpleName());
		}

		verifierCompteEnregistre(compte.getId());
		verifierIdentifiantValideEtUnique(compte.getId(), compte.getIdentifiant());

		return compte;
	}

	protected T controlerEtPreparerPourSuppression(Long compteId) throws ServiceException {

		if ( compteId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL,
					getTClass().getSimpleName());
		}

		verifierCompteEnregistre(compteId);
		verifierAbsenceOperationAssociee(compteId);
		
		return rechercherParId(compteId);
	}

	private void verifierCompteEnregistre(Long compteId) throws ServiceException {

		if ( compteId == null || ! isExistantParId(compteId) ) {
			throw new ServiceException (
					ErreurFonctionnelle.COMPTE_NON_ENREGISTRE_PAR_ID,
					getTClass().getSimpleName(),
					compteId);
		}
	}
	
	private void verifierCompteNonEnregistre(Long compteId) throws ServiceException {
		
		if ( compteId != null && isExistantParId(compteId) ) {
			throw new ServiceException (
					ErreurFonctionnelle.COMPTE_DEJA_ENREGISTRE_PAR_ID,
					compteId,
					getTClass().getSimpleName());
		}
	}

	private void verifierIdentifiantValideEtUnique(
			@Nullable Long compteId,
			String compteIdentifiant) 
					throws ServiceException {

		if ( compteIdentifiant == null || compteIdentifiant.isBlank() ) {
			throw new ServiceException (
					ErreurFonctionnelle.COMPTE_IDENTIFIANT_INVALIDE,
					getTClass().getSimpleName());
		}

		boolean isIdentifiantCreeOuModifie;
		boolean isIdentifiantDejaUtilise;
		
		if ( compteId == null ) {
			isIdentifiantCreeOuModifie = true;
			isIdentifiantDejaUtilise = isExistantParIdentifiant(compteIdentifiant);
		}
		else {
			try {
				isIdentifiantCreeOuModifie = ! getRepository().existsByIdentifiantAndId(compteIdentifiant, compteId);
				isIdentifiantDejaUtilise = getRepository().existsByIdentifiantAndIdNot(compteIdentifiant, compteId); 
			}
			catch ( Throwable t ) {
				throw new ServiceException (
						t,
						ErreurTechnique.TECH_EXISTANCE_COMPTE_PAR_IDENTIFIANT,
						compteIdentifiant);
			}
		}
		
		if ( isIdentifiantCreeOuModifie && isIdentifiantDejaUtilise )
		{
			throw new ServiceException (
					ErreurFonctionnelle.COMPTE_IDENTIFIANT_DEJA_UTILISE,
					getTClass().getSimpleName(),
					compteIdentifiant);
		}
	}
	
	private void verifierAbsenceOperationAssociee(Long compteId) throws ServiceException {
	
		int nombreOperationAssociee;
		try {
			nombreOperationAssociee = getRepository().countOperationByCompteId(compteId);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_NOMBRE_OPERATION_PAR_COMPTE_ID,
					compteId);
		}
		
		if ( nombreOperationAssociee > 0 ) {
			throw new ServiceException (
					ErreurFonctionnelle.COMPTE_SUPPRESSION_AVEC_OPERATION,
					compteId,
					nombreOperationAssociee);
		}
	}
	
	protected abstract Class<T> getTClass();
	protected abstract CompteRepository<T> getRepository();
}
