package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.time.LocalDate;

public class RemunerationsFraisPeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = -5002551371949255016L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	
	public double montantRemunerationsEnEuros;
	public double montantFraisEnEuros;
	public double soldeRemunerationsFraisEnEuros;

}
