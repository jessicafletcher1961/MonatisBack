package fr.colline.monatis.references.controller.mapper;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.mapper.CompteInterneDtoMapper;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.references.controller.dto.titulaires.TitulaireBasicResponseDto;
import fr.colline.monatis.references.controller.dto.titulaires.TitulaireDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.titulaires.TitulaireSimpleResponseDto;
import fr.colline.monatis.references.model.Titulaire;

public class TitulaireDtoMapper {

	public static TitulaireBasicResponseDto modelToBasicResponseDto(Titulaire titulaire) {

		TitulaireBasicResponseDto dto = new TitulaireBasicResponseDto();

		dto.nom = titulaire.getNom();
		dto.libelle = titulaire.getLibelle();
		
		return dto;
	}

	public static TitulaireSimpleResponseDto modelToSimpleResponseDto(Titulaire titulaire) {

		TitulaireSimpleResponseDto dto = new TitulaireSimpleResponseDto();
		
		dto.nom = titulaire.getNom();
		dto.libelle = titulaire.getLibelle();

		dto.identifiantsComptesInternes = new ArrayList<>();
		if ( titulaire.getComptesInternes() != null ) {
			for ( CompteInterne compteInterne : titulaire.getComptesInternes() ) {
				dto.identifiantsComptesInternes.add(compteInterne.getIdentifiant());
			}
			Collections.sort(dto.identifiantsComptesInternes, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}
		
		return dto;
	}

	public static TitulaireDetailedResponseDto modelToDetailedResponseDto(Titulaire titulaire) {

		TitulaireDetailedResponseDto dto = new TitulaireDetailedResponseDto();
		
		dto.nom = titulaire.getNom();
		dto.libelle = titulaire.getLibelle();

		dto.comptesInternes = new ArrayList<>();
		if ( titulaire.getComptesInternes() != null ) {
			for ( CompteInterne compteInterne : titulaire.getComptesInternes() ) {
				dto.comptesInternes.add(CompteInterneDtoMapper.modelToSimpleResponseDto(compteInterne));
			}
			Collections.sort(dto.comptesInternes, (o1, o2) -> {
				return o1.identifiant.compareTo(o2.identifiant);
			});
		}
		
		return dto;
	}
}
