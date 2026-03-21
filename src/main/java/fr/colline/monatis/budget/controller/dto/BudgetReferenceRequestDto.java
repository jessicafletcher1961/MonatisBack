package fr.colline.monatis.budget.controller.dto;

import java.time.ZonedDateTime;

public class BudgetReferenceRequestDto {

	public String codeTypeReference;
	public String nomReference;
	public String codeTypeBudget;
	public ZonedDateTime dateDebut;
	public ZonedDateTime dateFin;
	public Long montantBudgetEnCentimes;
}
