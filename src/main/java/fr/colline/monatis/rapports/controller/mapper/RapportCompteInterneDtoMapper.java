package fr.colline.monatis.rapports.controller.mapper;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.comptes.controller.mapper.TypeCompteInterneDtoMapper;
import fr.colline.monatis.operations.controller.mapper.OperationDtoMapper;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.rapports.controller.dto.RapportCompteInterneBasicResponseDto;
import fr.colline.monatis.rapports.controller.dto.RapportCompteInterneDetailedResponseDto;
import fr.colline.monatis.rapports.model.RapportCompteInterne;

public class RapportCompteInterneDtoMapper {

	public static RapportCompteInterneBasicResponseDto modelToBasicResponseDto(
			RapportCompteInterne rapport) {
		
		RapportCompteInterneBasicResponseDto dto = new RapportCompteInterneBasicResponseDto();
		
		dto.identifiantCompte = rapport.getCompteInterne().getIdentifiant();
		dto.libelleCompte = rapport.getCompteInterne().getLibelle();
		dto.codeTypeCompteInterne = rapport.getCompteInterne().getTypeCompteInterne().getCode();
		dto.dateSoldeInitial = rapport.getDateSoldeInitial();
		dto.dateSoldeFinal = rapport.getDateSoldeFinal();
		dto.montantSoldeInitialEnCentimes = rapport.getMontantSoldeInitialEnCentimes();
		dto.montantTotalRecetteEnCentimes = rapport.getMontantTotalRecetteEnCentimes();
		dto.montantTotalDepenseEnCentimes = rapport.getMontantTotalDepenseEnCentimes();
		dto.montantSoldeFinalEnCentimes = rapport.getMontantSoldeFinalEnCentimes();
		dto.montantDeltaEnCentimes = rapport.getMontantDeltaEnCentimes();
		
		return dto;
	}
	
	public static RapportCompteInterneDetailedResponseDto modelToDetailedResponseDto(
			RapportCompteInterne rapport) {

		RapportCompteInterneDetailedResponseDto dto = new RapportCompteInterneDetailedResponseDto();

		dto.identifiantCompte = rapport.getCompteInterne().getIdentifiant();
		dto.libelleCompte = rapport.getCompteInterne().getLibelle();
		dto.typeCompteInterne = TypeCompteInterneDtoMapper.modelToResponseDto(rapport.getCompteInterne().getTypeCompteInterne());
		dto.dateSoldeInitial = rapport.getDateSoldeInitial();
		dto.dateSoldeFinal = rapport.getDateSoldeFinal();
		dto.montantSoldeInitialEnCentimes = rapport.getMontantSoldeInitialEnCentimes();
		dto.montantTotalRecetteEnCentimes = rapport.getMontantTotalRecetteEnCentimes();
		dto.montantTotalDepenseEnCentimes = rapport.getMontantTotalDepenseEnCentimes();
		dto.montantSoldeFinalEnCentimes = rapport.getMontantSoldeFinalEnCentimes();
		dto.montantDeltaEnCentimes = rapport.getMontantDeltaEnCentimes();

		if ( rapport.getOperationsRecette() != null ) {
			dto.operationsRecette = new ArrayList<>();
			for ( Operation operation : rapport.getOperationsRecette() ) {
				dto.operationsRecette.add(OperationDtoMapper.modelToSimpleResponseDto(operation));
			}
			Collections.sort(dto.operationsRecette, (o1, o2) -> {
				return o1.dateValeur.compareTo(o2.dateValeur);
			});
		}
		if ( rapport.getOperationsDepense() != null ) {
			dto.operationsDepense = new ArrayList<>();
			for ( Operation operation : rapport.getOperationsDepense() ) {
				dto.operationsDepense.add(OperationDtoMapper.modelToSimpleResponseDto(operation));
			}
			Collections.sort(dto.operationsDepense, (o1, o2) -> {
				return o1.dateValeur.compareTo(o2.dateValeur);
			});
		}
		
		return dto;
	}
}
