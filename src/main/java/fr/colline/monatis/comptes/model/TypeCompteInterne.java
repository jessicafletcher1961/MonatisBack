package fr.colline.monatis.comptes.model;

public enum TypeCompteInterne {

	/**
	BANCAIRE ("BANCAIRE", false, false, "Compte bancaire utilisé pour les échanges avec des tiers", TypeFonctionnementCompte.COURANT),
	 */
	BANCAIRE ("BANCAIRE", false, false, "Compte bancaire utilisé pour les échanges avec des tiers", TypeFonctionnementCompte.COURANT),
	
	/**
	LIQUIDE("LIQUIDE", false, false, "Argent liquide", TypeFonctionnementCompte.COURANT),
	 */
	LIQUIDE("LIQUIDE", false, false, "Argent liquide", TypeFonctionnementCompte.COURANT),

	/**
	LIVRET ("LIVRET", false, false, "Livret d'Epargne donnant droit au versement d'intérêts", TypeFonctionnementCompte.INVESTISSEMENT),
	 */
	LIVRET ("LIVRET", false, false, "Livret d'Epargne donnant droit au versement d'intérêts", TypeFonctionnementCompte.COURANT),

	/**
	PEA("PEA", false, false, "Plan d'Epargne en Actions", TypeFonctionnementCompte.INVESTISSEMENT),
	 */
	PEA("PEA", false, false, "Plan d'Epargne en Actions", TypeFonctionnementCompte.PLACEMENT),

	/**
	ASSURANCE_VIE ("ASS-VIE", false, false, "Assurance-vie", TypeFonctionnementCompte.INVESTISSEMENT),
	 */
	ASSURANCE_VIE ("ASS-VIE", false, false, "Assurance-vie", TypeFonctionnementCompte.PLACEMENT),

	/**
	COMPTE_TITRES ("TITRES", false, false, "Compte titres", TypeFonctionnementCompte.INVESTISSEMENT),
	 */
	COMPTE_TITRES ("TITRES", false, false, "Compte titres", TypeFonctionnementCompte.PLACEMENT),

	/**
	BIEN_IMMOBILIER("IMMOBILIER", false, false, "Bien immobilier", TypeFonctionnementCompte.IMMOBILIER),
	 */
	BIEN_IMMOBILIER("IMMOBILIER", false, false, "Bien immobilier", TypeFonctionnementCompte.IMMOBILIER),

	/**
	BIEN_MOBILIER("MOBILIER", false, false, "Objets de valeur (bijoux, pièces d'or, oeuvres d'art, meubles anciens...)", TypeFonctionnementCompte.IMMOBILISATION),
	 */
	BIEN_MOBILIER("MOBILIER", false, false, "Objets de valeur (bijoux, pièces d'or, oeuvres d'art, meubles anciens...)", TypeFonctionnementCompte.MOBILIER),

	/**
	CONTREPARTIE("CONTREPART", false, false, "Enregistrement de la contrepartie d'une opération d'ajustement du solde (correction ou +/- value)", TypeFonctionnementCompte.CONTREPART),
	 */
	CONTREPARTIE("CONTREPART", false, false, "Enregistrement de la contrepartie d'une opération d'ajustement du solde (correction ou +/- value)", TypeFonctionnementCompte.CONTREPARTIE),

	;
	
	private String code;
	
	private String libelle;
	
	private boolean isBanqueObligatoire;
	
	private boolean isAuMoinsUnTitulaireObligatoire;
	
	private TypeFonctionnementCompte typeFonctionnementCompte;
	
	private TypeCompteInterne(
			String code, 
			boolean isBanqueObligatoire, 
			boolean isAuMoinsUnTitulaireObligatoire, 
			String libelle, 
			TypeFonctionnementCompte typeFonctionnementCompte) {
	
		this.code = code;
		this.isBanqueObligatoire = isBanqueObligatoire;
		this.isAuMoinsUnTitulaireObligatoire = isAuMoinsUnTitulaireObligatoire();
		this.libelle = libelle;
		this.typeFonctionnementCompte = typeFonctionnementCompte;
	}

	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public boolean isBanqueObligatoire() {
		return isBanqueObligatoire;
	}

	public boolean isAuMoinsUnTitulaireObligatoire() {
		return isAuMoinsUnTitulaireObligatoire;
	}

	public TypeFonctionnementCompte getTypeFonctionnementCompte() {
		return typeFonctionnementCompte;
	}

	public static TypeCompteInterne findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			
			for ( TypeCompteInterne value : TypeCompteInterne.values() ) {
			
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		
		return null;
	}
}
