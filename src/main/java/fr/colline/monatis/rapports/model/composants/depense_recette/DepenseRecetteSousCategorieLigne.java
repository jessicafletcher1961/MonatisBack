package fr.colline.monatis.rapports.model.composants.depense_recette;

import fr.colline.monatis.references.model.SousCategorie;

public class DepenseRecetteSousCategorieLigne {

	private SousCategorie sousCategorie;
	
	private DepenseRecettePeriode[] cumulSousCategorie;

	public SousCategorie getSousCategorie() {
		return sousCategorie;
	}

	public void setSousCategorie(SousCategorie sousCategorie) {
		this.sousCategorie = sousCategorie;
	}

	public DepenseRecettePeriode[] getCumulSousCategorie() {
		return cumulSousCategorie;
	}

	public void setCumulSousCategorie(DepenseRecettePeriode[] periodes) {
		this.cumulSousCategorie = periodes;
	}
}
