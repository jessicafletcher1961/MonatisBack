package fr.colline.monatis.budget.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import fr.colline.monatis.model.references.Reference;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="budget")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_reference", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Budget {

	@Id
	@GeneratedValue(generator = "gen_seq_budget", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_budget", sequenceName = "seq_budget", allocationSize = 1)
	Long id;
	
	@Column(length = 10, nullable = false)
	TypeBudget typeBudget;
	
	@Column(nullable = false)
	ZonedDateTime dateDebut;

	@Column(nullable = false)
	ZonedDateTime dateFin;
	
	@Column(nullable = false)
	Long montantBudgetEnCentimes;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "reference_id")
	private Reference reference;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TypeBudget getTypeBudget() {
		return typeBudget;
	}

	public void setTypeBudget(TypeBudget typeBudget) {
		this.typeBudget = typeBudget;
	}

	public ZonedDateTime getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(ZonedDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}

	public ZonedDateTime getDateFin() {
		return dateFin;
	}

	public void setDateFin(ZonedDateTime dateFin) {
		this.dateFin = dateFin;
	}

	public Long getMontantBudgetEnCentimes() {
		return montantBudgetEnCentimes;
	}

	public void setMontantBudgetEnCentimes(Long montantBudgetEnCentimes) {
		this.montantBudgetEnCentimes = montantBudgetEnCentimes;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Budget() {}
	
	public Budget(
			Long montantBudgetEnCentimes,
			TypeBudget typeBudget,
			ZonedDateTime dateDebut,
			ZonedDateTime dateFin,
			Reference reference) {
		
		this.montantBudgetEnCentimes = montantBudgetEnCentimes;
		this.typeBudget = typeBudget;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.reference = reference;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateDebut, dateFin, id, montantBudgetEnCentimes, reference, typeBudget);
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
		return Objects.equals(dateDebut, other.dateDebut) && Objects.equals(dateFin, other.dateFin)
				&& Objects.equals(id, other.id)
				&& Objects.equals(montantBudgetEnCentimes, other.montantBudgetEnCentimes)
				&& Objects.equals(reference, other.reference) && typeBudget == other.typeBudget;
	}
}
