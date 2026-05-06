package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.references.model.SousCategorie;

public class ReleveSousCategorie {

	private LocalDate dateDebutReleve;
	private LocalDate dateFinReleve;
	private SousCategorie sousCategorie;
	private List<OperationLigne> operationsLignes;

	public LocalDate getDateDebutReleve() {
		return dateDebutReleve;
	}

	public void setDateDebutReleve(LocalDate dateDebutReleve) {
		this.dateDebutReleve = dateDebutReleve;
	}

	public LocalDate getDateFinReleve() {
		return dateFinReleve;
	}

	public void setDateFinReleve(LocalDate dateFinReleve) {
		this.dateFinReleve = dateFinReleve;
	}

	public SousCategorie getSousCategorie() {
		return sousCategorie;
	}

	public void setSousCategorie(SousCategorie sousCategorie) {
		this.sousCategorie = sousCategorie;
	}

	public List<OperationLigne> getOperationsLignes() {
		return operationsLignes;
	}

	public void setOperationsLignes(List<OperationLigne> operationsLignes) {
		this.operationsLignes = operationsLignes;
	}
	
	
}
