package fr.colline.monatis.budget.model;

import fr.colline.monatis.references.model.Categorie;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CATEGORIE")
public class BudgetCategorie extends Budget {

	@Override
	public Categorie getReference() {
		return (Categorie) super.getReference();
	}
}
