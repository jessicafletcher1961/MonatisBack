package fr.colline.monatis.rapports.model;

import fr.colline.monatis.comptes.model.CompteInterne;

public class ResumeCompteInterne {
	
	private CompteInterne compteInterne;
	private Long solde;
	
	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}
	
	public Long getSolde() {
		return solde;
	}
	
	public void setSolde(Long solde) {
		this.solde = solde;
	}

}
