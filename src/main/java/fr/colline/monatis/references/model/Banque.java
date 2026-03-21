package fr.colline.monatis.references.model;

import java.util.HashSet;
import java.util.Set;

import fr.colline.monatis.comptes.model.CompteInterne;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
public class Banque extends Reference {

	@OneToMany(
			mappedBy = "banque",
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
		return TypeReference.BANQUE;
	}

	public Banque() {}
	
	public Banque(String nom, String libelle) {
		super(nom, libelle);
	}
}
