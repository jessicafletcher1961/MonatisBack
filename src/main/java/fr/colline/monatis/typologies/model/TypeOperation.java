package fr.colline.monatis.typologies.model;

public enum TypeOperation {

	RECETTE ("RECETTE", "Recette", "Fonds en provenance de personnes ou d'organismes n'appartenant pas au foyer (externe) et mis à disposition sur un compte courant du foyer (interne/COURANT)", false, true),
	DEPENSE ("DEPENSE", "Dépense", "Achats de biens ou de services à des personnes et des organismes n'appartenant pas au foyer (externe) par l'utilisation des fonds disponibles sur un compte courant (interne/COURANT)", false, true),

	TRANSFERT ("TRANSFERT", "Transfert", "Transfert des fonds entre deux comptes de dépôt (interne/COURANT)", false, false),
	DEPOT ("DEPOT", "Dépôt", "Dépôt de fonds sur un compte d'épargne : placement d'une partie des fonds disponibles d'un compte courant (interne/COURANT) sur un compte financier (interne/FINANCIER)", false, false),
	INVESTISSEMENT ("INVEST", "Investissement", "Achat de parts sociales : placement d'une partie des fonds disponibles d'un compte courant (interne/COURANT) sur un compte financier (interne/FINANCIER)", false, false),
	RETRAIT("RETRAIT", "Retrait", "Retrait de fonds d'un compte d'épargne : récupération des fonds présents sur un compte financier (interne/FINANCIER) pour les mettre à disposition sur un compte courant (interne/COURANT)", false, false),
	LIQUIDATION("LIQUID", "Liquidation", "Liquidation de parts sociales : récupération des fonds présents sur un compte financier (interne/FINANCIER) pour les mettre à disposition sur un compte courant (interne/COURANT)", false, false),
	VENTE ("VENTE", "Vente", "Vente d'un bien enregistré au patrimoine du foyer : diminutions du compte de patrimoine (interne/BIEN) et augmentation du compte courant (interne/COURANT) par la valeur du bien cédé à l'acheteur (externe)", false, false),
	ACHAT ("ACHAT", "Achat", "Achat d'un bien patrimonial par le foyer : augmentation du compte de patrimoine (interne/BIEN) et diminution du compte courant (interne/COURANT) par la valeur du bien cédé par le vendeur (externe)", false, false),

	REMUNERATION_COMPTE_COURANT("COURANT+", "Rémunérations des comptes courants", "Prise en compte des recettes intrinsèques à un compte courant (interne/COURANT) comme la rémunération des comptes de dépôts proposée par certaines banques", true, false),
	FRAIS_COMPTE_COURANT("COURANT-", "Frais des comptes courants", "Prise en compte des dépenses intrinsèques à un compte courant (interne/COURANT) comme les cotisations, les agios, les surcoûts pour des versements hors UE et autres frais bancaires prélevés sur le compte", true, false),
	REMUNERATION_COMPTE_FINANCIER("FINANCIER+", "Rémunérations des comptes financiers", "Prise en compte des recettes intrinsèques à un compte financier (interne/FINANCIER) comme le réinvestissement des dividendes ou le versement d'intérêts", true, false),
	FRAIS_COMPTE_FINANCIER("FINANCIER-", "Frais des comptes financiers", "Prise en compte des dépenses intrinsèques à un compte financier (interne/FINANCIER) comme les cotisations, les intérêts versés et autres frais de gestion prélevés sur le compte", true, false),
	REMUNERATION_COMPTE_BIEN("BIEN+", "Rémunérations des biens patrimoniaux", "Prise en compte des revenus induits par un bien (interne/BIEN) comme le versement des loyers", true, false),
	FRAIS_COMPTE_BIEN("BIEN-", "Frais des biens patrimoniaux", "Prise en compte des frais induits par un bien (interne/BIEN) comme les charges de copropriété, les diagnostiques, la rémunération du gestionnaire...", true, false),
	
	;
	
	private String code;
	
	private String libelleCourt;
	
	private String libelle;

	private boolean fluxTechnique;
	
	private boolean categorisable;

	public String getCode() {
		return code;
	}

	public String getLibelleCourt() {
		return libelleCourt;
	}

	public String getLibelle() {
		return libelle;
	}

	public boolean isFluxTechnique() {
		return fluxTechnique;
	}

	public boolean isCategorisable() {
		return categorisable;
	}

	private TypeOperation(String code, String libelleCourt, String libelle, boolean fluxTechnique, boolean categorisable) {
		this.code = code;
		this.libelleCourt = libelleCourt;
		this.libelle = libelle;
		this.fluxTechnique = fluxTechnique;
		this.categorisable = categorisable;
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
