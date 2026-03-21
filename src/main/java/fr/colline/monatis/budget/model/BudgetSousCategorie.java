package fr.colline.monatis.budget.model;

import fr.colline.monatis.references.model.SousCategorie;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SOUS_CATEGORIE")
public class BudgetSousCategorie extends Budget {

	@Override
	public SousCategorie getReference() {
		return (SousCategorie) super.getReference();
	}
}
