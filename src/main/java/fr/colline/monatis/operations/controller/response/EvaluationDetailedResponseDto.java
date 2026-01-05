package fr.colline.monatis.operations.controller.response;

import java.time.LocalDate;

import fr.colline.monatis.comptes.controller.interne.CompteInterneSimpleResponseDto;
import fr.colline.monatis.comptes.controller.technique.CompteTechniqueSimpleResponseDto;

public class EvaluationDetailedResponseDto {

	public String cle;
	public CompteInterneSimpleResponseDto compteInterne;
	public CompteTechniqueSimpleResponseDto compteTechnique;
	public LocalDate dateSolde;
	public String libelle;
	public Long montantSoldeEnCentimes;
	public OperationSimpleResponseDto operationPlusMoinsSolde;

}
