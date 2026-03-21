package fr.colline.monatis.operations.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireSimpleResponseDto;
import fr.colline.monatis.references.controller.dto.souscategories.SousCategorieSimpleResponseDto;

public class DetailOperationDetailedResponseDto implements Serializable {

	private static final long serialVersionUID = -8722453135180610357L;
	
	public Integer sequence;
	public ZonedDateTime dateComptabilisation;
	public Long montantDetailEnCentimes;
	public String libelle;
	public SousCategorieSimpleResponseDto sousCategorie;
	public List<BeneficiaireSimpleResponseDto> beneficiaires;
}
