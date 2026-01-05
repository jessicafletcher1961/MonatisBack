package fr.colline.monatis.erreurs;

public enum TypeDomaine {

	BUDGET ("BUD", "budget", "Budgets"),
	REFERENCE ("REF", "reference", "Références (générique)"),
	COMPTE("CPT", "compte", "Comptes (générique)"),
	EVALUATION("EVL", "evaluation", "Evaluations"),
	OPERATION("OPE", "operation", "Opérations"),
	RAPPORT("RAP", "rapport", "Elaboration des rapports"),
	GENERIQUE("GEN", "generique", "Plusieurs domaines"),
	
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

	private TypeDomaine(String code, String prefixe, String libelle) {

		this.code = code;
		this.prefixe = prefixe;
		this.libelle = libelle;
	}
}
