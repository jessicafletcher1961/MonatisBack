package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class EmpruntSimpleResponseDto implements Serializable {

	private static final long serialVersionUID = -4338918514383499040L;
	
	public String cle;
	public String libelle;
	public CompteResponseDto compteInterne;
	public ConditionEmpruntSimpleResponseDto conditionEmpruntInitiale;
	public List<ConditionEmpruntSimpleResponseDto> revisions;
}
