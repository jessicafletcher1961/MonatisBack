package fr.colline.monatis.references.controller.categorie;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class CategorieSimpleResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -6847824408568187044L;

	public List<ReferenceResponseDto> sousCategories;

}
