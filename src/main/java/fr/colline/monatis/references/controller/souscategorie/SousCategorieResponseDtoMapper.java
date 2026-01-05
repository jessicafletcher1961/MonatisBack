package fr.colline.monatis.references.controller.souscategorie;

import fr.colline.monatis.references.controller.categorie.CategorieResponseDtoMapper;
import fr.colline.monatis.references.model.SousCategorie;

public class SousCategorieResponseDtoMapper {

	public static SousCategorieBasicResponseDto mapperModelToBasicResponseDto(SousCategorie sousCategorie) {
		
		SousCategorieBasicResponseDto dto = new SousCategorieBasicResponseDto();
		
		dto.nom = sousCategorie.getNom();
		dto.libelle = sousCategorie.getLibelle();
		
		if ( sousCategorie.getCategorie() != null ) {
			dto.nomCategorie = sousCategorie.getCategorie().getNom();
		}
		
		return dto;
	}

	public static SousCategorieSimpleResponseDto mapperModelToSimpleResponseDto(SousCategorie sousCategorie) {
		
		SousCategorieSimpleResponseDto dto = new SousCategorieSimpleResponseDto();
		
		dto.nom = sousCategorie.getNom();
		dto.libelle = sousCategorie.getLibelle();

		if ( sousCategorie.getCategorie() != null ) {
			dto.categorie = CategorieResponseDtoMapper.mapperModelToBasicResponseDto(sousCategorie.getCategorie());
		}

		return dto;
	}

	public static SousCategorieDetailedResponseDto mapperModelToDetailedResponseDto(SousCategorie sousCategorie) {
		
		SousCategorieDetailedResponseDto dto = new SousCategorieDetailedResponseDto();
		
		dto.nom = sousCategorie.getNom();
		dto.libelle = sousCategorie.getLibelle();

		if ( sousCategorie.getCategorie() != null ) {
			dto.categorie = CategorieResponseDtoMapper.mapperModelToSimpleResponseDto(sousCategorie.getCategorie());
		}
		
		return dto;
	}
}
