package fr.colline.monatis.operations.controller.response;

import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class OperationSimpleResponseDto extends OperationResponseDto {

	private static final long serialVersionUID = -8111224730730720112L;
	
	public TypeOperationResponseDto typeOperation;
	public CompteResponseDto compteRecette;
	public CompteResponseDto compteDepense;
	
	public List<OperationLigneSimpleResponseDto> lignes;
}
