package fr.colline.monatis.exceptions;

public enum GeneriqueControleErreur implements MonatisErreur {

	/**
			"Le libellé est obligatoire"),
	 */
	LIBELLE_OBLIGATOIRE(
			"Le libellé est obligatoire"),

	/**
			"La date est obligatoire"),
	 */
	DATE_OBLIGATOIRE(
			"La date est obligatoire"),

	/**
			"La date '%s' est invalide. Le format attendu est 'YYYY-MM-DD'"),
	 */
	DATE_INVALIDE(
			"La date '%s' est invalide. Le format attendu est 'YYYY-MM-DD'"),

	/**
			"Le montant est obligatoire"),
	 */
	MONTANT_OBLIGATOIRE(
			"Le montant est obligatoire"),

	/**
			"Nombre obligatoire"),
	 */
	NOMBRE_OBLIGATOIRE(
			"Nombre obligatoire"),
	
	/**
			"Le nombre %s ne devrait pas être inférieur à %s"),
	 */
	NOMBRE_TROP_PETIT(
			"Le nombre %s ne devrait pas être inférieur à %s"),

	/**
			"Le nombre %s ne devrait pas être supérieur à %s"),
	 */
	NOMBRE_TROP_GRAND(
			"Le nombre %s ne devrait pas être supérieur à %s"),

	/**
			"Aucune typologie de type '%s' ne correspond au code '%s'"),
	 */
	NON_TROUVE_PAR_CODE(
			"Aucune typologie de type '%s' ne correspond au code '%s'"),
	
	/**
			"Le booléen est obligatoire"),
	 */
	BOOLEEN_OBLIGATOIRE(
			"Le booléen est obligatoire"),
	
	/**
			"Le numéro de page est obligatoire"),
	 */
	NUMERO_PAGE_OBLIGATOIRE(
			"Le numéro de page est obligatoire"),
	
	/**
			"La taille de la page est obligatoire"),
	 */
	TAILLE_PAGE_OBLIGATOIRE(
			"La taille de la page est obligatoire"),

	
	;
	

	private final TypeDomaine typeDomaine = TypeDomaine.GENERIQUE;
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
	
	private GeneriqueControleErreur(String pattern) {
		this.pattern = pattern;
	}

}
