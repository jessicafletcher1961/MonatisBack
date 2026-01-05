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

	@GetMapping("/all")
	public List<BudgetsParReferenceResponseDto> getAllBudgets() throws ServiceException, ControllerException {
		
		return super.getAllBudgets();
	}
	
	@GetMapping("/get/{nom}")
	public BudgetsParReferenceResponseDto getBudgetsParNomReference(
			@PathVariable (name = "nom") String nomCategorie) throws ServiceException, ControllerException {

		return super.getBudgetsParNomReference(nomCategorie);
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
	protected ReferenceService<Categorie> getReferenceService() {
		return categorieService;
	}

	@Override
	protected Categorie getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException {
		return verificateur.verifierCategorie(nomReference, obligatoire);
	}

}
