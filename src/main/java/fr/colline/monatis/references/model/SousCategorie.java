package fr.colline.monatis.references.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SousCategorie extends Reference {
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "categorie_id")
	private Categorie categorie;

	public Categorie getCategorie() {
		return categorie;
	}

	@Override
	public TypeReference getTypeReference() {
		return TypeReference.SOUS_CATEGORIE;
	}
	
	public SousCategorie() {}
	
	public SousCategorie(String nom, String libelle, Categorie categorie) {
		super(nom, libelle);
		this.changerCategorie(categorie);
	}
	
	public void changerCategorie(Categorie nouvelleCategorie) {
		if (Objects.equals(this.categorie, nouvelleCategorie)) 
			return;
		if (this.categorie != null) 
			this.categorie.getSousCategories().remove(this);
		this.categorie = nouvelleCategorie;
		if (nouvelleCategorie != null) 
			nouvelleCategorie.getSousCategories().add(this);
	}
}
