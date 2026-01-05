package fr.colline.monatis.operations.model;

public enum TypeOperation {

	TRANSFERT ("TRANSFERT", "Mouvement depuis un compte du foyer vers un autre compte du foyer"),
	DEPOT ("DEPOT", "Mouvement depuis un compte courant du foyer vers un compte d'épargne du foyer"),
	ACHAT ("ACHAT", "Mouvement depuis un compte courant du foyer vers un compte de patrimoine du foyer"),
	DEPENSE ("DEPENSE", "Mouvement depuis un compte courant du foyer vers un compte n'appartenant pas au foyer (externe)"),

	RETRAIT ("RETRAIT", "Mouvement depuis un compte d'épargne du foyer vers un compte courant du foyer"),

	VENTE ("VENTE", "Mouvement depuis un compte de patrimoine du foyer vers un compte courant du foyer"),

	RECETTE ("RECETTE", "Mouvement depuis un compte n'appartenant pas au foyer (externe) vers un compte courant du foyer"),

	FRAIS ("FRAIS", "Frais bancaires, agios, intérêts d'emprunts ou frais de gestion appliqués à un compte du foyer"),
	GAIN ("GAIN", "Intérêts ou dividendes produits par un compte du foyer"),

	PLUS_SOLDE ("SOLDE+", "Opération fictive de recette permettant d'augmenter artificiellement le solde d'un compte du foyer"),
	MOINS_SOLDE ("SOLDE-", "Opération fictive de dépense permettant de diminuer artificiellement le solde d'un compte du foyer"),
	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeOperation(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}

	public static TypeOperation findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypeOperation value : TypeOperation.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
