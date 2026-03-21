package fr.colline.monatis.comptes.controller.mapper;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.dto.response.CompteInterneBasicResponseDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteInterneDetailedResponseDto;
import fr.colline.monatis.comptes.controller.dto.response.CompteInterneSimpleResponseDto;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.references.controller.mapper.BanqueDtoMapper;
import fr.colline.monatis.references.controller.mapper.TitulaireDtoMapper;
import fr.colline.monatis.references.model.Titulaire;

public class CompteInterneDtoMapper {

	public static CompteInterneBasicResponseDto modelToBasicResponseDto(CompteInterne compteInterne) {

		CompteInterneBasicResponseDto dto = new CompteInterneBasicResponseDto();

		dto.identifiant = compteInterne.getIdentifiant();
		dto.libelle = compteInterne.getLibelle();
		dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
		dto.montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		dto.codeTypeCompteInterne = compteInterne.getTypeCompteInterne().getCode();
		
		return dto;
		
	}
	
	public static CompteInterneSimpleResponseDto modelToSimpleResponseDto(CompteInterne compteInterne) {

		CompteInterneSimpleResponseDto dto = new CompteInterneSimpleResponseDto();

		dto.identifiant = compteInterne.getIdentifiant();
		dto.libelle = compteInterne.getLibelle();
		dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
		dto.montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		dto.codeTypeCompteInterne = compteInterne.getTypeCompteInterne().getCode();
		
		if ( compteInterne.getBanque() != null ) {
			dto.nomBanque = compteInterne.getBanque().getNom();
		}
		dto.nomsTitulaires = new ArrayList<>();
		if ( compteInterne.getTitulaires() != null ) {
			for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
				dto.nomsTitulaires.add(titulaire.getNom());
			}
			Collections.sort(dto.nomsTitulaires, (o1, o2) -> {
				return (o1.compareTo(o2));
			});
		}
		
		return dto;		
	}

	public static CompteInterneDetailedResponseDto modelToDetailedResponseDto(CompteInterne compteInterne) {

		CompteInterneDetailedResponseDto dto = new CompteInterneDetailedResponseDto();

		dto.identifiant = compteInterne.getIdentifiant();
		dto.libelle = compteInterne.getLibelle();
		dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
		dto.montantSoldeInitialEnCentimes = compteInterne.getMontantSoldeInitialEnCentimes();
		dto.typeCompteInterne = TypeCompteInterneDtoMapper.modelToResponseDto(compteInterne.getTypeCompteInterne());
		
		if ( compteInterne.getBanque() != null ) {
			dto.banque = BanqueDtoMapper.modelToSimpleResponseDto(compteInterne.getBanque());
		}
		dto.titulaires = new ArrayList<>();
		if ( compteInterne.getTitulaires() != null && compteInterne.getTitulaires().size() > 0 ) {
			for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
				dto.titulaires.add(TitulaireDtoMapper.modelToSimpleResponseDto(titulaire));
			}
			Collections.sort(dto.titulaires, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});
		}

		return dto;
	}
}
