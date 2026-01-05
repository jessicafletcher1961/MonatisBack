package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.operations.model.Operation;

public class ReleveCompte {

	private Compte compte;
	
	private LocalDate dateDebutReleve;
	
	private Long montantSoldeDebutReleveEnCentimes;
	
	private List<Operation> operationsRecette;
	
	private List<Operation> operationsDepense;
	
	private LocalDate dateFinReleve;
	
	private Long montantSoldeFinReleveEnCentimes;

	private Long montantTotalOperationsRecetteEnCentimes;
	
	private Long montantTotalOperationsDepenseEnCentimes;

	public Compte getCompte() {
		return compte;
	}

	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	public LocalDate getDateDebutReleve() {
		return dateDebutReleve;
	}

	public void setDateDebutReleve(LocalDate dateDebutReleve) {
		this.dateDebutReleve = dateDebutReleve;
	}

	public Long getMontantSoldeDebutReleveEnCentimes() {
		return montantSoldeDebutReleveEnCentimes;
	}

	public void setMontantSoldeDebutReleveEnCentimes(Long montantSoldeDebutReleveEnCentimes) {
		this.montantSoldeDebutReleveEnCentimes = montantSoldeDebutReleveEnCentimes;
	}

	public List<Operation> getOperationsRecette() {
		return operationsRecette;
	}

	public void setOperationsRecette(List<Operation> operationsRecette) {
		this.operationsRecette = operationsRecette;
	}

	public List<Operation> getOperationsDepense() {
		return operationsDepense;
	}

	public void setOperationsDepense(List<Operation> operationsDepense) {
		this.operationsDepense = operationsDepense;
	}

	public LocalDate getDateFinReleve() {
		return dateFinReleve;
	}

	public void setDateFinReleve(LocalDate dateFinReleve) {
		this.dateFinReleve = dateFinReleve;
	}

	public Long getMontantSoldeFinReleveEnCentimes() {
		return montantSoldeFinReleveEnCentimes;
	}

	public void setMontantSoldeFinReleveEnCentimes(Long montantSoldeFinReleveEnCentimes) {
		this.montantSoldeFinReleveEnCentimes = montantSoldeFinReleveEnCentimes;
	}

	public Long getMontantTotalOperationsRecetteEnCentimes() {
		return montantTotalOperationsRecetteEnCentimes;
	}

	public void setMontantTotalOperationsRecetteEnCentimes(Long montantTotalOperationsRecetteEnCentimes) {
		this.montantTotalOperationsRecetteEnCentimes = montantTotalOperationsRecetteEnCentimes;
	}

	public Long getMontantTotalOperationsDepenseEnCentimes() {
		return montantTotalOperationsDepenseEnCentimes;
	}

	public void setMontantTotalOperationsDepenseEnCentimes(Long montantTotalOperationsDepenseEnCentimes) {
		this.montantTotalOperationsDepenseEnCentimes = montantTotalOperationsDepenseEnCentimes;
	}	
}
