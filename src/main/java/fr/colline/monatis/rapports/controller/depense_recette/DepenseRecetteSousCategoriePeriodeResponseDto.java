package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;
import java.time.LocalDate;

public class DepenseRecetteSousCategoriePeriodeResponseDto implements Serializable {

	private static final long serialVersionUID = 2588102557351178002L;

	public LocalDate dateDebutPeriode;
	public LocalDate dateFinPeriode;
	public double montantRecetteEnEuros;
	public double montantDepenseEnEuros;
	public double soldeDepenseRecetteEnEuros;

}
