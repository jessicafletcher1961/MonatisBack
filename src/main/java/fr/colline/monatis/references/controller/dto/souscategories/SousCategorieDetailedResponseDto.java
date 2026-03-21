package fr.colline.monatis.references.controller.dto.souscategories;

import java.io.Serializable;

import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;
import fr.colline.monatis.references.controller.dto.categories.CategorieSimpleResponseDto;

public class SousCategorieDetailedResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -3160910005325715497L;

	public CategorieSimpleResponseDto categorie;
}
