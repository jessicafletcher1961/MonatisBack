package fr.colline.monatis.operations.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.colline.monatis.comptes.model.Compte;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Operation {

	@Id
	@GeneratedValue(generator = "gen_seq_operation", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_operation", sequenceName = "seq_operation", allocationSize = 1)
	private Long id;
	
	@Column(length = 30)
	private String numero;
	
	@Column(length = 240)
	private String libelle;
	
	@Column(nullable = false)
	private LocalDate dateValeur;
	
	@Column(length = 10, nullable = false)
	private TypeOperation typeOperation;
	
	@Column(nullable = false)
	private Long montantEnCentimes;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "compte_recette_id")
	private Compte compteRecette;

	@ManyToOne(optional = false)
	@JoinColumn(name = "compte_depense_id")
	private Compte compteDepense;

	private Boolean pointee;
	
	@OneToMany(
			mappedBy = "operation",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	private Set<OperationLigne> lignes = new HashSet<OperationLigne>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public LocalDate getDateValeur() {
		return dateValeur;
	}

	public void setDateValeur(LocalDate dateValeur) {
		this.dateValeur = dateValeur;
	}

	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	public Long getMontantEnCentimes() {
		return montantEnCentimes;
	}

	public void setMontantEnCentimes(Long montantEnCentimes) {
		this.montantEnCentimes = montantEnCentimes;
	}

	public Compte getCompteRecette() {
		return compteRecette;
	}

	public void setCompteRecette(Compte compteRecette) {
		this.compteRecette = compteRecette;
	}

	public Compte getCompteDepense() {
		return compteDepense;
	}

	public void setCompteDepense(Compte compteDepense) {
		this.compteDepense = compteDepense;
	}

	public Boolean isPointee() {
		return pointee;
	}

	public void setPointee(Boolean pointee) {
		this.pointee = pointee;
	}

	public Set<OperationLigne> getLignes() {
		return lignes;
	}

	public Operation() {}
	
	public Operation(
			String numero,
			TypeOperation typeOperation,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			Compte compteRecette,
			Compte compteDepense,
			Boolean pointee) {
	
		this.numero = numero;
		this.libelle = libelle;
		this.dateValeur = dateValeur;
		this.typeOperation = typeOperation;
		this.montantEnCentimes = montantEnCentimes;
		this.compteRecette = compteRecette;
		this.compteDepense = compteDepense;
		this.pointee = pointee;
	}
	
	public Operation(
			String numero,
			TypeOperation typeOperation,
			String libelle,
			LocalDate dateValeur,
			Long montantEnCentimes,
			Compte compteRecette,
			Compte compteDepense,
			Boolean pointee,
			OperationLigne...lignes) {
		
		this.numero = numero;
		this.libelle = libelle;
		this.dateValeur = dateValeur;
		this.typeOperation = typeOperation;
		this.montantEnCentimes = montantEnCentimes;
		this.compteRecette = compteRecette;
		this.compteDepense = compteDepense;
		this.pointee = pointee;
		changerLignes(new HashSet<OperationLigne>(Arrays.asList(lignes)));
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				compteDepense, 
				compteRecette, 
				dateValeur, 
				id, 
				libelle, 
				lignes,
				montantEnCentimes, 
				numero,
				typeOperation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operation other = (Operation) obj;
		return Objects.equals(compteDepense, other.compteDepense) 
				&& Objects.equals(compteRecette, other.compteRecette)
				&& Objects.equals(dateValeur, other.dateValeur) 
				&& Objects.equals(id, other.id)
				&& Objects.equals(libelle, other.libelle) 
				&& Objects.equals(lignes, other.lignes) 
				&& Objects.equals(montantEnCentimes, other.montantEnCentimes) 
				&& Objects.equals(numero, other.numero)
				&& typeOperation == other.typeOperation;
	}

	public void changerLignes(Set<OperationLigne> nouvellesLignes) {
		
		List<OperationLigne> anciennesLignes = new ArrayList<>(this.lignes);

		List<OperationLigne> aCreer = new ArrayList<>(nouvellesLignes);
		aCreer.removeAll(anciennesLignes);
		for ( OperationLigne ligne : aCreer ) {
			if ( this.lignes.add(ligne) ) ligne.setOperation(this);
		}

		List<OperationLigne> aSupprimer = new ArrayList<>(anciennesLignes);
		aSupprimer.removeAll(nouvellesLignes);
		for ( OperationLigne ligne : aSupprimer ) {
			this.lignes.remove(ligne);
		}
	}

}
