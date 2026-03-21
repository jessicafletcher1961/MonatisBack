package fr.colline.monatis.references.controller.dto.banques;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;

public class BanqueSimpleResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = 1458743126973999673L;
	
	public List<String> identifiantsComptesInternes;
}
