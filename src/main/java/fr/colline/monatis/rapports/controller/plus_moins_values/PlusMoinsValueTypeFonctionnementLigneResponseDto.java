package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.TypeFonctionnementResponseDto;

public class PlusMoinsValueTypeFonctionnementLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 2374241254211050214L;
	
	public TypeFonctionnementResponseDto typeFonctionnement;

	public List<PlusMoinsValueCompteInterneLigneResponseDto> lignesCompteInterne;
	public PlusMoinsValueTypeFonctionnementPeriodeResponseDto[] cumulsPeriodes;

}
