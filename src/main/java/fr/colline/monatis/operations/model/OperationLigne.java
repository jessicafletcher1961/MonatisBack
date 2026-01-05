package fr.colline.monatis.operations.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.SousCategorie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class OperationLigne {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int numeroLigne;
	
	private Long montantEnCentimes;

	private LocalDate dateComptabilisation;
	
	@Column(length = 240)
	private String libelle;

	@ManyToOne
	@JoinColumn(name = "operation_id")
	private Operation operation;

	@ManyToOne
	@JoinColumn(name = "sous_categorie_id")
	private SousCategorie sousCategorie;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "operation_ligne_beneficiaire", 
			  joinColumns = @JoinColumn(name = "operation_ligne_id"), 
			  inverseJoinColumns = @JoinColumn(name = "beneficiaire_id"))
	private Set<Beneficiaire> beneficiaires = new HashSet<>();

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public int getNumeroLigne() {
		return numeroLigne;
	}

	public void setNumeroLigne(int numeroLigne) {
		this.numeroLigne = numeroLigne;
	}

	public long getMontantEnCentimes() {
		return montantEnCentimes;
	}

	public void setMontantEnCentimes(long montantEnCentimes) {
		this.montantEnCentimes = montantEnCentimes;
	}

	public LocalDate getDateComptabilisation() {
		return dateComptabilisation;
	}

	public void setDateComptabilisation(LocalDate dateComptabilisation) {
		this.dateComptabilisation = dateComptabilisation;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public SousCategorie getSousCategorie() {
		return sousCategorie;
	}

	public void setSousCategorie(SousCategorie sousCategorie) {
		this.sousCategorie = sousCategorie;
	}

	public Set<Beneficiaire> getBeneficiaires() {
		return beneficiaires;
	}

	public OperationLigne() {}
	
	public OperationLigne(
			int numeroLigne,
			String libelle,
			LocalDate dateComptabilisation,
			Long montantEnCentimes,
			SousCategorie sousCategorie,
			Beneficiaire...beneficiaires) {

		this.numeroLigne = numeroLigne;
		this.montantEnCentimes = montantEnCentimes;
		this.dateComptabilisation = dateComptabilisation;
		this.libelle = libelle;
		this.sousCategorie = sousCategorie;
		changerBeneficiaires(new HashSet<Beneficiaire>(Arrays.asList(beneficiaires)));
	}

	public void changerBeneficiaires(Set<Beneficiaire> nouveauxBeneficiaires) {
		
		List<Beneficiaire> anciensBeneficiaires = new ArrayList<>(this.beneficiaires);
		
		List<Beneficiaire> aCreer = new ArrayList<>(nouveauxBeneficiaires);
		aCreer.removeAll(anciensBeneficiaires);
		for ( Beneficiaire beneficiaire : aCreer ) {
			this.beneficiaires.add(beneficiaire);
		}

		List<Beneficiaire> aSupprimer = new ArrayList<>(anciensBeneficiaires);
		aSupprimer.removeAll(nouveauxBeneficiaires);
		for ( Beneficiaire beneficiaire : aSupprimer ) {
			this.beneficiaires.remove(beneficiaire);
		}
	}

}
