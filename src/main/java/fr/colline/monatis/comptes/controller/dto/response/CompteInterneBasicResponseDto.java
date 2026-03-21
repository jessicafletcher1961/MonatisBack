package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;

import fr.colline.monatis.comptes.model.CategorieCompte;

public class CompteInterneBasicResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 5856428020795862939L;

	public String codeCategorieCompte;
	public ZonedDateTime dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public String codeTypeCompteInterne;

	public CompteInterneBasicResponseDto() {
		this.codeCategorieCompte = CategorieCompte.INTERNE.getCode();
	}
}
