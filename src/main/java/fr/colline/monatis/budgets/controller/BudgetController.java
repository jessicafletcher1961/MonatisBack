package fr.colline.monatis.budgets.controller;

import java.time.LocalDate;
import java.util.Comparator;
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

import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.service.BudgetService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.references.service.ReferenceService;
import jakarta.transaction.Transactional;

//@RestController
//@RequestMapping("/monatis/budgets")
//@Transactional
public abstract class BudgetController<T extends Reference> {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private BudgetService budgetService; 
	@Autowired private ControllerVerificateurService verificateur;

	@GetMapping("/all")
	public List<BudgetResponseDto> getAllBudgets() throws ServiceException, ControllerException {

		return budgetService.rechercherTous()
				.stream()
				.sorted((b1, b2) -> {return b2.getDateFin().compareTo(b1.getDateFin());})
				.map((b) -> {return BudgetResponseDtoMapper.mapperModelToResponseDto(b);})
				.toList();
	}

	@GetMapping("/get/{cle}")
	public BudgetResponseDto getBudgetParCle(@PathVariable String cle) throws ControllerException, ServiceException {

		Budget budget = verificateur.verifierBudget(
				cle, 
				OBLIGATOIRE);
		return BudgetResponseDtoMapper.mapperModelToResponseDto(budget);
	}

	@PostMapping("/new")
	public BudgetResponseDto creerBudget(@RequestBody BudgetCreationRequestDto dto) throws ControllerException, ServiceException {

		Budget budget = new Budget();
		budget = mapperCreationRequestDtoToModel(dto, budget);
		budget = budgetService.creerBudget(budget);
		return BudgetResponseDtoMapper.mapperModelToResponseDto(budget);
	}

	@PostMapping("/next/{cle}")
	public BudgetResponseDto reconduireBudget(@PathVariable String cle) throws ServiceException, ControllerException {
		
		Budget budgetAReconduire = verificateur.verifierBudget(cle, OBLIGATOIRE);
		Budget budgetReconduit = budgetService.reconduireBudget(budgetAReconduire);
		return BudgetResponseDtoMapper.mapperModelToResponseDto(budgetReconduit);
	}
	
	@PutMapping("/mod/{cle}")
	public BudgetResponseDto modifierBudget(
			@PathVariable String cle, 
			@RequestBody BudgetModificationRequestDto dto) throws ControllerException, ServiceException {

		Budget budget = verificateur.verifierBudget(cle, OBLIGATOIRE);
		budget = mapperModificationRequestDtoToModel(dto, budget);
		budget = budgetService.modifierBudget(budget);
		return BudgetResponseDtoMapper.mapperModelToResponseDto(budget);
	}

	@DeleteMapping("/del/{cle}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void supprimerBudget(@PathVariable String cle) throws ControllerException, ServiceException {

		Budget budget = verificateur.verifierBudget(cle, OBLIGATOIRE);
		budgetService.supprimerBudget(budget);
	}

	@PostMapping("/selection")
	public List<BudgetResponseDto>selectionnerBudgets(
			@RequestBody BudgetSelectionRequestDto requestDto) throws ServiceException, ControllerException {

		final String cle = verificateur.standardiserIdentifiantFonctionnel(requestDto.cleContient);
		final String libelle = verificateur.verifierLibelle(requestDto.libelleContient, FACULTATIF, null);
		final Reference reference = getReferenceVerifiee(requestDto.nomReference, FACULTATIF);
		final LocalDate avantLe = verificateur.verifierDate(requestDto.avantLe, FACULTATIF, null);
		final LocalDate apresLe = verificateur.verifierDate(requestDto.apresLe, FACULTATIF, null);
		final LocalDate dateCible = verificateur.verifierDate(requestDto.dateCible, FACULTATIF, null);
		
		return budgetService.rechercherTous()
				.stream()
				.filter((b) -> {return cle == null 
						|| b.getCle().contains(cle);})
				.filter((b) -> {return libelle == null 
						|| b.getLibelle().toUpperCase().contains(libelle.toUpperCase());})
				.filter((b) -> {return reference == null 
						|| b.getReference().getId().equals(reference.getId());})
				.filter((b) -> {return avantLe == null
						|| b.getDateFin().isBefore(avantLe);})
				.filter((b) -> {return apresLe == null
						|| b.getDateDebut().isAfter(apresLe);})
				.filter((b) -> {return dateCible == null
						|| ( !dateCible.isBefore(b.getDateDebut()) && !dateCible.isAfter(b.getDateFin()));} )
				.sorted(Comparator.comparing(Budget::getDateFin, Comparator.reverseOrder()))
				.map((o) -> {return BudgetResponseDtoMapper.mapperModelToResponseDto(o);})
				.toList();
	}

	private Budget mapperCreationRequestDtoToModel(
			BudgetCreationRequestDto dto, 
			Budget budget) throws ControllerException, ServiceException {
		
		budget.setCle(verificateur.verifierCleBudgetValideEtUnique(dto.cle, budget.getId(), FACULTATIF));
		budget.setReference(getReferenceVerifiee(dto.nomReference, OBLIGATOIRE));
		budget.setTypePeriode(verificateur.verifierTypePeriode(dto.codeTypePeriode, OBLIGATOIRE, null));
		budget.setDateDebut(verificateur.verifierDate(dto.dateCible, OBLIGATOIRE, null));
		budget.setMontantBudgetEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantBudgetEnCentimes, OBLIGATOIRE, null));
		budget.setLibelle(verificateur.verifierLibelle(dto.libelle, FACULTATIF, null));
		
		return budget;
	}

	private Budget mapperModificationRequestDtoToModel(
			BudgetModificationRequestDto requestDto, 
			Budget budget) throws ControllerException, ServiceException {
		
		if ( requestDto.cle != null ) budget.setCle(verificateur.verifierCleBudgetValideEtUnique(requestDto.cle, budget.getId(), OBLIGATOIRE));
		if ( requestDto.nomReference != null ) budget.setReference(getReferenceVerifiee(requestDto.nomReference, OBLIGATOIRE));
		if ( requestDto.codeTypePeriode != null ) budget.setTypePeriode(verificateur.verifierTypePeriode(requestDto.codeTypePeriode, OBLIGATOIRE, null));
		if ( requestDto.dateDebut != null ) budget.setDateDebut(verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null));
		if ( requestDto.montantBudgetEnCentimes != null ) budget.setMontantBudgetEnCentimes(verificateur.verifierMontantEnCentimes(requestDto.montantBudgetEnCentimes, OBLIGATOIRE, null));
		if ( requestDto.libelle != null ) budget.setLibelle(verificateur.verifierLibelle(requestDto.libelle, FACULTATIF, null));
		
		return budget;
	}
	
	protected abstract T getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException;
	protected abstract ReferenceService<T> getReferenceService();
}
