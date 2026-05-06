package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.rapports.controller.commun.BanqueResponseDto;
import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;
import fr.colline.monatis.rapports.controller.commun.TitulaireResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class EtatPlusMoinsValueResponseDto implements Serializable {

	private static final long serialVersionUID = -599395544297234063L;

	public LocalDate dateDebutEtat;
	public LocalDate dateFinEtat;
	public TypologieResponseDto typePeriode;

	public List<EnteteCompteResponseDto> comptesInternes;
	public List<TypologieResponseDto> typesFonctionnements;
	public BanqueResponseDto banque;
	public TitulaireResponseDto titulaire;
	
	public List<PlusMoinsValueTypeFonctionnementLigneResponseDto> lignesTypeFonctionnement;
	public PlusMoinsValuePeriodeResponseDto[] cumuls;
}
