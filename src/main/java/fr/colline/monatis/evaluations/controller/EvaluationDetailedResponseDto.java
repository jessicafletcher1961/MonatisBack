package fr.colline.monatis.evaluations.controller;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class EvaluationDetailedResponseDto extends EvaluationResponseDto {
	
	private static final long serialVersionUID = 8654505516515741868L;

	public CompteResponseDto compteInterne;
	public CompteResponseDto compteTechnique;

}
