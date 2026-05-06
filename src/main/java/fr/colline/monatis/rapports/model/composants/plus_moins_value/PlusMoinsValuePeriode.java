package fr.colline.monatis.rapports.model.composants.plus_moins_value;

import java.time.LocalDate;

public class PlusMoinsValuePeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;

	private Long montantSoldeInitialEnCentimes;
	private Long montantOperationsEnCentimes;
	private Long montantPlusMoinsValueNetteEnCentimes;
	private Double tauxPlusMoinsValueNette;
	private Long montantSoldeFinalEnCentimes;
	private Long montantFraisEnCentimes;
	private Double tauxFrais;

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

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public Long getMontantOperationsEnCentimes() {
		return montantOperationsEnCentimes;
	}

	public void setMontantOperationsEnCentimes(Long montantOperationsEnCentimes) {
		this.montantOperationsEnCentimes = montantOperationsEnCentimes;
	}

	public Long getMontantPlusMoinsValueNetteEnCentimes() {
		return montantPlusMoinsValueNetteEnCentimes;
	}

	public void setMontantPlusMoinsValueNetteEnCentimes(Long montantPlusMoinsValueNetteEnCentimes) {
		this.montantPlusMoinsValueNetteEnCentimes = montantPlusMoinsValueNetteEnCentimes;
	}

	public Double getTauxPlusMoinsValueNette() {
		return tauxPlusMoinsValueNette;
	}

	public void setTauxPlusMoinsValueNette(Double tauxPlusMoinsValueNette) {
		this.tauxPlusMoinsValueNette = tauxPlusMoinsValueNette;
	}

	public Long getMontantSoldeFinalEnCentimes() {
		return montantSoldeFinalEnCentimes;
	}

	public void setMontantSoldeFinalEnCentimes(Long montantSoldeFinalEnCentimes) {
		this.montantSoldeFinalEnCentimes = montantSoldeFinalEnCentimes;
	}

	public Long getMontantFraisEnCentimes() {
		return montantFraisEnCentimes;
	}

	public void setMontantFraisEnCentimes(Long montantFraisEnCentimes) {
		this.montantFraisEnCentimes = montantFraisEnCentimes;
	}

	public Double getTauxFrais() {
		return tauxFrais;
	}

	public void setTauxFrais(Double tauxFrais) {
		this.tauxFrais = tauxFrais;
	}
}
