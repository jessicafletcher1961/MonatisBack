package fr.colline.monatis.budget.model;

public enum TypeBudget {

	ANNUEL ("ANNUEL", "Budget annuel", true),
	MENSUEL ("MENSUEL", "Budget mensuel", true),
	
	;
	
	private String code;
	
	private String libelle;
	
	private boolean autoReconductible;
	
	public String getCode() {
		return code;
	}

	public String getLibelle() {
		return libelle;
	}

	public boolean isAutoReconductible() {
		return autoReconductible;
	}

	private TypeBudget(String code, String libelle, boolean autoReconductible) {
		
		this.code = code;
		this.libelle = libelle;
		this.autoReconductible = autoReconductible;
	}
	
	public static TypeBudget findByCode(String code) {
		
		if ( code != null && !code.isBlank() ) {
			
			for ( TypeBudget value : TypeBudget.values() ) {
			
				if ( value.code.equalsIgnoreCase(code) ) {
					return value;
				}
			}
		}
		
		return null;
	}
}
