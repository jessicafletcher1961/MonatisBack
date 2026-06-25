package fr.colline.monatis.evaluations.controller;

import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.exceptions.ControllerException;

public class EvaluationResponseDtoMapper {

	public static EvaluationResponseDto mapperModelToBasicResponseDto(Evaluation evaluation) {
		
		EvaluationBasicResponseDto dto = new EvaluationBasicResponseDto();
		
		dto.id = evaluation.getId();
		dto.cle = evaluation.getCle();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		dto.identifiantCompteInterne = evaluation.getCompteInterne().getIdentifiant();
	
		return dto;
		
	}
	
	public static EvaluationResponseDto mapperModelToSimpleResponseDto(Evaluation evaluation) {
		
		EvaluationSimpleResponseDto dto = new EvaluationSimpleResponseDto();
		
		dto.id = evaluation.getId();
		dto.cle = evaluation.getCle();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(evaluation.getCompteInterne());
		
		return dto;
	}

	public static EvaluationResponseDto mapperModelToDetailedResponseDto(Evaluation evaluation) throws ControllerException {
		
		EvaluationDetailedResponseDto dto = new EvaluationDetailedResponseDto();
		
		dto.id = evaluation.getId();
		dto.cle = evaluation.getCle();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation.getCompteInterne());

		return dto;
	}

}
