package fr.colline.monatis.comptes.controller.interne;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class CompteInterneBasicResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 3038348672159554926L;
	
	public String codeTypeFonctionnement;
	public LocalDate dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public String nomBanque;
	public List<String> nomsTitulaires;
	
}
