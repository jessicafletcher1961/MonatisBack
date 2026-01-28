package fr.colline.monatis.evaluations;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum EvaluationControleErreur implements MonatisErreur {

	/**
			"La clé de l'évaluation est obligatoire"),
	 */
	CLE_OBLIGATOIRE(
			"La clé de l'évaluation est obligatoire"),

	/**
			"Aucune évaluation ne correspond à la clé '%s'"), 
	 */
	NON_TROUVE_PAR_CLE(
			"Aucune évaluation ne correspond à la clé '%s'"), 

	/**
			"La clé d'évolution '%s' a déjà été utilisée"),
	 */
	CLE_DEJA_UTILISE(
			"La clé d'évolution '%s' a déjà été utilisée"),
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.EVALUATION;
	private final TypeErreur typeErreur = TypeErreur.CONTROLE;

	String pattern;
	
	private EvaluationControleErreur(String pattern) {
		this.pattern = pattern;
	}
	
	
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
