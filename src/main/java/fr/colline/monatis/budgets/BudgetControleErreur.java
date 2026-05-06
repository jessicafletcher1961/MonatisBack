package fr.colline.monatis.budgets;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum BudgetControleErreur implements MonatisErreur {

	/**
			"Aucun budget n'existe pour la référence de type '%s' et de nom '%s' pour la date %s"),
	 */
	NON_TROUVE_PAR_REFERENCE_ID_ET_DATE(
			"Aucun budget n'existe pour la référence de type '%s' et de nom '%s' pour la date %s"),
	
	/**
			"Un ou plusieurs budgets existent déjà pour la référence de type '%s' et de nom '%s'. Utiliser la reconduction."),
	 */
	CREATION_AVEC_HISTORIQUE(
			"Un ou plusieurs budgets existent déjà pour la référence de type '%s' et de nom '%s'. Utiliser la reconduction."),
	
	/**
			"Aucun budget n'existe encore pour la référence de type '%s' de nom '%s'. Utiliser la création."),
	 */
	RECONDUCTION_SANS_HISTORIQUE(
			"Aucun budget n'existe encore pour la référence de type '%s' de nom '%s'"),
	
	/**
			"Aucun budget avec la cle '%s' n'a été trouvé"),
	 */
	NON_TROUVE_PAR_CLE(
			"Aucun budget avec la cle '%s' n'a été trouvé"),
	
	;
	
	private final TypeDomaine typeDomaine = TypeDomaine.BUDGET;
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

	private BudgetControleErreur(String pattern) {
		this.pattern = pattern;
	}

}
