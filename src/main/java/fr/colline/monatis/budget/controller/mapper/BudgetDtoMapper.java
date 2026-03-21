package fr.colline.monatis.budget.controller.mapper;

import fr.colline.monatis.budget.controller.dto.BudgetResponseDto;
import fr.colline.monatis.budget.model.Budget;

public abstract class BudgetDtoMapper {

	public static BudgetResponseDto modelToResponseDto(Budget budget) {

		BudgetResponseDto dto = new BudgetResponseDto();
		
		dto.codeTypeBudget = budget.getTypeBudget().getCode();
		dto.dateDebut = budget.getDateDebut();
		dto.dateFin = budget.getDateFin();
		dto.montantBudgetEnCentimes = budget.getMontantBudgetEnCentimes();
		
		return dto;
	}
}
