package fr.colline.monatis.rapports.controller.bilan_patrimoine;

import java.io.Serializable;
import java.time.LocalDate;

public class BilanPatrimoineCompteInternePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = -1418051041059834983L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;

	public Double montantSoldeInitialEnEuros;
	public Double montantSoldeFinalEnEuros;
	public Double montantTotalRecetteEnEuros;
	public Double montantTotalDepenseEnEuros;
	public Double soldeTotalTechniqueEnEuros;
	public Double montantEcartNonJustifieEnEuros;
	
}
