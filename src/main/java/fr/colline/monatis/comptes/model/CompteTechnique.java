package fr.colline.monatis.comptes.model;

import jakarta.persistence.Entity;

@Entity
public class CompteTechnique extends Compte {

	@Override
	public TypeCompte getTypeCompte() {
		return TypeCompte.TECHNIQUE;
	}
	
	public CompteTechnique() {}
	
	public CompteTechnique(String identifiant, String libelle) {
		super(identifiant, libelle);
	}
}
