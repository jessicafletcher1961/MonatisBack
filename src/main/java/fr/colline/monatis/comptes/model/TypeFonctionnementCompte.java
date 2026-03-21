package fr.colline.monatis.comptes.model;

public enum TypeFonctionnementCompte {

	/**
	COURANT ("COURANT", "Compte en numéraire utilisé pour les dépenses et les recettes courantes du foyer"),
	 */
	COURANT ("COURANT", "Compte en numéraire utilisé pour les dépenses et les recettes courantes du foyer"),
	
	/**
	PLACEMENT ("PLACEMENT", "Compte d'investissement du foyer donnant lieu à des plus ou moins values ou des versements d'intérêts (ex: titres, livrets, assurances-vie...)"),
	 */
	PLACEMENT ("PLACEMENT", "Compte d'investissement du foyer donnant lieu à des plus ou moins values ou des versements d'intérêts (ex: titres, livrets, assurances-vie...)"),

	/**
	MOBILIER ("MOBILIER", "Bien du foyer dont la valeur dépend d'un marché (ex: collections, oeuvres d'art,...)"),
	 */
	MOBILIER ("MOBILIER", "Bien du foyer dont la valeur dépend d'un marché spécifique (ex: collections, oeuvres d'art, bijoux,...)"),

	/**
	IMMOBILIER ("IMMOBILIER", "Bien immobilier (ex: habitation principale, résidence secondaire, appartement loué à des tiers...)"),
	 */
	IMMOBILIER ("IMMOBILIER", "Bien immobilier (ex: habitation principale, résidence secondaire, appartement loué à des tiers...)"),

	/**
	CONTREPARTIE("CONTREPART", "Compte technique d'enregistrement la contrepartie de corrections de soldes diverses"),
	 */
	CONTREPARTIE("CONTREPART", "Compte technique d'enregistrement la contrepartie de corrections de soldes diverses"),
	
	/**
	TIERS("TIERS", "Compte de tiers (extérieur au foyer)"),
	 */
	TIERS("TIERS", "Compte de tiers (extérieur au foyer)"),
	;	
	
	private String code;
	
	private String libelle;
	
	private TypeFonctionnementCompte(String code, String libelle) {
	
		this.code = code;
		this.libelle = libelle;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public static TypeFonctionnementCompte findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			
			for ( TypeFonctionnementCompte value : TypeFonctionnementCompte.values() ) {
			
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		
		return null;
	}
}
