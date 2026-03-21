package fr.colline.monatis.references.model;

import java.util.Objects;

import fr.colline.monatis.model.references.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SousCategorie extends Reference {

	@ManyToOne(
			optional = false, 
			fetch = FetchType.EAGER)
	@JoinColumn(name = "categorie_id")
	private Categorie categorie;

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	
	public SousCategorie() {}

	public SousCategorie(
			String nom, 
			String commentaire) {
		
		super(nom, commentaire);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(categorie);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SousCategorie other = (SousCategorie) obj;
		return Objects.equals(categorie, other.categorie);
	}
}
