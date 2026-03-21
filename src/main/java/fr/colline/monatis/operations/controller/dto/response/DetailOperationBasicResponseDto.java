package fr.colline.monatis.operations.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class DetailOperationBasicResponseDto implements Serializable {

	private static final long serialVersionUID = -1014713757868418416L;

	public Integer sequence;
	public ZonedDateTime dateComptabilisation;
	public Long montantDetailEnCentimes;
	public String libelle;
}
