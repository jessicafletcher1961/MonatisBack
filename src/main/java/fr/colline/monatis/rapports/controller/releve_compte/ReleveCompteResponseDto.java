package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ReleveCompteResponseDto implements Serializable {

	private static final long serialVersionUID = -7622684698439516245L;

	// Infos compte
	public EnteteCompteResponseDto enteteCompte;
	
	// Infos globales relevé
	public LocalDate dateDebutReleve;
	public LocalDate dateFinReleve;
	public Float montantSoldeDebutReleveEnEuros;
	public Float montantSoldeFinReleveEnEuros;
	public Float montantTotalOperationsRecetteEnEuros;
	public Float montantTotalOperationsDepenseEnEuros;

	// Listes des opérations en recette ou en dépense
	public List<ReleveCompteOperationResponseDto> operationsRecette;
	public List<ReleveCompteOperationResponseDto> operationsDepense;
}
