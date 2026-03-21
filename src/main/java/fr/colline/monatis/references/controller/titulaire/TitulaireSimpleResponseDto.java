package fr.colline.monatis.references.controller.titulaire;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class TitulaireSimpleResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -3598266859517553189L;

	public List<CompteResponseDto> comptesInternes;

}
