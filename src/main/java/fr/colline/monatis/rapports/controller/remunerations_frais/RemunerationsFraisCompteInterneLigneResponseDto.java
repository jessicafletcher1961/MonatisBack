package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;

public class RemunerationsFraisCompteInterneLigneResponseDto implements Serializable {

	private static final long serialVersionUID = -4431313323609236683L;

	public EnteteCompteResponseDto compteInterne;
	
	public RemunerationsFraisCompteInternePeriodeResponseDto[] periodes;
	
}
