package fr.colline.monatis.rapports.controller.releve_non_categorise;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ReleveNonCategoriseResponseDto implements Serializable {

	private static final long serialVersionUID = -7622684698439516245L;

	// Infos globales relevé

	public LocalDate dateDebutReleve;
	public LocalDate dateFinReleve;

	// Listes des opérations non catégorisées
	public List<ReleveNonCategoriseOperationLigneResponseDto> operationsLignes;
}
