package fr.colline.monatis.operations.controller.dto.request;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class OperationModificationRequestDto implements Serializable{

	private static final long serialVersionUID = -8623646198594089047L;

	public String codeTypeOperation;
	public String numero;
	public String libelle;
	public ZonedDateTime dateValeur;
	public Long montantTotalEnCentimes;
	public String identifiantCompteDepense;
	public String identifiantCompteRecette;

	public List<DetailOperationRequestDto> detailsOperation;
}
