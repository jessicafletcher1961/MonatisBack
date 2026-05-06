package fr.colline.monatis.rapports.controller.releve_non_categorise;

import java.io.Serializable;
import java.time.LocalDate;

public class ReleveNonCategoriseRequestDto implements Serializable {

	private static final long serialVersionUID = 3923182007121724252L;

	public LocalDate dateDebut;
	
	public LocalDate dateFin;

}
