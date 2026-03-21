package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;

public class PlusMoinsValueCompteInternePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = 3745449613340820571L;
	
	public LocalDate dateDebutperiode;
	public LocalDate dateFinPeriode;
	
	public Double montantMouvementEnEuros;
	public Double montantTechniqueEnEuros; 
	public Double montantPlusMoinsValuePotentielleEnEuros;
	public Double tauxPlusMoinsValuePotentielle;
	public Double montantPlusMoinsValueRealiseeEnEuros;
	public Double montantSoldeInitialEnEuros;
	public Double montantSoldeFinalEnEuros;

}
