package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;

public class PlusMoinsValueTypeFonctionnementPeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = 6500711655703775351L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	
	public Double montantPlusMoinsValuePotentielleEnCentimes;

}
