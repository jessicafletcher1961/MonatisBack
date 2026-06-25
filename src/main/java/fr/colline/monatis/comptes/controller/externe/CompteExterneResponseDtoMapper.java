package fr.colline.monatis.comptes.controller.externe;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.comptes.model.CompteExterne;

public class CompteExterneResponseDtoMapper {

	public static CompteResponseDto mapperModelToBasicResponseDto(CompteExterne compteExterne) {

		CompteExterneBasicResponseDto dto = new CompteExterneBasicResponseDto();
		
		dto.id = compteExterne.getId();
		dto.identifiant = compteExterne.getIdentifiant();
		dto.libelle = compteExterne.getLibelle();
				
		return dto;
	}

	public static CompteResponseDto mapperModelToSimpleResponseDto(CompteExterne compteExterne) {

		CompteExterneSimpleResponseDto dto = new CompteExterneSimpleResponseDto();
		
		dto.id = compteExterne.getId();
		dto.identifiant = compteExterne.getIdentifiant();
		dto.libelle = compteExterne.getLibelle();

		return dto;
	}

	public static CompteResponseDto mapperModelToDetailedResponseDto(CompteExterne compteExterne) {

		CompteExterneDetailedResponseDto dto = new CompteExterneDetailedResponseDto();
		
		dto.id = compteExterne.getId();
		dto.identifiant = compteExterne.getIdentifiant();
		dto.libelle = compteExterne.getLibelle();
		
		return dto;
	}
}
