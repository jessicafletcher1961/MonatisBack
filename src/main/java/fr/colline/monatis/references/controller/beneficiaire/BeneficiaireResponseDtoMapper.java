package fr.colline.monatis.references.controller.beneficiaire;

import fr.colline.monatis.references.controller.ReferenceResponseDto;
import fr.colline.monatis.references.model.Beneficiaire;

public class BeneficiaireResponseDtoMapper {

	public static ReferenceResponseDto mapperModelToBasicResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireBasicResponseDto dto = new BeneficiaireBasicResponseDto();
		
		dto.id = beneficiaire.getId();
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();

		return dto;
	}

	public static ReferenceResponseDto mapperModelToSimpleResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireSimpleResponseDto dto = new BeneficiaireSimpleResponseDto();
		
		dto.id = beneficiaire.getId();
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();

		return dto;
	}

	public static ReferenceResponseDto mapperModelToDetailedResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireDetailedResponseDto dto = new BeneficiaireDetailedResponseDto();
		
		dto.id = beneficiaire.getId();
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();
		
		return dto;
	}

}
