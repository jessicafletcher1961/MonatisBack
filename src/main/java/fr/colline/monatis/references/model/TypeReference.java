package fr.colline.monatis.references.model;

public enum TypeReference {

	BANQUE("BANQUE", "Banque"),
	BENEFICIAIRE("BENEF", "Bénéficiaire"),
	CATEGORIE("CATEGORIE", "Catégorie"),
	SOUS_CATEGORIE("SOUS-CAT", "Sous-catégorie"),
	TITULAIRE("TITULAIRE", "Titulaire"),
	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeReference(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}
	
	public static TypeReference findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			
			for ( TypeReference value : TypeReference.values() ) {
			
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
