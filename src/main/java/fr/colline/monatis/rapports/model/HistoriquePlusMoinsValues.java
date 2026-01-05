package fr.colline.monatis.rapports.model;

import java.util.List;

import fr.colline.monatis.comptes.model.CompteInterne;

public class HistoriquePlusMoinsValues {

	private CompteInterne compteInterne;
	
	private List<PlusMoinsValue> plusMoinsValues;

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public List<PlusMoinsValue> getPlusMoinsValues() {
		return plusMoinsValues;
	}

	public void setPlusMoinsValues(List<PlusMoinsValue> plusMoinsValues) {
		this.plusMoinsValues = plusMoinsValues;
	}
	
}
