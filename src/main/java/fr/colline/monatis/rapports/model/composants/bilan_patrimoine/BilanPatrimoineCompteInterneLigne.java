package fr.colline.monatis.rapports.model.composants.bilan_patrimoine;

import fr.colline.monatis.comptes.model.CompteInterne;

public class BilanPatrimoineCompteInterneLigne {

	private CompteInterne compteInterne;
	
	private Long montantSoldeInitialEnCentimes;
	private BilanPatrimoineCompteInternePeriode[] periodes;

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long soldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = soldeInitialEnCentimes;
	}

	public BilanPatrimoineCompteInternePeriode[] getPeriodes() {
		return periodes;
	}

	public void setPeriodes(BilanPatrimoineCompteInternePeriode[] periodes) {
		this.periodes = periodes;
	}
}
