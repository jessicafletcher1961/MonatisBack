package fr.colline.monatis.comptes.controller.interne;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteRequestDto;

public class CompteInterneRequestDto extends CompteRequestDto implements Serializable {

	private static final long serialVersionUID = 6039735257697945819L;
	
	public String codeTypeFonctionnement;
	public LocalDate dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public String nomBanque;
	public List<String> nomsTitulaires;

}
