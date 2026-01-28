package fr.colline.monatis.rapports.controller.liste_resume_comptes_interne;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.comptes.controller.interne.TypeFonctionnementDto;

public class ListeResumeCompteInterneParTypeFonctionnementResponseDto implements Serializable {

	private static final long serialVersionUID = 258134918083989238L;

	public TypeFonctionnementDto typeFonctionnement;
	public List<ResumeCompteInterneResponseDto> comptesInternes;
	
}
