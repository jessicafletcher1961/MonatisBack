package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.operations.controller.response.OperationResponseDto;

public class EcheanceResponseDto implements Serializable {

	private static final long serialVersionUID = -7354616748593814695L;
	
	public int numero;
	public LocalDate date;
	public long montantPaiementEnCentimes;
	public long partCapitalEnCentimes;
	public long partInteretEnCentimes;
	public long partFraisFixesEnCentimes;
	public OperationResponseDto operationPaiement;
	public OperationResponseDto operationPartInterets;
	public OperationResponseDto operationPartFraisFixes;
	public long capitalEmprunteRestantDuEnCentimes;
	public long capitalEmprunteDejaRembourseEnCentimes;
}
