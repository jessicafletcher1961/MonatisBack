package fr.colline.monatis.exceptions.erreurs;

import fr.colline.monatis.exceptions.interfaces.MonatisErreurInterface;

public enum ErreurProgrammation implements MonatisErreurInterface {

	/**
			"PROG-PAR-0001",
			"L'id passé en paramètre de cette méthode est obligatoire [%s]"),
	 */
	ID_NULL(
			"PROG-PAR-0001",
			"L'id passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),

	/**
			"PROG-PAR-0002",
			"Le numéro passé en paramètre de cette méthode est obligatoire"),
	 */
	NUMERO_NULL(
			"PROG-PAR-0002",
			"Le numéro passé en paramètre de cette méthode est obligatoire"),

	/**
			"PROG-PAR-0003",
			"Le nom passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),
	 */
	NOM_NULL(
			"PROG-PAR-0003",
			"Le nom passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),

	/**
			"PROG-PAR-0004",
			"L'identifiant passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),
	 */
	IDENTIFIANT_NULL(
			"PROG-PAR-0004",
			"L'identifiant passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),

	/**
			"PROG-PAR-0005",
			"L'opération passée en paramètre de cette méthode est obligatoire"),
	 */
	OPERATION_NULL(
			"PROG-PAR-0005",
			"L'opération passée en paramètre de cette méthode est obligatoire"),

	/**
			"PROG-PAR-0006",
			"Le compte passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),
	 */
	COMPTE_NULL(
			"PROG-PAR-0006",
			"Le compte passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),

	/**
			"PROG-PAR-0007",
			"Le tri passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),
	 */
	TRI_NULL(
			"PROG-PAR-0007",
			"Le tri passé en paramètre de cette méthode est obligatoire (pour le type [%s])"),

	/**
			"PROG-PAR-0008",
			"La référence passée en paramètre de cette méthode est obligatoire (pour le type [%s])"),
	 */
	REFERENCE_NULL(
			"PROG-PAR-0008",
			"La référence passée en paramètre de cette méthode est obligatoire (pour le type [%s])"),


	/**
			"PROG-PAR-0009",
			"La liste des noms de titulaires passée en paramètre de cette méthode est obligatoire"),
	 */
	TABLEAU_NOMS_TITULAIRES_NULL(
			"PROG-PAR-0009",
			"La liste des noms de titulaires passée en paramètre de cette méthode est obligatoire"),

	/**
			"PROG-PAR-0010",
			"La liste des noms de bénéficiaires passée en paramètre de cette méthode est obligatoire"),
	 */
	TABLEAU_NOMS_BENEFICIAIRES_NULL(
			"PROG-PAR-0010",
			"La liste des noms de bénéficiaires passée en paramètre de cette méthode est obligatoire"),
	
	/**
			"PROG-PAR-0011",
			"Le solde après ajustement passé en paramètre de cette méthode est obligatoire"),
	 */
	SOLDE_APRES_AJUSTEMENT_NULL(
			"PROG-PAR-0011",
			"Le solde après ajustement passé en paramètre de cette méthode est obligatoire"),

	/**
			"PROG-PAR-0012",
			"La date de l'ajustement passée en paramètre de cette méthode est obligatoire"),
	 */
	DATE_AJUSTEMENT_NULL(
			"PROG-PAR-0012",
			"La date de l'ajustement passée en paramètre de cette méthode est obligatoire"),

	/**
			"PROG-PAR-0013",
			"Le solde après l'actualisation passé en paramètre de cette méthode est obligatoire"),
	 */
	SOLDE_APRES_ACTUALISATION_NULL(
			"PROG-PAR-0013",
			"Le solde après l'actualisation passé en paramètre de cette méthode est obligatoire"),

	/**
			"PROG-PAR-0014",
			"La date de l'actualisation passée en paramètre de cette méthode est obligatoire"),
	 */
	DATE_ACTUALISATION_NULL(
			"PROG-PAR-0014",
			"La date de l'actualisation passée en paramètre de cette méthode est obligatoire"),

	/**
			 "PROG-PAR-0015", 
			 "Le budget passé en paramètre de cette méthode est obligatoire"),
	 */
	 BUDGET_NULL(
			 "PROG-PAR-0015", 
			 "Le budget passé en paramètre de cette méthode est obligatoire"),
	 
	 /**
			 "PROG-PAR-0016", 
			 "Une date cible du budget passée en paramètre de cette méthode est obligatoire"),
	  */
	 DATE_BUDGET_NULL(
			 "PROG-PAR-0016", 
			 "Une date cible du budget passée en paramètre de cette méthode est obligatoire"),
	 
	/**
			"PROG-PRG-0001",
			"La recherche par nom ne s'applique qu'aux références spécialisées, pas à la référence générique %s"),
	 */
	RECHERCHE_PAR_NOM_IMPOSSIBLE(
			"PROG-PRG-0001",
			"La recherche par nom ne s'applique qu'aux références spécialisées, pas à la référence générique %s"),

	/**
			"PROG-PRG-0002",
			"Le type de fonctionnement '%s'-'%s' n'est pas géré ici"),
	 */
	TYPE_FONCTIONNEMENT_COMPTE_NON_GERE(
			"PROG-PRG-0002",
			"Le type de fonctionnement '%s'-'%s' n'est pas géré ici"),

	/**
			"PROG-PRG-0003",
			"La classe de compte '%s' n'est pas gérée"),
	 */
	COMPTE_CLASSE_NON_GEREE(
			"PROG-PRG-0003",
			"La classe du compte '%s' n'est pas gérée"),
	
	/**
			"PROG-PRG-0004",
			"Le type de budget '%s'-'%s' n'est pas géré ici"),
	 */
	TYPE_BUDGET_NON_GERE(
			"PROG-PRG-0004",
			"Le type de budget '%s'-'%s' n'est pas géré ici"),

	;

	private final TypeErreur type = TypeErreur.PROGRAMMATION;

	private String code;
	private String pattern;

	@Override
	public TypeErreur getType() {
		return type;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage(Object[] values) {
		return String.format(pattern, values);
	}

	private ErreurProgrammation(
			String code, 
			String pattern) {

		this.code = code;
		this.pattern = pattern;
	}
}
