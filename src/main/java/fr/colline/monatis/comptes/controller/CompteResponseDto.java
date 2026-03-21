package fr.colline.monatis.comptes.controller;

import java.io.Serializable;

import fr.colline.monatis.MonatisResponseDto;

public abstract class CompteResponseDto implements Serializable, MonatisResponseDto {

	private static final long serialVersionUID = 6926376429549463963L;

	public String identifiant;
	public String libelle;
	
}
