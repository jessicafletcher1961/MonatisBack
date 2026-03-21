package fr.colline.monatis.rapports.model.composants.depense_recette;

import java.time.LocalDate;

public class DepenseRecettePeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private Long montantRecetteEnCentimes;
	private Long montantDepenseEnCentimes;
	private Long soldeDepenseRecetteEnCentimes;

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

	public Long getMontantRecetteEnCentimes() {
		return montantRecetteEnCentimes;
	}

	public void setMontantRecetteEnCentimes(Long montantRecetteEnCentimes) {
		this.montantRecetteEnCentimes = montantRecetteEnCentimes;
	}

	public Long getMontantDepenseEnCentimes() {
		return montantDepenseEnCentimes;
	}

	public void setMontantDepenseEnCentimes(Long montantDepenseEnCentimes) {
		this.montantDepenseEnCentimes = montantDepenseEnCentimes;
	}

	public Long getSoldeDepenseRecetteEnCentimes() {
		return soldeDepenseRecetteEnCentimes;
	}

	public void setSoldeDepenseRecetteEnCentimes(Long soldeDepenseRecetteEnCentimes) {
		this.soldeDepenseRecetteEnCentimes = soldeDepenseRecetteEnCentimes;
	}

}
