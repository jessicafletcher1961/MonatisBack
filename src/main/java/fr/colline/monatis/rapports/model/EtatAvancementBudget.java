package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.references.model.SousCategorie;

public class EtatAvancementBudget {
	
	private SousCategorie sousCategorie;
	
	private Budget budget;
	
	private LocalDate dateDebutEtat;
	
	private LocalDate dateFinEtat;

	private List<OperationLigne> lignesDepense;

	private Long montantTotalLignesDepenseEnCentimes;
	
	private List<OperationLigne> lignesRecette;
	
	private Long montantTotalLignesRecetteEnCentimes;

	private List<OperationLigne> lignesExclues;

	private Long montantTotalLignesExcluesEnCentimes;
	
	private Long montantExecutionEnCentimes;
	
	private Long montantBudgetEnCentimes;
	
	private Long resteADepenserEnCentimes;
	
	private Float montantExecutionEnPourcentage;

	public SousCategorie getSousCategorie() {
		return sousCategorie;
	}

	public void setSousCategorie(SousCategorie sousCategorie) {
		this.sousCategorie = sousCategorie;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public LocalDate getDateDebutEtat() {
		return dateDebutEtat;
	}

	public void setDateDebutEtat(LocalDate dateDebutEtat) {
		this.dateDebutEtat = dateDebutEtat;
	}

	public LocalDate getDateFinEtat() {
		return dateFinEtat;
	}

	public void setDateFinEtat(LocalDate dateFinEtat) {
		this.dateFinEtat = dateFinEtat;
	}

	public List<OperationLigne> getLignesDepense() {
		return lignesDepense;
	}

	public void setLignesDepense(List<OperationLigne> lignesDepense) {
		this.lignesDepense = lignesDepense;
	}

	public Long getMontantTotalLignesDepenseEnCentimes() {
		return montantTotalLignesDepenseEnCentimes;
	}

	public void setMontantTotalLignesDepenseEnCentimes(Long montantTotalLignesDepenseEnCentimes) {
		this.montantTotalLignesDepenseEnCentimes = montantTotalLignesDepenseEnCentimes;
	}

	public List<OperationLigne> getLignesRecette() {
		return lignesRecette;
	}

	public void setLignesRecette(List<OperationLigne> lignesRecette) {
		this.lignesRecette = lignesRecette;
	}

	public Long getMontantTotalLignesRecetteEnCentimes() {
		return montantTotalLignesRecetteEnCentimes;
	}

	public void setMontantTotalLignesRecetteEnCentimes(Long montantTotalLignesRecetteEnCentimes) {
		this.montantTotalLignesRecetteEnCentimes = montantTotalLignesRecetteEnCentimes;
	}

	public List<OperationLigne> getLignesExclues() {
		return lignesExclues;
	}

	public void setLignesExclues(List<OperationLigne> lignesExclues) {
		this.lignesExclues = lignesExclues;
	}

	public Long getMontantTotalLignesExcluesEnCentimes() {
		return montantTotalLignesExcluesEnCentimes;
	}

	public void setMontantTotalLignesExcluesEnCentimes(Long montantTotalLignesExcluesEnCentimes) {
		this.montantTotalLignesExcluesEnCentimes = montantTotalLignesExcluesEnCentimes;
	}

	public Long getMontantExecutionEnCentimes() {
		return montantExecutionEnCentimes;
	}

	public void setMontantExecutionEnCentimes(Long montantExecutionEnCentimes) {
		this.montantExecutionEnCentimes = montantExecutionEnCentimes;
	}

	public Long getMontantBudgetEnCentimes() {
		return montantBudgetEnCentimes;
	}

	public void setMontantBudgetEnCentimes(Long montantBudgetEnCentimes) {
		this.montantBudgetEnCentimes = montantBudgetEnCentimes;
	}

	public Long getResteADepenserEnCentimes() {
		return resteADepenserEnCentimes;
	}

	public void setResteADepenserEnCentimes(Long resteADepenserEnCentimes) {
		this.resteADepenserEnCentimes = resteADepenserEnCentimes;
	}

	public Float getMontantExecutionEnPourcentage() {
		return montantExecutionEnPourcentage;
	}

	public void setMontantExecutionEnPourcentage(Float montantExecutionEnPourcentage) {
		this.montantExecutionEnPourcentage = montantExecutionEnPourcentage;
	}

}
