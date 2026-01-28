package fr.colline.monatis.evaluations.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class EvaluationModificationRequestDto implements Serializable {

	private static final long serialVersionUID = 7946224970324234876L;

	public String cle;
	public String identifiantCompteInterne;
	public LocalDate dateSolde;
	public String libelle;
	public Long montantSoldeEnCentimes;
	
}
