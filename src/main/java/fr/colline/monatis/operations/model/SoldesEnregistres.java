package fr.colline.monatis.operations.model;

import java.time.LocalDate;

public class SoldesEnregistres {

	private LocalDate date;
	private Long montantEnCentimes;
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public Long getMontantEnCentimes() {
		return montantEnCentimes;
	}
	
	public void setMontantEnCentimes(Long montantEnCentimes) {
		this.montantEnCentimes = montantEnCentimes;
	}

}
