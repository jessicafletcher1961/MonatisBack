package fr.colline.monatis.comptes.controller;

import java.io.Serializable;

public abstract class CompteRequestDto implements Serializable {

	private static final long serialVersionUID = 2005091913157519304L;

	public String identifiant;
	public String libelle;

}
