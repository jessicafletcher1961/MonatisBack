package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class BudgetRequestDto implements Serializable {

	private static final long serialVersionUID = 8074894209496574942L;
	
	public String nomReference;
	public String codeTypePeriode;
	public LocalDate dateCible;
	public Long montantEnCentimes;
}
