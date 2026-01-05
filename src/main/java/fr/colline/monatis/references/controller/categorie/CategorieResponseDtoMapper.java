package fr.colline.monatis.references.controller.categorie;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.references.controller.souscategorie.SousCategorieResponseDtoMapper;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;

public class CategorieResponseDtoMapper {

	public static CategorieBasicResponseDto mapperModelToBasicResponseDto(Categorie categorie) {
		
		CategorieBasicResponseDto dto = new CategorieBasicResponseDto();
		
		dto.nom = categorie.getNom();
		dto.libelle = categorie.getLibelle();

		if ( categorie.getSousCategories() != null ) {
			dto.nomsSousCategories = new ArrayList<>();
			for ( SousCategorie sousCategorie : categorie.getSousCategories() ) {
				dto.nomsSousCategories.add(sousCategorie.getNom());
			}
			Collections.sort(dto.nomsSousCategories, (o1, o2) -> {
				return o1.compareTo(o2);
			});		
		}
		
		return dto;
	}

	public static CategorieSimpleResponseDto mapperModelToSimpleResponseDto(Categorie categorie) {
		
		CategorieSimpleResponseDto dto = new CategorieSimpleResponseDto();
		
		dto.nom = categorie.getNom();
		dto.libelle = categorie.getLibelle();

		if ( categorie.getSousCategories() != null ) {
			dto.sousCategories = new ArrayList<>();
			for ( SousCategorie sousCategorie : categorie.getSousCategories() ) {
				dto.sousCategories.add(SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto(sousCategorie));
			}
			Collections.sort(dto.sousCategories, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});		
		}

		return dto;
	}

	public static CategorieDetailedResponseDto mapperModelToDetailedResponseDto(Categorie categorie) {
		
		CategorieDetailedResponseDto dto = new CategorieDetailedResponseDto();
		
		dto.nom = categorie.getNom();
		dto.libelle = categorie.getLibelle();

		if ( categorie.getSousCategories() != null ) {
			dto.sousCategories = new ArrayList<>();
			for ( SousCategorie sousCategorie : categorie.getSousCategories() ) {
				dto.sousCategories.add(SousCategorieResponseDtoMapper.mapperModelToSimpleResponseDto(sousCategorie));
			}
			Collections.sort(dto.sousCategories, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});		
		}
		
		return dto;
	}

}
