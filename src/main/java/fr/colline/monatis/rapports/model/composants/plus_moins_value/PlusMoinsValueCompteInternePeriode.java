package fr.colline.monatis.rapports.model.composants.plus_moins_value;

import java.time.LocalDate;

public class PlusMoinsValueCompteInternePeriode {

	private LocalDate dateDebutPeriode;
	private LocalDate dateFinPeriode;
	
	private Long montantSoldeInitialEnCentimes;
	private Long montantSoldeFinalEnCentimes;
	private Long montantMouvementTransactionEnCentimes;
	private Long montantMouvementTechniqueEnCentimes;
	private Long montantPlusMoinsValuePotentielleEnCentimes;
	private Double tauxPlusMoinsValuePotentielle;
	private Long montantPlusValueRealiseeEnCentimes;

	public LocalDate getDateDebutPeriode() {
		return dateDebutPeriode;
	}

	public void setDateDebutPeriode(LocalDate dateDebutEvaluation) {
		this.dateDebutPeriode = dateDebutEvaluation;
	}

	public LocalDate getDateFinPeriode() {
		return dateFinPeriode;
	}

	public void setDateFinPeriode(LocalDate dateFinEvaluation) {
		this.dateFinPeriode = dateFinEvaluation;
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

	public Long getMontantMouvementTransactionEnCentimes() {
		return montantMouvementTransactionEnCentimes;
	}

	public void setMontantMouvementTransactionEnCentimes(Long montantMouvementEnCentimes) {
		this.montantMouvementTransactionEnCentimes = montantMouvementEnCentimes;
	}

	public Long getMontantMouvementTechniqueEnCentimes() {
		return montantMouvementTechniqueEnCentimes;
	}

	public void setMontantMouvementTechniqueEnCentimes(Long montantTechniqueEnCentimes) {
		this.montantMouvementTechniqueEnCentimes = montantTechniqueEnCentimes;
	}

	public Long getMontantPlusMoinsValuePotentielleEnCentimes() {
		return montantPlusMoinsValuePotentielleEnCentimes;
	}

	public void setMontantPlusMoinsValuePotentielleEnCentimes(Long montantPlusMoinsValuePotentielleEnCentimes) {
		this.montantPlusMoinsValuePotentielleEnCentimes = montantPlusMoinsValuePotentielleEnCentimes;
	}

	public Long getMontantPlusValueRealiseeEnCentimes() {
		return montantPlusValueRealiseeEnCentimes;
	}

	public void setMontantPlusValueRealiseeEnCentimes(Long montantPlusMoinsValueReelleEnCentimes) {
		this.montantPlusValueRealiseeEnCentimes = montantPlusMoinsValueReelleEnCentimes;
	}

	public Double getTauxPlusMoinsValuePotentielle() {
		return tauxPlusMoinsValuePotentielle;
	}

	public void setTauxPlusMoinsValuePotentielle(Double tauxPlusMoinsValuePotentielle) {
		this.tauxPlusMoinsValuePotentielle = tauxPlusMoinsValuePotentielle;
	}

}
