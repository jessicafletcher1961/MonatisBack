package fr.colline.monatis.references.controller.dto.titulaires;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;

public class TitulaireSimpleResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = 2090849729010328330L;

	public List<String> identifiantsComptesInternes;
}
