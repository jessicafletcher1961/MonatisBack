package fr.colline.monatis.comptes.model;

import jakarta.persistence.Entity;

@Entity
public class CompteTiers extends Compte {

	public CompteTiers() {}
	
	public CompteTiers(
			String identifiant,
			String libelle) {
		
		super(identifiant, libelle);
	}
}
