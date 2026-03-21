package fr.colline.monatis.references.model;

import java.util.HashSet;
import java.util.Set;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.model.references.Reference;
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

	public Titulaire() {}
	
	public Titulaire(String nom, String commentaire) {
		
		super(nom, commentaire);
	}
}
