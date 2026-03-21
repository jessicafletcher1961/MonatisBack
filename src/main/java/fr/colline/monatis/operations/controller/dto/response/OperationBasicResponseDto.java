package fr.colline.monatis.operations.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class OperationBasicResponseDto implements Serializable {

	private static final long serialVersionUID = -227416719941487153L;

	public String numero;
	public ZonedDateTime dateValeur;
	public Long montantTotalEnCentimes;
	public String libelle;
	public String codeTypeOperation;

	public List<DetailOperationBasicResponseDto> detailsOperation;
}
