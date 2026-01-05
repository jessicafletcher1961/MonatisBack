package fr.colline.monatis.operations.model;

public enum TypeProgrammation {

	HEBDOMADAIRE("SEMAINE", "Toutes les semaines"),
	MENSUEL("MOIS", "Tous les mois"),
	BIMESTRIEL("BIMESTRE", "Tous les deux mois"),
	TRIMESTRIEL("TRIMESTRE", "Tous les trois mois"),
	SEMESTRIEL("SEMESTRE", "Tous les six mois"),
	ANNUEL("ANNEE", "Tous les ans"),
	
	;
	
	private String code;
	
	private String libelle;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	private TypeProgrammation(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}

	public static TypeProgrammation findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			for ( TypeProgrammation value : TypeProgrammation.values() ) {
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		return null;
	}

}
