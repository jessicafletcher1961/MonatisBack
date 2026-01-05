package fr.colline.monatis.comptes;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum CompteTechniqueErreur implements MonatisErreur {


	/**
			"Un problème technique est survenu lors de la recherche d'un compte de type '%s' d'ID %s"),
	 */
	RECHERCHE_PAR_ID(
			"Un problème technique est survenu lors de la recherche d'un compte de type '%s' d'ID %s"),

	/**
			"Un problème technique est survenu lors de la vérification de l'existence d'un compte de type '%s' d'ID %s"),
	 */
	EXISTENCE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de l'existence d'un compte de type '%s' d'ID %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche du compte de type '%s' d'identifiant '%s'"),
	 */
	RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la recherche du compte de type '%s' d'identifiant '%s'"),
	
	/**
			"Un problème technique est survenu lors de la vérification de l'existence du compte de type '%s' d'identifiant '%s'"),
	 */
	EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la vérification de l'existence du compte de type '%s' d'identifiant '%s'"),

	/**
			"Un problème technique est survenu lors de la recherche de toutes les réferences de type '%s'"),
	 */
	RECHERCHE_TOUS(
			"Un problème technique est survenu lors de la recherche de tous les comptes de type '%s'"),
	
	/**
			"Un problème technique est survenu lors de la suppression de tous les comptes de type '%s'"),
	 */
	SUPPRESSION_TOUS(
			"Un problème technique est survenu lors de la suppression de tous les comptes de type '%s'"),

	/**
			"Un problème technique est survenu lors de l'enregistrement du compte d'identifiant '%s'"),
	 */
	ENREGISTREMENT(
			"Un problème technique est survenu lors de l'enregistrement du compte d'identifiant '%s'"),
	
	/**
			"Un problème technique est survenu lors de la suppression du compte d'identifiant '%s'"),
	 */
	SUPPRESSION(
			"Un problème technique est survenu lors de la suppression du compte d'identifiant '%s'"),
	
	/**
			"Un problème technique est survenu lors de la recherche des comptes internes ayant le type de fonctionnement %s-%s"),
	 */
	RECHERCHE_PAR_TYPE_FONCTIONNEMENT(
			"Un problème technique est survenu lors de la recherche des comptes internes ayant le type de fonctionnement %s-%s"),

	/**
			"Un problème technique est survenu lors de la vérification de la non-utilisation par des opérations du compte de type '%s' et d'ID %s"),
	 */
	COMPTAGE_USAGE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de la non-utilisation par des opérations du compte de type '%s' et d'ID %s"),
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.COMPTE;
	private final TypeErreur typeErreur= TypeErreur.TECHNIQUE;

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
	
	private CompteTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}
}
