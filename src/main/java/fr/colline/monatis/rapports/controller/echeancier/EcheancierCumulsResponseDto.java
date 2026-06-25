package fr.colline.monatis.rapports.controller.echeancier;

import java.io.Serializable;

public class EcheancierCumulsResponseDto implements Serializable {

	private static final long serialVersionUID = 8144392579889113555L;
	
	public double cumulMontantPaiementsEnEuros;
	public double cumulPartCapitalEnEuros;
	public double cumulPartInteretsEnEuros;
	public double cumulPartFraisFixesEnEuros;
	public double capitalEmpruntDejaRembourseEnEuros;
	public double capitalEmpruntRestantDuEnEuros;

}
