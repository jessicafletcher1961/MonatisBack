package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class BudgetModificationRequestDto implements Serializable {

	private static final long serialVersionUID = 5382791971141666816L;
	public String cle;
	public String nomReference;
	public String codeTypePeriode;
	public LocalDate dateCible;
	public String codeTypeBudget;
	public Long montantBudgetEnCentimes;
	public String libelle;
	
}
