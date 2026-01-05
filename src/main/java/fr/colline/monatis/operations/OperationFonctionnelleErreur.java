package fr.colline.monatis.operations;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum OperationFonctionnelleErreur implements MonatisErreur {


	/**
			"Les opération du type '%s' ne sont pas compatibles en dépense avec le compte d'identifiant '%s'"),
	 */
	TYPE_OPERATION_ET_COMPTE_DEPENSE_INCOMPATIBLES(
			"Les opération du type '%s' ne sont pas compatibles en dépense avec le compte d'identifiant '%s'"),
	
	/**
			"Les opération du type '%s' ne sont pas compatibles en recette avec le compte d'identifiant '%s'"),
	 */
	TYPE_OPERATION_ET_COMPTE_RECETTE_INCOMPATIBLES(
			"Les opération du type '%s' ne sont pas compatibles en recette avec le compte d'identifiant '%s'"),
	
	/**
			"Au moins une ligne de détail est requise pour chaque opération"),
	 */
	OPERATION_AU_MOINS_UN_DETAIL_REQUIS(
			"Au moins une ligne de détail est requise pour chaque opération"),
	
	/**
			"Le numéro de ligne de détail %d est dupliqué"),
	 */
	OPERATION_LISTE_DETAIL_NUMERO_DUPLIQUE(
			"Le numéro de ligne de détail %d est dupliqué"),
	
	/**
			"La somme des montants des détails de l'opération (%s) n'est pas égale au montant total de l'opération (%s)"),	
	 */
	OPERATION_LISTE_DETAIL_SOMME_MONTANTS_ERRONEE(
			"La somme des montants des détails de l'opération (%s) n'est pas égale au montant total de l'opération (%s)"),

	/**
			"Le solde du compte interne d'identifiant '%s' est déjà à jour"),
	 */
	CORRECTION_SOLDE_INUTILE(
			"Le solde du compte interne d'identifiant '%s' est déjà à jour"), 

	;
	
	private final TypeDomaine typeDomaine = TypeDomaine.OPERATION;
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
	
	private OperationFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}

}
