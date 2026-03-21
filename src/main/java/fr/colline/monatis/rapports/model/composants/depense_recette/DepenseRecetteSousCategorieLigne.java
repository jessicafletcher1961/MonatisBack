package fr.colline.monatis.rapports.model.composants.depense_recette;

import fr.colline.monatis.references.model.SousCategorie;

public class DepenseRecetteSousCategorieLigne {

	private SousCategorie sousCategorie;
	
	private DepenseRecetteSousCategoriePeriode[] periodes;

	public SousCategorie getSousCategorie() {
		return sousCategorie;
	}

	public void setSousCategorie(SousCategorie sousCategorie) {
		this.sousCategorie = sousCategorie;
	}

	public DepenseRecetteSousCategoriePeriode[] getPeriodes() {
		return periodes;
	}

	public void setPeriodes(DepenseRecetteSousCategoriePeriode[] periodes) {
		this.periodes = periodes;
	}
}
