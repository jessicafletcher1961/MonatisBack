package fr.colline.monatis.operations.controller;

import fr.colline.monatis.operations.controller.response.TypeOperationResponseDto;
import fr.colline.monatis.operations.model.TypeOperation;

public class TypeOperationDtoMapper {

	public static TypeOperationResponseDto mapperModelToResponseDto(TypeOperation typeOperation) {
		
		TypeOperationResponseDto dto = new TypeOperationResponseDto();
		
		dto.code = typeOperation.getCode();
		dto.libelle = typeOperation.getLibelle();
		
		return dto;
	}

}
