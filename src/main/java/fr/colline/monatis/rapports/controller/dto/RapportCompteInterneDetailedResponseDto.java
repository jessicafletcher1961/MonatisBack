package fr.colline.monatis.rapports.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.comptes.controller.dto.response.TypeCompteInterneResponseDto;
import fr.colline.monatis.operations.controller.dto.response.OperationSimpleResponseDto;

public class RapportCompteInterneDetailedResponseDto {

	public String identifiantCompte;
	public String libelleCompte;
	public TypeCompteInterneResponseDto typeCompteInterne;
	public ZonedDateTime dateSoldeInitial;
	public ZonedDateTime dateSoldeFinal;
	public Long montantSoldeInitialEnCentimes;
	public Long montantTotalRecetteEnCentimes;
	public Long montantTotalDepenseEnCentimes;
	public Long montantSoldeFinalEnCentimes;
	public Long montantDeltaEnCentimes;

	public List<OperationSimpleResponseDto> operationsRecette;
	public List<OperationSimpleResponseDto> operationsDepense;
}
