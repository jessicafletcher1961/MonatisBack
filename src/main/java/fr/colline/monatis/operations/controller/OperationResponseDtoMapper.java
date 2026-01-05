package fr.colline.monatis.operations.controller;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.CompteResponseDtoMapper;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.operations.controller.response.OperationBasicResponseDto;
import fr.colline.monatis.operations.controller.response.OperationDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneBasicResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneSimpleResponseDto;
import fr.colline.monatis.operations.controller.response.OperationSimpleResponseDto;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.references.controller.beneficiaire.BeneficiaireResponseDtoMapper;
import fr.colline.monatis.references.controller.souscategorie.SousCategorieResponseDtoMapper;
import fr.colline.monatis.references.model.Beneficiaire;

public class OperationResponseDtoMapper {

	public static OperationBasicResponseDto mapperModelToBasicResponseDto(Operation operation) {

		OperationBasicResponseDto dto = new OperationBasicResponseDto();
		
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		dto.numero = operation.getNumero();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnCentimes = operation.getMontantEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.identifiantCompteDepense = operation.getCompteDepense().getIdentifiant();
		dto.identifiantCompteRecette = operation.getCompteRecette().getIdentifiant();
		
		dto.lignes = new ArrayList<>();
		if ( operation.getLignes() != null ) {
			for ( OperationLigne ligne : operation.getLignes()) {
				dto.lignes.add(mapperModelToBasicResponseDto(ligne));
			}
			Collections.sort(dto.lignes, (o1, o2) -> {
				return o1.dateComptabilisation.compareTo(o2.dateComptabilisation);
			});
		}

		return dto;
	}

	private static OperationLigneBasicResponseDto mapperModelToBasicResponseDto(OperationLigne ligne) {

		OperationLigneBasicResponseDto dto = new OperationLigneBasicResponseDto();
		
		dto.numeroLigne = ligne.getNumeroLigne();
		dto.dateComptabilisation = ligne.getDateComptabilisation();
		dto.libelle = ligne.getLibelle();
		dto.montantEnCentimes = ligne.getMontantEnCentimes();
		
		if ( ligne.getSousCategorie() != null ) {
			dto.nomSousCategorie = ligne.getSousCategorie().getNom();
		}
		dto.nomsBeneficiaires = new ArrayList<>();
		if ( ligne.getBeneficiaires() != null ) {
			for ( Beneficiaire beneficiaire : ligne.getBeneficiaires() ) {
				dto.nomsBeneficiaires.add(beneficiaire.getNom());
			}
			Collections.sort(dto.nomsBeneficiaires, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}

		return dto;
	}

	public static OperationSimpleResponseDto mapperModelToSimpleResponseDto(Operation operation) throws ControllerException {

		OperationSimpleResponseDto dto = new OperationSimpleResponseDto();
		
		dto.typeOperation = TypeOperationDtoMapper.mapperModelToResponseDto(operation.getTypeOperation());
		dto.numero = operation.getNumero();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnCentimes = operation.getMontantEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.compteDepense = CompteResponseDtoMapper.mapperModelToBasicResponseDto(operation.getCompteDepense());
		dto.compteRecette = CompteResponseDtoMapper.mapperModelToBasicResponseDto(operation.getCompteRecette());
		
		dto.lignes = new ArrayList<>();
		if ( operation.getLignes() != null ) {
			for ( OperationLigne ligne : operation.getLignes()) {
				dto.lignes.add(mapperModelToSimpleResponseDto(ligne));
			}
			Collections.sort(dto.lignes, (o1, o2) -> {
				return o1.dateComptabilisation.compareTo(o2.dateComptabilisation);
			});
		}

		return dto;
	}

	private static OperationLigneSimpleResponseDto mapperModelToSimpleResponseDto(OperationLigne ligne) {

		OperationLigneSimpleResponseDto dto = new OperationLigneSimpleResponseDto();
		
		dto.numeroLigne = ligne.getNumeroLigne();
		dto.dateComptabilisation = ligne.getDateComptabilisation();
		dto.libelle = ligne.getLibelle();
		dto.montantEnCentimes = ligne.getMontantEnCentimes();
		
		if ( ligne.getSousCategorie() != null ) {
			dto.sousCategorie = SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto(ligne.getSousCategorie());
		}
		dto.beneficiaires = new ArrayList<>();
		if ( ligne.getBeneficiaires() != null ) {
			for ( Beneficiaire beneficiaire : ligne.getBeneficiaires() ) {
				dto.beneficiaires.add(BeneficiaireResponseDtoMapper.mapperModelToBasicResponseDto(beneficiaire));
			}
			Collections.sort(dto.beneficiaires, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});
		}

		return dto;
	}

	public static OperationDetailedResponseDto mapperModelToDetailedResponseDto(Operation operation) throws ControllerException {

		OperationDetailedResponseDto dto = new OperationDetailedResponseDto();
		
		dto.typeOperation = TypeOperationDtoMapper.mapperModelToResponseDto(operation.getTypeOperation());
		dto.numero = operation.getNumero();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnCentimes = operation.getMontantEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.compteDepense = CompteResponseDtoMapper.mapperModelToSimpleResponseDto(operation.getCompteDepense());
		dto.compteRecette = CompteResponseDtoMapper.mapperModelToSimpleResponseDto(operation.getCompteRecette());
		
		dto.lignes = new ArrayList<>();
		if ( operation.getLignes() != null ) {
			for ( OperationLigne ligne : operation.getLignes()) {
				dto.lignes.add(mapperModelToDetailedResponseDto(ligne));
			}
			Collections.sort(dto.lignes, (o1, o2) -> {
				return o1.dateComptabilisation.compareTo(o2.dateComptabilisation);
			});
		}

		return dto;
	}

	private static OperationLigneDetailedResponseDto mapperModelToDetailedResponseDto(OperationLigne ligne) {
		
		OperationLigneDetailedResponseDto dto = new OperationLigneDetailedResponseDto();
		
		dto.numeroLigne = ligne.getNumeroLigne();
		dto.dateComptabilisation = ligne.getDateComptabilisation();
		dto.libelle = ligne.getLibelle();
		dto.montantEnCentimes = ligne.getMontantEnCentimes();
		
		if ( ligne.getSousCategorie() != null ) {
			dto.sousCategorie = SousCategorieResponseDtoMapper.mapperModelToSimpleResponseDto(ligne.getSousCategorie());
		}
		dto.beneficiaires = new ArrayList<>();
		if ( ligne.getBeneficiaires() != null ) {
			for ( Beneficiaire beneficiaire : ligne.getBeneficiaires() ) {
				dto.beneficiaires.add(BeneficiaireResponseDtoMapper.mapperModelToSimpleResponseDto(beneficiaire));
			}
			Collections.sort(dto.beneficiaires, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});
		}

		return dto;
	}

}
