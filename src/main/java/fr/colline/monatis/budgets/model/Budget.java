package fr.colline.monatis.budgets.model;

import java.time.LocalDate;
import java.util.Objects;

import fr.colline.monatis.references.model.Reference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(indexes = { 
		  @Index(name = "budget_if", columnList = "reference_id, date_fin DESC")
		})
public class Budget {

	@Id
	@GeneratedValue(generator = "gen_seq_budget", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_budget", sequenceName = "seq_budget", allocationSize = 1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "reference_id")
	private Reference reference;

	@Column(length = 10, nullable = false)
	private TypePeriode typePeriode;

	@Column(nullable = false)
	private LocalDate dateDebut;
	
	@Column(nullable = false)
	private LocalDate dateFin;
	
	@Column(nullable = false)
	private Long montantEnCentimes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getMontantEnCentimes() {
		return montantEnCentimes;
	}

	public void setMontantEnCentimes(Long montantEnCentimes) {
		this.montantEnCentimes = montantEnCentimes;
	}

	public Budget() {}
	
	public Budget(Reference reference, TypePeriode typePeriode, LocalDate dateDebut, LocalDate dateFin, Long monantEnCentimes) {
		this.reference = reference;
		this.typePeriode = typePeriode;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.montantEnCentimes = monantEnCentimes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateDebut, dateFin, id, montantEnCentimes, reference.getId(), typePeriode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Budget other = (Budget) obj;
		return Objects.equals(dateDebut, other.dateDebut) 
				&& Objects.equals(dateFin, other.dateFin)
				&& Objects.equals(id, other.id) 
				&& Objects.equals(montantEnCentimes, other.montantEnCentimes)
				&& Objects.equals(reference.getId(), other.reference.getId()) 
				&& typePeriode == other.typePeriode;
	}
}
