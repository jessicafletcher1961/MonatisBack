package fr.colline.monatis.rapports.model.composants.echeancier;

import fr.colline.monatis.emprunts.model.Echeance;

public class EcheancierEcheanceLigne extends EcheancierLigne {

	private Echeance echeance;

	private EcheancierConditionEmpruntLigne conditionEmpruntLigne;

	public Echeance getEcheance() {
		return echeance;
	}

	public void setEcheance(Echeance echeance) {
		this.echeance = echeance;
	}

	public EcheancierConditionEmpruntLigne getConditionEmpruntLigne() {
		return conditionEmpruntLigne;
	}

	public void setConditionEmpruntLigne(EcheancierConditionEmpruntLigne conditionEmpruntLigne) {
		this.conditionEmpruntLigne = conditionEmpruntLigne;
	}
}
