package fr.colline.monatis.references.model;

import java.util.HashSet;
import java.util.Set;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.typologies.model.TypeReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

@Entity
public class Titulaire extends Reference {

    @ManyToMany(
    		mappedBy = "titulaires", 
    		fetch = FetchType.LAZY)
    private Set<CompteInterne> comptesInternes = new HashSet<>();

	public Set<CompteInterne> getComptesInternes() {
		return comptesInternes;
	}

	public void setComptesInternes(Set<CompteInterne> comptesInternes) {
		this.comptesInternes = comptesInternes;
	}

	@Override
	public TypeReference getTypeReference() {
		return TypeReference.TITULAIRE;
	}

	public Titulaire() {}
	
	public Titulaire(String nom, String libelle) {
		
		super(nom, libelle);
	}
}
