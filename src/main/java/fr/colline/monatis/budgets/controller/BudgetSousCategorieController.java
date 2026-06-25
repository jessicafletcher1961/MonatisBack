package fr.colline.monatis.budgets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.service.ReferenceService;
import fr.colline.monatis.references.service.SousCategorieService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/budgets/souscategorie")
@Transactional
public class BudgetSousCategorieController extends BudgetController<SousCategorie> {

	@Autowired private ControllerVerificateurService verificateur;
	@Autowired private SousCategorieService sousCategorieService;
	
	@Override
	protected SousCategorie getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException {
		return verificateur.verifierSousCategorie(nomReference, obligatoire);
	}

	@Override
	protected ReferenceService<SousCategorie> getReferenceService() {
		return sousCategorieService;
	}

}
