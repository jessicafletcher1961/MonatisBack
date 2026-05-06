package fr.colline.monatis.rapports.model.composants.bilan_patrimoine;

import java.util.List;

import fr.colline.monatis.typologies.model.TypeFonctionnement;

public class BilanPatrimoineTypeFonctionnementLigne {

	private TypeFonctionnement typeFonctionnement;
	
	private List<BilanPatrimoineCompteInterneLigne> lignesCompteInterne;
	private Long montantSoldeInitialEnCentimes;
	private BilanPatrimoineTypeFonctionnementPeriode[] cumulsPeriodes;

	public TypeFonctionnement getTypeFonctionnement() {
		return typeFonctionnement;
	}

	public void setTypeFonctionnement(TypeFonctionnement typeFonctionnement) {
		this.typeFonctionnement = typeFonctionnement;
	}

	public List<BilanPatrimoineCompteInterneLigne> getLignesCompteInterne() {
		return lignesCompteInterne;
	}

	public void setLignesCompteInterne(List<BilanPatrimoineCompteInterneLigne> details) {
		this.lignesCompteInterne = details;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public BilanPatrimoineTypeFonctionnementPeriode[] getCumulsPeriodes() {
		return cumulsPeriodes;
	}

	public void setCumulsPeriodes(BilanPatrimoineTypeFonctionnementPeriode[] periodes) {
		this.cumulsPeriodes = periodes;
	}
}
