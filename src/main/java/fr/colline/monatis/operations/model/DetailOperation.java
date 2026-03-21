package fr.colline.monatis.operations.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
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
import jakarta.persistence.SequenceGenerator;

@Entity
public class DetailOperation {
	
	@Id
	@GeneratedValue(generator = "gen_seq_operation", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_operation", sequenceName = "seq_operation", allocationSize = 1)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "operation_id")
	private Operation operation;

	private int sequence;
	
	private long montantDetailEnCentimes;

	private ZonedDateTime dateComptabilisation;
	
	@Column(length = 240)
	private String libelle;

	@ManyToOne
	@JoinColumn(name = "sous_categorie_id")
	private SousCategorie sousCategorie;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "detail_operation_beneficiaire", 
			  joinColumns = @JoinColumn(name = "detail_operation_id"), 
			  inverseJoinColumns = @JoinColumn(name = "beneficiaire_id"))
	private Set<Beneficiaire> beneficiaires = new HashSet<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public long getMontantDetailEnCentimes() {
		return montantDetailEnCentimes;
	}

	public void setMontantDetailEnCentimes(long montantEnCentimes) {
		this.montantDetailEnCentimes = montantEnCentimes;
	}

	public ZonedDateTime getDateComptabilisation() {
		return dateComptabilisation;
	}

	public void setDateComptabilisation(ZonedDateTime date) {
		this.dateComptabilisation = date;
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

	public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
		this.beneficiaires = beneficiaires;
	}

	public DetailOperation() {}
	
	public DetailOperation(
			long montantEnCentimes,
			ZonedDateTime dateValeur,
			String libelle) {
		
		this.montantDetailEnCentimes = montantEnCentimes;
		this.dateComptabilisation = dateValeur;
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateComptabilisation, 
				id, 
				libelle, 
				montantDetailEnCentimes, 
				operation.getId(), 
				sequence, 
				sousCategorie == null ? 0 : sousCategorie.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetailOperation other = (DetailOperation) obj;
		return Objects.equals(dateComptabilisation, other.dateComptabilisation) 
				&& Objects.equals(id, other.id)
				&& Objects.equals(libelle, other.libelle) 
				&& montantDetailEnCentimes == other.montantDetailEnCentimes
				&& Objects.equals(operation.getId(), other.operation.getId()) 
				&& sequence == other.sequence
				&& Objects.equals(sousCategorie == null ? 0 : sousCategorie.getId(), sousCategorie == null ? 0 : other.sousCategorie.getId());
	}
}
