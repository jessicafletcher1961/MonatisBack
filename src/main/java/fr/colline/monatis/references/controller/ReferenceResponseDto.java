package fr.colline.monatis.references.controller;

import java.io.Serializable;

import fr.colline.monatis.MonatisResponseDto;

public abstract class ReferenceResponseDto implements Serializable, MonatisResponseDto {

	private static final long serialVersionUID = 4129405708810085743L;

	public String nom;
	public String libelle;
	
}
