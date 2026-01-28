package fr.colline.monatis.evaluations.model;

import java.time.LocalDate;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
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
		@Index(name = "evaluation_cle_idx", columnList = "cle", unique = true),
		@Index(name = "evaluation_compte_interne_id_date_solde_idx", columnList = "compte_interne_id,date_solde", unique = true)})

public class Evaluation {

	@Id
	@GeneratedValue(generator = "gen_seq_evaluation", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_evaluation", sequenceName = "seq_evaluation", allocationSize = 1)
	private Long id;
	
	@Column(length = 30)
	private String cle;
	
	@ManyToOne(optional = false)
	private CompteInterne compteInterne;

	@ManyToOne(optional = false)
	private CompteTechnique compteTechnique;
	
	@Column(nullable = false) 
	private LocalDate dateSolde;
	
	@Column(nullable = false) 
	private Long montantSoldeEnCentimes;
	
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

	public void setCle(String identifiant) {
		this.cle = identifiant;
	}

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public CompteTechnique getCompteTechnique() {
		return compteTechnique;
	}

	public void setCompteTechnique(CompteTechnique compteTechnique) {
		this.compteTechnique = compteTechnique;
	}

	public LocalDate getDateSolde() {
		return dateSolde;
	}

	public void setDateSolde(LocalDate dateSolde) {
		this.dateSolde = dateSolde;
	}

	public Long getMontantSoldeEnCentimes() {
		return montantSoldeEnCentimes;
	}

	public void setMontantSoldeEnCentimes(Long montantSoldeEnCentimes) {
		this.montantSoldeEnCentimes = montantSoldeEnCentimes;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Evaluation() {}
	
	public Evaluation(
			String identifiant,
			CompteInterne compteInterne,
			CompteTechnique compteTechnique,
			LocalDate dateSolde,
			Long montantSoldeEnCentimes,
			String libelle) {
		
		this.cle = identifiant;
		this.compteInterne = compteInterne;
		this.compteTechnique = compteTechnique;
		this.dateSolde = dateSolde;
		this.montantSoldeEnCentimes = montantSoldeEnCentimes;
		this.libelle = libelle;
	}
	
}
