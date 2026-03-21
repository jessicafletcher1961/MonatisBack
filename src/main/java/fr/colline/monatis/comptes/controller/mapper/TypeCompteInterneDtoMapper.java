package fr.colline.monatis.comptes.controller.mapper;

import fr.colline.monatis.comptes.controller.dto.response.TypeCompteInterneResponseDto;
import fr.colline.monatis.comptes.model.TypeCompteInterne;

public class TypeCompteInterneDtoMapper {

	public static TypeCompteInterneResponseDto modelToResponseDto(TypeCompteInterne typeCompteInterne) {
		
		TypeCompteInterneResponseDto dto = new TypeCompteInterneResponseDto();
		
		dto.code = typeCompteInterne.getCode();
		dto.libelle = typeCompteInterne.getLibelle();
		dto.typeFonctionnementCompte = TypeFonctionnementCompteDtoMapper.modelToResponseDto(typeCompteInterne.getTypeFonctionnementCompte());
		
		return dto;
	}
}
