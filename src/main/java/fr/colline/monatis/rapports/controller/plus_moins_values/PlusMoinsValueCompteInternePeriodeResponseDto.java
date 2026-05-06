package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;

public class PlusMoinsValueCompteInternePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = 3745449613340820571L;
	
	public LocalDate dateDebutperiode;
	public LocalDate dateFinPeriode;
	
	public Double montantSoldeInitialEnEuros;
	public Double montantOperationsEnEuros;
	public Double montantPlusMoinsValueNetteEnEuros;
	public Double tauxPlusMoinsValueNette;
	public Double montantSoldeFinalEnEuros;
	
	public Double montantFraisEnEuros; 
	public Double tauxFrais;
}
