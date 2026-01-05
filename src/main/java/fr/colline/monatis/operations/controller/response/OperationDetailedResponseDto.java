package fr.colline.monatis.operations.controller.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class OperationDetailedResponseDto implements Serializable {

	private static final long serialVersionUID = -8123052695805765163L;
	
	public String numero;
	public String libelle;
	public TypeOperationResponseDto typeOperation;
	public LocalDate dateValeur;
	public Long montantEnCentimes;
	public CompteResponseDto compteRecette;
	public CompteResponseDto compteDepense;
	public List<OperationLigneDetailedResponseDto> lignes;
}
