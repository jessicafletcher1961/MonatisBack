package fr.colline.monatis.emprunts;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum EmpruntControleErreur implements MonatisErreur {


	/**
			"Aucun emprunt ne correspond à la clé '%s'"), 
	 */
	NON_TROUVE_PAR_CLE(
			"Aucun emprunt ne correspond à la clé '%s'"), 

	/**
			"La clé d'emprunt '%s' a déjà été utilisée"),
	 */
	CLE_DEJA_UTILISE(
			"La clé d'emprunt '%s' a déjà été utilisée"),

	;

	private final TypeDomaine typeDomaine = TypeDomaine.EMPRUNT;
	private final TypeErreur typeErreur = TypeErreur.CONTROLE;

	String pattern;
	
	private EmpruntControleErreur(String pattern) {
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
