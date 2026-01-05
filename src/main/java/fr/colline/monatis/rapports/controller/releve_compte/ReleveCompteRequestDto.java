package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;
import java.time.LocalDate;

public class ReleveCompteRequestDto implements Serializable {

	private static final long serialVersionUID = 3923182007121724252L;

	public String identifiantCompte;
	public LocalDate dateDebut;
	public LocalDate dateFin;

}
