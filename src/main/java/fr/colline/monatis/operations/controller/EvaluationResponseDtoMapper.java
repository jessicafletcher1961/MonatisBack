package fr.colline.monatis.operations.controller;

import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.comptes.controller.technique.CompteTechniqueResponseDtoMapper;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.operations.controller.response.EvaluationBasicResponseDto;
import fr.colline.monatis.operations.controller.response.EvaluationDetailedResponseDto;
import fr.colline.monatis.operations.controller.response.EvaluationSimpleResponseDto;
import fr.colline.monatis.operations.model.Evaluation;

public class EvaluationResponseDtoMapper {

	public static EvaluationBasicResponseDto mapperModelToBasicResponseDto(Evaluation evaluation) {
		
		EvaluationBasicResponseDto dto = new EvaluationBasicResponseDto();
		
		dto.cle = evaluation.getCle();
		dto.compteInterne = evaluation.getCompteInterne().getIdentifiant();
		dto.compteTechnique = evaluation.getCompteTechnique().getIdentifiant();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
	
		return dto;
		
	}
	
	public static EvaluationSimpleResponseDto mapperModelToSimpleResponseDto(Evaluation evaluation) {
		
		EvaluationSimpleResponseDto dto = new EvaluationSimpleResponseDto();
		
		dto.cle = evaluation.getCle();
		dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(evaluation.getCompteInterne());
		dto.compteTechnique = CompteTechniqueResponseDtoMapper.mapperModelToBasicResponseDto(evaluation.getCompteTechnique());
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		
		return dto;
	}

	public static EvaluationDetailedResponseDto mapperModelToDetailedResponseDto(Evaluation evaluation) throws ControllerException {
		
		EvaluationDetailedResponseDto dto = new EvaluationDetailedResponseDto();
		
		dto.cle = evaluation.getCle();
		dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation.getCompteInterne());
		dto.compteTechnique = CompteTechniqueResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation.getCompteTechnique());
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();

		return dto;
	}

}
