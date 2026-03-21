package fr.colline.monatis.rapports.controller.bilan_patrimoine;

import java.io.Serializable;
import java.time.LocalDate;

public class BilanPatrimoinePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = 4483546480849902871L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	
	public double montantSoldeFinalEnEuros;
	public double montantTotalRecetteEnEuros;
	public double montantTotalDepenseEnEuros;
	public double soldeTotalTechniqueEnEuros;
	public double montantEcartNonJustifieEnEuros;

}
