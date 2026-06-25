package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class ConditionEmpruntBasicResponseDto implements Serializable {

	private static final long serialVersionUID = 5748629499006440682L;

	public String libelle;
	public double tauxAnnuel;
	public long capitalEmprunteEnCentimes;
	public int duree;
	public TypologieResponseDto typePeriodeEcheances;
}
