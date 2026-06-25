package fr.colline.monatis.rapports.controller.echeancier;

import java.io.Serializable;
import java.time.LocalDate;

public class EcheancierRequestDto implements Serializable {

	private static final long serialVersionUID = -4357378297793752820L;
	
	public String cleEmprunt;
	public LocalDate dateCible;
}
