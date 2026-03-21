package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;

public class OperationSelectionRequestDto implements Serializable {

	private static final long serialVersionUID = -7673309836499475590L;

	public String identifiantCompte;
	public LocalDate dateValeurFin;
	public Long nombreLimite;
}
