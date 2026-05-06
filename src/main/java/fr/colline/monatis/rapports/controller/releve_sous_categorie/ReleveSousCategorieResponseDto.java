package fr.colline.monatis.rapports.controller.releve_sous_categorie;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.SousCategorieResponseDto;

public class ReleveSousCategorieResponseDto implements Serializable {

	private static final long serialVersionUID = -2211711183930854124L;

	public LocalDate dateDebutReleve;
	public LocalDate dateFinReleve;
	public SousCategorieResponseDto sousCategorie;
	
	public List<ReleveSousCategorieOperationLigneResponseDto> operationsLignes;
}
