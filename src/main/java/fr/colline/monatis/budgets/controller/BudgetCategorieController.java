package fr.colline.monatis.budgets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.references.service.ReferenceService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/budgets/categorie")
@Transactional
public class BudgetCategorieController extends BudgetController<Categorie> {

	@Autowired private ControllerVerificateurService verificateur;
	@Autowired private CategorieService categorieService;

	@Override
	protected ReferenceService<Categorie> getReferenceService() {
		return categorieService;
	}

	@Override
	protected Categorie getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException {
		return verificateur.verifierCategorie(nomReference, obligatoire);
	}

}
