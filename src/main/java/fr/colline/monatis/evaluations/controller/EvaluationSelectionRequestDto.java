package fr.colline.monatis.evaluations.controller;

import java.io.Serializable;
import java.time.LocalDate;

public class EvaluationSelectionRequestDto implements Serializable {

	private static final long serialVersionUID = -5991189138311348699L;
	
	public String cleContient;
	public String libelleContient;
	public String identifiantCompteInterne;
	public LocalDate avantLe;
	
}
