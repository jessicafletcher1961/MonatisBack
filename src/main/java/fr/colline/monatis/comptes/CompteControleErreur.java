package fr.colline.monatis.comptes;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum CompteControleErreur implements MonatisErreur {


	/**
			"L'identifiant est obligatoire"),
	 */
	IDENTIFIANT_OBLIGATOIRE(
			"L'identifiant est obligatoire"),

	/**
			"Aucun compte ne correspond à l'identifiant '%s'"), 
	 */
	NON_TROUVE_PAR_IDENTIFIANT(
			"Aucun compte ne correspond à l'identifiant '%s'"), 

	/**
			"L'identifiant '%s' correspond à un compte existant du type '%s'" ),
	 */
	IDENTIFIANT_DEJA_UTILISE(
			"L'identifiant '%s' correspond à un compte existant du type '%s'" ),

	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.COMPTE;
	private final TypeErreur typeErreur = TypeErreur.CONTROLE;

	private String pattern;
	
	@Override
	public String getCode() {
		String numero = String.format("%04d", this.ordinal());
		return typeDomaine.getCode().concat("-").concat(typeErreur.getCode()).concat("-").concat(numero);
	}

	@Override
	public String getPrefixe() {
		String numero = String.format("%04d", this.ordinal());
		return typeDomaine.getPrefixe().concat(".").concat(typeErreur.getPrefixe()).concat(".").concat(numero);
	}

	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public TypeErreur getTypeErreur() {
		return typeErreur;
	}

	@Override
	public TypeDomaine getTypeDomaine() {
		return typeDomaine;
	}

	private CompteControleErreur(String pattern) {
		this.pattern = pattern;
	}
}
