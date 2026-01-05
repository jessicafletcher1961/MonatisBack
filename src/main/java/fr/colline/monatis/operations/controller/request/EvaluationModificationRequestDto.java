package fr.colline.monatis.operations.controller.request;

import java.time.LocalDate;

public class EvaluationModificationRequestDto {

	public String cle;
	public String identifiantCompteInterne;
	public LocalDate dateSolde;
	public Long montantSoldeEnCentimes;
	public String libelle;
}
