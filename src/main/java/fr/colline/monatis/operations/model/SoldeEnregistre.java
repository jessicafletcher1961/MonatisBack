package fr.colline.monatis.operations.model;

import java.time.LocalDate;

public class SoldeEnregistre {

	private Long operationPlusMoinsValueId;
	private Long compteId;
	private LocalDate dateEnregistrementPlusMoinsValue;
	private Long montantSoldeVouluEnCentimes;

	public SoldeEnregistre(
			Long operationPlusMoinsValueId,
			Long compteId,
			LocalDate dateEnregistrementPlusMoinsValue,
			Long montantSoldeVouluEnCentimes) {
	
		this.compteId = compteId;
		this.dateEnregistrementPlusMoinsValue = dateEnregistrementPlusMoinsValue;
		this.montantSoldeVouluEnCentimes = montantSoldeVouluEnCentimes;
		this.operationPlusMoinsValueId = operationPlusMoinsValueId;
	}

	public Long getOperationPlusMoinsValueId() {
		return operationPlusMoinsValueId;
	}

	public void setOperationPlusMoinsValueId(Long operationPlusMoinsValueId) {
		this.operationPlusMoinsValueId = operationPlusMoinsValueId;
	}

	public Long getCompteId() {
		return compteId;
	}

	public void setCompteId(Long compteId) {
		this.compteId = compteId;
	}

	public LocalDate getDateEnregistrementPlusMoinsValue() {
		return dateEnregistrementPlusMoinsValue;
	}

	public void setDateEnregistrementPlusMoinsValue(LocalDate dateEnregistrementPlusMoinsValue) {
		this.dateEnregistrementPlusMoinsValue = dateEnregistrementPlusMoinsValue;
	}

	public Long getMontantSoldeVouluEnCentimes() {
		return montantSoldeVouluEnCentimes;
	}

	public void setMontantSoldeVouluEnCentimes(Long montantSoldeVouluEnCentimes) {
		this.montantSoldeVouluEnCentimes = montantSoldeVouluEnCentimes;
	}
	
}
