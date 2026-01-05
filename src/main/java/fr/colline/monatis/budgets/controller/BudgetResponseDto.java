package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class BudgetResponseDto implements Serializable {

	private static final long serialVersionUID = -449293913563674883L;

	public String typePeriode;
	public LocalDate dateDebut;
	public LocalDate dateFin;
	public Long montantEnCentimes;

}
