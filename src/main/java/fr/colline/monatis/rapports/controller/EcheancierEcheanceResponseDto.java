package fr.colline.monatis.rapports.controller;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.rapports.controller.echeancier.EcheancierLigneResponseDto;

public class EcheancierEcheanceResponseDto extends EcheancierLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 1043703422943478412L;
	public int rang;
	public LocalDate dateEcheance;
	public double montantARecouvrerEnEuros;
	public double capitalAmortiEnEuros;
	public double partInteretsEnEuros;
	public double capitalRestantDuEnEuros;
	public double partFraisFixesEnEuros;

}
