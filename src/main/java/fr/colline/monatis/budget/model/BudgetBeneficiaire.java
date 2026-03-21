package fr.colline.monatis.budget.model;

import fr.colline.monatis.references.model.Beneficiaire;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BENEFICIAIRE")
public class BudgetBeneficiaire extends Budget {

	@Override
	public Beneficiaire getReference() {
		return (Beneficiaire) super.getReference();
	}
}
