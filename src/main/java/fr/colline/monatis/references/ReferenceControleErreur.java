package fr.colline.monatis.references;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum ReferenceControleErreur implements MonatisErreur {

	/**
			"Le nom est obligatoire"), 
	 */
	NOM_OBLIGATOIRE(
			"Le nom est obligatoire"), 
	
	/**
			"Aucune référence de type '%s' et de nom '%s' n'a été trouvée"),
	 */
	NON_TROUVE_PAR_NOM(
			"Aucune référence de type '%s' et de nom '%s' n'a été trouvée"),

	/**
			"Le nom '%s' est déjà utilisé par une référence de type '%s'" ),
	 */
	NOM_DEJA_UTILISE(
			"Le nom '%s' est déjà utilisé par une référence de type '%s'" ),
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.REFERENCE;
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
	
	private ReferenceControleErreur(String pattern) {
		this.pattern = pattern;
	}

}
