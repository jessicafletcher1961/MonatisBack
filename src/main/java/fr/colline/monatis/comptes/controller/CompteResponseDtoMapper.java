package fr.colline.monatis.comptes.controller;

import fr.colline.monatis.comptes.controller.externe.CompteExterneResponseDtoMapper;
import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.comptes.controller.technique.CompteTechniqueResponseDtoMapper;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.erreurs.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ControllerException;

public class CompteResponseDtoMapper {

	public static CompteResponseDto mapperModelToBasicResponseDto(Compte compte) throws ControllerException {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto((CompteInterne) compte);
		}
		else if ( CompteExterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteExterneResponseDtoMapper.mapperModelToBasicResponseDto((CompteExterne) compte);
		}
		else if ( CompteTechnique.class.isAssignableFrom(compte.getClass()) ) {
			return CompteTechniqueResponseDtoMapper.mapperModelToBasicResponseDto((CompteTechnique) compte);
		}
		
		throw new ControllerException(
				GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE, 
				compte.getClass().getSimpleName());
	}

	public static CompteResponseDto mapperModelToSimpleResponseDto(Compte compte) throws ControllerException {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto((CompteInterne) compte);
		}
		else if ( CompteExterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteExterneResponseDtoMapper.mapperModelToSimpleResponseDto((CompteExterne) compte);
		}
		else if ( CompteTechnique.class.isAssignableFrom(compte.getClass()) ) {
			return CompteTechniqueResponseDtoMapper.mapperModelToSimpleResponseDto((CompteTechnique) compte);
		}
		
		throw new ControllerException(
				GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE, 
				compte.getClass().getSimpleName());
	}

	public static CompteResponseDto mapperModelToDetailedResponseDto(Compte compte) throws ControllerException {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteInterneResponseDtoMapper.mapperModelToDetailedResponseDto((CompteInterne) compte);
		}
		else if ( CompteExterne.class.isAssignableFrom(compte.getClass()) ) {
			return CompteExterneResponseDtoMapper.mapperModelToDetailedResponseDto((CompteExterne) compte);
		}
		else if ( CompteTechnique.class.isAssignableFrom(compte.getClass()) ) {
			return CompteTechniqueResponseDtoMapper.mapperModelToDetailedResponseDto((CompteTechnique) compte);
		}
		
		throw new ControllerException(
				GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE, 
				compte.getClass().getSimpleName());
	}

}
