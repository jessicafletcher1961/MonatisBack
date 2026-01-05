package fr.colline.monatis.references.controller.banque;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.references.model.Banque;

public class BanqueResponseDtoMapper {

	public static BanqueBasicResponseDto mapperModelToBasicResponseDto(Banque banque) {
		
		BanqueBasicResponseDto dto = new BanqueBasicResponseDto();
		
		dto.nom = banque.getNom();
		dto.libelle = banque.getLibelle();

		if ( banque.getComptesInternes() != null ) {
			dto.identifiantsComptesInternes = new ArrayList<>();
			for ( CompteInterne compteInterne : banque.getComptesInternes() ) {
				dto.identifiantsComptesInternes.add(compteInterne.getIdentifiant());
			}
			Collections.sort(dto.identifiantsComptesInternes, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}

		return dto;
	}

	public static BanqueSimpleResponseDto mapperModelToSimpleResponseDto(Banque banque) {
		
		BanqueSimpleResponseDto dto = new BanqueSimpleResponseDto();
		
		dto.nom = banque.getNom();
		dto.libelle = banque.getLibelle();

		if ( banque.getComptesInternes() != null ) {
			dto.comptesInternes = new ArrayList<>();
			for ( CompteInterne compteInterne : banque.getComptesInternes() ) {
				dto.comptesInternes.add(CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(compteInterne));
			}
			Collections.sort(dto.comptesInternes, (o1, o2) -> {
				return o1.identifiant.compareTo(o2.identifiant);
			});		
		}
		
		return dto;
	}

	public static BanqueDetailedResponseDto mapperModelToDetailedResponseDto(Banque banque) {
		
		BanqueDetailedResponseDto dto = new BanqueDetailedResponseDto();
		
		dto.nom = banque.getNom();
		dto.libelle = banque.getLibelle();

		if ( banque.getComptesInternes() != null ) {
			dto.comptesInternes = new ArrayList<>();
			for ( CompteInterne compteInterne : banque.getComptesInternes() ) {
				dto.comptesInternes.add(CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(compteInterne));
			}
			Collections.sort(dto.comptesInternes, (o1, o2) -> {
				return o1.identifiant.compareTo(o2.identifiant);
			});		
		}
		
		return dto;
	}
}
