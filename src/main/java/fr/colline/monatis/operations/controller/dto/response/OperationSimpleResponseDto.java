package fr.colline.monatis.operations.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class OperationSimpleResponseDto implements Serializable {

	private static final long serialVersionUID = -748760118944977912L;

	public String numero;
	public ZonedDateTime dateValeur;
	public Long montantTotalEnCentimes;
	public String libelle;
	public String codeTypeOperation;

	public String identifiantCompteDepense;
	public String identifiantCompteRecette;

	public List<DetailOperationSimpleResponseDto> detailsOperation;
}
