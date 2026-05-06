package fr.colline.monatis.rapports.controller.remunerations_frais;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.rapports.controller.commun.TitulaireResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class EtatRemunerationsFraisResponseDto implements Serializable {

	private static final long serialVersionUID = -6895930102223209029L;

	public LocalDate dateDebutEtat;
	public LocalDate dateFinEtat;
	public TypologieResponseDto typePeriode;
	
	public List<EnteteCompteResponseDto> comptesInternes;
	public List<TypologieResponseDto> typesFonctionnements;
	public TitulaireResponseDto titulaire;
	
	public List<RemunerationsFraisTypeFonctionnementLigneResponseDto> lignesTypeFonctionnement;
	public RemunerationsFraisPeriodeResponseDto[] cumuls;
	
}
