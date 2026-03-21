package fr.colline.monatis.comptes.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Compte {

	@Id
	@GeneratedValue(generator = "gen_seq_compte", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_compte", sequenceName = "seq_compte", allocationSize = 1)
	private Long id;
	
	@Column(length = 30, nullable = false, unique = true)
	private String identifiant;
	
	@Column(length = 240)
	private String libelle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public Compte() {}
	
	public Compte(String identifiant, String libelle) {
		this.identifiant = identifiant;
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, identifiant, libelle);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compte other = (Compte) obj;
		return Objects.equals(id, other.id) 
				&& Objects.equals(identifiant, other.identifiant)
				&& Objects.equals(libelle, other.libelle);
	}
	
	public abstract TypeCompte getTypeCompte();
}
