package fr.colline.monatis.references.controller.mapper;

import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieBasicResponseDto;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieSimpleResponseDto;
import fr.colline.monatis.references.model.SousCategorie;

public class SousCategorieDtoMapper {

	public static SousCategorieBasicResponseDto modelToBasicResponseDto(SousCategorie sousCategorie) {

		SousCategorieBasicResponseDto dto = new SousCategorieBasicResponseDto();

		dto.nom = sousCategorie.getNom();
		dto.libelle = sousCategorie.getLibelle();
		
		return dto;
	}

	public static SousCategorieSimpleResponseDto modelToSimpleResponseDto(SousCategorie sousCategorie) {

		SousCategorieSimpleResponseDto dto = new SousCategorieSimpleResponseDto();
		
		dto.nom = sousCategorie.getNom();
		dto.libelle = sousCategorie.getLibelle();
		
		if ( sousCategorie.getCategorie() != null ) {
			dto.nomCategorie = sousCategorie.getCategorie().getNom();
		}
		
		return dto;
	}
	
	public static SousCategorieDetailedResponseDto modelToDetailedResponseDto(SousCategorie sousCategorie) {
		
		SousCategorieDetailedResponseDto dto = new SousCategorieDetailedResponseDto();
		
		dto.nom = sousCategorie.getNom();
		dto.libelle = sousCategorie.getLibelle();

		if ( sousCategorie.getCategorie() != null ) {
			dto.categorie = CategorieDtoMapper.modelToSimpleResponseDto(sousCategorie.getCategorie());
		}
		
		return dto;
	}
}
