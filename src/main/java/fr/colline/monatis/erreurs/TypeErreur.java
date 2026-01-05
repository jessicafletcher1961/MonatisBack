package fr.colline.monatis.erreurs;

public enum TypeErreur {

	FONCTIONNELLE("FCNT", "fonctionnelle", "Erreur fonctionnelle"),
	TECHNIQUE("TECH", "technique", "Erreur technique"),
	CONTROLE("CTRL", "controle", "Contrôle validité demande"),
	
	;
	
	private String code;
	
	private String prefixe;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getPrefixe() {
		return prefixe;
	}
	
	public String getLibelle() {
		return libelle;
	}

	private TypeErreur(String code, String prefixe, String libelle) {
		this.code = code;
		this.prefixe = prefixe;
		this.libelle = libelle;
	}
}
