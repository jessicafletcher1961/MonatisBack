package fr.colline.monatis.model.references;

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

	public Banque() {}
	
	public Banque(String nom, String commentaire) {

		super(nom, commentaire);
	}
}
