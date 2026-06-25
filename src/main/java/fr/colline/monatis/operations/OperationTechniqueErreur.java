package fr.colline.monatis.operations;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum OperationTechniqueErreur implements MonatisErreur {

	/**
			"Un problème technique est survenu lors de la recherche de l'opération d'ID %s"),
	 */
	RECHERCHE_PAR_ID(
			"Un problème technique est survenu lors de la recherche de l'opération d'ID %s"),

	/**
			"Un problème technique est survenu lors de la vérification de l'existence de l'opération d'ID %s"),
	 */
	EXISTENCE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de l'existence de l'opération d'ID %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche de l'opération numéro '%s'"),
	 */
	RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la recherche de l'opération numéro '%s'"),
	
	/**
			"Un problème technique est survenu lors de la vérification de l'existence de l'opération numéro '%s'"),
	 */
	EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la vérification de l'existence de l'opération numéro '%s'"),

	/**
			"Un problème technique est survenu lors de la recherche de toutes les opérations"),
	 */
	RECHERCHE_TOUS(
			"Un problème technique est survenu lors de la recherche de toutes les opérations"),
	
	/**
			"Un problème technique est survenu lors de la suppression de toutes les opérations"),
	 */
	SUPPRESSION_TOUS(
			"Un problème technique est survenu lors de la suppression de toutes les opérations"),

	/**
			"Un problème technique est survenu lors de l'enregistrement de l'opération numéro '%s'"),
	 */
	ENREGISTREMENT(
			"Un problème technique est survenu lors de l'enregistrement de l'opération numéro '%s'"),
	
	/**
			"Un problème technique est survenu lors de la suppression de l'opération numéro '%s"),
	 */
	SUPPRESSION(
			"Un problème technique est survenu lors de la suppression de l'opération numéro '%s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des lignes d'opérations correspondant à la référence d'ID %s entre les dates %s et %s"),
	 */
	RECHERCHE_OPERATION_LIGNE_PAR_REFERENCE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des lignes d'opérations correspondant à la référence d'ID %s entre les dates %s et %s"),

	/**
			"Un problème technique est survenu lors de la recherche des opérations du compte d'identifiant '%s' entre les dates %s et %s"), 
	 */
	RECHERCHE_OPERATION_PAR_COMPTE_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des opérations du compte d'identifiant '%s' entre les dates %s et %s"),

	/**
			"Un problème technique est survenu lors de la recherche paginées d'opérations filtrées sur un exemple de libelle"),
	 */
	RECHERCHE_OPERATION_PAR_EXEMPLE(
			"Un problème technique est survenu lors de la recherche paginées d'opérations filtrées sur un exemple de libelle"),

	/**
			"Un problème technique est survenu lors de la recherche paginées d'opérations filtrées par critères"), 
	 */
	RECHERCHE_OPERATION_PAR_PAGE_FILTREES(
			"Un problème technique est survenu lors de la recherche paginées d'opérations filtrées par critères"), 

	/**
			"Un problème technique est survenu lors de la recherche des opérations visibles"), 
	 */
	RECHERCHER_OPERATIONS_VISIBLES(
			"Un problème technique est survenu lors de la recherche des opérations visibles"),
	
	/**
			"Un problème technique est survenu lors de l'analyse CSV du fichier '%s'"), 
	 */
	FICHIER_NON_PARSABLE(
			"Un problème technique est survenu lors de l'analyse CSV du fichier '%s'"), 
	
	/**
			"Un problème technique est survenu lors de la lecture du fichier '%s'"), 
	 */
	FICHIER_NON_LISIBLE(
			"Un problème technique est survenu lors de la lecture du fichier '%s'"),
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.OPERATION;
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
	
	private OperationTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}

}
