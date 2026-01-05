package fr.colline.monatis.budgets.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.references.controller.ReferenceResponseDtoMapper;
import fr.colline.monatis.references.model.Reference;

public class BudgetResponseDtoMapper {

	public static BudgetsParReferenceResponseDto mapperModelToResponseDto(Reference reference, List<Budget> budgets) throws ControllerException {
		
		BudgetsParReferenceResponseDto dto = new BudgetsParReferenceResponseDto();

		dto.reference = ReferenceResponseDtoMapper.mapperModelToBasicResponseDto(reference);
		dto.budgets = new ArrayList<>();
		for ( Budget budget : budgets ) {
			dto.budgets.add(BudgetResponseDtoMapper.mapperModelToResponseDto(budget));
		}
		Collections.sort(dto.budgets, (o1, o2) -> {
			return o2.dateFin.compareTo(o1.dateFin);
		});
		
		return dto;
	}

	public static BudgetResponseDto mapperModelToResponseDto(Budget budget) {
		
		BudgetResponseDto dto = new BudgetResponseDto();
		
		dto.typePeriode = budget.getTypePeriode().getLibelle().concat(" [").concat(budget.getTypePeriode().getCode()).concat("]");
		dto.dateDebut = budget.getDateDebut();
		dto.dateFin = budget.getDateFin().minus(1, ChronoUnit.DAYS);
		dto.montantEnCentimes = budget.getMontantEnCentimes();
		
		return dto;
	}

}
