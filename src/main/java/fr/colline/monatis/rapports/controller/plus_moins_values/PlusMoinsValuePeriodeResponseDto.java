package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;

public class PlusMoinsValuePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = -3773206995012137487L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;

	public double montantPlusMoinsValueNetteEnEuros;
	public Double tauxPlusMoinsValueNette;
	public double montantFraisEnEuros;
	public Double tauxFrais;
}
