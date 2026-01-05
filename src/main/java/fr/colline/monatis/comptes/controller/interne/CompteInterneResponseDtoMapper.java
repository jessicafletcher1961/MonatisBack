package fr.colline.monatis.comptes.controller.interne;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.references.controller.banque.BanqueResponseDtoMapper;
import fr.colline.monatis.references.controller.titulaire.TitulaireResponseDtoMapper;
import fr.colline.monatis.references.model.Titulaire;

public class CompteInterneResponseDtoMapper {

	public static CompteInterneBasicResponseDto mapperModelToBasicResponseDto(CompteInterne compteInterne) {

		CompteInterneBasicResponseDto dto = new CompteInterneBasicResponseDto();
		
		dto.identifiant = compteInterne.getIdentifiant();
		dto.libelle = compteInterne.getLibelle();
		
		dto.codeTypeFonctionnement = compteInterne.getTypeFonctionnement().getCode();
		dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
		dto.montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		if ( compteInterne.getBanque() != null ) {
			dto.nomBanque = compteInterne.getBanque().getNom();
		}
		if ( compteInterne.getTitulaires() != null ) {
			dto.nomsTitulaires = new ArrayList<>();
			for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
				dto.nomsTitulaires.add(titulaire.getNom());
			}
			Collections.sort(dto.nomsTitulaires, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}
		
		return dto;
	}

	public static CompteInterneSimpleResponseDto mapperModelToSimpleResponseDto(CompteInterne compteInterne) {

		CompteInterneSimpleResponseDto dto = new CompteInterneSimpleResponseDto();
		
		dto.identifiant = compteInterne.getIdentifiant();
		dto.libelle = compteInterne.getLibelle();

		dto.typeFonctionnement = CompteInterneResponseDtoMapper.mapperModelToResponseDto(compteInterne.getTypeFonctionnement());
		dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
		dto.montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		if ( compteInterne.getBanque() != null ) {
			dto.banque = BanqueResponseDtoMapper.mapperModelToBasicResponseDto(compteInterne.getBanque());
		}
		if ( compteInterne.getTitulaires() != null ) {
			dto.titulaires = new ArrayList<>();
			for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
				dto.titulaires.add(TitulaireResponseDtoMapper.mapperModelToBasicResponseDto(titulaire));
			}
			Collections.sort(dto.titulaires, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});
		}
		
		return dto;
	}

	public static CompteInterneDetailedResponseDto mapperModelToDetailedResponseDto(CompteInterne compteInterne) {

		CompteInterneDetailedResponseDto dto = new CompteInterneDetailedResponseDto();
		
		dto.identifiant = compteInterne.getIdentifiant();
		dto.libelle = compteInterne.getLibelle();

		dto.typeFonctionnement = mapperModelToResponseDto(compteInterne.getTypeFonctionnement());
		dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
		dto.montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		if ( compteInterne.getBanque() != null ) {
			dto.banque = BanqueResponseDtoMapper.mapperModelToSimpleResponseDto(compteInterne.getBanque());
		}
		if ( compteInterne.getTitulaires() != null ) {
			dto.titulaires = new ArrayList<>();
			for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
				dto.titulaires.add(TitulaireResponseDtoMapper.mapperModelToSimpleResponseDto(titulaire));
			}
			Collections.sort(dto.titulaires, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});
		}
		
		return dto;
	}

	private static TypeFonctionnementDto mapperModelToResponseDto(TypeFonctionnement typeFonctionnement) {
		
		TypeFonctionnementDto dto = new TypeFonctionnementDto();
		
		dto.code = typeFonctionnement.getCode();
		dto.libelle = typeFonctionnement.getLibelle();
		
		return dto;
	}
}
