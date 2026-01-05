package fr.colline.monatis.comptes.controller.technique;

import fr.colline.monatis.comptes.model.CompteTechnique;

public class CompteTechniqueResponseDtoMapper {

	public static CompteTechniqueBasicResponseDto mapperModelToBasicResponseDto(CompteTechnique compteTechnique) {

		CompteTechniqueBasicResponseDto dto = new CompteTechniqueBasicResponseDto();
		
		dto.identifiant = compteTechnique.getIdentifiant();
		dto.libelle = compteTechnique.getLibelle();
		
		return dto;
	}

	public static CompteTechniqueSimpleResponseDto mapperModelToSimpleResponseDto(CompteTechnique compteTechnique) {

		CompteTechniqueSimpleResponseDto dto = new CompteTechniqueSimpleResponseDto();
		
		dto.identifiant = compteTechnique.getIdentifiant();
		dto.libelle = compteTechnique.getLibelle();

		return dto;
	}

	public static CompteTechniqueDetailedResponseDto mapperModelToDetailedResponseDto(CompteTechnique compteTechnique) {

		CompteTechniqueDetailedResponseDto dto = new CompteTechniqueDetailedResponseDto();
		
		dto.identifiant = compteTechnique.getIdentifiant();
		dto.libelle = compteTechnique.getLibelle();

		return dto;
	}

}
