package fr.colline.monatis.operations.controller.response;

import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class CompatibilitesResponseDto {

	public List<CompteResponseDto> comptesCompatiblesDepense;
	
	public List<CompteResponseDto> comptesCompatiblesRecette;

	public List<TypeOperationResponseDto> typesOperationsCompatiblesDepense;

	public List<TypeOperationResponseDto> typesOperationsCompatiblesRecette;

}
