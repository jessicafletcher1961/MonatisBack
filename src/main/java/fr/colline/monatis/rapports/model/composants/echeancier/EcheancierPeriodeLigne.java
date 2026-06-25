package fr.colline.monatis.rapports.model.composants.echeancier;

import java.time.LocalDate;

public class EcheancierPeriodeLigne extends EcheancierLigne {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private EcheancierCumuls cumuls;

	public LocalDate getDateDebutPeriode() {
		return dateDebutPeriode;
	}

	public void setDateDebutPeriode(LocalDate dateDebutPeriode) {
		this.dateDebutPeriode = dateDebutPeriode;
	}

	public LocalDate getDateFinPeriode() {
		return dateFinPeriode;
	}

	public void setDateFinPeriode(LocalDate dateFinPeriode) {
		this.dateFinPeriode = dateFinPeriode;
	}

	public EcheancierCumuls getCumuls() {
		return cumuls;
	}

	public void setCumuls(EcheancierCumuls cumuls) {
		this.cumuls = cumuls;
	}

}
