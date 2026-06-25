package fr.colline.monatis.rapports.controller.echeancier;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class EcheancierConditionEmpruntResponseDto extends EcheancierLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 8177719273446127069L;
	public String libelle;
	public int numeroEcheanceApplicabilite;
	public double montantPretEnEuros;
	public int dureePret;
	public TypologieResponseDto periodiciteEcheances;
	public double tauxAnnuel;
	public LocalDate dateDebutApplicabilite;
	public LocalDate dateFinApplicabilite;
	public int nombreEcheancesApplicabilite;
	public EcheancierCumulsResponseDto cumuls;

}
