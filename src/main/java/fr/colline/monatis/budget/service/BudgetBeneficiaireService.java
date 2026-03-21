package fr.colline.monatis.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budget.model.BudgetBeneficiaire;
import fr.colline.monatis.budget.repository.BudgetBeneficiaireRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;

@Service
public class BudgetBeneficiaireService extends BudgetService<BudgetBeneficiaire> {

	@Autowired 
	private BudgetBeneficiaireRepository budgetBeneficiaireRepository;

	@Override
	protected BudgetBeneficiaireRepository getRepository() {
		return budgetBeneficiaireRepository;
	}

	@Override
	protected BudgetBeneficiaire getCopie(BudgetBeneficiaire original) throws ServiceException {
		
		if ( original == null ) {
			throw new ServiceException(
					ErreurProgrammation.BUDGET_NULL);
		}
		
		BudgetBeneficiaire copie = new BudgetBeneficiaire();

		copie.setTypeBudget(original.getTypeBudget());
		copie.setDateDebut(original.getDateDebut());
		copie.setDateFin(original.getDateFin());
		copie.setMontantBudgetEnCentimes(original.getMontantBudgetEnCentimes());
		copie.setReference(original.getReference());
		
		return copie;
	}
}
