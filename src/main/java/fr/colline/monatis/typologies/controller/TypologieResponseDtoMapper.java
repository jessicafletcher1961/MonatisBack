package fr.colline.monatis.typologies.controller;

import fr.colline.monatis.typologies.model.TypeCompte;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;
import fr.colline.monatis.typologies.model.TypeProgrammation;
import fr.colline.monatis.typologies.model.TypeReference;

public class TypologieResponseDtoMapper {

	public static TypologieResponseDto mapperModelToResponseDto(TypeFonctionnement type) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		dto.code = type.getCode();
		dto.libelle = type.getLibelle();
		
		return dto;
	}

	public static TypologieResponseDto mapperModelToResponseDto(TypeCompte type) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		dto.code = type.getCode();
		dto.libelle = type.getLibelle();
		
		return dto;
	}

	public static TypologieResponseDto mapperModelToResponseDto(TypeOperation type) {
		
		TypeOperationResponseDto dto = new TypeOperationResponseDto();
		
		dto.code = type.getCode();
		dto.libelle = type.getLibelle();
		dto.libelleCourt = type.getLibelleCourt();
		dto.categorisable = type.isCategorisable();
		dto.fluxTechnique = type.isFluxTechnique();
		
		return dto;
	}

	public static TypologieResponseDto mapperModelToResponseDto(TypePeriode type) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		dto.code = type.getCode();
		dto.libelle = type.getLibelle();
		
		return dto;
	}

	public static TypologieResponseDto mapperModelToResponseDto(TypeProgrammation type) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		dto.code = type.getCode();
		dto.libelle = type.getLibelle();
		
		return dto;
	}

	public static TypologieResponseDto mapperModelToResponseDto(TypeReference type) {
		
		TypologieResponseDto dto = new TypologieResponseDto();
		
		dto.code = type.getCode();
		dto.libelle = type.getLibelle();
		
		return dto;
	}
}
