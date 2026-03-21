package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.utils.TypePeriode;

public class EtatDepenseRecette {

	private LocalDate dateDebutEtat;
	private LocalDate dateFinEtat;
	private TypePeriode typePeriode;
	private List<SousCategorie> sousCategories;
	private List<Categorie> categories;
	private Beneficiaire beneficiaire;
	
	private List<DepenseRecetteCategorieLigne> lignesCategorie;
	private DepenseRecettePeriode[] cumuls;
		
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

	public List<Categorie> getCategories() {
		return categories;
	}

	public void setCategories(List<Categorie> categories) {
		this.categories = categories;
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

	public DepenseRecettePeriode[] getCumuls() {
		return cumuls;
	}

	public void setCumuls(DepenseRecettePeriode[] cumuls) {
		this.cumuls = cumuls;
	}

}
