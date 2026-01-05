package fr.colline.monatis.comptes.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.colline.monatis.references.model.Banque;
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

	@Column(length = 10, nullable = false)
	private TypeFonctionnement typeFonctionnement;
	
	@Column(nullable = false)
	private LocalDate dateSoldeInitial;

	@Column(nullable = false)
	private Long montantSoldeInitialEnCentimes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "banque_id")
	private Banque banque;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "compte_interne_titulaire", 
			joinColumns = @JoinColumn(name = "compte_interne_id"), 
			inverseJoinColumns = @JoinColumn(name = "titulaire_id"))
	private Set<Titulaire> titulaires = new HashSet<>();

	public TypeFonctionnement getTypeFonctionnement() {
		return typeFonctionnement;
	}

	public void setTypeFonctionnement(TypeFonctionnement typeFonctionnement) {
		this.typeFonctionnement = typeFonctionnement;
	}

	public LocalDate getDateSoldeInitial() {
		return dateSoldeInitial;
	}

	public void setDateSoldeInitial(LocalDate dateSoldeInitial) {
		this.dateSoldeInitial = dateSoldeInitial;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public Banque getBanque() {
		return banque;
	}

	public Set<Titulaire> getTitulaires() {
		return titulaires;
	}
	
	@Override 
	public TypeCompte getTypeCompte() {
		return TypeCompte.INTERNE;
	}

	public CompteInterne() {}

	public CompteInterne(
			String identifiant, 
			String libelle,
			TypeFonctionnement typeFonctionnement,
			LocalDate dateSoldeInitial,
			Long montantSoldeInitialEnCentimes,
			Banque banque,
			Titulaire...titulaires) {
		
		super(identifiant, libelle);
		this.typeFonctionnement = typeFonctionnement;
		this.dateSoldeInitial = dateSoldeInitial;
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
		changerBanque(banque);
		changerTitulaires(new HashSet<Titulaire>(Arrays.asList(titulaires)));
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
	
	public void changerTitulaires(Set<Titulaire> nouveauxTitulaires) {

	    List<Titulaire> anciensTitulaires = new ArrayList<>(this.titulaires);
	    
	    List<Titulaire> aCreer = new ArrayList<>(nouveauxTitulaires);   
	    aCreer.removeAll(anciensTitulaires);
	    aCreer.forEach(this::ajouterTitulaire);

	    List<Titulaire> aSupprimer = new ArrayList<>(anciensTitulaires);   
	    aSupprimer.removeAll(nouveauxTitulaires);	
	    aSupprimer.forEach(this::retirerTitulaire);
	}

	private void ajouterTitulaire(Titulaire titulaire) {
		if (titulaires.add(titulaire)) titulaire.getComptesInternes().add(this);
	}

	private void retirerTitulaire(Titulaire titulaire) {
		if (titulaires.remove(titulaire)) titulaire.getComptesInternes().remove(this);
	}
	
}
