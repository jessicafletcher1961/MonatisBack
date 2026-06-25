package fr.colline.monatis.operations.controller;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.data.domain.Page;

import fr.colline.monatis.comptes.controller.CompteResponseDtoMapper;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.operations.controller.response.OperationBasicResponseDto;
import fr.colline.monatis.operations.controller.response.OperationDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneBasicResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.OperationLigneSimpleResponseDto;
import fr.colline.monatis.operations.controller.response.OperationPageResponseDto;
import fr.colline.monatis.operations.controller.response.OperationResponseDto;
import fr.colline.monatis.operations.controller.response.OperationSimpleResponseDto;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.references.controller.beneficiaire.BeneficiaireResponseDtoMapper;
import fr.colline.monatis.references.controller.souscategorie.SousCategorieResponseDtoMapper;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.typologies.controller.TypologieResponseDtoMapper;

public class OperationResponseDtoMapper {

	public static OperationResponseDto mapperModelToBasicResponseDto(Operation operation) {

		OperationBasicResponseDto dto = new OperationBasicResponseDto();
		
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		dto.numero = operation.getNumero();
		dto.dateCreation = operation.getDateCreation();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnCentimes = operation.getMontantEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.identifiantCompteDepense = operation.getCompteDepense().getIdentifiant();
		dto.identifiantCompteRecette = operation.getCompteRecette().getIdentifiant();
		dto.pointee = operation.isPointee();
		
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

	public static OperationResponseDto mapperModelToSimpleResponseDto(Operation operation) throws ControllerException {

		OperationSimpleResponseDto dto = new OperationSimpleResponseDto();
		
		dto.typeOperation = TypologieResponseDtoMapper.mapperModelToResponseDto(operation.getTypeOperation());
		dto.numero = operation.getNumero();
		dto.dateCreation = operation.getDateCreation();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnCentimes = operation.getMontantEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.compteDepense = CompteResponseDtoMapper.mapperModelToBasicResponseDto(operation.getCompteDepense());
		dto.compteRecette = CompteResponseDtoMapper.mapperModelToBasicResponseDto(operation.getCompteRecette());
		dto.pointee = operation.isPointee();
		
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

	public static OperationResponseDto mapperModelToDetailedResponseDto(Operation operation) throws ControllerException {

		OperationDetailedResponseDto dto = new OperationDetailedResponseDto();
		
		dto.typeOperation = TypologieResponseDtoMapper.mapperModelToResponseDto(operation.getTypeOperation());
		dto.numero = operation.getNumero();
		dto.dateCreation = operation.getDateCreation();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnCentimes = operation.getMontantEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.compteDepense = CompteResponseDtoMapper.mapperModelToSimpleResponseDto(operation.getCompteDepense());
		dto.compteRecette = CompteResponseDtoMapper.mapperModelToSimpleResponseDto(operation.getCompteRecette());
		dto.pointee = operation.isPointee();
		
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

	public static OperationPageResponseDto mapperPageToResponseDto(Page<Operation> page) { // MODIFIE: ajoute les metadonnees de pagination.

		OperationPageResponseDto dto = new OperationPageResponseDto();
		
		dto.operations = page.getContent()
				.stream()
				.map((o) -> {return OperationResponseDtoMapper.mapperModelToBasicResponseDto(o);})
				.toList();
		dto.numeroPage = page.getNumber() + 1;
		dto.taillePage = page.getSize();
		dto.totalOperations = page.getTotalElements();
		dto.totalPages = page.getTotalPages();
		dto.premierElement = page.getTotalElements() == 0 ? 0L : page.getNumber() * (long) page.getSize() + 1;
		dto.dernierElement = page.getTotalElements() == 0 ? 0L : dto.premierElement + page.getNumberOfElements() - 1;
		
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
