package fr.colline.monatis.typologies.controller;

import java.io.Serializable;

import fr.colline.monatis.MonatisResponseDto;

public class TypologieResponseDto implements MonatisResponseDto, Serializable {

	private static final long serialVersionUID = -26333043931153690L;
	
	public String code;
	public String libelle;
	
}
