package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;

import fr.colline.monatis.comptes.controller.mapper.CategorieCompteDtoMapper;
import fr.colline.monatis.comptes.model.CategorieCompte;

public class CompteTiersDetailedResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = -8266113439223832305L;

	public CategorieCompteResponseDto categorieCompte; 

	public CompteTiersDetailedResponseDto() {
		this.categorieCompte = CategorieCompteDtoMapper.modelToResponseDto(
				CategorieCompte.TIERS);
	}
}
