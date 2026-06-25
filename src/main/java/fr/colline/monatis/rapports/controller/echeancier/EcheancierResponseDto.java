package fr.colline.monatis.rapports.controller.echeancier;

import java.util.List;

import fr.colline.monatis.rapports.controller.commun.EnteteEmpruntResponseDto;

public class EcheancierResponseDto {

	public EnteteEmpruntResponseDto emprunt;
	public List<EcheancierLigneResponseDto> lignes;
	public EcheancierCumulsResponseDto cumulFinal;
	public EcheancierCumulsResponseDto cumulDateCible;
	
}
