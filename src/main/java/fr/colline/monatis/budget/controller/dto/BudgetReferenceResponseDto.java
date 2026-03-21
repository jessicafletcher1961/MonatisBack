package fr.colline.monatis.budget.controller.dto;

import java.util.List;

import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;

public class BudgetReferenceResponseDto {

	public String codeTypeReference;
	public ReferenceResponseDto reference;
	public List<BudgetResponseDto> budgets;
}
