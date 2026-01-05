package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.MonatisResponseDto;
import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class BudgetsParReferenceResponseDto implements Serializable, MonatisResponseDto {

	private static final long serialVersionUID = 4816399499973077952L;
	
	public ReferenceResponseDto reference;
	public List<BudgetResponseDto> budgets;
}
