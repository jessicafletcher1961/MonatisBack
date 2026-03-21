package fr.colline.monatis.operations.controller.mapper;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.mapper.CompteDtoMapper;
import fr.colline.monatis.operations.controller.dto.response.OperationBasicResponseDto;
import fr.colline.monatis.operations.controller.dto.response.OperationDetailedResponseDto;
import fr.colline.monatis.operations.controller.dto.response.OperationSimpleResponseDto;
import fr.colline.monatis.operations.model.DetailOperation;
import fr.colline.monatis.operations.model.Operation;

public class OperationDtoMapper {

	public static OperationBasicResponseDto modelToBasicResponseDto(Operation operation) {
		
		OperationBasicResponseDto dto = new OperationBasicResponseDto();
		
		dto.numero = operation.getNumero();
		dto.dateValeur = operation.getDateValeur();
		dto.montantTotalEnCentimes = operation.getMontantTotalEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		
		dto.detailsOperation = new ArrayList<>();
		if ( operation.getDetailsOperation() != null ) {
			for ( DetailOperation detailOperation : operation.getDetailsOperation() ) {
				dto.detailsOperation.add(DetailOperationDtoMapper.modelToBasicResponseDto(detailOperation));
			}
			Collections.sort(dto.detailsOperation, (o1, o2) -> {
				return o1.dateComptabilisation.compareTo(o2.dateComptabilisation);
			});
		}

		return dto;
	}
	
	public static OperationSimpleResponseDto modelToSimpleResponseDto(Operation operation) {

		OperationSimpleResponseDto dto = new OperationSimpleResponseDto();
		
		dto.numero = operation.getNumero();
		dto.dateValeur = operation.getDateValeur();
		dto.montantTotalEnCentimes = operation.getMontantTotalEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.codeTypeOperation = operation.getTypeOperation().getCode();

		if ( operation.getCompteDepense() != null) {
			dto.identifiantCompteDepense = operation.getCompteDepense().getIdentifiant();
		}
		if ( operation.getCompteRecette() != null ) {
			dto.identifiantCompteRecette = operation.getCompteRecette().getIdentifiant();
		}
		
		dto.detailsOperation = new ArrayList<>();
		if ( operation.getDetailsOperation() != null ) {
			for ( DetailOperation detailOperation : operation.getDetailsOperation() ) {
				dto.detailsOperation.add(DetailOperationDtoMapper.modelToSimpleResponseDto(detailOperation));
			}
			Collections.sort(dto.detailsOperation, (o1, o2) -> {
				return o1.dateComptabilisation.compareTo(o2.dateComptabilisation);
			});
		}
		
		return dto;
	}
	
	public static OperationDetailedResponseDto modelToDetailedResponseDto(Operation operation) {
		
		OperationDetailedResponseDto dto = new OperationDetailedResponseDto();
		
		dto.numero = operation.getNumero();
		dto.dateValeur = operation.getDateValeur();
		dto.montantTotalEnCentimes = operation.getMontantTotalEnCentimes();
		dto.libelle = operation.getLibelle();
		dto.typeOperation = TypeOperationDtoMapper.modelToResponseDto(operation.getTypeOperation());

		if ( operation.getCompteDepense() != null) {
			dto.compteDepense = CompteDtoMapper.modelToSimpleResponseDto(operation.getCompteDepense());
		}
		if ( operation.getCompteRecette() != null ) {
			dto.compteRecette = CompteDtoMapper.modelToSimpleResponseDto(operation.getCompteRecette());
		}
		
		dto.detailsOperation = new ArrayList<>();
		if ( operation.getDetailsOperation() != null ) {
			for ( DetailOperation detailOperation : operation.getDetailsOperation() ) {
				dto.detailsOperation.add(DetailOperationDtoMapper.modelToDetailedResponseDto(detailOperation));
			}
			Collections.sort(dto.detailsOperation, (o1, o2) -> {
				return o1.dateComptabilisation.compareTo(o2.dateComptabilisation);
			});
		}
		
		return dto;
	}
}
