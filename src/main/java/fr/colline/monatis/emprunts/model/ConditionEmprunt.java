package fr.colline.monatis.emprunts.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.typologies.model.TypePeriode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

@Entity
public class ConditionEmprunt {

	@Id
	@GeneratedValue(generator = "gen_seq_condition_emprunt", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_condition_emprunt", sequenceName = "seq_condition_emprunt", allocationSize = 1)
	private Long id;

	@ManyToOne(optional = false)
	private Emprunt emprunt;

	@Column(length = 240) 
	private String libelle;

	@Column(nullable = false) 
	private Long capitalEmprunteEnCentimes;

	private int duree;
	
	@Column(nullable = false)
	private TypePeriode typePeriodeEcheances;

	@Column(nullable = false) 
	private double tauxAnnuel; 
	
	@Column(nullable = false)
	private Long montantTotalEcheanceEnCentimes;
	
	@Column(nullable = false) 
	private Long montantFraisFixesEcheanceEnCentimes;

	/** Numéro de l'échéance à partir de laquelle s'appliquent ces conditions */
	private int numeroPremiereEcheance;

	/** Date de l'échéance à partir de laquelle s'appliquent ces conditions */
	private LocalDate datePremiereEcheance;

	// Les échéances, calculées, ne sont pas stockées en base et doivent être recalculées au besoin
	
	@Transient
	private List<Echeance> echeances;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Emprunt getEmprunt() {
		return emprunt;
	}

	public void setEmprunt(Emprunt emprunt) {
		this.emprunt = emprunt;
	}

	public double getTauxAnnuel() {
		return tauxAnnuel;
	}

	public void setTauxAnnuel(double tauxAnnuel) {
		this.tauxAnnuel = tauxAnnuel;
	}

	public Long getCapitalEmprunteEnCentimes() {
		return capitalEmprunteEnCentimes;
	}

	public void setCapitalEmprunteEnCentimes(Long montantCapitalEmprunteEnCentimes) {
		this.capitalEmprunteEnCentimes = montantCapitalEmprunteEnCentimes;
	}

	public TypePeriode getTypePeriodeEcheances() {
		return typePeriodeEcheances;
	}

	public void setTypePeriodeEcheances(TypePeriode typePeriodeEcheance) {
		this.typePeriodeEcheances = typePeriodeEcheance;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int nombreTotalEcheancesEmprunt) {
		this.duree = nombreTotalEcheancesEmprunt;
	}

	public Long getMontantTotalEcheanceEnCentimes() {
		return montantTotalEcheanceEnCentimes;
	}

	public void setMontantTotalEcheanceEnCentimes(Long montantTotalEcheanceEnCentimes) {
		this.montantTotalEcheanceEnCentimes = montantTotalEcheanceEnCentimes;
	}

	public Long getMontantFraisFixesEcheanceEnCentimes() {
		return montantFraisFixesEcheanceEnCentimes;
	}

	public void setMontantFraisFixesEcheanceEnCentimes(Long montantFraisFixesEcheanceEnCentimes) {
		this.montantFraisFixesEcheanceEnCentimes = montantFraisFixesEcheanceEnCentimes;
	}

	public int getNumeroPremiereEcheance() {
		return numeroPremiereEcheance;
	}

	public void setNumeroPremiereEcheance(int numeroPremiereEcheance) {
		this.numeroPremiereEcheance = numeroPremiereEcheance;
	}

	public LocalDate getDatePremiereEcheance() {
		return datePremiereEcheance;
	}

	public void setDatePremiereEcheance(LocalDate datePremiereEcheance) {
		this.datePremiereEcheance = datePremiereEcheance;
	}

	public List<Echeance> getEcheances() {
		return echeances;
	}

	public void setEcheances(List<Echeance> echeances) {
		this.echeances = echeances;
	}	
}
