package fr.colline.monatis.evaluations.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class EvaluationCreationRequestDto implements Serializable {

	private static final long serialVersionUID = -8254392332228854072L;

	public String cle;
	public String identifiantCompteInterne;
	public LocalDate dateSolde;
	public String libelle;
	public Long montantSoldeEnCentimes;

}
