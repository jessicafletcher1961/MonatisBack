package fr.colline.monatis.references.controller.mapper;

import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.model.references.Reference;
import fr.colline.monatis.references.controller.dto.ReferenceResponseDto;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;

public class ReferenceDtoMapper {

	public static ReferenceResponseDto modelToBasicResponseDto(Reference reference) {
		
		if ( Beneficiaire.class.isAssignableFrom(reference.getClass()) ) {
			return BeneficiaireDtoMapper.modelToBasicResponseDto((Beneficiaire) reference);
		}
		else if ( Categorie.class.isAssignableFrom(reference.getClass()) ) {
			return CategorieDtoMapper.modelToBasicResponseDto((Categorie) reference);
		}
		else if ( SousCategorie.class.isAssignableFrom(reference.getClass()) ) {
			return SousCategorieDtoMapper.modelToBasicResponseDto((SousCategorie) reference);
		}
		else if ( Titulaire.class.isAssignableFrom(reference.getClass()) ) {
			return TitulaireDtoMapper.modelToBasicResponseDto((Titulaire) reference);
		}
		else if ( Banque.class.isAssignableFrom(reference.getClass()) ) {
			return BanqueDtoMapper.modelToBasicResponseDto((Banque) reference);
		}
		else {
			return null;
		}
	}

	public static ReferenceResponseDto modelToSimpleResponseDto(Reference reference) {
		
		if ( Beneficiaire.class.isAssignableFrom(reference.getClass()) ) {
			return BeneficiaireDtoMapper.modelToSimpleResponseDto((Beneficiaire) reference);
		}
		else if ( Categorie.class.isAssignableFrom(reference.getClass()) ) {
			return CategorieDtoMapper.modelToSimpleResponseDto((Categorie) reference);
		}
		else if ( SousCategorie.class.isAssignableFrom(reference.getClass()) ) {
			return SousCategorieDtoMapper.modelToSimpleResponseDto((SousCategorie) reference);
		}
		else if ( Titulaire.class.isAssignableFrom(reference.getClass()) ) {
			return TitulaireDtoMapper.modelToSimpleResponseDto((Titulaire) reference);
		}
		else if ( Banque.class.isAssignableFrom(reference.getClass()) ) {
			return BanqueDtoMapper.modelToSimpleResponseDto((Banque) reference);
		}
		else {
			return null;
		}
	}

	public static ReferenceResponseDto modelToDetailedResponseDto(Reference reference) {
		
		if ( Beneficiaire.class.isAssignableFrom(reference.getClass()) ) {
			return BeneficiaireDtoMapper.modelToDetailedResponseDto((Beneficiaire) reference);
		}
		else if ( Categorie.class.isAssignableFrom(reference.getClass()) ) {
			return CategorieDtoMapper.modelToDetailedResponseDto((Categorie) reference);
		}
		else if ( SousCategorie.class.isAssignableFrom(reference.getClass()) ) {
			return SousCategorieDtoMapper.modelToDetailedResponseDto((SousCategorie) reference);
		}
		else if ( Titulaire.class.isAssignableFrom(reference.getClass()) ) {
			return TitulaireDtoMapper.modelToDetailedResponseDto((Titulaire) reference);
		}
		else if ( Banque.class.isAssignableFrom(reference.getClass()) ) {
			return BanqueDtoMapper.modelToDetailedResponseDto((Banque) reference);
		}
		else {
			return null;
		}
	}
}
