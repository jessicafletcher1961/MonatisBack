package fr.colline.monatis.operations.controller.response;

import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class OperationDetailedResponseDto extends OperationResponseDto {

	private static final long serialVersionUID = -8123052695805765163L;
	
	public TypeOperationResponseDto typeOperation;
	public CompteResponseDto compteRecette;
	public CompteResponseDto compteDepense;

	public List<OperationLigneDetailedResponseDto> lignes;
}
