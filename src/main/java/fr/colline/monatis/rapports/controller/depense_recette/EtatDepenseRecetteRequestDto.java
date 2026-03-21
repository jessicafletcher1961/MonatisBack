package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EtatDepenseRecetteRequestDto implements Serializable {

	private static final long serialVersionUID = 6024534843039084688L;
	
	public LocalDate dateDebut;
	public LocalDate dateFin;
	public String codeTypePeriode;
	public List<String> nomsSousCategories;
	public List<String> nomsCategories;
	public String nomBeneficiaire;

}
