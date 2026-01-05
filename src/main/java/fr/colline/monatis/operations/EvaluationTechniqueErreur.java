package fr.colline.monatis.operations;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum EvaluationTechniqueErreur implements MonatisErreur {

	/**
	"Un problème technique est survenu lors de la recherche de l'évaluation d'ID %s"),
	 */
	RECHERCHE_PAR_ID(
			"Un problème technique est survenu lors de la recherche de l'évaluation d'ID %s"),

	/**
	"Un problème technique est survenu lors de la vérification de l'existence de l'évaluation d'ID %s"),
	 */
	EXISTENCE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de l'existence de l'évaluation d'ID %s"),

	/**
	"Un problème technique est survenu lors de la recherche de l'évaluation numéro '%s'"),
	 */
	RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la recherche de l'évaluation numéro '%s'"),

	/**
	"Un problème technique est survenu lors de la vérification de l'existence de l'évaluation numéro '%s'"),
	 */
	EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la vérification de l'existence de l'évaluation numéro '%s'"),

	/**
	"Un problème technique est survenu lors de la recherche de toutes les évaluations"),
	 */
	RECHERCHE_TOUS(
			"Un problème technique est survenu lors de la recherche de toutes les évaluations"),

	/**
	"Un problème technique est survenu lors de la suppression de toutes les évaluations"),
	 */
	SUPPRESSION_TOUS(
			"Un problème technique est survenu lors de la suppression de toutes les évaluations"),

	/**
	"Un problème technique est survenu lors de l'enregistrement de l'évaluation numéro '%s'"),
	 */
	ENREGISTREMENT(
			"Un problème technique est survenu lors de l'enregistrement de l'évaluation numéro '%s'"),

	/**
	"Un problème technique est survenu lors de la suppression de l'évaluation numéro '%s"),
	 */
	SUPPRESSION(
			"Un problème technique est survenu lors de la suppression de l'évaluation numéro '%s"), 
	
	/**
			"Un problème technique est survenu lors de la recherche des évaluation du compte interne d'ID %s"),
	 */
	RECHERCHE_PAR_COMPTE_INTERNE_ID(
			"Un problème technique est survenu lors de la recherche des évaluation du compte interne d'ID %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des évaluation du compte interne d'ID %s entre les dates %s et %s"),
	 */
	RECHERCHE_PAR_COMPTE_INTERNE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des évaluation du compte interne d'ID %s entre les dates %s et %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche de la première évaluation du compte interne d'ID %s depuis le %s"),
	 */
	RECHERCHE_PAR_COMPTE_INTERNE_ID_DEPUIS_DATE_CIBLE(
			"Un problème technique est survenu lors de la recherche de la première évaluation du compte interne d'ID %s depuis le %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche de la dernière évaluation du compte interne d'ID %s jusqu'au %s"),
	 */
	RECHERCHE_PAR_COMPTE_INTERNE_ID_JUSQUE_DATE_CIBLE(
			"Un problème technique est survenu lors de la recherche de la dernière évaluation du compte interne d'ID %s jusqu'au %s"),

	;

	private final TypeDomaine typeDomaine = TypeDomaine.EVALUATION;
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
	
	private EvaluationTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}

}
