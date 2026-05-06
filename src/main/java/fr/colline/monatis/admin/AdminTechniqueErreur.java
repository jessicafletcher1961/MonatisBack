package fr.colline.monatis.admin;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum AdminTechniqueErreur implements MonatisErreur {

	/**
			"Un problème technique est survenu lors de la sauvegarde de la base de données"),
	 */
	PROBLEME_SAUVEGARDE(
			"Un problème technique est survenu lors de la sauvegarde de la base de données"),

	/**
			"Un problème technique est survenu lors de la création du script de mise à jour des séquences %s"), 
	 */
	PROBLEME_SAUVEGARDE_SEQUENCES(
			"Un problème technique est survenu lors de la création du script de mise à jour des séquences %s"), 

	/**
			"Un problème technique est survenu lors de suppression des données avant restauration du fichier de sauvegarde '%s'"), 
	 */
	PROBLEME_VIDAGE_COMPLET(
			"Un problème technique est survenu lors de suppression des données avant restauration du fichier de sauvegarde '%s'"), 
	
	/**
			"Un problème technique est survenu lors de la restauration du fichier de sauvegarde '%s'"), 
	 */
	PROBLEME_RESTAURATION(
			"Un problème technique est survenu lors de la restauration du fichier de sauvegarde '%s'"),
	
	/**
			"Un problème technique est survenu lors de la désactivation des contraintes d'intégrité de la base"),
	 */
	ERREUR_DESACTIVATION_CONTRAINTES(
			"Un problème technique est survenu lors de la désactivation des contraintes d'intégrité de la base"),
	
	/**
			"Un problème technique est survenu lors de la réactivation des contraintes d'intégrité de la base"),
	 */
	ERREUR_REACTIVATION_CONTRAINTES(
			"Un problème technique est survenu lors de la réactivation des contraintes d'intégrité de la base"),

	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.ADMIN;
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
	
	private AdminTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}

}
