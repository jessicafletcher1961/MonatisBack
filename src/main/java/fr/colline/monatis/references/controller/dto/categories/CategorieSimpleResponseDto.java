package fr.colline.monatis.references.controller.dto.categories;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;

public class CategorieSimpleResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -175196515622999598L;

	public List<String> nomsSousCategories;
}
