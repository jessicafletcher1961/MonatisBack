package fr.colline.monatis.evaluations.controller;

import fr.colline.monatis.comptes.controller.interne.CompteInterneResponseDtoMapper;
import fr.colline.monatis.comptes.controller.technique.CompteTechniqueResponseDtoMapper;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.exceptions.ControllerException;

public class EvaluationResponseDtoMapper {

	public static EvaluationResponseDto mapperModelToBasicResponseDto(Evaluation evaluation) {
		
		EvaluationBasicResponseDto dto = new EvaluationBasicResponseDto();
		
		dto.cle = evaluation.getCle();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		dto.identifiantCompteInterne = evaluation.getCompteInterne().getIdentifiant();
		dto.identifiantompteTechnique = evaluation.getCompteTechnique().getIdentifiant();
	
		return dto;
		
	}
	
	public static EvaluationResponseDto mapperModelToSimpleResponseDto(Evaluation evaluation) {
		
		EvaluationSimpleResponseDto dto = new EvaluationSimpleResponseDto();
		
		dto.cle = evaluation.getCle();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToBasicResponseDto(evaluation.getCompteInterne());
		dto.compteTechnique = CompteTechniqueResponseDtoMapper.mapperModelToBasicResponseDto(evaluation.getCompteTechnique());
		
		return dto;
	}

	public static EvaluationResponseDto mapperModelToDetailedResponseDto(Evaluation evaluation) throws ControllerException {
		
		EvaluationDetailedResponseDto dto = new EvaluationDetailedResponseDto();
		
		dto.cle = evaluation.getCle();
		dto.dateSolde = evaluation.getDateSolde();
		dto.montantSoldeEnCentimes = evaluation.getMontantSoldeEnCentimes();
		dto.libelle = evaluation.getLibelle();
		dto.compteInterne = CompteInterneResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation.getCompteInterne());
		dto.compteTechnique = CompteTechniqueResponseDtoMapper.mapperModelToSimpleResponseDto(evaluation.getCompteTechnique());

		return dto;
	}

}
