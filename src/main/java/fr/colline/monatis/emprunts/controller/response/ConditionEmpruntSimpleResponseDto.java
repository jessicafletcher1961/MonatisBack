package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class ConditionEmpruntSimpleResponseDto implements Serializable {

	private static final long serialVersionUID = 4364818399346915460L;
	
	public String libelle;
	public double tauxAnnuel;
	public long capitalEmprunteEnCentimes;
	public int duree;
	public TypologieResponseDto typePeriodeEcheances;

	public int numeroPremiereEcheance;
	public LocalDate datePremiereEcheance;
	public long montantTotalEcheanceEnCentimes;
	public long montantFraisFixesEcheanceEnCentimes;
}
