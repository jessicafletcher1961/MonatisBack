package fr.colline.monatis.rapports.controller.bilan_patrimoine;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.rapports.controller.commun.TitulaireResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class EtatBilanPatrimoineResponseDto implements Serializable {

	private static final long serialVersionUID = 8859854939614310173L;

	public LocalDate dateDebutEtat;
	public LocalDate dateFinEtat;
	public TypologieResponseDto typePeriode;
	
	public List<EnteteCompteResponseDto> comptesInternes;
	public List<TypologieResponseDto> typesFonctionnements;
	public TitulaireResponseDto titulaire;
	
	public List<BilanPatrimoineTypeFonctionnementLigneResponseDto> lignesTypeFonctionnement;
	public Double montantSoldeInitialEnEuros;
	public BilanPatrimoinePeriodeResponseDto[] cumuls;

}
