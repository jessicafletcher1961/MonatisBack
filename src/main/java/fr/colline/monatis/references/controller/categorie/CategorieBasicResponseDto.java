package fr.colline.monatis.references.controller.categorie;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class CategorieBasicResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -2876845789108576641L;

	public List<String> nomsSousCategories;

}
