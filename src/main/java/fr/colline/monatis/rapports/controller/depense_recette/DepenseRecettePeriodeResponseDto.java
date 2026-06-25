package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;
import java.time.LocalDate;

public class DepenseRecettePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = -3773681250344334771L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	public double montantRecetteEnEuros;
	public double montantDepenseEnEuros;
	public double soldeDepenseRecetteEnEuros;
	public SuiviBudgetResponseDto suiviBudget;
}
