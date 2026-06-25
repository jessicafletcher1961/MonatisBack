package fr.colline.monatis.emprunts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import fr.colline.monatis.comptes.model.CompteInterne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Emprunt {

	@Id
	@GeneratedValue(generator = "gen_seq_emprunt", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gen_seq_emprunt", sequenceName = "seq_emprunt", allocationSize = 1)
	private Long id;

	@Column(length = 30) 
	private String cle;
	
	@Column(length = 240) 
	private String libelle;
	
	@OneToOne
	private CompteInterne compteInterne;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "emprunt", orphanRemoval = true)
	private List<ConditionEmprunt> conditionsEmprunt = new ArrayList<ConditionEmprunt>();

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

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public List<ConditionEmprunt> getConditionsEmprunt() {
		return conditionsEmprunt;
	}

	public void setConditionsEmprunt(List<ConditionEmprunt> revisions) {
		this.conditionsEmprunt = revisions;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				cle, 
				compteInterne, 
				id, 
				libelle, 
				conditionsEmprunt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emprunt other = (Emprunt) obj;
		return Objects.equals(cle, other.cle)
				&& Objects.equals(compteInterne, other.compteInterne)
				&& Objects.equals(id, other.id) && Objects.equals(libelle, other.libelle)
				&& Objects.equals(conditionsEmprunt, other.conditionsEmprunt);
	}

	public void changerConditionsEmprunt(List<ConditionEmprunt> nouvellesRevisions) {
		
		List<ConditionEmprunt> anciennesRevisions = new ArrayList<ConditionEmprunt>(this.conditionsEmprunt);
		
		List<ConditionEmprunt> aCreer = new ArrayList<ConditionEmprunt>(nouvellesRevisions);
		aCreer.removeAll(anciennesRevisions);
		for ( ConditionEmprunt revision : aCreer ) {
			if ( this.conditionsEmprunt.add(revision) ) revision.setEmprunt(this);
		}
		
		List<ConditionEmprunt> aSupprimer = new ArrayList<ConditionEmprunt>(anciennesRevisions);
		aSupprimer.removeAll(nouvellesRevisions);
		for ( ConditionEmprunt revision : aSupprimer ) {
			this.conditionsEmprunt.remove(revision);
		}
	}
	
	public ConditionEmprunt getConditionEmpruntInitiale() {
		
		Optional<ConditionEmprunt> conditionEmpruntInitiale = conditionsEmprunt
				.stream()
				.sorted((e1, e2) -> {return Integer.compare(e1.getNumeroPremiereEcheance(), e2.getNumeroPremiereEcheance());})
				.findFirst();
		
		return conditionEmpruntInitiale.get();
	}
	
	public List<ConditionEmprunt> getRevisions() {

		Optional<ConditionEmprunt> conditionEmpruntInitiale = conditionsEmprunt
				.stream()
				.sorted((e1, e2) -> {return Integer.compare(e1.getNumeroPremiereEcheance(), e2.getNumeroPremiereEcheance());})
				.findFirst();

		final int premiereValeur = conditionEmpruntInitiale.get().getNumeroPremiereEcheance();
		return conditionsEmprunt
				.stream()
				.filter((e) -> {return e.getNumeroPremiereEcheance() != premiereValeur;})
				.sorted((e1, e2) -> {return Integer.compare(e1.getNumeroPremiereEcheance(), e2.getNumeroPremiereEcheance());})
				.toList();
	}
}
