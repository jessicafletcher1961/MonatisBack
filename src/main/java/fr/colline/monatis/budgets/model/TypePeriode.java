package fr.colline.monatis.budgets.model;

public enum TypePeriode {

	ANNUEL ("ANNEE", "Périodicité annuelle"),
	SEMESTRIEL ("SEMESTRE", "Périodicité semestrielle"),
	TRIMESTRIEL ("TRIMESTRE", "Périodicité trimestrielle"),
	BIMESTRIEL ("BIMESTRE", "Périodicité bimestrielle (tous les 2 mois)"),
	MENSUEL ("MOIS", "Périodicité mensuelle"),
	TECHNIQUE ("TECHNIQUE", "Période de durée indéterminée");

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
