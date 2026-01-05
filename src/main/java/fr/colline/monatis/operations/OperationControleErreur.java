package fr.colline.monatis.operations;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum OperationControleErreur implements MonatisErreur {

	/**
			"Le numéro de l'opération est obligatoire"),
	 */
	NUMERO_OBLIGATOIRE(
			"Le numéro de l'opération est obligatoire"),

	/**
	"Aucune opération ne correspond au numéro '%s'"), 
	 */
	NON_TROUVE_PAR_NUMERO(
			"Aucune opération ne correspond au numéro '%s'"), 

	/**
			"Le numéro d'opération '%s' a déjà été utilisé"),
	 */
	NUMERO_DEJA_UTILISE(
			"Le numéro d'opération '%s' a déjà été utilisé"),

	/**
			"L'opération numéro '%s' n'a pas de ligne de détail n° %s"),
	 */
	NON_TROUVE_PAR_NUMERO_LIGNE(
			"L'opération numéro '%s' n'a pas de ligne de détail n° %s"),
	
	/**
			"Le type d'opération '%s - %s' ne peut être géré de cette façon. Voir les évaluations."),
	 */
	TYPE_OPERATION_INTERDIT(
			"Le type d'opération '%s - %s' ne peut être géré de cette façon. Voir les évaluations."),

	;

	private OperationControleErreur(String pattern) {
		this.pattern = pattern;
	}
	
	private final TypeDomaine typeDomaine = TypeDomaine.OPERATION;
	private final TypeErreur typeErreur = TypeErreur.CONTROLE;

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

}
