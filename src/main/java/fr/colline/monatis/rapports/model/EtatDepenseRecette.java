package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.typologies.model.TypePeriode;

public class EtatDepenseRecette {

	private LocalDate dateDebutEtat;
	private LocalDate dateFinEtat;
	private TypePeriode typePeriode;
	private List<SousCategorie> sousCategories;
	private Beneficiaire beneficiaire;
	
	private List<DepenseRecetteCategorieLigne> lignesCategorie;
	private DepenseRecettePeriode[] cumulEtat;
		
	public LocalDate getDateDebutEtat() {
		return dateDebutEtat;
	}

	public void setDateDebutEtat(LocalDate dateDebutEtat) {
		this.dateDebutEtat = dateDebutEtat;
	}

	public LocalDate getDateFinEtat() {
		return dateFinEtat;
	}

	public void setDateFinEtat(LocalDate dateFinEtat) {
		this.dateFinEtat = dateFinEtat;
	}

	public TypePeriode getTypePeriode() {
		return typePeriode;
	}

	public void setTypePeriode(TypePeriode typePeriode) {
		this.typePeriode = typePeriode;
	}

	public List<SousCategorie> getSousCategories() {
		return sousCategories;
	}

	public void setSousCategories(List<SousCategorie> sousCategories) {
		this.sousCategories = sousCategories;
	}

	public Beneficiaire getBeneficiaire() {
		return beneficiaire;
	}

	public void setBeneficiaire(Beneficiaire beneficiaire) {
		this.beneficiaire = beneficiaire;
	}

	public List<DepenseRecetteCategorieLigne> getLignesCategorie() {
		return lignesCategorie;
	}

	public void setLignesCategorie(List<DepenseRecetteCategorieLigne> lignesCategorie) {
		this.lignesCategorie = lignesCategorie;
	}

	public DepenseRecettePeriode[] getCumulEtat() {
		return cumulEtat;
	}

	public void setCumulEtat(DepenseRecettePeriode[] periodes) {
		this.cumulEtat = periodes;
	}

}
