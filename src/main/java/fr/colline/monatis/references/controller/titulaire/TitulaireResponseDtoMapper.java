package fr.colline.monatis.references.controller.titulaire;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.references.model.Titulaire;

public class TitulaireResponseDtoMapper {

	public static TitulaireBasicResponseDto mapperModelToBasicResponseDto(Titulaire titulaire) {

		TitulaireBasicResponseDto dto = new TitulaireBasicResponseDto();
		
		dto.nom = titulaire.getNom();
		dto.libelle = titulaire.getLibelle();
		
		if ( titulaire.getComptesInternes() != null ) {
			dto.identifiantsComptesInternes = new ArrayList<>();
			for ( CompteInterne compteInterne : titulaire.getComptesInternes() ) {
				dto.identifiantsComptesInternes.add(compteInterne.getIdentifiant());
			}
			Collections.sort(dto.identifiantsComptesInternes, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}
		
		return dto;
	}

	public static TitulaireSimpleResponseDto mapperModelToSimpleResponseDto(Titulaire titulaire) {

		TitulaireSimpleResponseDto dto = new TitulaireSimpleResponseDto();
		
		dto.nom = titulaire.getNom();
		dto.libelle = titulaire.getLibelle();
		
		if ( titulaire.getComptesInternes() != null ) {
			dto.comptesInternes = new ArrayList<>();
			for ( CompteInterne compteInterne : titulaire.getComptesInternes() ) {
				dto.comptesInternes.add(CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(compteInterne));
			}
			Collections.sort(dto.comptesInternes, (o1, o2) -> {
				return o1.identifiant.compareTo(o2.identifiant);
			});		
		}
		
		return dto;
	}

	public static TitulaireDetailedResponseDto mapperModelToDetailedResponseDto(Titulaire titulaire) {

		TitulaireDetailedResponseDto dto = new TitulaireDetailedResponseDto();
		
		dto.nom = titulaire.getNom();
		dto.libelle = titulaire.getLibelle();
		
		if ( titulaire.getComptesInternes() != null ) {
			dto.comptesInternes = new ArrayList<>();
			for ( CompteInterne compteInterne : titulaire.getComptesInternes() ) {
				dto.comptesInternes.add(CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(compteInterne));
			}
			Collections.sort(dto.comptesInternes, (o1, o2) -> {
				return o1.identifiant.compareTo(o2.identifiant);
			});		
		}
		
		return dto;
	}

}
