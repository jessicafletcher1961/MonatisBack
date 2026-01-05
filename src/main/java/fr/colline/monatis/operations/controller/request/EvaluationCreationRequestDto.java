package fr.colline.monatis.operations.controller.request;

import java.time.LocalDate;

public class EvaluationCreationRequestDto {

	public String cle;
	public String identifiantCompteInterne;
	public LocalDate dateSolde;
	public String libelle;
	public Long montantSoldeEnCentimes;

}
