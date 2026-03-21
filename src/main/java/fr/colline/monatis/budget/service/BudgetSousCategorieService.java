package fr.colline.monatis.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budget.model.BudgetSousCategorie;
import fr.colline.monatis.budget.repository.BudgetSousCategorieRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;

@Service
public class BudgetSousCategorieService extends BudgetService<BudgetSousCategorie> {

	@Autowired private BudgetSousCategorieRepository budgetSousCategorieRepository; 

	@Override
	protected BudgetSousCategorieRepository getRepository() {
		return budgetSousCategorieRepository;
	}

	@Override
	protected BudgetSousCategorie getCopie(BudgetSousCategorie original) throws ServiceException {
		
		if ( original == null ) {
			throw new ServiceException(
					ErreurProgrammation.BUDGET_NULL);
		}
		
		BudgetSousCategorie copie = new BudgetSousCategorie();

		copie.setTypeBudget(original.getTypeBudget());
		copie.setDateDebut(original.getDateDebut());
		copie.setDateFin(original.getDateFin());
		copie.setMontantBudgetEnCentimes(original.getMontantBudgetEnCentimes());
		copie.setReference(original.getReference());
		
		return copie;
	}
}
