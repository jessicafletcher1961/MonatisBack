package fr.colline.monatis.emprunts.controller;

import java.util.ArrayList;

import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.emprunts.controller.response.ConditionEmpruntBasicResponseDto;
import fr.colline.monatis.emprunts.controller.response.ConditionEmpruntDetailedResponseDto;
import fr.colline.monatis.emprunts.controller.response.ConditionEmpruntSimpleResponseDto;
import fr.colline.monatis.emprunts.controller.response.EcheanceResponseDto;
import fr.colline.monatis.emprunts.controller.response.EmpruntBasicResponseDto;
import fr.colline.monatis.emprunts.controller.response.EmpruntDetailedResponseDto;
import fr.colline.monatis.emprunts.controller.response.EmpruntSimpleResponseDto;
import fr.colline.monatis.emprunts.model.ConditionEmprunt;
import fr.colline.monatis.emprunts.model.Echeance;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.operations.controller.OperationResponseDtoMapper;
import fr.colline.monatis.typologies.controller.TypologieResponseDtoMapper;

public class EmpruntResponseDtoMapper {

	public static EmpruntBasicResponseDto mapperModelToBasicResponseDto(Emprunt emprunt) {
		
		EmpruntBasicResponseDto dto = new EmpruntBasicResponseDto();

		dto.cle = emprunt.getCle();
		dto.libelle = emprunt.getLibelle();
		if ( emprunt.getCompteInterne() != null ) {
			dto.identifiantCompteInterne = emprunt.getCompteInterne().getIdentifiant();
		}
		
		dto.conditionEmpruntInitiale = mapperModelToBasicResponseDto(emprunt.getConditionEmpruntInitiale());
		dto.revisions = new ArrayList<ConditionEmpruntBasicResponseDto>();
		for ( ConditionEmprunt revision : emprunt.getRevisions() ) {
			dto.revisions.add(mapperModelToBasicResponseDto(revision));
		}
		
		return dto;
	}

	static EmpruntSimpleResponseDto mapperModelToSimpleResponseDto(Emprunt emprunt) {
		
		EmpruntSimpleResponseDto dto = new EmpruntSimpleResponseDto();
		
		dto.cle = emprunt.getCle();
		dto.libelle = emprunt.getLibelle();
		if ( emprunt.getCompteInterne() != null ) {
			dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(emprunt.getCompteInterne());
		}
		
		dto.conditionEmpruntInitiale = mapperModelToSimpleResponseDto(emprunt.getConditionEmpruntInitiale());
		dto.revisions = new ArrayList<ConditionEmpruntSimpleResponseDto>();
		for ( ConditionEmprunt revision : emprunt.getRevisions() ) {
			dto.revisions.add(mapperModelToSimpleResponseDto(revision));
		}
		
		return dto;
	}

	static EmpruntDetailedResponseDto mapperModelToDetailedResponseDto(Emprunt emprunt) {

		EmpruntDetailedResponseDto dto = new EmpruntDetailedResponseDto();
		
		dto.cle = emprunt.getCle();
		dto.libelle = emprunt.getLibelle();
		if ( emprunt.getCompteInterne() != null ) {
			dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(emprunt.getCompteInterne());
		}
		
		dto.conditionEmpruntInitiale = mapperModelToDetailedResponseDto(emprunt.getConditionEmpruntInitiale());
		dto.revisions = new ArrayList<>();
		for ( ConditionEmprunt revision : emprunt.getRevisions() ) {
			dto.revisions.add(mapperModelToDetailedResponseDto(revision));
		}
		
		return dto;
	}
	
	static EcheanceResponseDto mapperModelToResponseDto(Echeance echeance) {
		
		EcheanceResponseDto dto = new EcheanceResponseDto();
		
		dto.numero = echeance.getNumeroEcheance();
		dto.date = echeance.getDateEcheance();
		dto.montantPaiementEnCentimes = echeance.getMontantPaiementEnCentimes();
		dto.partCapitalEnCentimes = echeance.getPartCapitalEnCentimes(); 
		dto.partInteretEnCentimes = echeance.getPartInteretsEnCentimes();
		dto.partFraisFixesEnCentimes = echeance.getPartFraisFixesEnCentimes();
		if ( echeance.getOperationPaiement() != null ) {
			dto.operationPaiement = OperationResponseDtoMapper.mapperModelToBasicResponseDto(echeance.getOperationPaiement());
		}
		if ( echeance.getOperationPartInterets() != null ) {
			dto.operationPartInterets = OperationResponseDtoMapper.mapperModelToBasicResponseDto(echeance.getOperationPartInterets());
		}
		dto.capitalEmprunteRestantDuEnCentimes = echeance.getCapitalEmprunteRestantDuEnCentimes();
		if ( echeance.getOperationPartFraisFixes() != null ) {
			dto.operationPartFraisFixes = OperationResponseDtoMapper.mapperModelToBasicResponseDto(echeance.getOperationPartFraisFixes());
		}
		dto.capitalEmprunteRestantDuEnCentimes = echeance.getCapitalEmprunteRestantDuEnCentimes();
		dto.capitalEmprunteDejaRembourseEnCentimes = echeance.getCapitalEmprunteDejaRembourseEnCentimes();
		
		return dto;
	}
	
	public static ConditionEmpruntBasicResponseDto mapperModelToBasicResponseDto(ConditionEmprunt conditionEmprunt) {
		
		ConditionEmpruntBasicResponseDto dto = new ConditionEmpruntBasicResponseDto();
		
		dto.libelle = conditionEmprunt.getLibelle();
		dto.tauxAnnuel = conditionEmprunt.getTauxAnnuel();
		dto.capitalEmprunteEnCentimes = conditionEmprunt.getCapitalEmprunteEnCentimes();
		dto.duree = conditionEmprunt.getDuree();
		dto.typePeriodeEcheances = TypologieResponseDtoMapper.mapperModelToResponseDto(conditionEmprunt.getTypePeriodeEcheances());

		return dto;
	}
	
	private static ConditionEmpruntSimpleResponseDto mapperModelToSimpleResponseDto(ConditionEmprunt conditionEmprunt) {
		
		ConditionEmpruntSimpleResponseDto dto = new ConditionEmpruntSimpleResponseDto();
		
		dto.libelle = conditionEmprunt.getLibelle();
		dto.tauxAnnuel = conditionEmprunt.getTauxAnnuel();
		dto.capitalEmprunteEnCentimes = conditionEmprunt.getCapitalEmprunteEnCentimes();
		dto.duree = conditionEmprunt.getDuree();
		dto.typePeriodeEcheances = TypologieResponseDtoMapper.mapperModelToResponseDto(conditionEmprunt.getTypePeriodeEcheances());

		dto.numeroPremiereEcheance = conditionEmprunt.getNumeroPremiereEcheance();
		dto.datePremiereEcheance = conditionEmprunt.getDatePremiereEcheance();
		dto.montantTotalEcheanceEnCentimes = conditionEmprunt.getMontantTotalEcheanceEnCentimes();
		dto.montantFraisFixesEcheanceEnCentimes = conditionEmprunt.getMontantFraisFixesEcheanceEnCentimes();

		return dto;
	}
	
	private static ConditionEmpruntDetailedResponseDto mapperModelToDetailedResponseDto(ConditionEmprunt conditionEmprunt) {

		ConditionEmpruntDetailedResponseDto dto = new ConditionEmpruntDetailedResponseDto();
		
		dto.libelle = conditionEmprunt.getLibelle();
		dto.tauxAnnuel = conditionEmprunt.getTauxAnnuel();
		dto.capitalEmprunteEnCentimes = conditionEmprunt.getCapitalEmprunteEnCentimes();
		dto.duree = conditionEmprunt.getDuree();
		dto.typePeriodeEcheances = TypologieResponseDtoMapper.mapperModelToResponseDto(conditionEmprunt.getTypePeriodeEcheances());

		dto.numeroPremiereEcheance = conditionEmprunt.getNumeroPremiereEcheance();
		dto.datePremiereEcheance = conditionEmprunt.getDatePremiereEcheance();
		dto.montantTotalEcheanceEnCentimes = conditionEmprunt.getMontantTotalEcheanceEnCentimes();
		dto.montantFraisFixesEcheanceEnCentimes = conditionEmprunt.getMontantFraisFixesEcheanceEnCentimes();

		if ( conditionEmprunt.getEcheances() != null ) {
			dto.echeances = new ArrayList<EcheanceResponseDto>();
			for ( Echeance echeance : conditionEmprunt.getEcheances() ) {
				dto.echeances.add(mapperModelToResponseDto(echeance));
			}
		}

		return dto;
	}
}
