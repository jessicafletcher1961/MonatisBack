package fr.colline.monatis.comptes.model;

public enum TypeFonctionnement {

	COURANT ("COURANT", "Compte servant à payer des dépenses ou à encaisser des recettes et ne donnant pas lieu à des plus ou moins values : compte bancaire, porte monnaie..." ),
	FINANCIER ("FINANCIER", "Compte financier de placement/investissement : comptes d'épargne, comptes titre, assurances-vie, PEA, ... "),
	BIEN ("BIEN", "Compte permettant de suivre la valeur d'un bien du foyer en fonction d'un marché : habitation principale, collection de timbres, collection d'oeuvres d'art, commode Louis XV..."),
	
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
