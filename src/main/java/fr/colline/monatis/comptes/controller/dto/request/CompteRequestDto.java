package fr.colline.monatis.comptes.controller.dto.request;

import java.io.Serializable;

public abstract class CompteRequestDto implements Serializable {

	private static final long serialVersionUID = -7353580969300611356L;

	public String identifiant;
	public String libelle;
}
