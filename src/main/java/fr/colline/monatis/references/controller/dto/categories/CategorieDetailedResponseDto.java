package fr.colline.monatis.references.controller.dto.categories;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieSimpleResponseDto;

public class CategorieDetailedResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -3904845026454862739L;

	public List<SousCategorieSimpleResponseDto> sousCategories;
}
