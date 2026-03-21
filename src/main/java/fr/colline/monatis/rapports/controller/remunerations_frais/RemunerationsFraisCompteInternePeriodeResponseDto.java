package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.time.LocalDate;

public class RemunerationsFraisCompteInternePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = -2404725451898018896L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	
	public double montantRemunerationsEnEuros;
	public double montantFraisEnEuros;
	public double soldeRemunerationsFraisEnEuros;

}
