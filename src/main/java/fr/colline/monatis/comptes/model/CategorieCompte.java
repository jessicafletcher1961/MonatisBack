package fr.colline.monatis.comptes.model;

public enum CategorieCompte {

	INTERNE("INTERNE", "Compte interne dont l'un au moins des titulaires appartient au foyer"),
	TIERS("TIERS", "Compte de tiers avec lesquels des transactions de dépense ou de recette sont établies"),
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private CategorieCompte(String code, String libelle) {
		
		this.code = code;
		this.libelle = libelle;
	}
	
	public static CategorieCompte findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			
			for ( CategorieCompte value : CategorieCompte.values() ) {
			
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		
		return null;
	}
}
