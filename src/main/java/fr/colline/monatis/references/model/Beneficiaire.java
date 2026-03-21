package fr.colline.monatis.references.model;

import fr.colline.monatis.model.references.Reference;
import jakarta.persistence.Entity;

@Entity
public class Beneficiaire extends Reference {

	public Beneficiaire() {}
	
	public Beneficiaire(String nom, String commentaire) {
		
		super(nom, commentaire);
	}
}
