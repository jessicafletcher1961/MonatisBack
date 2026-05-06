package fr.colline.monatis.operations.controller.response;

import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class CompatibilitesResponseDto {

	public List<CompteResponseDto> comptesCompatiblesDepense;
	
	public List<CompteResponseDto> comptesCompatiblesRecette;

	public List<TypologieResponseDto> typesOperationsCompatiblesDepense;

	public List<TypologieResponseDto> typesOperationsCompatiblesRecette;

}
