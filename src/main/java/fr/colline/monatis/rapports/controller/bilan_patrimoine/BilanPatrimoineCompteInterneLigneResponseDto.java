package fr.colline.monatis.rapports.controller.bilan_patrimoine;

import java.io.Serializable;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;

public class BilanPatrimoineCompteInterneLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 7382232345581544306L;

	public EnteteCompteResponseDto compteInterne;
	
	public Double montantSoldeInitialEnEuros;
	public BilanPatrimoineCompteInternePeriodeResponseDto[] periodes;
}
