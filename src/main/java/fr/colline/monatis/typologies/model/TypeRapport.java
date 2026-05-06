package fr.colline.monatis.typologies.model;

public enum TypeRapport {

	ETAT_PAR_COMPTE("ETAT-CPT", "Etat des comptes")

	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeRapport(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}
	
	public static TypeRapport findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypeRapport value : TypeRapport.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
