package fr.colline.monatis.rapports.model.composants.plus_moins_value;

import java.time.LocalDate;

public class PlusMoinsValueTypeFonctionnementPeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private Long montantPlusMoinsValuePotentielleEnCentimes = 0L;

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

	public Long getMontantPlusMoinsValuePotentielleEnCentimes() {
		return montantPlusMoinsValuePotentielleEnCentimes;
	}

	public void setMontantPlusMoinsValuePotentielleEnCentimes(Long montantPlusMoinsValuePotentielleEnCentimes) {
		this.montantPlusMoinsValuePotentielleEnCentimes = montantPlusMoinsValuePotentielleEnCentimes;
	}
}
