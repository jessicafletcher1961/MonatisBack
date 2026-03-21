package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;

import fr.colline.monatis.comptes.model.CategorieCompte;

public class CompteTiersBasicResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 7340916415216102470L;

	public String codeCategorieCompte;
	
	public CompteTiersBasicResponseDto() {
		this.codeCategorieCompte = CategorieCompte.TIERS.getCode();
	}
}
