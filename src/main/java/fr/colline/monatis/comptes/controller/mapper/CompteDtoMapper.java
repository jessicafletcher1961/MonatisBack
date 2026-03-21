package fr.colline.monatis.comptes.controller.mapper;

import fr.colline.monatis.comptes.controller.dto.response.CompteResponseDto;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTiers;

public class CompteDtoMapper {

	public static CompteResponseDto modelToBasicResponseDto(Compte compte) {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteInterneDtoMapper.modelToBasicResponseDto((CompteInterne) compte);
		}
		else if (CompteTiers.class.isAssignableFrom(compte.getClass()) ) {
			return CompteTiersDtoMapper.modelToBasicResponseDto((CompteTiers) compte);
		}
		else {
			return null;
		}
	}

	public static CompteResponseDto modelToSimpleResponseDto(Compte compte) {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteInterneDtoMapper.modelToSimpleResponseDto((CompteInterne) compte);
		}
		else if (CompteTiers.class.isAssignableFrom(compte.getClass()) ) {
			return CompteTiersDtoMapper.modelToSimpleResponseDto((CompteTiers) compte);
		}
		else return null;
	}

	public static CompteResponseDto modelToDetailedResponseDto(Compte compte) {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteInterneDtoMapper.modelToDetailedResponseDto((CompteInterne) compte);
		}
		else if (CompteTiers.class.isAssignableFrom(compte.getClass()) ) {
			return CompteTiersDtoMapper.modelToDetailedResponseDto((CompteTiers) compte);
		}
		else return null;
	}
}
