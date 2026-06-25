package fr.colline.monatis.emprunts.controller.request;

import java.io.Serializable;
import java.time.LocalDate;

public class ConditionEmpruntRequestDto implements Serializable {

	private static final long serialVersionUID = 6742279412843698633L;
	
	public String libelle;
	public Double tauxAnnuel;
	public Long capitalEmprunteEnCentimes;
	public Integer duree;
	public String codeTypePeriodeEcheances;
	public Integer numeroPremiereEcheance;
	public LocalDate datePremiereEcheance;
	public Long montantTotalEcheanceEnCentimes;
	public Long montantFraisFixesEcheanceEnCentimes;
	
}
