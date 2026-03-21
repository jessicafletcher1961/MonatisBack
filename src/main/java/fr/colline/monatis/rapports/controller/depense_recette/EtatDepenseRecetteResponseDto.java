package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import fr.colline.monatis.rapports.controller.commun.BeneficiaireResponseDto;
import fr.colline.monatis.rapports.controller.commun.CategorieResponseDto;
import fr.colline.monatis.rapports.controller.commun.SousCategorieResponseDto;
import fr.colline.monatis.rapports.controller.commun.TypePeriodeResponseDto;

public class EtatDepenseRecetteResponseDto implements Serializable {

	private static final long serialVersionUID = 7479446503060647232L;
	public LocalDate dateDebutEtat;
	public LocalDate dateFinEtat;
	public TypePeriodeResponseDto typePeriode;
	public ArrayList<SousCategorieResponseDto> sousCategories;
	public ArrayList<CategorieResponseDto> categories;
	public BeneficiaireResponseDto beneficiaire;
	
	public ArrayList<DepenseRecetteCategorieLigneResponseDto> lignesCategorie;
	public DepenseRecettePeriodeResponseDto[] cumuls;

}
