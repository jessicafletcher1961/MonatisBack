package fr.colline.monatis.operations.controller.dto.response;

import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.comptes.controller.dto.response.CompteResponseDto;

public class OperationDetailedResponseDto {

	public String numero;
	public ZonedDateTime dateValeur;
	public Long montantTotalEnCentimes;
	public String libelle;
	public TypeOperationResponseDto typeOperation;

	public CompteResponseDto compteDepense;
	public CompteResponseDto compteRecette;
	
	public List<DetailOperationDetailedResponseDto> detailsOperation;
}
