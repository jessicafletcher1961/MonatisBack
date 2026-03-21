package fr.colline.monatis.comptes.controller.technique;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.comptes.model.CompteTechnique;

public class CompteTechniqueResponseDtoMapper {

	public static CompteResponseDto mapperModelToBasicResponseDto(CompteTechnique compteTechnique) {

		CompteTechniqueBasicResponseDto dto = new CompteTechniqueBasicResponseDto();
		
		dto.identifiant = compteTechnique.getIdentifiant();
		dto.libelle = compteTechnique.getLibelle();
		
		return dto;
	}

	public static CompteResponseDto mapperModelToSimpleResponseDto(CompteTechnique compteTechnique) {

		CompteTechniqueSimpleResponseDto dto = new CompteTechniqueSimpleResponseDto();
		
		dto.identifiant = compteTechnique.getIdentifiant();
		dto.libelle = compteTechnique.getLibelle();

		return dto;
	}

	public static CompteResponseDto mapperModelToDetailedResponseDto(CompteTechnique compteTechnique) {

		CompteTechniqueDetailedResponseDto dto = new CompteTechniqueDetailedResponseDto();
		
		dto.identifiant = compteTechnique.getIdentifiant();
		dto.libelle = compteTechnique.getLibelle();

		return dto;
	}

}
