package fr.colline.monatis.operations.model;

import java.util.List;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.typologies.model.TypeOperation;

public class CompatibiliteCompte {

	private Compte compte;
	
	private List<TypeOperation> typesOperationsCompatiblesDepense;
	
	private List<TypeOperation> typesOperationsCompatiblesRecette;

	public Compte getCompte() {
		return compte;
	}

	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	public List<TypeOperation> getTypesOperationsCompatiblesDepense() {
		return typesOperationsCompatiblesDepense;
	}

	public void setTypesOperationsCompatiblesDepense(List<TypeOperation> typesOperationsCompatiblesDepense) {
		this.typesOperationsCompatiblesDepense = typesOperationsCompatiblesDepense;
	}

	public List<TypeOperation> getTypesOperationsCompatiblesRecette() {
		return typesOperationsCompatiblesRecette;
	}

	public void setTypesOperationsCompatiblesRecette(List<TypeOperation> typesOperationsCompatiblesRecette) {
		this.typesOperationsCompatiblesRecette = typesOperationsCompatiblesRecette;
	}
}
