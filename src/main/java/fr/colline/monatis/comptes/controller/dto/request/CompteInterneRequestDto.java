package fr.colline.monatis.comptes.controller.dto.request;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class CompteInterneRequestDto extends CompteRequestDto implements Serializable {

	private static final long serialVersionUID = -2903673429241830093L;
	
	public String codeTypeCompteInterne;
	public ZonedDateTime dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public String nomBanque;
	public List<String> nomsTitulaires;
}
