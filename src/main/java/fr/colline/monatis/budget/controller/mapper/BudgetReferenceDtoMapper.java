package fr.colline.monatis.budget.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.colline.monatis.budget.controller.dto.BudgetReferenceResponseDto;
import fr.colline.monatis.budget.controller.dto.BudgetResponseDto;
import fr.colline.monatis.budget.controller.dto.TypeReferenceResponseDto;
import fr.colline.monatis.budget.model.Budget;
import fr.colline.monatis.budget.model.BudgetCategorie;
import fr.colline.monatis.budget.model.TypeReference;
import fr.colline.monatis.model.references.Reference;
import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;
import fr.colline.monatis.references.controller.mapper.ReferenceDtoMapper;

public class BudgetReferenceDtoMapper {
	
	public static BudgetReferenceResponseDto modelToResponseDto(
			TypeReference typeReference,
			Reference reference,
			ArrayList<? extends Budget> arrayList) {
		
		BudgetReferenceResponseDto dto = new BudgetReferenceResponseDto();
		
		dto.codeTypeReference = typeReference.getCode();
		dto.reference = ReferenceDtoMapper.modelToBasicResponseDto(reference);
		dto.budgets = new ArrayList<>();
		for ( Budget budget : arrayList ) {
			dto.budgets.add(BudgetDtoMapper.modelToResponseDto(budget));
		}
		
		return dto;
	}
}
