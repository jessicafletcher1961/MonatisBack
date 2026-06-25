package fr.colline.monatis.typologies.model;

public enum TypeBudget {

	SOLDE_PREVU_POSITIF("SOLDE+", "Pour cette référence, pendant cette période, on prévoit plus de recettes que de dépenses"),
	SOLDE_PREVU_NEGATIF("SOLDE-", "Pour cette référence, pendant cette période, on prévoit autant ou plus de dépenses que de recettes"),
	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeBudget(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}
	
	public static TypeBudget findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypeBudget value : TypeBudget.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}

}
