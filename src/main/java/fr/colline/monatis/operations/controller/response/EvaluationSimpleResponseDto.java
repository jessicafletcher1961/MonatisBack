package fr.colline.monatis.operations.controller.response;

import java.time.LocalDate;

import fr.colline.monatis.comptes.controller.interne.CompteInterneBasicResponseDto;
import fr.colline.monatis.comptes.controller.technique.CompteTechniqueBasicResponseDto;

public class EvaluationSimpleResponseDto {

	public String cle;
	public CompteInterneBasicResponseDto compteInterne;
	public CompteTechniqueBasicResponseDto compteTechnique;
	public LocalDate dateSolde;
	public String libelle;
	public Long montantSoldeEnCentimes;
	public OperationBasicResponseDto operationPlusMoinsSolde;
	
}
