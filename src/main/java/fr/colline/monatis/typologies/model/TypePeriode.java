package fr.colline.monatis.typologies.model;

public enum TypePeriode {

	ANNUEL ("ANNEE", "Périodicité annuelle"),
	SEMESTRIEL ("SEMESTRE", "Périodicité semestrielle (6 mois)"),
	QUADRIMESTRIEL ("QUADRIM", "Périodicité quadrimestrielle (4 mois)"),
	TRIMESTRIEL ("TRIMESTRE", "Périodicité trimestrielle (3 mois)"),
	BIMESTRIEL ("BIMESTRE", "Périodicité bimestrielle (2 mois)"),
	MENSUEL ("MOIS", "Périodicité mensuelle"),
	
	;

	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypePeriode(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}
	
	public static TypePeriode findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypePeriode value : TypePeriode.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}
}
