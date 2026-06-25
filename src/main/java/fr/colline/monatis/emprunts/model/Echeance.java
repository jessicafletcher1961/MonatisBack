package fr.colline.monatis.emprunts.model;

import java.time.LocalDate;

import fr.colline.monatis.operations.model.Operation;

public class Echeance {

	private int numeroEcheance;

	private LocalDate dateEcheance;
	
	private Long montantPaiementEnCentimes;
	
	private Long partCapitalEnCentimes;
	
	private Long partInteretsEnCentimes;
	
	private Long partFraisFixesEnCentimes;
	
	private Long capitalEmprunteRestantDuEnCentimes;
	
	private Long capitalEmprunteDejaRembourseEnCentimes;

	private Operation operationPaiement;
	
	private Operation operationPartInterets;
	
	private Operation operationPartFraisFixes;

	private ConditionEmprunt conditionEmprunt;

	public int getNumeroEcheance() {
		return numeroEcheance;
	}

	public void setNumeroEcheance(int numeroEcheance) {
		this.numeroEcheance = numeroEcheance;
	}

	public LocalDate getDateEcheance() {
		return dateEcheance;
	}

	public void setDateEcheance(LocalDate dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	public Long getMontantPaiementEnCentimes() {
		return montantPaiementEnCentimes;
	}

	public void setMontantPaiementEnCentimes(Long montantTotalEcheance) {
		this.montantPaiementEnCentimes = montantTotalEcheance;
	}

	public Long getPartCapitalEnCentimes() {
		return partCapitalEnCentimes;
	}

	public void setPartCapitalEnCentimes(Long capitalCalculeEnCentimes) {
		this.partCapitalEnCentimes = capitalCalculeEnCentimes;
	}

	public Long getPartInteretsEnCentimes() {
		return partInteretsEnCentimes;
	}

	public void setPartInteretsEnCentimes(Long interetsCalculesEnCentimes) {
		this.partInteretsEnCentimes = interetsCalculesEnCentimes;
	}

	public Long getPartFraisFixesEnCentimes() {
		return partFraisFixesEnCentimes;
	}

	public void setPartFraisFixesEnCentimes(Long fraisFixesCalculesEnCentimes) {
		this.partFraisFixesEnCentimes = fraisFixesCalculesEnCentimes;
	}

	public Long getCapitalEmprunteRestantDuEnCentimes() {
		return capitalEmprunteRestantDuEnCentimes;
	}

	public void setCapitalEmprunteRestantDuEnCentimes(Long capitalRestantDuEnCentimes) {
		this.capitalEmprunteRestantDuEnCentimes = capitalRestantDuEnCentimes;
	}

	public Long getCapitalEmprunteDejaRembourseEnCentimes() {
		return capitalEmprunteDejaRembourseEnCentimes;
	}

	public void setCapitalEmprunteDejaRembourseEnCentimes(Long capitalRembourseEnCentimes) {
		this.capitalEmprunteDejaRembourseEnCentimes = capitalRembourseEnCentimes;
	}

	public Operation getOperationPaiement() {
		return operationPaiement;
	}

	public void setOperationPaiement(Operation remboursementOperation) {
		this.operationPaiement = remboursementOperation;
	}

	public Operation getOperationPartInterets() {
		return operationPartInterets;
	}

	public void setOperationPartInterets(Operation remboursementPartInteretsOperation) {
		this.operationPartInterets = remboursementPartInteretsOperation;
	}

	public Operation getOperationPartFraisFixes() {
		return operationPartFraisFixes;
	}

	public void setOperationPartFraisFixes(Operation remboursementPartFraisFixesOperation) {
		this.operationPartFraisFixes = remboursementPartFraisFixesOperation;
	}

	public ConditionEmprunt getConditionEmprunt() {
		return conditionEmprunt;
	}

	public void setConditionEmprunt(ConditionEmprunt conditionEmprunt) {
		this.conditionEmprunt = conditionEmprunt;
	}
}
