package fr.colline.monatis.operations.controller.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OperationBasicResponseDto implements Serializable {

	private static final long serialVersionUID = -2470111800397301732L;
	
	public String numero;
	public String libelle;
	public String codeTypeOperation;
	public LocalDate dateValeur;
	public Long montantEnCentimes;
	public String identifiantCompteRecette;
	public String identifiantCompteDepense;
	public List<OperationLigneBasicResponseDto> lignes;

}
