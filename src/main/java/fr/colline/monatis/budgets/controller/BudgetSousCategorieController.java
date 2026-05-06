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
//
//	@GetMapping("/all")
//	public List<BudgetResponseDto> getAllBudgets() throws ServiceException, ControllerException {
//		
//		return super.getAllBudgets();
//	}
//	
//	@GetMapping("/get/{cle}")
//	public BudgetResponseDto getBudgetsParNomReference(
//			@PathVariable String cle) throws ServiceException, ControllerException {
//
//		return super.getBudgetParCle(cle);
//	}
//	
//	@PostMapping("/new")
//	public BudgetResponseDto creerBudget(
//			@RequestBody BudgetCreationRequestDto dto) throws ControllerException, ServiceException {
//
//		return super.creerBudget(dto);
//	}
//
//	@PostMapping("/next/{cle}")
//	public BudgetResponseDto reconduireBudget(
//			@PathVariable String cle) throws ControllerException, ServiceException {
//
//		return super.reconduireBudget(cle);
//	}
//
//	@PutMapping("/mod/{cle}")
//	public BudgetResponseDto modifierBudget(
//			@PathVariable String cle,
//			@RequestBody BudgetModificationRequestDto dto) throws ControllerException, ServiceException {
//
//		return super.modifierBudget(cle, dto);
//	}
//
//	@DeleteMapping("/del/{cle}")
//	@ResponseStatus(value = HttpStatus.NO_CONTENT)
//	public void supprimerBudget(
//			@PathVariable String cle) throws ControllerException, ServiceException {
//
//		super.supprimerBudget(cle);
//	}
//
//	@PostMapping("/selection")
//	public List<BudgetResponseDto> selectionnerBudgets(
//			@RequestBody BudgetSelectionRequestDto requestDto) throws ServiceException, ControllerException {
//	
//		return super.selectionnerBudgets(requestDto);
//	}
	
	@Override
	protected SousCategorie getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException {
		return verificateur.verifierSousCategorie(nomReference, obligatoire);
	}

	@Override
	protected ReferenceService<SousCategorie> getReferenceService() {
		return sousCategorieService;
	}

}
