package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class ConditionEmpruntDetailedResponseDto implements Serializable {

	private static final long serialVersionUID = -257411389041716924L;

	public String libelle;
	public double tauxAnnuel;
	public long capitalEmprunteEnCentimes;
	public int duree;
	public TypologieResponseDto typePeriodeEcheances;

	public int numeroPremiereEcheance;
	public LocalDate datePremiereEcheance;
	public long montantTotalEcheanceEnCentimes;
	public long montantFraisFixesEcheanceEnCentimes;

	public List<EcheanceResponseDto> echeances;
}
