package fr.colline.monatis.rapports.controller.liste_resume_comptes_interne;

import java.io.Serializable;

import fr.colline.monatis.comptes.controller.CompteResponseDto;

public class ResumeCompteInterneResponseDto implements Serializable {

	private static final long serialVersionUID = 4528320038885929469L;

	public CompteResponseDto compteInterne;
	public float soldeEnEuros;
	
}
