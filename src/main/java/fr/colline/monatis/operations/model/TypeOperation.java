package fr.colline.monatis.operations.model;

public enum TypeOperation {

	TRANSFERT ("TRANSFERT", "Transfert des fonds entre deux comptes de liquidités (interne/COURANT)"),
	
	DEPENSE ("DEPENSE", "Achats de biens ou de services à des personnes et des organismes n'appartenant pas au foyer (externe) par l'utilisation des fonds disponibles sur un compte courant (interne/COURANT)"),
	RECETTE ("RECETTE", "Fonds en provenance de personnes ou d'organismes n'appartenant pas au foyer (externe) et mis à disposition sur un compte courant du foyer (interne/COURANT)"),

	DEPOT ("DEPOT", "Dépôt sur un compte d'épargne : placement d'une partie des fonds disponibles d'un compte courant (interne/COURANT) sur un compte financier (interne/FINANCIER)"),
	INVESTISSEMENT ("INVEST", "Achat de parts sociales : placement d'une partie des fonds disponibles d'un compte courant (interne/COURANT) sur un compte financier (interne/FINANCIER)"),
	RETRAIT("RETRAIT", "Retrait de fonds sur un compte d'épargne : récupération des fonds présents sur un compte financier (interne/FINANCIER) pour les mettre à disposition sur un compte courant (interne/COURANT)"),
	LIQUIDATION("LIQUID", "Liquidation de parts sociales : récupération des fonds présents sur un compte financier (interne/FINANCIER) pour les mettre à disposition sur un compte courant (interne/COURANT)"),
	
	VENTE ("VENTE", "Vente d'un bien enregistré au patrimoine du foyer : diminutions de la valeur du compte de patrimoine (interne/PATRIMOINE) par la valeur du bien cédé à l'acheteur (externe)"),
	ACHAT ("ACHAT", "Achat d'un bien patrimonial par le foyer : augmentation du compte de patrimoine (interne/PATRIMOINE) par la valeur du bien cédé par le vendeur (externe)"),

	VIRTUELLE_MOINS("SOLDE-", "Opération calculée permettant de constater un écart négatif entre le solde calculé (à partir des opérations enregistrées) et le solde enregistré (évaluation)"),
	VIRTUELLE_ZERO("SOLDE0", "Opération calculée permettant de constater l'absence d'écart entre le solde calculé (à partir des opérations enregistrées) et le solde enregistré (évaluation)"),
	VIRTUELLE_PLUS("SOLDE+", "Opération calculée permettant de constater un écart positif entre le solde calculé (à partir des opérations enregistrées) et le solde enregistré (évaluation)"),
	TECHNIQUE("TECHNIQUE", "Opération entre un compte technique et compte interne/COURANT ou interne/FINANCIER"),
	
//	EMPRUNT_REMBOURSEMENT ("REMBOURSMT", "Echéance de remboursement d'emprunt : utilisation des fonds disponibles sur un compte de liquidités (interne/COURANT) pour le paiement d'une échéance d'un compte d'emprunt (interne/DETTE). Permet également d'enregistrer des remboursements anticipés."),
//	EMPRUNT_VERSEMENT ("CAPITAL", "Versement du capital emprunté à un prêteur (externe) sur un compte de liquidités (interne/COURANT)"),
	
//	FRAIS_BANCAIRES ("F_BANCAIRE", "Frais divers imputés au fonctionnement des comptes de dépôt"),
//	FRAIS_GESTION ("F_GESTION", "Frais divers imputés au fonctionnement des comptes d'épargne"),
	
//
//	FRAIS ("FRAIS", "Frais bancaires, agios, intérêts d'emprunts ou frais de gestion appliqués à un compte du foyer"),
//	GAIN ("GAIN", "Intérêts ou dividendes produits par un compte du foyer"),
//
//	PLUS_SOLDE ("SOLDE+", "Opération fictive de recette permettant d'augmenter artificiellement le solde d'un compte du foyer"),
//	MOINS_SOLDE ("SOLDE-", "Opération fictive de dépense permettant de diminuer artificiellement le solde d'un compte du foyer"),
	
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
