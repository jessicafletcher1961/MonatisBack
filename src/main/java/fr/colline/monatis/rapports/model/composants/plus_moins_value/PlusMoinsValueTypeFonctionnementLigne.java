package fr.colline.monatis.rapports.model.composants.plus_moins_value;

import java.util.List;

import fr.colline.monatis.comptes.model.TypeFonctionnement;

public class PlusMoinsValueTypeFonctionnementLigne {

	private TypeFonctionnement typeFonctionnement;
	
	private List<PlusMoinsValueCompteInterneLigne> lignesCompteInterne;
	private PlusMoinsValueTypeFonctionnementPeriode[] cumulsPeriodes;

	public TypeFonctionnement getTypeFonctionnement() {
		return typeFonctionnement;
	}

	public void setTypeFonctionnement(TypeFonctionnement typeFonctionnement) {
		this.typeFonctionnement = typeFonctionnement;
	}

	public List<PlusMoinsValueCompteInterneLigne> getLignesCompteInterne() {
		return lignesCompteInterne;
	}

	public void setLignesCompteInterne(List<PlusMoinsValueCompteInterneLigne> lignesCompteInterne) {
		this.lignesCompteInterne = lignesCompteInterne;
	}

	public PlusMoinsValueTypeFonctionnementPeriode[] getCumulsPeriodes() {
		return cumulsPeriodes;
	}

	public void setCumulsPeriodes(PlusMoinsValueTypeFonctionnementPeriode[] cumulsPeriodes) {
		this.cumulsPeriodes = cumulsPeriodes;
	}
}
