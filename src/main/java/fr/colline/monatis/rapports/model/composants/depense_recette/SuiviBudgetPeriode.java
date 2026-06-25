package fr.colline.monatis.rapports.model.composants.depense_recette;

import java.time.LocalDate;

public class SuiviBudgetPeriode {

	private LocalDate dateDebut;
	private LocalDate dateFin;
	
	private Long montantBudgetEnCentimes;
	private Long montantExecutionEnCentimes;
	private Long montantVertEnCentimes;
	private Long montantRougeEnCentimes;
	private Double tauxExecutionBudget;

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public Long getMontantBudgetEnCentimes() {
		return montantBudgetEnCentimes;
	}

	public void setMontantBudgetEnCentimes(Long montantBudgetEnCentimes) {
		this.montantBudgetEnCentimes = montantBudgetEnCentimes;
	}

	public Long getMontantExecutionEnCentimes() {
		return montantExecutionEnCentimes;
	}

	public void setMontantExecutionEnCentimes(Long montantExecutionEnCentimes) {
		this.montantExecutionEnCentimes = montantExecutionEnCentimes;
	}

	public Long getMontantVertEnCentimes() {
		return montantVertEnCentimes;
	}

	public void setMontantVertEnCentimes(Long montantVertEnCentimes) {
		this.montantVertEnCentimes = montantVertEnCentimes;
	}

	public Long getMontantRougeEnCentimes() {
		return montantRougeEnCentimes;
	}

	public void setMontantRougeEnCentimes(Long montantRougeEnCentimes) {
		this.montantRougeEnCentimes = montantRougeEnCentimes;
	}

	public Double getTauxExecutionBudget() {
		return tauxExecutionBudget;
	}

	public void setTauxExecutionBudget(Double tauxExecutionBudget) {
		this.tauxExecutionBudget = tauxExecutionBudget;
	}
}
