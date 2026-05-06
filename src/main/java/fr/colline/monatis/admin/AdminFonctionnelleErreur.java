package fr.colline.monatis.admin;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum AdminFonctionnelleErreur implements MonatisErreur {

	/**
			"La séquence '%s' n'existe pas"),
	 */
	SEQUENCE_NON_TROUVEE(
			"La séquence '%s' n'existe pas"),
	
	/**
			"Le script '%s' n'a pas été exécuté car il comporte une erreur de syntaxe"),
	 */
	ERREUR_EXECUTION_SCRIPT(
			"Le script '%s' n'a pas été exécuté car il comporte une erreur de syntaxe"),
	
	/**
			"La table %s n'a pas pu être exportée dans le fichier '%s'"),
	 */
	ERREUR_EXPORT_CSV(
			"La table %s n'a pas pu être exportée dans le fichier '%s'"), 
	
	/**
			"La table %s n'a pas pu être importée du fichier '%s'"),
	 */
	ERREUR_IMPORT_CSV(
			"La table %s n'a pas pu être importée du fichier '%s'"),
	
	/**
			"Les tables n'ont pas pu être vidées"),
	 */
	ERREUR_VIDANGE_BASE(
			"Les tables n'ont pas pu être vidées"),
	
	/**
			"Les modifications apportées sont rejetées car elles ne permettent pas de maintenir l'intégrité de la base de données"),
	 */
	ERREUR_REACTIVATION_CONTRAINTES(
			"Les modifications apportées sont rejetées car elles ne permettent pas de maintenir l'intégrité de la base de données"),

	;
	
	private final TypeDomaine typeDomaine = TypeDomaine.ADMIN;
	private final TypeErreur typeErreur = TypeErreur.FONCTIONNELLE;

	private String pattern;
	
	@Override
	public String getCode() {
		String numero = String.format("%04d", this.ordinal());
		return typeDomaine.getCode().concat("-").concat(typeErreur.getCode()).concat("-").concat(numero);
	}

	@Override
	public String getPrefixe() {
		String numero = String.format("%04d", this.ordinal());
		return typeDomaine.getPrefixe().concat(".").concat(typeErreur.getPrefixe()).concat(".").concat(numero);
	}

	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public TypeErreur getTypeErreur() {
		return typeErreur;
	}

	@Override
	public TypeDomaine getTypeDomaine() {
		return typeDomaine;
	}
	
	private AdminFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}

}
