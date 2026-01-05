package fr.colline.monatis.comptes.model;

public enum TypeFonctionnement {

	COURANT ("COURANT", "Compte servant à payer des dépenses ou à encaisser des recettes et ne donnant pas lieu à des plus ou moins values : compte bancaire, porte monnaie..." ),
	EPARGNE ("EPARGNE", "Compte d'épargne sur lequel on effectue des dépôts ou des retraits, et donnant lieu à des versements d'intérêts de la part de la banque : livret A... "),
	PATRIMOINE ("PATRIMOINE", "Compte permettant de suivre le patrimoine du foyer et donnant lieu à des plus ou moins values en fonction de la Bourse ou d'un marché spécifique : comptes-titre, assurances-vie, PEA, habitation principale, collection de timbres, oeuvres d'art..."),
	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeFonctionnement (String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}
	
	public static TypeFonctionnement findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypeFonctionnement value : TypeFonctionnement.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
