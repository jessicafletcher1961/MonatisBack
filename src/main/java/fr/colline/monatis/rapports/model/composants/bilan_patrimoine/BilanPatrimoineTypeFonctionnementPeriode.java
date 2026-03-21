package fr.colline.monatis.rapports.model.composants.bilan_patrimoine;

import java.time.LocalDate;

public class BilanPatrimoineTypeFonctionnementPeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private Long montantSoldeFinalEnCentimes = 0L;
	private Long montantTotalDepenseEnCentimes;
	private Long montantTotalRecetteEnCentimes;
	private Long soldeTotalTechniqueEnCentimes;
	private Long montantEcartNonJustifieEnCentimes;

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

	public Long getMontantSoldeFinalEnCentimes() {
		return montantSoldeFinalEnCentimes;
	}

	public void setMontantSoldeFinalEnCentimes(Long soldeFinalEnCentimes) {
		this.montantSoldeFinalEnCentimes = soldeFinalEnCentimes;
	}

	public Long getMontantTotalDepenseEnCentimes() {
		return montantTotalDepenseEnCentimes;
	}

	public void setMontantTotalDepenseEnCentimes(Long montantTotalDepenseEnCentimes) {
		this.montantTotalDepenseEnCentimes = montantTotalDepenseEnCentimes;
	}

	public Long getMontantTotalRecetteEnCentimes() {
		return montantTotalRecetteEnCentimes;
	}

	public void setMontantTotalRecetteEnCentimes(Long montantTotalRecetteEnCentimes) {
		this.montantTotalRecetteEnCentimes = montantTotalRecetteEnCentimes;
	}

	public Long getSoldeTotalTechniqueEnCentimes() {
		return soldeTotalTechniqueEnCentimes;
	}

	public void setSoldeTotalTechniqueEnCentimes(Long soldeTotalTechniqueEnCentimes) {
		this.soldeTotalTechniqueEnCentimes = soldeTotalTechniqueEnCentimes;
	}

	public Long getMontantEcartNonJustifieEnCentimes() {
		return montantEcartNonJustifieEnCentimes;
	}

	public void setMontantEcartNonJustifieEnCentimes(Long montantEcartNonJustifieEnCentimes) {
		this.montantEcartNonJustifieEnCentimes = montantEcartNonJustifieEnCentimes;
	}
}
