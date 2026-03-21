package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;

import fr.colline.monatis.comptes.model.CategorieCompte;

public class CompteTiersSimpleResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 5225022472405377658L;

	public String codeCategorieCompte;

	public CompteTiersSimpleResponseDto() {
		codeCategorieCompte = CategorieCompte.TIERS.getCode();
	}
}
