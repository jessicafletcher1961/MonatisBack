package fr.colline.monatis.rapports.model.composants.remunerations_frais;

import java.time.LocalDate;

public class RemunerationsFraisCompteInternePeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private Long montantRemunerationsEnCentimes;
	private Long montantFraisEnCentimes;
	private Long soldeRemunerationsFraisEnCentimes;

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

	public Long getMontantRemunerationsEnCentimes() {
		return montantRemunerationsEnCentimes;
	}

	public void setMontantRemunerationsEnCentimes(Long montantRemunerationsEnCentimes) {
		this.montantRemunerationsEnCentimes = montantRemunerationsEnCentimes;
	}

	public Long getMontantFraisEnCentimes() {
		return montantFraisEnCentimes;
	}

	public void setMontantFraisEnCentimes(Long montantFraisEnCentimes) {
		this.montantFraisEnCentimes = montantFraisEnCentimes;
	}

	public Long getSoldeRemunerationsFraisEnCentimes() {
		return soldeRemunerationsFraisEnCentimes;
	}

	public void setSoldeRemunerationsFraisEnCentimes(Long soldeRemunerationsFraisEnCentimes) {
		this.soldeRemunerationsFraisEnCentimes = soldeRemunerationsFraisEnCentimes;
	}
}
