package fr.colline.monatis.rapports.controller.depense_recette;

import java.io.Serializable;

import fr.colline.monatis.rapports.controller.commun.SousCategorieResponseDto;

public class DepenseRecetteSousCategorieLigneResponseDto implements Serializable {

	private static final long serialVersionUID = -1292722887838174071L;
	
	public SousCategorieResponseDto sousCategorie;
	
	public DepenseRecettePeriodeResponseDto[] cumulSousCategorie;
}
