package fr.colline.monatis.rapports.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.operations.controller.dto.response.OperationBasicResponseDto;

public class RapportCompteInterneSimpleResponseDto {

	public String identifiantCompte;
	public String libelleCompte;
	public String codeTypeCompteInterne;
	public ZonedDateTime dateSoldeInitial;
	public ZonedDateTime dateSoldeFinal;
	public Long montantSoldeInitialEnCentimes;
	public Long montantTotalRecetteEnCentimes;
	public Long montantTotalDepenseEnCentimes;
	public Long montantSoldeFinalEnCentimes;
	public Long montantDeltaEnCentimes;

	public List<OperationBasicResponseDto> operationsRecette;
	public List<OperationBasicResponseDto> operationsDepense;
}
