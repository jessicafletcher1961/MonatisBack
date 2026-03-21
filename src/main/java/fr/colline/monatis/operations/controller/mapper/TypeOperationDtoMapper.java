package fr.colline.monatis.operations.controller.mapper;

import fr.colline.monatis.operations.controller.dto.response.TypeOperationResponseDto;
import fr.colline.monatis.operations.model.TypeOperation;

public class TypeOperationDtoMapper {

	public static TypeOperationResponseDto modelToResponseDto(TypeOperation typeOperation) {
		
		TypeOperationResponseDto dto = new TypeOperationResponseDto();
		
		dto.code = typeOperation.getCode();
		dto.libelle = typeOperation.getLibelle();
		
		return dto;
	}
}
