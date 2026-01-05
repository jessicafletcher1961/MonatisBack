package fr.colline.monatis.budgets;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum BudgetFonctionnelleErreur implements MonatisErreur {

	
	/**
			"Chevauchement de périodes pour la référence de type '%s' et de nom '%s' entre une période de type '%s' finissant le %s et la période de type '%s' commençant le %s"),
	 */
	CHEVAUCHEMENT_PERIODES(
			"Chevauchement de périodes pour la référence de type '%s' et de nom '%s' entre une période de type '%s' finissant le %s et la période de type '%s' commençant le %s"),


	/**
			"La création et la reconduction automatique d'un budget de type de période '%s' est impossible"),
	 */
	CREATION_ET_RECONDUCTION_AUTOMATIQUE_IMPOSSIBLE(
			"La création et la reconduction automatique d'un budget de type de période '%s' est impossible"),

	;

	private final TypeDomaine typeDomaine = TypeDomaine.BUDGET;
	private final TypeErreur typeErreur = TypeErreur.FONCTIONNELLE;
	
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

	private BudgetFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}
}
