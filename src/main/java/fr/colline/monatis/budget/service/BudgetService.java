package fr.colline.monatis.budget.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budget.model.Budget;
import fr.colline.monatis.budget.repository.BudgetRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;
import fr.colline.monatis.exceptions.erreurs.ErreurTechnique;
import fr.colline.monatis.model.references.Reference;

@Service
public abstract class BudgetService<T extends Budget> {

	public T rechercherParId(Long budgetId) throws ServiceException {

		if ( budgetId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Budget.class.getSimpleName());
		}

		try {
			Optional<T> optional = getRepository().findById(budgetId);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_BUDGET_PAR_ID,
					budgetId );
		}
	}

	public boolean isExistantParId(Long budgetId) throws ServiceException {

		if ( budgetId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Budget.class.getSimpleName());
		}

		try {
			return this.getRepository().existsById(budgetId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_BUDGET_PAR_ID,
					budgetId);
		}
	}

	public ArrayList<T> rechercherParReferenceId(Long referenceId) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Reference.class.getSimpleName());
		}

		try {
			return getRepository().findByReferenceIdOrderByDateFinDesc(referenceId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_LISTE_BUDGET_PAR_REFERENCE_ID,
					referenceId);
		}
	}

	public boolean isExistantParReferenceId(Long referenceId) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Reference.class.getSimpleName());
		}

		try {
			return getRepository().existsByReferenceId(referenceId);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_LISTE_BUDGET_PAR_REFERENCE_ID,
					referenceId);
		}
	}

	public T rechercherParReferenceIdEtDateCible(Long referenceId, ZonedDateTime dateCible) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Reference.class.getSimpleName());
		}
		if ( dateCible == null ) {
			throw new ServiceException(
					ErreurProgrammation.DATE_BUDGET_NULL);
		}
		
		try {
			Optional<T> optional = getRepository().findByReferenceIdAndDateCibleBetweenDateDebutAndDateFin(referenceId, dateCible);
			return optional.isEmpty() ? null : optional.get(); 
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_BUDGET_PAR_REFERENCE_ID_ET_DATE_CIBLE,
					referenceId,
					dateCible.format(DateTimeFormatter.ISO_DATE));
		}
	}

	public boolean isExistantParReferenceIdEtDateCible(Long referenceId, ZonedDateTime dateCible) throws ServiceException {

		if ( referenceId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Reference.class.getSimpleName());
		}
		if ( dateCible == null ) {
			throw new ServiceException(
					ErreurProgrammation.DATE_BUDGET_NULL);
		}

		try {
			return getRepository().existsByReferenceIdAndDateCibleBetweenDateDebutAndDateFin(referenceId, dateCible);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_EXISTANCE_BUDGET_PAR_REFERENCE_ID_ET_DATE_CIBLE,
					referenceId,
					dateCible.format(DateTimeFormatter.ISO_DATE));
		}
	}

	public List<T> rechercherTous() throws ServiceException {

		try {
			return getRepository().findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_BUDGET_TOUS);
		}
	}
	
	public List<T> rechercherTous(Sort tri) throws ServiceException {
		
		if ( tri == null ) {
			throw new ServiceException(
					ErreurProgrammation.TRI_NULL,
					Budget.class.getSimpleName());
		}

		try {
			return getRepository().findAll(tri);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_RECHERCHE_BUDGET_TOUS);
		}
	}
	
	public void supprimerTous() throws ServiceException {

		try {
			getRepository().deleteAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_SUPPRESSION_BUDGET_TOUS);
		}
	}
	
	public T creerBudget(T budget) throws ServiceException {

		if ( budget == null ) {
			throw new ServiceException(
					ErreurProgrammation.BUDGET_NULL);
		}

		budget = controlerEtPreparerPourCreation(budget);

		return enregistrer(budget);
	}

	public T reconduireBudget(T budget) throws ServiceException {

		if ( budget == null ) {
			throw new ServiceException(
					ErreurProgrammation.BUDGET_NULL);
		}

		budget = controlerEtPreparerPourReconduction(budget);

		return enregistrer(budget);
	}

	public void supprimerBudget(Long budgetId) throws ServiceException {

		if ( budgetId == null ) {
			throw new ServiceException(
					ErreurProgrammation.ID_NULL, 
					Budget.class.getSimpleName());
		}
		T budget = controlerEtPreparerPourSuppression(budgetId);
		
		supprimer(budget);
	}
	
	private T enregistrer(T budget) throws ServiceException {

		try {
			return getRepository().save(budget);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_ENREGISTREMENT_BUDGET,
					budget.getTypeBudget().getLibelle(),
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom(),
					budget.getDateDebut().format(DateTimeFormatter.ISO_DATE));
		}
	}

	private void supprimer(T budget) throws ServiceException {

		try {
			getRepository().delete(budget);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					ErreurTechnique.TECH_SUPPRESSION_BUDGET,
					budget.getTypeBudget().getLibelle(),
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom(),
					budget.getDateDebut().format(DateTimeFormatter.ISO_DATE));
		}
	}

	private T controlerEtPreparerPourCreation(T budget) throws ServiceException {

		if ( isExistantParReferenceId(budget.getReference().getId()) ) {
			throw new ServiceException(
					ErreurFonctionnelle.BUDGET_CREATION_AVEC_HISTORIQUE,
					budget.getTypeBudget().getLibelle(),
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom());
		}
		
		budget.setDateDebut(arrondirDateDebut(budget));
		budget.setDateFin(calculerDateFin(budget));
		
		return budget;
	}

	private T controlerEtPreparerPourReconduction(T budget) throws ServiceException {

		Reference reference = budget.getReference();

		ZonedDateTime dateCible = budget.getDateDebut();
		
		ArrayList<T> historique = rechercherParReferenceId(reference.getId());
		if ( historique == null || historique.isEmpty() ) {
			throw new ServiceException(
					ErreurFonctionnelle.BUDGET_RECONDUCTION_SANS_HISTORIQUE,
					budget.getTypeBudget().getLibelle(),
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom());
		}

		Budget dernierBudget = historique.get(0);
		if ( dateCible.isBefore(dernierBudget.getDateFin())) {
			throw new ServiceException(
					ErreurFonctionnelle.BUDGET_RECONDUCTION_INUTILE, 
					budget.getTypeBudget().getLibelle(),
					budget.getReference().getClass().getSimpleName(),
					budget.getReference().getNom(),
					budget.getDateDebut().format(DateTimeFormatter.ISO_DATE));
		}
		
		boolean trouve = false;
		while ( !trouve ) {
			T nouveauBudget = getCopie(budget);
			nouveauBudget.setDateDebut(dernierBudget.getDateFin());
			nouveauBudget.setDateFin(calculerDateFin(nouveauBudget));
			
			if ( dateCible.isBefore(nouveauBudget.getDateFin()) ) {
				// Le nouveau budget contient la date cible : fin de la création des budgets intermédiaires
				budget.setDateDebut(nouveauBudget.getDateDebut());
				budget.setDateFin(nouveauBudget.getDateFin());
				trouve = true;
			}
			else {
				// Création d'un budget intermédiaire
				dernierBudget = enregistrer(nouveauBudget);
			}
		}
		
		return budget;
	}

	private T controlerEtPreparerPourSuppression(Long budgetId) throws ServiceException {
		
		T budget = rechercherParId(budgetId);
		Reference reference = budget.getReference();
		ArrayList<T> historique = rechercherParReferenceId(reference.getId());

		for ( T dernierBudget : historique ) {
			if ( budgetId.equals(dernierBudget.getId()) ) {
				break;
			}
			// Suppression des budgets ultérieurs au budget à supprimer
			supprimer(dernierBudget);
		}
		
		return budget;
	}
	
	private ZonedDateTime arrondirDateDebut(T budget) throws ServiceException {

		ZonedDateTime dateDebut;
		
		switch(budget.getTypeBudget()) {
		case ANNUEL:
			dateDebut = ZonedDateTime.of(
					budget.getDateDebut().getYear(),
					1, 1,
					0, 0, 0, 0,
					budget.getDateDebut().getZone());
			break;
		case MENSUEL:
			dateDebut = ZonedDateTime.of(
					budget.getDateDebut().getYear(),
					budget.getDateDebut().getMonthValue(), 
					1,
					0, 0, 0, 0,
					budget.getDateDebut().getZone());
			break;
		default:
			throw new ServiceException(
					ErreurProgrammation.TYPE_BUDGET_NON_GERE,
					budget.getTypeBudget().getCode(),
					budget.getTypeBudget().getLibelle());
		}

		return dateDebut;
	}
	
	private ZonedDateTime calculerDateFin(T budget) throws ServiceException {

		ZonedDateTime dateDebut = arrondirDateDebut(budget);

		ZonedDateTime dateFin;
		switch(budget.getTypeBudget()) {
		case ANNUEL:
			dateFin = dateDebut.plusYears(1);
			break;
		case MENSUEL:
			dateFin = dateDebut.plusMonths(1);
			break;
		default:
			throw new ServiceException(
					ErreurProgrammation.TYPE_BUDGET_NON_GERE,
					budget.getTypeBudget().getCode(),
					budget.getTypeBudget().getLibelle());
		}

		return dateFin;
	}
	
	protected abstract T getCopie(T budget) throws ServiceException;
	protected abstract BudgetRepository<T> getRepository();
}


