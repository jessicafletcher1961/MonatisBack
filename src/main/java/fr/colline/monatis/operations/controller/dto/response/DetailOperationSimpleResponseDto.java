package fr.colline.monatis.operations.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class DetailOperationSimpleResponseDto implements Serializable {

	private static final long serialVersionUID = -7282455283259186385L;

	public Integer sequence;
	public ZonedDateTime dateComptabilisation;
	public Long montantDetailEnCentimes;
	public String libelle;
	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;
}
