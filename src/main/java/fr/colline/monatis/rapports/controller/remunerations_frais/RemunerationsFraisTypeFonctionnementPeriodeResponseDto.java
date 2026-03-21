package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.time.LocalDate;

public class RemunerationsFraisTypeFonctionnementPeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = 8485396175416840308L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	
	public double montantRemunerationsEnEuros;
	public double montantFraisEnEuros;
	public double soldeRemunerationsFraisEnEuros;

}
