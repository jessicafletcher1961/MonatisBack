package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OperationLigneModificationRequestDto implements Serializable {

	private static final long serialVersionUID = -5994934718149971700L;
	
	public Integer numeroLigne;
	public String libelle;
	public LocalDate dateComptabilisation;
	public Long montantEnCentimes;
	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;

}
