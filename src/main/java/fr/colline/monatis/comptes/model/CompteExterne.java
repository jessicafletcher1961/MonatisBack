package fr.colline.monatis.comptes.model;

import fr.colline.monatis.typologies.model.TypeCompte;
import jakarta.persistence.Entity;

@Entity
public class CompteExterne extends Compte {

	@Override
	public TypeCompte getTypeCompte() {
		return TypeCompte.EXTERNE;
	}
	
	public CompteExterne() {};
	
	public CompteExterne(String identifiant, String libelle) {
		super(identifiant, libelle);
	}
}
