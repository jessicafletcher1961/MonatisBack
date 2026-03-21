package fr.colline.monatis.rapports.controller.dto;

import java.time.ZonedDateTime;

public class RapportCompteInterneBasicResponseDto {

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
}
