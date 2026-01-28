package fr.colline.monatis.evaluations.controller;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class EvaluationSimpleResponseDto extends EvaluationResponseDto {

	private static final long serialVersionUID = 7187783141889624563L;
	
	public CompteResponseDto compteInterne;
	public CompteResponseDto compteTechnique;
	
}
