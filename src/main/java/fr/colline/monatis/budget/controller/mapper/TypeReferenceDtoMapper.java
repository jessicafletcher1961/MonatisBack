package fr.colline.monatis.budget.controller.mapper;

import fr.colline.monatis.budget.controller.dto.TypeReferenceResponseDto;
import fr.colline.monatis.budget.model.TypeReference;

public class TypeReferenceDtoMapper {

	public static TypeReferenceResponseDto modelToResponseDto(TypeReference typeReference) {
		
		TypeReferenceResponseDto dto = new TypeReferenceResponseDto();
		
		dto.code = typeReference.getCode();
		dto.libelle = typeReference.getLibelle();
		
		return dto;
	}

}
