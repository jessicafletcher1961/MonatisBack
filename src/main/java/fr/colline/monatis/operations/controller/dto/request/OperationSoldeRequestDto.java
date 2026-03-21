package fr.colline.monatis.operations.controller.dto.request;

import java.time.ZonedDateTime;

public class OperationSoldeRequestDto {

	public String identifiantCompteContrepartie;
	public ZonedDateTime dateValeur;
	public Long montantSoldeEnCentimes;
}
