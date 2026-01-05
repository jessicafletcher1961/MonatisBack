package fr.colline.monatis.budgets.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import fr.colline.monatis.budgets.BudgetControleErreur;
import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.model.TypePeriode;
import fr.colline.monatis.budgets.service.BudgetService;
import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.references.service.ReferenceService;

public abstract class BudgetController<T extends Reference> {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;
	
	@Autowired private BudgetService budgetService; 
	@Autowired private ControllerVerificateurService verificateur;

	public List<BudgetsParReferenceResponseDto> getAllBudgets() throws ServiceException, ControllerException {

		List<BudgetsParReferenceResponseDto> resultat = new ArrayList<BudgetsParReferenceResponseDto>();
		
		Sort tri = Sort.by("nom");
		for ( T reference : getReferenceService().rechercherTous(tri) ) {
			List<Budget> budgets = budgetService.rechercherParReferenceId(reference.getId());
			if ( budgets.size() > 0 ) {
				resultat.add(BudgetResponseDtoMapper.mapperModelToResponseDto(reference, budgets));
			}
		}
		
		return resultat;
	}

	public BudgetsParReferenceResponseDto getBudgetsParNomReference(
			String nomReference) throws ServiceException, ControllerException {

		T reference = getReferenceVerifiee(nomReference, OBLIGATOIRE);

		return BudgetResponseDtoMapper.mapperModelToResponseDto(
				reference, 
				budgetService.rechercherParReferenceId(reference.getId()));
	}
	
	public BudgetsParReferenceResponseDto creerBudget(
			BudgetRequestDto dto) throws ControllerException, ServiceException {

		T reference = getReferenceVerifiee(dto.nomReference, OBLIGATOIRE);

		Budget dernierBudget = budgetService.rechercherDernierParReferenceId(reference.getId());
		if ( dernierBudget != null ) {
			throw new ControllerException(
					BudgetControleErreur.CREATION_AVEC_HISTORIQUE,
					reference.getClass().getSimpleName(),
					reference.getNom());
		}

		Budget budget = new Budget();
		budget.setReference(reference);
		budget.setTypePeriode(verificateur.verifierTypePeriode(dto.codeTypePeriode, OBLIGATOIRE, null));
		budget.setDateDebut(verificateur.verifierDate(dto.dateCible, FACULTATIF, LocalDate.now()));
		budget.setDateFin(null);
		budget.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));

		budget = budgetService.creerBudget(budget);

		return BudgetResponseDtoMapper.mapperModelToResponseDto(
				reference, 
				budgetService.rechercherParReferenceId(reference.getId()));
	}

	public BudgetsParReferenceResponseDto reconduireBudget(
			BudgetRequestDto dto) throws ControllerException, ServiceException {

		T reference = getReferenceVerifiee(dto.nomReference, OBLIGATOIRE);

		Budget dernierBudget = budgetService.rechercherDernierParReferenceId(reference.getId());
		if ( dernierBudget == null ) {
			throw new ControllerException(
					BudgetControleErreur.RECONDUCTION_SANS_HISTORIQUE,
					reference.getClass().getSimpleName(),
					reference.getNom());
		}
		
		TypePeriode typePeriode = verificateur.verifierTypePeriode(dto.codeTypePeriode, FACULTATIF, dernierBudget.getTypePeriode());

		// s'il n'a pas été précisé, calcul du montant du budget reconduit en fonction du montant du budget pécédent 
		Long montantEnCentimes;
		if ( dto.montantEnCentimes == null ) {
			if ( dernierBudget.getTypePeriode() == typePeriode ) {
				montantEnCentimes = dernierBudget.getMontantEnCentimes();
			}
			else {
				montantEnCentimes = budgetService.estimerMontantBudget(typePeriode, dernierBudget);
			}
		}
		else {
			montantEnCentimes = dto.montantEnCentimes;
		}
		
		LocalDate dateCible = verificateur.verifierDate(dto.dateCible, FACULTATIF, dernierBudget.getDateFin());
		
		Budget budget = new Budget(); 
		budget.setReference(reference);
		budget.setTypePeriode(typePeriode);
		budget.setDateDebut(dateCible);
		budget.setDateFin(null);
		budget.setMontantEnCentimes(montantEnCentimes);
		
		budget = budgetService.reconduireBudget(budget);

		return BudgetResponseDtoMapper.mapperModelToResponseDto(
				reference, 
				budgetService.rechercherParReferenceId(reference.getId()));
	}

	public BudgetsParReferenceResponseDto modifierBudget(
			BudgetRequestDto dto) throws ControllerException, ServiceException {

		T reference = getReferenceVerifiee(dto.nomReference, OBLIGATOIRE);
		LocalDate dateCible = verificateur.verifierDate(dto.dateCible, OBLIGATOIRE, null);
		
		Budget budget = budgetService.rechercherParReferenceIdEtDateCible(reference.getId(), dateCible);
		if ( budget == null ) {
			throw new ControllerException(
					BudgetControleErreur.NON_TROUVE_PAR_REFERENCE_ID_ET_DATE,
					reference.getClass().getSimpleName(),
					reference.getNom(),
					dateCible);
		}
		budget.setMontantEnCentimes(verificateur.verifierMontantEnCentimes(dto.montantEnCentimes, OBLIGATOIRE, null));
		
		budget = budgetService.modifierBudget(budget);

		return BudgetResponseDtoMapper.mapperModelToResponseDto(
				reference, 
				budgetService.rechercherParReferenceId(reference.getId()));
	}
	
	public void supprimerBudget(
			BudgetRequestDto dto) throws ControllerException, ServiceException {

		T reference = getReferenceVerifiee(dto.nomReference, OBLIGATOIRE);
		LocalDate dateCible = verificateur.verifierDate(dto.dateCible, OBLIGATOIRE, null);
		
		Budget budget = budgetService.rechercherParReferenceIdEtDateCible(reference.getId(), dateCible);
		if ( budget == null ) {
			throw new ControllerException(
					BudgetControleErreur.NON_TROUVE_PAR_REFERENCE_ID_ET_DATE,
					reference.getClass().getSimpleName(),
					reference.getNom(),
					dateCible);
		}
		
		budgetService.supprimerBudget(budget);
	}
	
	protected abstract T getReferenceVerifiee(String nomReference, boolean obligatoire) throws ControllerException, ServiceException;
	protected abstract ReferenceService<T> getReferenceService();
}
