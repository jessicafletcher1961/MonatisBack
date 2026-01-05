package fr.colline.monatis.references.controller.categorie;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class CategorieDetailedResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -6433103415711371163L;

	public List<ReferenceResponseDto> sousCategories;
	
}
