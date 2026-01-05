package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;

public class EnteteCompteExterneResponseDto implements EnteteCompteResponseDto, Serializable {

	private static final long serialVersionUID = -3353463856102678036L;
	
	public String identifiantCompte;
	
	public String libelleCompte;
	
	public String codeTypeCompte;

}
