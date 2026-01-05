package fr.colline.monatis.references;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum ReferenceTechniqueErreur implements MonatisErreur {


	/**
			"Un problème technique est survenu lors de la recherche d'une référence de type '%s' d'ID %s"),
	 */
	RECHERCHE_PAR_ID(
			"Un problème technique est survenu lors de la recherche d'une référence de type '%s' d'ID %s"),

	/**
			"Un problème technique est survenu lors de la vérification de l'existence d'une référence de type '%s' d'ID %s"),
	 */
	EXISTENCE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de l'existence d'une référence de type '%s' d'ID %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche de la référence de type '%s' de nom '%s'"),
	 */
	RECHERCHE_PAR_NOM(
			"Un problème technique est survenu lors de la recherche de la référence de type '%s' de nom '%s'"),
	
	/**
			"Un problème technique est survenu lors de la vérification de l'existence de la référence de type '%s' de nom '%s'"),
	 */
	EXISTENCE_PAR_NOM(
			"Un problème technique est survenu lors de la vérification de l'existence de la référence de type '%s' de nom '%s'"),

	/**
			"Un problème technique est survenu lors de la recherche de toutes les réferences de type '%s'"),
	 */
	RECHERCHE_TOUS(
			"Un problème technique est survenu lors de la recherche de toutes les réferences de type '%s'"),
	
	/**
			"Un problème technique est survenu lors de la suppression de toutes les références de type '%s'"),
	 */
	SUPPRESSION_TOUS(
			"Un problème technique est survenu lors de la suppression de toutes les références de type '%s'"),
	
	/**
			"Un problème technique est survenu lors de l'enregistrement d'une référence de type '%s' de nom '%s'"),
	 */
	ENREGISTREMENT(
			"Un problème technique est survenu lors de l'enregistrement d'une référence de type '%s' de nom '%s'"),
	
	/**
			"Un problème technique est survenu lors de la suppression d'une référence de type '%s' de nom '%s'"),
	 */
	SUPPRESSION(
			"Un problème technique est survenu lors de la suppression d'une référence de type '%s' de nom '%s'"),

	/**
			"Un problème technique est survenu lors de la vérification de la non-utilisation par des opérations de la référence de type '%s' et d'ID %s"),
	 */
	COMPTAGE_USAGE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de la non-utilisation par des opérations de la référence de type '%s' et d'ID %s"),


	;

	private final TypeDomaine typeDomaine = TypeDomaine.REFERENCE;
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
	
	private ReferenceTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}

}
