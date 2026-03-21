package fr.colline.monatis.operations.controller.mapper;

import java.util.ArrayList;
import java.util.Collections;

import fr.colline.monatis.operations.controller.dto.response.DetailOperationBasicResponseDto;
import fr.colline.monatis.operations.controller.dto.response.DetailOperationDetailedResponseDto;
import fr.colline.monatis.operations.controller.dto.response.DetailOperationSimpleResponseDto;
import fr.colline.monatis.operations.model.DetailOperation;
import fr.colline.monatis.references.controller.mapper.BeneficiaireDtoMapper;
import fr.colline.monatis.references.controller.mapper.SousCategorieDtoMapper;
import fr.colline.monatis.references.model.Beneficiaire;

public class DetailOperationDtoMapper {

	public static DetailOperationBasicResponseDto modelToBasicResponseDto(DetailOperation detailOperation) {
		
		DetailOperationBasicResponseDto dto = new DetailOperationBasicResponseDto();
		
		dto.sequence = detailOperation.getSequence();
		dto.dateComptabilisation = detailOperation.getDateComptabilisation();
		dto.montantDetailEnCentimes = detailOperation.getMontantDetailEnCentimes();
		dto.libelle = detailOperation.getLibelle();
		
		return dto;
	}

	public static DetailOperationSimpleResponseDto modelToSimpleResponseDto(DetailOperation detailOperation) {

		DetailOperationSimpleResponseDto dto = new DetailOperationSimpleResponseDto();
		
		dto.sequence = detailOperation.getSequence();
		dto.dateComptabilisation = detailOperation.getDateComptabilisation();
		dto.montantDetailEnCentimes = detailOperation.getMontantDetailEnCentimes();
		dto.libelle = detailOperation.getLibelle();

		if ( detailOperation.getSousCategorie() != null ) {
			dto.nomSousCategorie = detailOperation.getSousCategorie().getNom();
		}
		dto.nomsBeneficiaires = new ArrayList<>();
		if ( detailOperation.getBeneficiaires() != null && detailOperation.getBeneficiaires().size() > 0 ) {
			for ( Beneficiaire beneficiaire : detailOperation.getBeneficiaires() ) {
				dto.nomsBeneficiaires.add(beneficiaire.getNom());
			}
			Collections.sort(dto.nomsBeneficiaires, (o1, o2) -> {
				return o1.compareTo(o2);
			});
		}
		
		return dto;
	}

	public static DetailOperationDetailedResponseDto modelToDetailedResponseDto(DetailOperation detailOperation) {
		
		DetailOperationDetailedResponseDto dto = new DetailOperationDetailedResponseDto();

		dto.sequence = detailOperation.getSequence();
		dto.dateComptabilisation = detailOperation.getDateComptabilisation();
		dto.montantDetailEnCentimes = detailOperation.getMontantDetailEnCentimes();
		dto.libelle = detailOperation.getLibelle();
		
		if ( detailOperation.getSousCategorie() != null ) {
			dto.sousCategorie = SousCategorieDtoMapper.modelToSimpleResponseDto(detailOperation.getSousCategorie());
		}
		dto.beneficiaires = new ArrayList<>();
		if ( detailOperation.getBeneficiaires() != null && detailOperation.getBeneficiaires().size() > 0 ) {
			for ( Beneficiaire beneficiaire : detailOperation.getBeneficiaires() ) {
				dto.beneficiaires.add(BeneficiaireDtoMapper.modelToSimpleResponseDto(beneficiaire));
			}
			Collections.sort(dto.beneficiaires, (o1, o2) -> {
				return o1.nom.compareTo(o2.nom);
			});
		}
		
		return dto;
	}
}
