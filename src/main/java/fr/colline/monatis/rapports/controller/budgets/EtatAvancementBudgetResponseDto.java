package fr.colline.monatis.rapports.controller.budgets;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.budgets.controller.BudgetResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneBasicResponseDto;
import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class EtatAvancementBudgetResponseDto implements Serializable {

	private static final long serialVersionUID = -7800936517985994818L;

	public ReferenceResponseDto reference;

	public BudgetResponseDto budget;

	public Float montantBudgeteEnEuros;
	
	public Float montantExecutionEnEuros;

	public Float montantExecutionEnPourcentage;

	public Float montantTotalLignesDepenseEnEuros;
	
	public Float montantTotalLignesRecetteEnEuros;

	public Float montantTotalLignesExcluesEnEuros;

	public Float resteADepenserEnEuros;
	
	public List<OperationLigneBasicResponseDto> lignesDepense;

	public List<OperationLigneBasicResponseDto> lignesRecette;
	
	public List<OperationLigneBasicResponseDto> lignesExclues;
	
}
