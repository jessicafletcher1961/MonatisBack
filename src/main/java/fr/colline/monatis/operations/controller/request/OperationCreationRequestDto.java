package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OperationCreationRequestDto implements Serializable {

	private static final long serialVersionUID = 6286435068357380112L;
	
	public String numero;
	public String libelle;
	public String codeTypeOperation;
	public LocalDate dateValeur;
	public Long montantEnCentimes;
	public String identifiantCompteDepense;
	public String identifiantCompteRecette;
	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;

}
