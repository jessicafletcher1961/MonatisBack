package fr.colline.monatis.budgets;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum BudgetTechniqueErreur implements MonatisErreur {
	
	/**
			"Un problème technique est survenu lors de la recherche d'un budget par l'ID %s"),
	 */
	RECHERCHE_PAR_ID(
			"Un problème technique est survenu lors de la recherche d'un budget par l'ID %s"),

	/**
			"Un problème technique est survenu lors de la vérification de l'existence d'un budget par l'ID %s"),
	 */
	EXISTENCE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de l'existence d'un budget par l'ID %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche de l'historique des budgets correspondant à la référence d'ID %s"),
	 */
	RECHERCHE_HISTORIQUE_PAR_REFERENCE_ID(
			"Un problème technique est survenu lors de la recherche de l'historique des budgets correspondant à la référence d'ID %s"),

	/**
			"Un problème technique est survenu lors de la vérification de l'existence de l'historique des budgets correspondant à la référence d'ID %s"),
	 */
	EXISTENCE_HISTORIQUE_PAR_REFERENCE_ID(
			"Un problème technique est survenu lors de la vérification de l'existence de l'historique des budgets correspondant à la référence d'ID %s"), 
	
	/**
			"Un problème technique est survenu lors de la recherche d'un budget correspondant à la référence d'ID %s pour la date %s"),
	 */
	RECHERCHE_PAR_REFERENCE_ID_ET_DATE_CIBLE(
			"Un problème technique est survenu lors de la recherche d'un budget correspondant à la référence d'ID %s pour la date %s"),
	
	/**
			"Un problème technique est survenu lors de la vérification de l'existence d'un budget correspondant à la référence d'ID %s pour la date %s"),
	 */
	EXISTENCE_PAR_REFERENCE_ID_ET_DATE_CIBLE(
			"Un problème technique est survenu lors de la vérification de l'existence d'un budget correspondant à la référence d'ID %s pour la date %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche de tous les budgets"),
	 */
	RECHERCHE_TOUS(
			"Un problème technique est survenu lors de la recherche de tous les budgets"),
	
	/**
			"Un problème technique est survenu lors de la suppression de tous les budgets"),
	 */
	SUPPRESSION_TOUS(
			"Un problème technique est survenu lors de la suppression de tous les budgets"),
	
	/**
			"Un problème technique est survenu lors de l'enregistrement d'un budget correspondant à la référence de type '%s' et de nom '%s' pour la période du %s au %s"),
	 */
	ENREGISTREMENT(
			"Un problème technique est survenu lors de l'enregistrement d'un budget correspondant à la référence de type '%s' et de nom '%s' pour la période du %s au %s"),
	
	/**
			"Un problème technique est survenu lors de la suppression d'un budget correspondant à la référence de type '%s' et de nom '%s' pour la période du %s au %s"),
	 */
	SUPPRESSION(
			"Un problème technique est survenu lors de la suppression d'un budget correspondant à la référence de type '%s' et de nom '%s' pour la période du %s au %s"),

	;

	private final TypeDomaine typeDomaine = TypeDomaine.BUDGET;
	private final TypeErreur typeErreur = TypeErreur.TECHNIQUE;
	
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
	
	private BudgetTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}
	
}
