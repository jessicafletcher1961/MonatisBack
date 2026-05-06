package fr.colline.monatis.typologies.controller;

import java.io.Serializable;

public class TypeOperationResponseDto extends TypologieResponseDto implements Serializable {

	private static final long serialVersionUID = 3822332303060262665L;
	
	public String libelleCourt;
	public boolean fluxTechnique;
	public boolean categorisable;

}
