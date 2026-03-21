package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.CategorieResponseDto;

public class DepenseRecetteCategorieLigneResponseDto implements Serializable {

	private static final long serialVersionUID = -6550875227331979492L;
	
	public CategorieResponseDto categorie;

	public List<DepenseRecetteSousCategorieLigneResponseDto> lignesSousCategorie;
	public DepenseRecetteCategoriePeriodeResponseDto[] cumuls;

}
