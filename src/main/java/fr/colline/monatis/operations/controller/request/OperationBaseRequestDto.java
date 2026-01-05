package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;

public class OperationBaseRequestDto implements Serializable {

	private static final long serialVersionUID = -8815498048410973600L;

	public String numeroOperation;
	public String identifiantCompteDepense;
	public String identifiantCompteRecette;
	public Long montantOperationEnCentimes;
	public LocalDate dateOperation;
	public String libelleOperation;
}
