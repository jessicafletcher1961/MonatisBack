package fr.colline.monatis.references.model;

import java.util.HashSet;
import java.util.Set;

import fr.colline.monatis.model.references.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
public class Categorie extends Reference{

	@OneToMany(
			mappedBy = "categorie",
			fetch = FetchType.LAZY)
	private Set<SousCategorie> sousCategories = new HashSet<>();

	public Set<SousCategorie> getSousCategories() {
		return sousCategories;
	}

	public void setSousCategories(Set<SousCategorie> listeSousCategorie) {
		this.sousCategories = listeSousCategorie;
	}

	public Categorie() {}
	
	public Categorie(String nom, String commentaire) {

		super(nom, commentaire);
	}
}
