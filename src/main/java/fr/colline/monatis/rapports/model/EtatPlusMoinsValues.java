package fr.colline.monatis.rapports.model;

import fr.colline.monatis.comptes.model.CompteInterne;

public class EtatPlusMoinsValues {

	private CompteInterne compteInterne;
	
	private PlusMoinsValue plusMoinsValue;

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public PlusMoinsValue getPlusMoinsValue() {
		return plusMoinsValue;
	}

	public void setPlusMoinsValue(PlusMoinsValue plusMoinsValue) {
		this.plusMoinsValue = plusMoinsValue;
	}
}
