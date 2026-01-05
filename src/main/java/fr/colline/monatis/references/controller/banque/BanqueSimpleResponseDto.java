package fr.colline.monatis.references.controller.banque;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class BanqueSimpleResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = -9175843585027775245L;

	public List<CompteResponseDto> comptesInternes;

}
