package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisPeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementLigne;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.utils.TypePeriode;

public class EtatRemunerationsFrais {

	private LocalDate dateDebutEtat;
	private LocalDate dateFinEtat;
	private TypePeriode typePeriode;
	
	private List<CompteInterne> comptesInternes;
	private List<TypeFonctionnement> typesFonctionnements;
	private Titulaire titulaire;

	private List<RemunerationsFraisTypeFonctionnementLigne> lignesTypeFonctionnement;
	private RemunerationsFraisPeriode[] cumuls;

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

	public List<CompteInterne> getComptesInternes() {
		return comptesInternes;
	}

	public void setComptesInternes(List<CompteInterne> comptesInternes) {
		this.comptesInternes = comptesInternes;
	}

	public List<TypeFonctionnement> getTypesFonctionnements() {
		return typesFonctionnements;
	}

	public void setTypesFonctionnements(List<TypeFonctionnement> typesFonctionnements) {
		this.typesFonctionnements = typesFonctionnements;
	}

	public Titulaire getTitulaire() {
		return titulaire;
	}

	public void setTitulaire(Titulaire titulaire) {
		this.titulaire = titulaire;
	}

	public List<RemunerationsFraisTypeFonctionnementLigne> getLignesTypeFonctionnement() {
		return lignesTypeFonctionnement;
	}

	public void setLignesTypeFonctionnement(List<RemunerationsFraisTypeFonctionnementLigne> lignesTypeFonctionnement) {
		this.lignesTypeFonctionnement = lignesTypeFonctionnement;
	}

	public RemunerationsFraisPeriode[] getCumuls() {
		return cumuls;
	}

	public void setCumuls(RemunerationsFraisPeriode[] cumuls) {
		this.cumuls = cumuls;
	}
}
