package fr.colline.monatis.rapports.model.composants.remunerations_frais;

import fr.colline.monatis.comptes.model.CompteInterne;

public class RemunerationsFraisCompteInterneLigne {

	private CompteInterne compteInterne;
	
	private RemunerationsFraisCompteInternePeriode[] periodes;

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public RemunerationsFraisCompteInternePeriode[] getPeriodes() {
		return periodes;
	}

	public void setPeriodes(RemunerationsFraisCompteInternePeriode[] periodes) {
		this.periodes = periodes;
	}

}
