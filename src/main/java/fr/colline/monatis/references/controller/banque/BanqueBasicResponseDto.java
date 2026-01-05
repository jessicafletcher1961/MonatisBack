package fr.colline.monatis.references.controller.banque;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class BanqueBasicResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -5611662492033904925L;

	public List<String> identifiantsComptesInternes;

}
