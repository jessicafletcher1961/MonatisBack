package fr.colline.monatis.references.controller;

import fr.colline.monatis.erreurs.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.references.controller.banque.BanqueResponseDtoMapper;
import fr.colline.monatis.references.controller.beneficiaire.BeneficiaireResponseDtoMapper;
import fr.colline.monatis.references.controller.categorie.CategorieResponseDtoMapper;
import fr.colline.monatis.references.controller.souscategorie.SousCategorieResponseDtoMapper;
import fr.colline.monatis.references.controller.titulaire.TitulaireResponseDtoMapper;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;

public class ReferenceResponseDtoMapper {

	public static ReferenceResponseDto mapperModelToBasicResponseDto(Reference reference) throws ControllerException {

		if ( Banque.class.isAssignableFrom(reference.getClass()) ) {
			return BanqueResponseDtoMapper.mapperModelToBasicResponseDto((Banque) reference);
		}
		else if ( Beneficiaire.class.isAssignableFrom(reference.getClass()) ) {
			return BeneficiaireResponseDtoMapper.mapperModelToBasicResponseDto((Beneficiaire) reference);
		}
		else if ( Categorie.class.isAssignableFrom(reference.getClass()) ) {
			return CategorieResponseDtoMapper.mapperModelToBasicResponseDto((Categorie) reference);
		}
		else if ( SousCategorie.class.isAssignableFrom(reference.getClass()) ) {
			return SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto((SousCategorie) reference);
		}
		else if ( Titulaire.class.isAssignableFrom(reference.getClass()) ) {
			return TitulaireResponseDtoMapper.mapperModelToBasicResponseDto((Titulaire) reference);
		}
		
		throw new ControllerException(
				GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE, 
				reference.getClass().getSimpleName());
	}

	public static ReferenceResponseDto mapperModelToSimpleResponseDto(Reference reference) throws ControllerException {

		if ( Banque.class.isAssignableFrom(reference.getClass()) ) {
			return BanqueResponseDtoMapper.mapperModelToSimpleResponseDto((Banque) reference);
		}
		else if ( Beneficiaire.class.isAssignableFrom(reference.getClass()) ) {
			return BeneficiaireResponseDtoMapper.mapperModelToSimpleResponseDto((Beneficiaire) reference);
		}
		else if ( Categorie.class.isAssignableFrom(reference.getClass()) ) {
			return CategorieResponseDtoMapper.mapperModelToSimpleResponseDto((Categorie) reference);
		}
		else if ( SousCategorie.class.isAssignableFrom(reference.getClass()) ) {
			return SousCategorieResponseDtoMapper.mapperModelToSimpleResponseDto((SousCategorie) reference);
		}
		else if ( Titulaire.class.isAssignableFrom(reference.getClass()) ) {
			return TitulaireResponseDtoMapper.mapperModelToSimpleResponseDto((Titulaire) reference);
		}
		
		throw new ControllerException(
				GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE, 
				reference.getClass().getSimpleName());
	}

	public static ReferenceResponseDto mapperModelToDetailedResponseDto(Reference reference) throws ControllerException {

		if ( Banque.class.isAssignableFrom(reference.getClass()) ) {
			return BanqueResponseDtoMapper.mapperModelToDetailedResponseDto((Banque) reference);
		}
		else if ( Beneficiaire.class.isAssignableFrom(reference.getClass()) ) {
			return BeneficiaireResponseDtoMapper.mapperModelToDetailedResponseDto((Beneficiaire) reference);
		}
		else if ( Categorie.class.isAssignableFrom(reference.getClass()) ) {
			return CategorieResponseDtoMapper.mapperModelToDetailedResponseDto((Categorie) reference);
		}
		else if ( SousCategorie.class.isAssignableFrom(reference.getClass()) ) {
			return SousCategorieResponseDtoMapper.mapperModelToDetailedResponseDto((SousCategorie) reference);
		}
		else if ( Titulaire.class.isAssignableFrom(reference.getClass()) ) {
			return TitulaireResponseDtoMapper.mapperModelToDetailedResponseDto((Titulaire) reference);
		}
		
		throw new ControllerException(
				GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE, 
				reference.getClass().getSimpleName());
	}

}
