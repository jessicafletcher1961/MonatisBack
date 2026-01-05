package fr.colline.monatis.operations;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

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
			"Un problème technique est survenu lors de la recherche des opérations de recette du compte d'ID %s entre les dates %s et %s"),
	 */
	RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des opérations de recette du compte d'ID %s entre les dates %s et %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des opérations de recette du compte d'ID %s jusqu'au %s"),
	 */
	RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_AVANT_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des opérations de recette du compte d'ID %s jusqu'au %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s depuis le %s"),
	 */
	RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_DEPUIS_DATE_DEBUT(
			"Un problème technique est survenu lors de la recherche des opérations de recette du compte d'ID %s depuis le %s"),

	/**
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s entre les dates %s et %s"),
	 */
	RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s entre les dates %s et %s"), 
	
	/**
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s jusqu'au %s"),
	 */
	RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_JUSQUE_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s jusqu'au %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des lignes d'opérations correspondant à la référence d'ID %s jusqu'au %s"),
	 */
	RECHERCHE_OPERATION_LIGNE_PAR_REFERENCE_ID_JUSQUE_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des lignes d'opérations correspondant à la référence d'ID %s jusqu'au %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s depuis le %s"),
	 */
	RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_DEPUIS_DATE_DEBUT(
			"Un problème technique est survenu lors de la recherche des opérations de dépense du compte d'ID %s depuis le %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des opérations du compte d'ID %s"),
	 */
	RECHERCHE_OPERATION_PAR_COMPTE_ID(
			"Un problème technique est survenu lors de la recherche des opérations du compte d'ID %s"),
	
	/**
			"Un problème technique est survenu lors de la recherche des opérations du compte d'ID %s depuis le %s"),
	 */
	RECHERCHE_OPERATION_PAR_COMPTE_ID_DEPUIS_DATE_DEBUT(
			"Un problème technique est survenu lors de la recherche des opérations du compte d'ID %s depuis le %s"),
	
	/**
			"Cette opération de numéro '%s' est du type '%s' et ne peut donc être supprimée de cette façon"),
	 */
	SUPPRESSION_SOLDE_MAUVAIS_TYPE_OPERATION(
			"Cette opération de numéro '%s' est du type '%s' et ne peut donc être supprimée de cette façon"),

	/**
			"Un problème technique est survenu lors de la recherche des opérations du compte d'ID %s entre les dates %s et %s"), 
	 */
	RECHERCHE_OPERATION_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des opérations du compte d'ID %s entre les dates %s et %s"), 

	/**
			"Un problème technique est survenu lors de la recherche des lignes d'opérations correspondant à la référence d'ID %s jusqu'au %s"),
	 */
	RECHERCHE_OPERATION_PAR_COMPTE_ID_JUSQUE_DATE_FIN(
			"Un problème technique est survenu lors de la recherche des lignes d'opérations correspondant à la référence d'ID %s jusqu'au %s"),


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
