package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;

public class OperationExempleRequestDto implements Serializable {

	private static final long serialVersionUID = 951274910904711888L;

	public Integer numeroPage;
	public Integer taillePage;
	
	public String libelle;
	public String identifiantCompteRecette;
	public String identifiantCompteDepense;
	public String codeTypeOperation;

	// pris en compte uniquement dans la recherche de doublons
	public LocalDate dateValeur;
	public Long montantEnCentimes;
}
