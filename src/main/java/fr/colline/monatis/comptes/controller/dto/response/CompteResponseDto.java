package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;

public abstract class CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 4231222336273055623L;

	public String identifiant;
	public String libelle;
}
