package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EtatPlusMoinsValueRequestDto implements Serializable {

	private static final long serialVersionUID = 525419212523249508L;

	public List<String> identifiantsComptesInternes;
	public List<String> codesTypesFonctionnements;
	public String nomTitulaire;
	
	public LocalDate dateDebut;
	public LocalDate dateFin;
	public String codeTypePeriode;

}
