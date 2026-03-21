package fr.colline.monatis.rapports.controller.resumes_comptes_internes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ResumeCompteInterneRequestDto implements Serializable {

	private static final long serialVersionUID = 8052356871831670811L;

	public List<String> identifiantsComptesInternes;
	public String codeTypeFonctionnement;
	public LocalDate dateSolde;
}
