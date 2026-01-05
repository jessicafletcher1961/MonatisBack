package fr.colline.monatis.references.controller.beneficiaire;

import fr.colline.monatis.references.model.Beneficiaire;

public class BeneficiaireResponseDtoMapper {

	public static BeneficiaireBasicResponseDto mapperModelToBasicResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireBasicResponseDto dto = new BeneficiaireBasicResponseDto();
		
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();

		return dto;
	}

	public static BeneficiaireSimpleResponseDto mapperModelToSimpleResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireSimpleResponseDto dto = new BeneficiaireSimpleResponseDto();
		
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();

		return dto;
	}

	public static BeneficiaireDetailedResponseDto mapperModelToDetailedResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireDetailedResponseDto dto = new BeneficiaireDetailedResponseDto();
		
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();
		
		return dto;
	}

}
