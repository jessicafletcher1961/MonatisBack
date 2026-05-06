package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class RemunerationsFraisTypeFonctionnementLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 3837056388268716542L;

	public TypologieResponseDto typeFonctionnement;
	
	public List<RemunerationsFraisCompteInterneLigneResponseDto> lignesCompteInterne;
	public RemunerationsFraisTypeFonctionnementPeriodeResponseDto[] cumulsPeriodes;
	
}
