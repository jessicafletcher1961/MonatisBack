package fr.colline.monatis.evaluations.controller;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.MonatisResponseDto;

public abstract class EvaluationResponseDto implements Serializable, MonatisResponseDto {

	private static final long serialVersionUID = 4876864948950927695L;

	public String cle;
	public LocalDate dateSolde;
	public Long montantSoldeEnCentimes;
	public String libelle;

}
