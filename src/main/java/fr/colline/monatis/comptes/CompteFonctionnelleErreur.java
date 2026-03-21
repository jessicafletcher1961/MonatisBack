package fr.colline.monatis.comptes;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum CompteFonctionnelleErreur implements MonatisErreur {

	/**
			"Suppression du compte d'identifiant '%s' : ce compte ne peut être supprimé car il est associé à %d opérations"),
	 */
	SUPPRESSION_COMPTE_AVEC_OPERATION(
			"Suppression du compte d'ID %s : ce compte ne peut être supprimé car il est associé à %d opérations"),

	/**
			"Suppression du compte d'ID %s : ce compte ne peut être supprimé car il est associé à %d évaluations"),
	 */
	SUPPRESSION_COMPTE_AVEC_EVALUATION(
			"Suppression du compte d'ID %s : ce compte ne peut être supprimé car il est associé à %d évaluations"),
	 
	/**
			"Un compte d'identifiant '%s' existe déjà mais ce n'est pas un compte technique (c'est un compte de type '%s')"),
	 */
	IDENTIFIANT_COMPTE_TECHNIQUE_DEJA_UTILISE(
			"Un compte d'identifiant '%s' existe déjà mais ce n'est pas un compte technique (c'est un compte de type '%s')"),

	/**
			"La date de clôture %s du compte d'identifiant '%s' est invalide car il existe %d opérations enregistrées après cette date"),
	 */
	OPERATIONS_APRES_DATE_CLOTURE(
			"La date de clôture %s du compte d'identifiant '%s' est invalide car il existe %d opérations enregistrées après cette date"),
	
	/**
			"La date de clôture %s du compte d'identifiant '%s' est invalide car elle est antérieure à la date du solde initial fixée au %s"),
	 */
	DATE_CLOTURE_AVANT_DATE_SOLDE_INITIAL(
			"La date de clôture %s du compte d'identifiant '%s' est invalide car elle est antérieure à la date du solde initial fixée au %s"),
	
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.COMPTE;
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

	private CompteFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}

}
