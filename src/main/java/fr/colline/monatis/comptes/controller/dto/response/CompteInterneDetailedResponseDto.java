package fr.colline.monatis.comptes.controller.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.comptes.controller.mapper.CategorieCompteDtoMapper;
import fr.colline.monatis.comptes.model.CategorieCompte;
import fr.colline.monatis.references.controller.dto.banques.BanqueSimpleResponseDto;
import fr.colline.monatis.references.controller.dto.titulaires.TitulaireSimpleResponseDto;

public class CompteInterneDetailedResponseDto extends CompteResponseDto implements Serializable {

	private static final long serialVersionUID = -6251189489199865586L;
	
	public CategorieCompteResponseDto categorieCompte;
	public ZonedDateTime dateSoldeInitial;
	public Long montantSoldeInitialEnCentimes;
	public TypeCompteInterneResponseDto typeCompteInterne;

	public BanqueSimpleResponseDto banque;
	public List<TitulaireSimpleResponseDto> titulaires;
	
	public CompteInterneDetailedResponseDto() {
		this.categorieCompte = CategorieCompteDtoMapper.modelToResponseDto(
				CategorieCompte.INTERNE);
	}
}
