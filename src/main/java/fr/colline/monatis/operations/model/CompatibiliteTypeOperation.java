package fr.colline.monatis.operations.model;

import java.util.List;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.typologies.model.TypeOperation;

public class CompatibiliteTypeOperation {

	private TypeOperation typeOperation;
	
	private List<Compte> comptesCompatiblesDepense;
	private List<Compte> comptesCompatiblesRecette;

	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	public List<Compte> getComptesCompatiblesDepense() {
		return comptesCompatiblesDepense;
	}

	public void setComptesCompatiblesDepense(List<Compte> comptesDepense) {
		this.comptesCompatiblesDepense = comptesDepense;
	}

	public List<Compte> getComptesCompatiblesRecette() {
		return comptesCompatiblesRecette;
	}

	public void setComptesCompatiblesRecette(List<Compte> compteRecette) {
		this.comptesCompatiblesRecette = compteRecette;
	}
}
