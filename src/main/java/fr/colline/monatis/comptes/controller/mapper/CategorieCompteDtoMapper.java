package fr.colline.monatis.comptes.controller.mapper;

import fr.colline.monatis.comptes.controller.dto.response.CategorieCompteResponseDto;
import fr.colline.monatis.comptes.model.CategorieCompte;

public class CategorieCompteDtoMapper {

	public static CategorieCompteResponseDto modelToResponseDto(CategorieCompte categorieCompte) {
		
		CategorieCompteResponseDto dto = new CategorieCompteResponseDto();
		
		dto.code = categorieCompte.getCode();
		dto.libelle = categorieCompte.getLibelle();
		
		return dto;
	}
}
