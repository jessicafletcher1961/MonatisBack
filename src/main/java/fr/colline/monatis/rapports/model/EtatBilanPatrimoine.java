package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoinePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypePeriode;

public class EtatBilanPatrimoine {
	
	private LocalDate dateDebutEtat;
	private LocalDate dateFinEtat;
	private TypePeriode typePeriode;
	
	private List<CompteInterne> comptesInternes;
	private List<TypeFonctionnement> typesFonctionnements;
	private Titulaire titulaire;

	private List<BilanPatrimoineTypeFonctionnementLigne> lignesTypeFonctionnement;
	private Long montantSoldeInitialEnCentimes;
	private BilanPatrimoinePeriode[] cumuls;

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

	public List<BilanPatrimoineTypeFonctionnementLigne> getLignesTypeFonctionnement() {
		return lignesTypeFonctionnement;
	}

	public void setLignesTypeFonctionnement(List<BilanPatrimoineTypeFonctionnementLigne> details) {
		this.lignesTypeFonctionnement = details;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long soldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = soldeInitialEnCentimes;
	}

	public BilanPatrimoinePeriode[] getCumuls() {
		return cumuls;
	}

	public void setCumuls(BilanPatrimoinePeriode[] periodes) {
		this.cumuls = periodes;
	}
}
