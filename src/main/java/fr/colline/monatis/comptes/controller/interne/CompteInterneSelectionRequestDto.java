package fr.colline.monatis.comptes.controller.interne;

import java.io.Serializable;
import java.time.LocalDate;

public class CompteInterneSelectionRequestDto implements Serializable {

	private static final long serialVersionUID = 3355703160444715896L;

	public String codeTypeFonctionnement;
	public LocalDate ouvertAu;
	public LocalDate fermeAu;
	public String identifiantContient;
	public String libelleContient;
	public String nomBanqueContient;
	public String nomTitulaireContient;
}
