package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class BudgetSelectionRequestDto implements Serializable {

	private static final long serialVersionUID = 7442867435534589666L;
	public String cleContient;
	public String libelleContient;
	public String nomReference;
	public LocalDate avantLe;
	public LocalDate apresLe;
	public LocalDate dateCible;

}
