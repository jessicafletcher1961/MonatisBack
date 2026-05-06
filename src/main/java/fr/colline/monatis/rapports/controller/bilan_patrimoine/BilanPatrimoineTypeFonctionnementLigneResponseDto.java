package fr.colline.monatis.rapports.controller.bilan_patrimoine;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class BilanPatrimoineTypeFonctionnementLigneResponseDto implements Serializable{

	private static final long serialVersionUID = -5316281581053488894L;

	public TypologieResponseDto typeFonctionnement;
	
	public List<BilanPatrimoineCompteInterneLigneResponseDto> lignesCompteInterne;
	public Double montantSoldeInitialEnEuros;
	public BilanPatrimoineTypeFonctionnementPeriodeResponseDto[] cumulsPeriodes;

}
