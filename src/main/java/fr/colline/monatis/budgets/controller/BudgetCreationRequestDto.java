package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class BudgetCreationRequestDto implements Serializable {

	private static final long serialVersionUID = -9198638418983927057L;
	
	public String cle;
	public String nomReference;
	public String codeTypePeriode;
	public LocalDate dateCible;
	public String codeTypeBudget;
	public Long montantBudgetEnCentimes;
	public String libelle;
	

}
