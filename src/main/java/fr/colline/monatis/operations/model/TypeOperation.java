package fr.colline.monatis.operations.model;

public enum TypeOperation {

	/** compte courant <-> compte courant */
	TRANSFERT ("TRANSFERT", "Transfert entre comptes courants (ex: retrait de liquide au distributeur, ou dépôt produit vente en liquide sur compte bancaire"),

	/** compte tiers -> compte courant */
	RECETTE ("RECETTE", "Encaissement du versement d'un tiers"),
	
	/** compte courant -> compte tiers */
	DEPENSE ("DEPENSE", "Versement à un tiers"),
	
	/** compte immobilisation -> compte courant */
	VENTE ( "VENTE", "Vente d'un bien"),

	/** compte courant -> compte immobilisation */
	ACHAT ( "ACHAT", "Achat d'un bien"),
	
	/** compte investissement -> compte ajustement */
	MOINS_VALUE ("VALUE-", "Enregistrement d'une moins-value"),

	/** compte ajustement -> compte investissement */
	PLUS_VALUE ("VALUE+", "Enregistrement d'une plus-value"),
	
	/** compte courant -> compte ajustement */
	MOINS_AJUSTEMENT ("AJUST-", "Ajustement (-): diminution du solde d'un compte"),
	
	/** compte ajustement -> compte courant */
	PLUS_AJUSTEMENT ("AJUST+", "Ajustement (+): augmentation du solde d'un compte"),
	
	;
	
	private String code;
	
	private String libelle;
	
	private TypeOperation(String code, String libelle) {
		
		this.code = code;
		this.libelle = libelle;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
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
