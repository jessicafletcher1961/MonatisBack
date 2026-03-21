package fr.colline.monatis.operations.controller.dto.request;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class OperationCreationRequestDto implements Serializable {

	private static final long serialVersionUID = 3142818869051389552L;

	public String codeTypeOperation;
	public String numero;
	public String libelle;
	public ZonedDateTime dateValeur;
	public Long montantTotalEnCentimes;
	
	public String identifiantCompteDepense;
	public String identifiantCompteRecette;
	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;
}
