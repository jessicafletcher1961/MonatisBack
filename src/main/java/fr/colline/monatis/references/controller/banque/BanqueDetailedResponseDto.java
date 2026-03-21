package fr.colline.monatis.references.controller.banque;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class BanqueDetailedResponseDto extends ReferenceResponseDto implements Serializable {

	private static final long serialVersionUID = 93569296055362893L;

	public List<CompteResponseDto> comptesInternes;

}
