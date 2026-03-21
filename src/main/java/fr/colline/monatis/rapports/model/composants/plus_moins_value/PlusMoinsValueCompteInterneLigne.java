package fr.colline.monatis.rapports.model.composants.plus_moins_value;

import fr.colline.monatis.comptes.model.CompteInterne;

public class PlusMoinsValueCompteInterneLigne {

	private CompteInterne compteInterne;
	
	private PlusMoinsValueCompteInternePeriode[] periodes;

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public PlusMoinsValueCompteInternePeriode[] getPeriodes() {
		return periodes;
	}

	public void setPeriodes(PlusMoinsValueCompteInternePeriode[] periodes) {
		this.periodes = periodes;
	} 
}
