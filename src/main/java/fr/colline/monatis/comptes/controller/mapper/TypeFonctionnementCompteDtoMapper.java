package fr.colline.monatis.comptes.controller.mapper;

import fr.colline.monatis.comptes.controller.dto.response.TypeFonctionnementCompteResponseDto;
import fr.colline.monatis.comptes.model.TypeFonctionnementCompte;

public class TypeFonctionnementCompteDtoMapper {

	public static TypeFonctionnementCompteResponseDto modelToResponseDto(
			TypeFonctionnementCompte typeFonctionnementCompte) {
		
		TypeFonctionnementCompteResponseDto dto = new TypeFonctionnementCompteResponseDto();
		
		dto.code = typeFonctionnementCompte.getCode();
		dto.libelle = typeFonctionnementCompte.getLibelle();
		
		return dto;
	}
}
