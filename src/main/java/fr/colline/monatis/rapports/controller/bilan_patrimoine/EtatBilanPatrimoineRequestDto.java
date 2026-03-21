package fr.colline.monatis.rapports.controller.bilan_patrimoine;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EtatBilanPatrimoineRequestDto implements Serializable {

	private static final long serialVersionUID = -6432820094418202319L;

	public List<String> identifiantsComptesInternes;
	public List<String> codesTypesFonctionnements;
	public String nomTitulaire;

	public LocalDate dateDebut;
	public LocalDate dateFin;
	public String codeTypePeriode;
	
}
