package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OperationModificationRequestDto implements Serializable {

	private static final long serialVersionUID = -3863830393882191680L;
	
	public String codeTypeOperation;
	public String numero;
	public String libelle;
	public LocalDate dateValeur;
	public Long montantEnCentimes;
	public String identifiantCompteDepense;
	public String identifiantCompteRecette;
	public List<OperationLigneRequestDto> lignes;

}
