package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;

public class ReleveCompteResponseDto implements Serializable {

	private static final long serialVersionUID = -7622684698439516245L;

	// Infos compte
	public EnteteCompteResponseDto enteteCompte;
	
	// Infos globales relevé
	public LocalDate dateDebutReleve;
	public LocalDate dateFinReleve;
	public double montantSoldeDebutReleveEnEuros;
	public double montantSoldeFinReleveEnEuros;
	public double montantTotalOperationsRecetteEnEuros;
	public double montantTotalOperationsDepenseEnEuros;
	public double montantEcartEnEuros;

	// Listes des opérations en recette ou en dépense
	public List<ReleveCompteOperationResponseDto> operationsRecette;
	public List<ReleveCompteOperationResponseDto> operationsDepense;
}
