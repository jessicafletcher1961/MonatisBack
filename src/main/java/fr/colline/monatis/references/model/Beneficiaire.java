package fr.colline.monatis.references.model;

import jakarta.persistence.Entity;

@Entity
public class Beneficiaire extends Reference {

	@Override
	public TypeReference getTypeReference() {
		return TypeReference.BENEFICIAIRE;
	}

	public Beneficiaire() {}
	
	public Beneficiaire(String nom, String libelle) {
		
		super(nom, libelle);
	}
}
