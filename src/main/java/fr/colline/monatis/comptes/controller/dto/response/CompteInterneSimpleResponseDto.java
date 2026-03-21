package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.comptes.model.CategorieCompte;

public class CompteInterneSimpleResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 7723843322713142392L;

	public String codeCategorieCompte;
	public ZonedDateTime dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public String codeTypeCompteInterne;

	public String nomBanque;
	public List<String> nomsTitulaires;
	
	public CompteInterneSimpleResponseDto() {
		this.codeCategorieCompte = CategorieCompte.INTERNE.getCode();
	}
}
