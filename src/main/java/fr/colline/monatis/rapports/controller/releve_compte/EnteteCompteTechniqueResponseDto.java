package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;

public class EnteteCompteTechniqueResponseDto implements EnteteCompteResponseDto, Serializable {

	private static final long serialVersionUID = 3240120647095275719L;

	public String identifiantCompte;
	
	public String libelleCompte;
	
	public String codeTypeCompte;

}
