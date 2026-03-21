package fr.colline.monatis.operations.controller.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OperationLigneBasicResponseDto implements Serializable {

	private static final long serialVersionUID = 3260665724183509362L;
	
	public Integer numeroLigne;
	public LocalDate dateComptabilisation;
	public Long montantEnCentimes;
	public String libelle;
	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;

}
