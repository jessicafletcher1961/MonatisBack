package fr.colline.monatis.rapports.controller.echeancier;

import java.io.Serializable;
import java.time.LocalDate;

public class EcheancierPeriodeResponseDto extends EcheancierLigneResponseDto implements Serializable {

	private static final long serialVersionUID = -7269013715107296787L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	public LocalDate datePremiereEcheancePeriode;
	public LocalDate dateDerniereEcheancePeriode;
	public int nombreEcheancesPeriode;
	public EcheancierCumulsResponseDto cumuls;

}
