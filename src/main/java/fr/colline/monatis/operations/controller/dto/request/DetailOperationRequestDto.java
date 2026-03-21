package fr.colline.monatis.operations.controller.dto.request;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class DetailOperationRequestDto implements Serializable {

	private static final long serialVersionUID = 2648157917286552091L;

	public Integer sequence;
	public ZonedDateTime dateComptabilisation;
	public Long montantDetailEnCentimes;
	public String libelle;
	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;
}
