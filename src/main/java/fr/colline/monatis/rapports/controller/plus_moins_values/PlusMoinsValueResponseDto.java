package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;

public class PlusMoinsValueResponseDto implements Serializable {

	private static final long serialVersionUID = 3745449613340820571L;
	
	public LocalDate dateDebutEvaluation;

	public LocalDate dateFinEvaluation;

	public Float montantSoldeInitialEnEuros;
	
	public Float montantSoldeFinalEnEuros;

	public float montantMouvementsEnEuros;
	
	public float montantRemunerationEnEuros; 

	public Float montantReevaluationEnEuros;
	
	public Float montantPlusMoinsValueEnPourcentage;

}
