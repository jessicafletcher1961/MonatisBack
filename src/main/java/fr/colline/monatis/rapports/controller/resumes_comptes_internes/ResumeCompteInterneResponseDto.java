package fr.colline.monatis.rapports.controller.resumes_comptes_internes;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;

public class ResumeCompteInterneResponseDto implements Serializable {

	private static final long serialVersionUID = 4528320038885929469L;

	public EnteteCompteResponseDto compteInterne;
	public LocalDate dateSolde; 
	public Double montantSoldeEnEuros;
}
