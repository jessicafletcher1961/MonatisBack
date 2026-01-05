package fr.colline.monatis.erreurs;

public enum GeneriqueTechniqueErreur implements MonatisErreur {

	/**
	TYPE_NON_GERE("Cette valeur du type '%s' (%s-%s) n'est pas encore gérée dans cette méthode"), 
	 */
	TYPE_NON_GERE("Cette valeur du type '%s' (%s-%s) n'est pas encore gérée dans cette méthode"), 

	/**
	CREATION_AVEC_ID("Lors de la création d'une entité, le champ ID doit être à null"),
	 */
	CREATION_AVEC_ID("Lors de la création d'une entité, le champ ID doit être à null"),

	/**
	METHODE_INTERDITE("L'utilisation de cette méthode est interdite dans ce contexte"),
	 */
	METHODE_INTERDITE("L'utilisation de cette méthode est interdite dans ce contexte"),
	
	/**
	CLASSE_NON_TRAITEE("La classe '%s' n'est pas encore gérée dans cette méthode"),
	 */
	CLASSE_NON_TRAITEE("La classe '%s' n'est pas encore gérée dans cette méthode"),

	;

	private final TypeErreur typeErreur = TypeErreur.TECHNIQUE;
	private final TypeDomaine typeDomaine = TypeDomaine.GENERIQUE;

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

	private GeneriqueTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}
}
