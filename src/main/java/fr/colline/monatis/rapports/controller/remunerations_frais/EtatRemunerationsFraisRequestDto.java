package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EtatRemunerationsFraisRequestDto implements Serializable {

	private static final long serialVersionUID = 1172230060987562729L;

	public List<String> identifiantsComptesInternes;
	public List<String> codesTypesFonctionnements;
	public String nomTitulaire;
	
	public LocalDate dateDebut;
	public LocalDate dateFin;
	public String codeTypePeriode;
 
}
