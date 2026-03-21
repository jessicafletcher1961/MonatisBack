package fr.colline.monatis.references.controller.mapper;

import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireBasicResponseDto;
import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.beneficiaires.BeneficiaireSimpleResponseDto;
import fr.colline.monatis.references.model.Beneficiaire;

public class BeneficiaireDtoMapper {

	public static BeneficiaireBasicResponseDto modelToBasicResponseDto(Beneficiaire beneficiaire) {

		BeneficiaireBasicResponseDto dto = new BeneficiaireBasicResponseDto();

		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();
		
		return dto;
	}

	public static BeneficiaireSimpleResponseDto modelToSimpleResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireSimpleResponseDto dto = new BeneficiaireSimpleResponseDto();
		
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();
		
		return dto;
	}

	public static BeneficiaireDetailedResponseDto modelToDetailedResponseDto(Beneficiaire beneficiaire) {
		
		BeneficiaireDetailedResponseDto dto = new BeneficiaireDetailedResponseDto();
		
		dto.nom = beneficiaire.getNom();
		dto.libelle = beneficiaire.getLibelle();
		
		return dto;
	}
}