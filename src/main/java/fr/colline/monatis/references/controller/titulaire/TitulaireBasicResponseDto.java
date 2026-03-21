package fr.colline.monatis.references.controller.titulaire;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class TitulaireBasicResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -4404612892203251458L;

	public List<String> identifiantsComptesInternes;
	
}

