package fr.colline.monatis.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budget.model.BudgetCategorie;
import fr.colline.monatis.budget.repository.BudgetCategorieRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;

@Service
public class BudgetCategorieService extends BudgetService<BudgetCategorie> {

	@Autowired private BudgetCategorieRepository budgetCategorieRepository;

	@Override
	protected BudgetCategorieRepository getRepository() {
		return budgetCategorieRepository;
	}

	@Override
	protected BudgetCategorie getCopie(BudgetCategorie original) throws ServiceException {
		
		if ( original == null ) {
			throw new ServiceException(
					ErreurProgrammation.BUDGET_NULL);
		}
		
		BudgetCategorie copie = new BudgetCategorie();

		copie.setTypeBudget(original.getTypeBudget());
		copie.setDateDebut(original.getDateDebut());
		copie.setDateFin(original.getDateFin());
		copie.setMontantBudgetEnCentimes(original.getMontantBudgetEnCentimes());
		copie.setReference(original.getReference());
		
		return copie;
	}
}
