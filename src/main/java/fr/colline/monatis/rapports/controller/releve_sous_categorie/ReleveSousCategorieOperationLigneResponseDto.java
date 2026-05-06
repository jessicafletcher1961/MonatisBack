package fr.colline.monatis.rapports.controller.releve_sous_categorie;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.BeneficiaireResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class ReleveSousCategorieOperationLigneResponseDto implements Serializable {

	private static final long serialVersionUID = -362863545182144828L;

	public LocalDate dateComptabilisation;
	public String libelle;
	public double montantEnEuros;

	public String numeroOperation;
	public int numeroLigne;
	public TypologieResponseDto typeOperation;
	public EnteteCompteResponseDto compteDepense;
	public EnteteCompteResponseDto compteRecette;

	public List<BeneficiaireResponseDto> beneficiaires;
	
}
