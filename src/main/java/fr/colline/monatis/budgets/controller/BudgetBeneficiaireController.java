package fr.colline.monatis.budgets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.service.BeneficiaireService;
import fr.colline.monatis.references.service.ReferenceService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/budgets/beneficiaire")
@Transactional
public class BudgetBeneficiaireController extends BudgetController<Beneficiaire> {

	@Autowired private ControllerVerificateurService verificateur;
	@Autowired private BeneficiaireService beneficiaireService;

	@Override
	protected Beneficiaire getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException {
		return verificateur.verifierBeneficiaire(nomReference, obligatoire);	}

	@Override
	protected ReferenceService<Beneficiaire> getReferenceService() {
		return beneficiaireService;
	}

}
