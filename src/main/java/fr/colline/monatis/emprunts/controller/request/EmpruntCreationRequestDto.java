package fr.colline.monatis.emprunts.controller.request;

import java.io.Serializable;

public class EmpruntCreationRequestDto implements Serializable {

	private static final long serialVersionUID = -8126101334356918774L;

	public String cle;
	public String libelle;
	public String identifiantCompteInterne;
	
	public ConditionEmpruntRequestDto conditionEmpruntInitiale;
}
