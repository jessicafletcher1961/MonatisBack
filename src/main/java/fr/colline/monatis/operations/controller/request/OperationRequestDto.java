package fr.colline.monatis.operations.controller.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OperationRequestDto implements Serializable {

	private static final long serialVersionUID = 3547581138806686603L;

	public String numero;
	public String libelle;
	public LocalDate dateValeur;
	public Long montantEnCentimes;

	public String identifiantCompteExterne;
	public String identifiantCompteCourant;
	public String identifiantCompteCourantRecette;
	public String identifiantCompteCourantDepense;
	public String identifiantCompteFinancier;
	public String identifiantCompteBien;

	public String nomSousCategorie;
	public List<String> nomsBeneficiaires;
}
