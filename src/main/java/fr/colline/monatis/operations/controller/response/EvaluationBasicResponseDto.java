package fr.colline.monatis.operations.controller.response;

import java.time.LocalDate;

public class EvaluationBasicResponseDto {

	public String cle;
	public String compteInterne;
	public String compteTechnique;
	public LocalDate dateSolde;
	public Long montantSoldeEnCentimes;
	public String libelle;
	public String operationPlusMoinsSolde;
}
