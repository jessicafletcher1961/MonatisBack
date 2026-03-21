package fr.colline.monatis.comptes.model;

public enum TypeCompte {
	
	INTERNE ("INTERNE", "Compte appartenant à l'un au moins des membres du foyer"),
	EXTERNE ("EXTERNE", "Compte n'appartenant à aucun des membres du foyers"),
	TECHNIQUE ("TECHNIQUE", "Compte technique permettant d'enregistrer la contrepartie des diverses opérations de correction des soldes");
	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeCompte(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}
	
	public static TypeCompte findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypeCompte value : TypeCompte.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
