package fr.colline.monatis.operations.controller.response;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.MonatisResponseDto;

public abstract class OperationResponseDto implements Serializable, MonatisResponseDto {

	private static final long serialVersionUID = 6335181972860713927L;

	public String numero;
	public String libelle;
	public LocalDate dateValeur;
	public Long montantEnCentimes;
	public Boolean pointee;

}
