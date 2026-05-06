package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;

public class OperationSelectionRequestDto implements Serializable {

	private static final long serialVersionUID = -7673309836499475590L;

	public String numeroContient;
	public String libelleContient;
	public LocalDate depuisLe;
	public LocalDate jusqueAu;
	public String codeTypeOperation;
	public Long montantEnCentimesApproximatif;
	public String identifiantCompteRecetteOuDepense;
	public String identifiantCompteRecette;
	public String identifiantCompteDepense;	
	public Boolean pointee;
	public Long nombreLimite;

}
