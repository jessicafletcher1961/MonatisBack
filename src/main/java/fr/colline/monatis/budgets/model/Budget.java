package fr.colline.monatis.budgets.model;

import java.time.LocalDate;

import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.typologies.model.TypeBudget;
import fr.colline.monatis.typologies.model.TypePeriode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(indexes = { 
		  @Index(name = "budget_reference_id_date_fin_idx", columnList = "reference_id, date_fin")
		})

public class Budget {

	@Id
	@GeneratedValue(generator = "gen_seq_budget", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_budget", sequenceName = "seq_budget", allocationSize = 1)
	private Long id;
	
	@Column(length = 30)
	private String cle;
	
	@ManyToOne(optional = false)
	private Reference reference;

	@Column(length = 10, nullable = false)
	private TypePeriode typePeriode;

	@Column(nullable = false)
	private LocalDate dateDebut;
	
	@Column(nullable = false)
	private LocalDate dateFin;
	
	@Column(nullable = false)
	private TypeBudget typeBudget;
	
	@Column(nullable = false)
	private Long montantBudgetEnCentimes;
	
	@Column(length = 240) 
	private String libelle;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public TypePeriode getTypePeriode() {
		return typePeriode;
	}

	public void setTypePeriode(TypePeriode typePeriode) {
		this.typePeriode = typePeriode;
	}
	
	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public TypeBudget getTypeBudget() {
		return typeBudget;
	}

	public void setTypeBudget(TypeBudget typeBudget) {
		this.typeBudget = typeBudget;
	}

	public Long getMontantBudgetEnCentimes() {
		return montantBudgetEnCentimes;
	}

	public void setMontantBudgetEnCentimes(Long montantEnCentimes) {
		this.montantBudgetEnCentimes = montantEnCentimes;
	}

	public Budget() {}
	
	public Budget(
			String cle,
			Reference reference, 
			TypePeriode typePeriode, 
			LocalDate dateDebut, 
			LocalDate dateFin, 
			Long monantBudgetEnCentimes,
			String libelle) {
		this.cle = cle;
		this.reference = reference;
		this.typePeriode = typePeriode;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.montantBudgetEnCentimes = monantBudgetEnCentimes;
		this.libelle = libelle;
	}
}
