package fr.colline.monatis.references.controller.mapper;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.mapper.CompteInterneDtoMapper;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.references.controller.dto.banques.BanqueBasicResponseDto;
import fr.colline.monatis.references.controller.dto.banques.BanqueDetailedResponseDto;
import fr.colline.monatis.references.controller.dto.banques.BanqueSimpleResponseDto;

public class BanqueDtoMapper {

	public static BanqueBasicResponseDto modelToBasicResponseDto(Banque banque) {

		BanqueBasicResponseDto dto = new BanqueBasicResponseDto();

		dto.nom = banque.getNom();
		dto.libelle = banque.getLibelle();
		
		return dto;
	}

	public static BanqueSimpleResponseDto modelToSimpleResponseDto(Banque banque) {
		
		BanqueSimpleResponseDto dto = new BanqueSimpleResponseDto();
		
		dto.nom = banque.getNom();
		dto.libelle = banque.getLibelle();

		dto.identifiantsComptesInternes = new ArrayList<>();
		if ( banque.getComptesInternes() != null ) {
			for ( CompteInterne compteInterne : banque.getComptesInternes() ) {
				dto.identifiantsComptesInternes.add(compteInterne.getIdentifiant());
			}
			Collections.sort(dto.identifiantsComptesInternes, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}

		return dto;
	}

	public static BanqueDetailedResponseDto modelToDetailedResponseDto(Banque banque) {
		
		BanqueDetailedResponseDto dto = new BanqueDetailedResponseDto();
		
		dto.nom = banque.getNom();
		dto.libelle = banque.getLibelle();
		
		dto.comptesInternes = new ArrayList<>();
		if ( banque.getComptesInternes() != null ) {
			for ( CompteInterne compteInterne : banque.getComptesInternes() ) {
				dto.comptesInternes.add(CompteInterneDtoMapper.modelToSimpleResponseDto(compteInterne));
			}
			Collections.sort(dto.comptesInternes, (o1, o2) -> {
				return o1.identifiant.compareTo(o2.identifiant);
			});
		}

		return dto;
	}
}
