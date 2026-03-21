package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.TypeFonctionnementResponseDto;

public class RemunerationsFraisTypeFonctionnementLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 3837056388268716542L;

	public TypeFonctionnementResponseDto typeFonctionnement;
	
	public List<RemunerationsFraisCompteInterneLigneResponseDto> lignesCompteInterne;
	public RemunerationsFraisTypeFonctionnementPeriodeResponseDto[] cumulsPeriodes;
	
}
