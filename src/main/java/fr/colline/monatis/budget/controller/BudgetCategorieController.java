package fr.colline.monatis.budget.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.budget.controller.dto.BudgetReferenceRequestDto;
import fr.colline.monatis.budget.controller.dto.BudgetReferenceResponseDto;
import fr.colline.monatis.budget.controller.mapper.BudgetDtoMapper;
import fr.colline.monatis.budget.controller.mapper.BudgetReferenceDtoMapper;
import fr.colline.monatis.budget.model.BudgetCategorie;
import fr.colline.monatis.budget.model.TypeReference;
import fr.colline.monatis.budget.service.BudgetCategorieService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurControle;
import fr.colline.monatis.references.controller.mapper.CategorieDtoMapper;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.service.CategorieService;

@RestController
@RequestMapping("/monatis/budgets/categorie")
@Transactional
public class BudgetCategorieController {

	@Autowired private BudgetCategorieService budgetCategorieService; 
	@Autowired private CategorieService categorieService;

	@GetMapping("/all")
	public List<BudgetReferenceResponseDto> getAllBudgets() throws ServiceException {

		List<BudgetReferenceResponseDto> resultat = new ArrayList<>();
		
		List<BudgetCategorie> budgets;
		for ( Categorie categorie : categorieService.rechercherTous() ) {
			budgets = budgetCategorieService.rechercherParReferenceId(categorie.getId());
			
			BudgetReferenceResponseDto dto = BudgetReferenceDtoMapper.modelToResponseDto(
					TypeReference.CATEGORIE,
					categorie,
					budgetCategorieService.rechercherParReferenceId(categorie.getId()));
			resultat.add(dto);
		}
		
		return resultat;
	}
	
	@GetMapping("/get/{nom}")
	public BudgetReferenceResponseDto getHistoriqueBudget(
			@PathVariable (name = "nom") String nomCategorie) throws ServiceException, ControllerException {

		Categorie categorie = categorieService.rechercherParNom(nomCategorie);
		if ( categorie == null ) {
			throw new ControllerException(
					ErreurControle.CATEGORIE_NON_TROUVEE_PAR_NOM, 
					nomCategorie);
		}
		
		BudgetReferenceResponseDto dto = BudgetReferenceDtoMapper.modelToResponseDto(
				TypeReference.CATEGORIE,
				categorie,
				budgetCategorieService.rechercherParReferenceId(categorie.getId()));  
		
		return dto;
	}
	
	@PostMapping("/new")
	public BudgetReferenceResponseDto creerBudget(
			@RequestBody BudgetReferenceRequestDto dto) {

		BudgetCategorie budget = new BudgetCategorie();
		budget = creationRequestDtoToModel(budget, dto);
		
		return null;
	}

	private BudgetCategorie creationRequestDtoToModel(BudgetCategorie budget, BudgetReferenceRequestDto dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
