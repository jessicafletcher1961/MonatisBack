package fr.colline.monatis.rapports.model.composants.echeancier;

import fr.colline.monatis.emprunts.model.ConditionEmprunt;

public class EcheancierConditionEmpruntLigne extends EcheancierLigne{
	
	private ConditionEmprunt conditionEmprunt;
	
	private EcheancierCumuls cumuls;

	public ConditionEmprunt getConditionEmprunt() {
		return conditionEmprunt;
	}

	public void setConditionEmprunt(ConditionEmprunt conditionEmprunt) {
		this.conditionEmprunt = conditionEmprunt;
	}

	public EcheancierCumuls getCumuls() {
		return cumuls;
	}

	public void setCumuls(EcheancierCumuls cumuls) {
		this.cumuls = cumuls;
	}
}
