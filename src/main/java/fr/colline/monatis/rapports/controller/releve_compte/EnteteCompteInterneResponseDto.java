package fr.colline.monatis.rapports.controller.releve_compte;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EnteteCompteInterneResponseDto implements EnteteCompteResponseDto, Serializable {
	
	private static final long serialVersionUID = 1763729308262173241L;

	// Infos tous comptes
	public String identifiantCompte;
	public String libelleCompte;
	public String codeTypeCompte;

	// Infos compte interne
	public String codeTypeFonctionnement;
	public String libelleBanque;
	public List<String> libellesTitulaires;
	public LocalDate dateSoldeInitial;
	public Float montantSoldeInitialEnEuros;

}
