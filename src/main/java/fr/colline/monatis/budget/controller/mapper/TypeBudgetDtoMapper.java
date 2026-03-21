package fr.colline.monatis.budget.controller.mapper;

import fr.colline.monatis.budget.controller.dto.TypeBudgetResponseDto;
import fr.colline.monatis.budget.model.TypeBudget;

public class TypeBudgetDtoMapper {

	public static TypeBudgetResponseDto modelToResponseDto(TypeBudget typeBudget) {
		
		TypeBudgetResponseDto dto = new TypeBudgetResponseDto();
		
		dto.code = typeBudget.getCode();
		dto.libelle = typeBudget.getLibelle();
		
		return dto;
	}

}
