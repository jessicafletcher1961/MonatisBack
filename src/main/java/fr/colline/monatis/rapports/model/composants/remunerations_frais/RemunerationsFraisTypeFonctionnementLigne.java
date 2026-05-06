package fr.colline.monatis.rapports.model.composants.remunerations_frais;

import java.util.List;

import fr.colline.monatis.typologies.model.TypeFonctionnement;

public class RemunerationsFraisTypeFonctionnementLigne {

	private TypeFonctionnement typeFonctionnement;
	
	private List<RemunerationsFraisCompteInterneLigne> lignesCompteInterne;
	private RemunerationsFraisTypeFonctionnementPeriode[] cumuls;

	public TypeFonctionnement getTypeFonctionnement() {
		return typeFonctionnement;
	}

	public void setTypeFonctionnement(TypeFonctionnement typeFonctionnement) {
		this.typeFonctionnement = typeFonctionnement;
	}

	public List<RemunerationsFraisCompteInterneLigne> getLignesCompteInterne() {
		return lignesCompteInterne;
	}

	public void setLignesCompteInterne(List<RemunerationsFraisCompteInterneLigne> lignesCompteInterne) {
		this.lignesCompteInterne = lignesCompteInterne;
	}

	public RemunerationsFraisTypeFonctionnementPeriode[] getCumuls() {
		return cumuls;
	}

	public void setCumuls(RemunerationsFraisTypeFonctionnementPeriode[] cumulsPeriodes) {
		this.cumuls = cumulsPeriodes;
	}

}
