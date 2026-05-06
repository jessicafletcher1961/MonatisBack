package fr.colline.monatis.budgets.controller;

import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.references.controller.ReferenceResponseDtoMapper;
import fr.colline.monatis.typologies.controller.TypologieResponseDtoMapper;

public class BudgetResponseDtoMapper {

	public static BudgetResponseDto mapperModelToResponseDto(Budget budget) {
		
		BudgetResponseDto dto = new BudgetResponseDto();
		
		dto.cle = budget.getCle();
		dto.libelle = budget.getLibelle();
		dto.reference = ReferenceResponseDtoMapper.mapperModelToBasicResponseDto(budget.getReference());
		dto.typePeriode = TypologieResponseDtoMapper.mapperModelToResponseDto(budget.getTypePeriode());
		dto.dateDebut = budget.getDateDebut();
		dto.dateFin = budget.getDateFin();
		dto.montantBudgetEnCentimes = budget.getMontantBudgetEnCentimes();
		
		return dto;
	}

}
