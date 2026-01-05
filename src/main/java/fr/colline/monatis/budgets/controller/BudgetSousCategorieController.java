package fr.colline.monatis.budgets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
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

	@GetMapping("/all")
	public List<BudgetsParReferenceResponseDto> getAllBudgets() throws ServiceException, ControllerException {
		
		return super.getAllBudgets();
	}
	
	@GetMapping("/get/{nom}")
	public BudgetsParReferenceResponseDto getBudgetsParNomReference(
			@PathVariable (name = "nom") String nomSousCategorie) throws ServiceException, ControllerException {

		return super.getBudgetsParNomReference(nomSousCategorie);
	}
	
	@PostMapping("/new")
	public BudgetsParReferenceResponseDto creerBudget(
			@RequestBody BudgetRequestDto dto) throws ControllerException, ServiceException {

		return super.creerBudget(dto);
	}

	@PostMapping("/next")
	public BudgetsParReferenceResponseDto reconduireBudget(
			@RequestBody BudgetRequestDto dto) throws ControllerException, ServiceException {

		return super.reconduireBudget(dto);
	}

	@PutMapping("/mod")
	public BudgetsParReferenceResponseDto modifierBudget(
			@RequestBody BudgetRequestDto dto) throws ControllerException, ServiceException {

		return super.modifierBudget(dto);
	}

	@DeleteMapping("/del")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerBudget(
			@RequestBody BudgetRequestDto dto) throws ControllerException, ServiceException {

		super.supprimerBudget(dto);
	}

	@Override
	protected SousCategorie getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException {
		return verificateur.verifierSousCategorie(nomReference, obligatoire);
	}

	@Override
	protected ReferenceService<SousCategorie> getReferenceService() {
		return sousCategorieService;
	}

}
