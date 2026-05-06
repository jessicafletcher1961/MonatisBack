package fr.colline.monatis.rapports.controller.releve_non_categorise;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.BeneficiaireResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class ReleveNonCategoriseOperationLigneResponseDto implements Serializable {

	private static final long serialVersionUID = 329656785197776559L;

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
