package fr.colline.monatis.rapports.model.composants.depense_recette;

import java.util.List;

import fr.colline.monatis.references.model.Categorie;

public class DepenseRecetteCategorieLigne {

	private Categorie categorie;
	
	private List<DepenseRecetteSousCategorieLigne> lignesSousCategorie;
	private DepenseRecettePeriode[] cumulCategorie;

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public List<DepenseRecetteSousCategorieLigne> getLignesSousCategorie() {
		return lignesSousCategorie;
	}

	public void setLignesSousCategorie(List<DepenseRecetteSousCategorieLigne> lignesSousCategorie) {
		this.lignesSousCategorie = lignesSousCategorie;
	}

	public DepenseRecettePeriode[] getCumulCategorie() {
		return cumulCategorie;
	}

	public void setCumulCategorie(DepenseRecettePeriode[] periodes) {
		this.cumulCategorie = periodes;
	}
}
