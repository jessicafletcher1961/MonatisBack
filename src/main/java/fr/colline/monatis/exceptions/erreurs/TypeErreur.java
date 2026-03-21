package fr.colline.monatis.exceptions.erreurs;

public enum TypeErreur {

	PROGRAMMATION ("PROG", "Erreur de programmation", "erreur.programmation"),
	FONCTIONNELLE ("FONC", "Erreur fonctionnelle", "erreur.fonctionnelle"),
	TECHNIQUE ("TECH", "Erreur technique", "erreur.technique"),
	CONTROLE ("CTRL", "Erreur requête", "erreur.requete"),

	;
	
	private String code;
	
	private String libelle;
	
	private String prefixe;

	protected String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}
	
	public String getPrefixe() {
		return prefixe;
	}

	private TypeErreur(String code, String libelle, String prefixe) {
		this.code = code;
		this.libelle = libelle;
		this.prefixe = prefixe;
	}
	
	public static TypeErreur findByCode(String code) {
		if ( code != null && !code.isBlank() ) {
			for ( TypeErreur value : TypeErreur.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
