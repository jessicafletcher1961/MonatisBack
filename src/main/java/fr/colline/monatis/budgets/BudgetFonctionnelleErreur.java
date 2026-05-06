package fr.colline.monatis.budgets;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum BudgetFonctionnelleErreur implements MonatisErreur {

	
	/**
			"Chevauchement de périodes pour la référence de type '%s' et de nom '%s' entre une période de type '%s' finissant le %s et la période de type '%s' commençant le %s"),
	 */
	CHEVAUCHEMENT_PERIODE_PRECEDENTE(
			"Chevauchement de périodes pour la référence de type '%s' et de nom '%s' entre une période de type '%s' finissant le %s et la période de type '%s' commençant le %s"),

	/**
			"Chevauchement de périodes pour la référence de type '%s' et de nom '%s' entre une période de type '%s' commençant le %s et la période de type '%s' finissant le %s"),
	 */
	CHEVAUCHEMENT_PERIODE_SUIVANTE(
			"Chevauchement de périodes pour la référence de type '%s' et de nom '%s' entre une période de type '%s' commençant le %s et la période de type '%s' finissant le %s"),

	/**
			"La reconduction automatique d'un budget pour la réfétence de type '%s' et de nom '%s' n'est pas possible car il n'existe encore aucun budget pour cette référence"),
	 */
	RECONDUCTION_AUTOMATIQUE_IMPOSSIBLE(
			"La reconduction automatique d'un budget pour la réfétence de type '%s' et de nom '%s' n'est pas possible car il n'existe encore aucun budget pour cette référence"),

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
