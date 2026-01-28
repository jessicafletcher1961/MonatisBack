package fr.colline.monatis.evaluations;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum EvaluationFonctionnelleErreur  implements MonatisErreur {

	/**
			"Il ne peut y avoir qu'une seule évaluation par jour et par compte interne, or une évaluation de clé '%' existe déjà à la date %s"),
	 */
	UNE_SEULE_EVALUATION_PAR_JOUR_PAR_COMPTE_INTERNE(
			"Il ne peut y avoir qu'une seule évaluation par jour et par compte interne, or une évaluation de clé '%' existe déjà à la date %s"),
	;
	
	private final TypeDomaine typeDomaine = TypeDomaine.EVALUATION;
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
	
	private EvaluationFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}
}
