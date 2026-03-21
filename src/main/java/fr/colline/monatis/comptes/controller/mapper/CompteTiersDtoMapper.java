package fr.colline.monatis.comptes.controller.mapper;

import fr.colline.monatis.comptes.controller.dto.response.CompteTiersBasicResponseDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteTiersDetailedResponseDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteTiersSimpleResponseDto;
import fr.colline.monatis.comptes.model.CompteTiers;

public class CompteTiersDtoMapper {

	public static CompteTiersBasicResponseDto modelToBasicResponseDto(CompteTiers compteTiers) {

		CompteTiersBasicResponseDto dto = new CompteTiersBasicResponseDto();
		
		dto.identifiant = compteTiers.getIdentifiant();
		dto.libelle = compteTiers.getLibelle();

		return dto;
	}

	public static CompteTiersSimpleResponseDto modelToSimpleResponseDto(CompteTiers compteTiers) {

		CompteTiersSimpleResponseDto dto = new CompteTiersSimpleResponseDto();
		
		dto.identifiant = compteTiers.getIdentifiant();
		dto.libelle = compteTiers.getLibelle();

		return dto;
	}

	public static CompteTiersDetailedResponseDto modelToDetailedResponseDto(CompteTiers compteTiers) {

		CompteTiersDetailedResponseDto dto = new CompteTiersDetailedResponseDto();
		
		dto.identifiant = compteTiers.getIdentifiant();
		dto.libelle = compteTiers.getLibelle();

		return dto;
	}
}
