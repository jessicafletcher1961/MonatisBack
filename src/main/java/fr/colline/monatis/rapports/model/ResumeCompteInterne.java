package fr.colline.monatis.rapports.model;

import java.time.LocalDate;

import fr.colline.monatis.comptes.model.CompteInterne;

public class ResumeCompteInterne {
	
	private CompteInterne compteInterne;
	private LocalDate dateSolde;
	private Long montantSoldeEnCentimes;
	
	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}
	
	public Long getMontantSoldeEnCentimes() {
		return montantSoldeEnCentimes;
	}
	
	public void setMontantSoldeEnCentimes(Long solde) {
		this.montantSoldeEnCentimes = solde;
	}

	public LocalDate getDateSolde() {
		return dateSolde;
	}

	public void setDateSolde(LocalDate dateSolde) {
		this.dateSolde = dateSolde;
	}

}
