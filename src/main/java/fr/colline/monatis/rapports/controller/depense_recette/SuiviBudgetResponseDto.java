package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class SuiviBudgetResponseDto implements Serializable {

	private static final long serialVersionUID = 5901837447644947446L;

	public TypologieResponseDto typeBudget;
	public Double montantBudgetEnEuros;
	public Double montantExecutionEnEuros;
	public Double montantVertEnEuros;
	public Double montantRougeEnEuros;
	public Double tauxExecutionBudget;
}
