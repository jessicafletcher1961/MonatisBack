package fr.colline.monatis.rapports.model;

import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierCumuls;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierLigne;

public class Echeancier {

	private Emprunt emprunt;
	private LocalDate dateCible;

	private EcheancierCumuls cumulFinal;
	private EcheancierCumuls cumulDateCible;
	private List<EcheancierLigne> lignes;

	public Emprunt getEmprunt() {
		return emprunt;
	}

	public void setEmprunt(Emprunt emprunt) {
		this.emprunt = emprunt;
	}

	public LocalDate getDateCible() {
		return dateCible;
	}

	public void setDateCible(LocalDate dateCible) {
		this.dateCible = dateCible;
	}

	public EcheancierCumuls getCumulFinal() {
		return cumulFinal;
	}

	public void setCumulFinal(EcheancierCumuls cumulFinal) {
		this.cumulFinal = cumulFinal;
	}

	public EcheancierCumuls getCumulDateCible() {
		return cumulDateCible;
	}

	public void setCumulDateCible(EcheancierCumuls cumulDateCible) {
		this.cumulDateCible = cumulDateCible;
	}

	public List<EcheancierLigne> getLignes() {
		return lignes;
	}

	public void setLignes(List<EcheancierLigne> lignes) {
		this.lignes = lignes;
	}

}
