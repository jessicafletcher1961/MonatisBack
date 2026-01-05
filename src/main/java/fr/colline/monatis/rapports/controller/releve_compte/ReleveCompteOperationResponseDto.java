package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;
import java.time.LocalDate;

public class ReleveCompteOperationResponseDto implements Serializable {

	private static final long serialVersionUID = 329656785197776559L;
	
	public String numero;
	public String codeTypeOperation;
	public LocalDate dateValeur;
	public String libelle;
	public Float montantEnEuros;
	public String identifiantAutreCompte;
	public String libelleAutreCompte;
	public String codeTypeAutreCompte;
}
