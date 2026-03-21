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

	public Double montantBudgeteEnEuros;
	
	public Double montantExecutionEnEuros;

	public Double montantExecutionEnPourcentage;

	public Double montantTotalLignesDepenseEnEuros;
	
	public Double montantTotalLignesRecetteEnEuros;

	public Double montantTotalLignesExcluesEnEuros;

	public Double resteADepenserEnEuros;
	
	public List<OperationLigneBasicResponseDto> lignesDepense;

	public List<OperationLigneBasicResponseDto> lignesRecette;
	
	public List<OperationLigneBasicResponseDto> lignesExclues;
	
}
