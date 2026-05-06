package fr.colline.monatis.comptes.controller.interne;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.comptes.controller.CompteResponseDto;
import fr.colline.monatis.references.controller.ReferenceResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class CompteInterneDetailedResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = 5072165532892832366L;
	
	public LocalDate dateCloture;
	public TypologieResponseDto typeFonctionnement;
	public LocalDate dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public ReferenceResponseDto banque;
	public List<ReferenceResponseDto> titulaires;

}
