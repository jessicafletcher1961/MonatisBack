package fr.colline.monatis.rapports.model.composants.echeancier;

import java.time.LocalDate;

public class EcheancierCumuls {

	private LocalDate datePremiereEcheance;
	private LocalDate dateDerniereEcheance;
	private int nombreEcheances;
	
	private long capitalEmprunteDejaRembourse;
	private long capitalEmprunteRestantDu;
	
	private long cumulPartCapital;
	private long cumulPartInterets;
	private long cumulPartFraisFixes;
	private long cumulMontantPaiement;

	public EcheancierCumuls(LocalDate datePremiereEcheance) {
		this.datePremiereEcheance = datePremiereEcheance;
	}
	
	public LocalDate getDatePremiereEcheance() {
		return datePremiereEcheance;
	}

	public void setDatePremiereEcheance(LocalDate datePremiereEcheance) {
		this.datePremiereEcheance = datePremiereEcheance;
	}

	public LocalDate getDateDerniereEcheance() {
		return dateDerniereEcheance;
	}

	public void setDateDerniereEcheance(LocalDate dateDerniereEcheance) {
		this.dateDerniereEcheance = dateDerniereEcheance;
	}

	public int getNombreEcheances() {
		return nombreEcheances;
	}

	public void setNombreEcheances(int nombreEcheances) {
		this.nombreEcheances = nombreEcheances;
	}

	public long getCapitalEmprunteDejaRembourse() {
		return capitalEmprunteDejaRembourse;
	}

	public void setCapitalEmprunteDejaRembourse(long capitalEmprunteDejaRembourse) {
		this.capitalEmprunteDejaRembourse = capitalEmprunteDejaRembourse;
	}

	public long getCapitalEmprunteRestantDu() {
		return capitalEmprunteRestantDu;
	}

	public void setCapitalEmprunteRestantDu(long capitalEmprunteRestantDu) {
		this.capitalEmprunteRestantDu = capitalEmprunteRestantDu;
	}

	public long getCumulPartCapital() {
		return cumulPartCapital;
	}

	public void setCumulPartCapital(long cumulPartCapital) {
		this.cumulPartCapital = cumulPartCapital;
	}

	public long getCumulPartInterets() {
		return cumulPartInterets;
	}

	public void setCumulPartInterets(long cumulPartInterets) {
		this.cumulPartInterets = cumulPartInterets;
	}

	public long getCumulPartFraisFixes() {
		return cumulPartFraisFixes;
	}

	public void setCumulPartFraisFixes(long cumulPartFraisFixes) {
		this.cumulPartFraisFixes = cumulPartFraisFixes;
	}

	public long getCumulMontantPaiement() {
		return cumulMontantPaiement;
	}

	public void setCumulMontantPaiement(long cumulMontantPaiement) {
		this.cumulMontantPaiement = cumulMontantPaiement;
	}

}
