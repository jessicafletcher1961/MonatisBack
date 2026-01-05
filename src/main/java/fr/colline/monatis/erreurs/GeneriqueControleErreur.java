package fr.colline.monatis.erreurs;

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
			"Le montant est obligatoire"),
	 */
	MONTANT_OBLIGATOIRE(
			"Le montant est obligatoire"),

	/**
			"Aucune typologie de type '%s' ne correspond au code '%s'"),
	 */
	NON_TROUVE_PAR_CODE(
			"Aucune typologie de type '%s' ne correspond au code '%s'"),
	
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
