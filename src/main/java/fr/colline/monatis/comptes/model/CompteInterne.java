package fr.colline.monatis.comptes.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.references.model.Titulaire;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class CompteInterne extends Compte {

	private ZonedDateTime dateSoldeInitial;

	private Long montantSoldeInitialEnCentimes;
	
	@Column(length = 10, nullable = false)
	private TypeCompteInterne typeCompteInterne;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "banque_id")
	private Banque banque;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	  name = "compte_interne_titulaire", 
	  joinColumns = @JoinColumn(name = "compte_interne_id"), 
	  inverseJoinColumns = @JoinColumn(name = "titulaire_id"))
    private Set<Titulaire> titulaires = new HashSet<>();

	public ZonedDateTime getDateSoldeInitial() {
		return dateSoldeInitial;
	}

	public void setDateSoldeInitial(ZonedDateTime dateSoldeInitial) {
		this.dateSoldeInitial = dateSoldeInitial;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public TypeCompteInterne getTypeCompteInterne() {
		return typeCompteInterne;
	}

	public void setTypeCompteInterne(TypeCompteInterne typeCompte) {
		this.typeCompteInterne = typeCompte;
	}

	public Banque getBanque() {
		return banque;
	}

	public void setBanque(Banque banque) {
		this.banque = banque;
	}

	public Set<Titulaire> getTitulaires() {
		return titulaires;
	}

	public void setTitulaires(Set<Titulaire> titulaires) {
		this.titulaires = titulaires;
	}
	
	public CompteInterne() {}
	
	public CompteInterne(
			String identifiant, 
			String libelle,
			TypeCompteInterne typeCompteInterne,
			ZonedDateTime dateSoldeInitial,
			Long montantSoldeInitialEnCentimes) {
		super(identifiant, libelle);
		this.typeCompteInterne = typeCompteInterne;
		this.dateSoldeInitial = dateSoldeInitial;
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public void changerBanque(@Nullable Banque nouvelleBanque) {
		if (Objects.equals(this.banque, nouvelleBanque)) 
			return;
		if (this.banque != null) 
			this.banque.getComptesInternes().remove(this);
		this.banque = nouvelleBanque;
		if (nouvelleBanque != null) 
			nouvelleBanque.getComptesInternes().add(this);
	}

	public void ajouterTitulaire(Titulaire titulaire) {
		if (titulaires.add(titulaire)) titulaire.getComptesInternes().add(this);
	}

	public void retirerTitulaire(Titulaire titulaire) {
		if (titulaires.remove(titulaire)) titulaire.getComptesInternes().remove(this);
	}
	
	public void changerTitulaires(Set<Titulaire> nouveauxTitulaires) {

		// On travaille sur des copies pour ne jamais modifier
		// la collection qu'on parcourt
	    Set<Titulaire> titulaires = new HashSet<>(this.titulaires);
	    
	    // Dans toAdd, on ne garde que les titulaires qui n'étaient
		// pas encore présents (les ajoutés)
	    Set<Titulaire> toAdd = new HashSet<>(nouveauxTitulaires);   
	    toAdd.removeAll(titulaires);

	    // Dans toDel, on ne garde que les titulaires qui ne figurent
	    // plus dans la nouvelle liste (les supprimés)
	    Set<Titulaire> toDel = new HashSet<>(titulaires);   
	    toDel.removeAll(nouveauxTitulaires);	

	    toDel.forEach(this::retirerTitulaire);
	    toAdd.forEach(this::ajouterTitulaire);
	  }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(
						banque == null ? 0 : banque.getId(),
						dateSoldeInitial, 
						montantSoldeInitialEnCentimes, 
						typeCompteInterne);
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
		CompteInterne other = (CompteInterne) obj;
		return Objects.equals(banque == null ? 0 : banque.getId(), other.getBanque() == null ? 0 : other.getBanque().getId())
				&& Objects.equals(dateSoldeInitial, other.dateSoldeInitial)
				&& Objects.equals(montantSoldeInitialEnCentimes, other.montantSoldeInitialEnCentimes)
				&& typeCompteInterne == other.typeCompteInterne;
	}
}
