package fr.colline.monatis.emprunts.controller.response;

import java.io.Serializable;
import java.util.ArrayList;

public class EmpruntBasicResponseDto implements Serializable {

	private static final long serialVersionUID = 3533105109423426540L;

	public String cle;
	public String libelle;
	public String identifiantCompteInterne;
	public ConditionEmpruntBasicResponseDto conditionEmpruntInitiale;
	public ArrayList<ConditionEmpruntBasicResponseDto> revisions;
}
