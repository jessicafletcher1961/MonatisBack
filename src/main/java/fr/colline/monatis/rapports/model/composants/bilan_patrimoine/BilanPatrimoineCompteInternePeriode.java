package fr.colline.monatis.rapports.model.composants.bilan_patrimoine;

import java.time.LocalDate;

public class BilanPatrimoineCompteInternePeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private Long montantSoldeInitialEnCentimes;
	private Long montantSoldeFinalEnCentimes;
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

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public Long getMontantSoldeFinalEnCentimes() {
		return montantSoldeFinalEnCentimes;
	}

	public void setMontantSoldeFinalEnCentimes(Long montantSoldeFinalEnCentimes) {
		this.montantSoldeFinalEnCentimes = montantSoldeFinalEnCentimes;
	}

	public Long getMontantTotalDepenseEnCentimes() {
		return montantTotalDepenseEnCentimes;
	}

	public void setMontantTotalDepenseEnCentimes(Long montantTotalDepense) {
		this.montantTotalDepenseEnCentimes = montantTotalDepense;
	}

	public Long getMontantTotalRecetteEnCentimes() {
		return montantTotalRecetteEnCentimes;
	}

	public void setMontantTotalRecetteEnCentimes(Long montantTotalRecette) {
		this.montantTotalRecetteEnCentimes = montantTotalRecette;
	}

	public Long getSoldeTotalTechniqueEnCentimes() {
		return soldeTotalTechniqueEnCentimes;
	}

	public void setSoldeTotalTechniqueEnCentimes(Long soldeTotalTechnique) {
		this.soldeTotalTechniqueEnCentimes = soldeTotalTechnique;
	}

	public Long getMontantEcartNonJustifieEnCentimes() {
		return montantEcartNonJustifieEnCentimes;
	}

	public void setMontantEcartNonJustifieEnCentimes(Long montantEcartNonJustifieEnCentimes) {
		this.montantEcartNonJustifieEnCentimes = montantEcartNonJustifieEnCentimes;
	}
}
