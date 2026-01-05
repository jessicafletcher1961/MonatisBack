package fr.colline.monatis.rapports;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum RapportControleErreur implements MonatisErreur {

	/**
			"Pour définir un intervalle de temps, la date de fin de l'intervalle (ici %s) doit être postérieure ou égale à la date de début (ici %s)"),
	 */
	DATE_FIN_AVANT_DATE_DEBUT(
			"Pour définir un intervalle de temps, la date de fin de l'intervalle (ici %s) doit être postérieure ou égale à la date de début (ici %s)"),
	
	/**
			"Seuls les comptes du foyer (comptes de type %s) peuvent faire l'objet d'une évaluation, et le compte d'identifiant '%s' n'en fait pas partie (c'est un compte de type %s)"),
	 */
	RECHERCHE_EVALUATION_SUR_COMPTE_PAS_INTERNE(
			"Seuls les comptes du foyer (comptes de type %s) peuvent faire l'objet d'une évaluation, et le compte d'identifiant '%s' n'en fait pas partie (c'est un compte de type %s)"),
	
	/**
			"Aucune évaluation n'a été enregistrée pour ce compte d'identifiant '%s'"),
	 */
	AUCUNE_EVALUATION_ENREGISTREE_SUR_CE_COMPTE(
			"Aucune évaluation n'a été enregistrée pour ce compte d'identifiant '%s'"),
	
	/**
			"Aucun budget n'a été défini en date du %s pour la référence de type '%s' et de nom '%s'"),
	 */
	AUCUN_BUDGET_DEFINI_A_CETTE_DATE(
			"Aucun budget n'a été défini en date du %s pour la référence de type '%s' et de nom '%s'"),
	
	/**
			"Le type de période '%s' n'est pas compatible avec la recherche d'historique des plus ou moins values"),
	 */
	RECHERCHE_HISTORIQUE_PLUS_MOINS_VALUE_SUR_PERIODE_INVALIDE(
			"Le type de période '%s' n'est pas compatible avec la recherche d'historique des plus ou moins values"), 
	
	/**
			"La génération PDF du document a échoué"),
	 */
	GENERATION_PDF_EN_ECHEC(
			"La génération PDF du document a échoué"),
	
	;
	
	private final TypeDomaine typeDomaine = TypeDomaine.RAPPORT;
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

	private RapportControleErreur(String pattern) {
		this.pattern = pattern;
	}

}
