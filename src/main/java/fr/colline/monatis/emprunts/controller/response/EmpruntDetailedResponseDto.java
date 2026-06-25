package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;
import java.util.ArrayList;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class EmpruntDetailedResponseDto implements Serializable {

	private static final long serialVersionUID = -5361482233281658855L;
	
	public String cle;
	public String libelle;
	public CompteResponseDto compteInterne;
	public ConditionEmpruntDetailedResponseDto conditionEmpruntInitiale;
	public ArrayList<ConditionEmpruntDetailedResponseDto> revisions;
}
