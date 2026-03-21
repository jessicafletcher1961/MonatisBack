package fr.colline.monatis.operations.controller.response;

import java.util.List;

public class OperationBasicResponseDto extends OperationResponseDto {

	private static final long serialVersionUID = -2470111800397301732L;
	
	public String codeTypeOperation;
	public String identifiantCompteDepense;
	public String identifiantCompteRecette;
	
	public List<OperationLigneBasicResponseDto> lignes;

}
