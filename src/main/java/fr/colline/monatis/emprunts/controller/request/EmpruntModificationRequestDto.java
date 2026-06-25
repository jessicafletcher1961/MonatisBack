package fr.colline.monatis.emprunts.controller.request;

import java.io.Serializable;
import java.util.List;

public class EmpruntModificationRequestDto implements Serializable {

	private static final long serialVersionUID = -5920262087966166478L;

	public String cle;
	public String libelle;
	public String identifiantCompteInterne;

	public List<ConditionEmpruntRequestDto> conditionsEmprunt;
}
