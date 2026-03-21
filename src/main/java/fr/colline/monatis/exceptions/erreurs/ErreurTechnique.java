package fr.colline.monatis.exceptions.erreurs;

import fr.colline.monatis.exceptions.interfaces.MonatisErreurInterface;

public enum ErreurTechnique implements MonatisErreurInterface{

	/** -- OperationService ---------------------------------------- */
	
	/**
			"TECH-OPE-0001",
			"Un problème technique est survenu lors de la recherche de l'opération d'id %s"),
	 */
	TECH_RECHERCHE_OPERATION_PAR_ID(
			"TECH-OPE-0001",
			"Un problème technique est survenu lors de la recherche de l'opération d'id %s"),

	/**
			"TECH-OPE-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de l'id %s dans les opérations"),
	 */
	TECH_EXISTANCE_OPERATION_PAR_ID(
			"TECH-OPE-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de l'id %s dans les opérations"),

	/**
			"TECH-OPE-0003",
			"Un problème technique est survenu lors de la recherche de l'opération de numéro '%s'"),
	 */
	TECH_RECHERCHE_OPERATION_PAR_NUMERO(
			"TECH-OPE-0003",
			"Un problème technique est survenu lors de la recherche de l'opération de numéro '%s'"),

	/**
			"TECH-OPE-0004",
			"Un problème technique est survenu lors de la vérification de l'existance du numero '%s' dans les opérations"),
	 */
	TECH_EXISTANCE_OPERATION_PAR_NUMERO(
			"TECH-OPE-0004",
			"Un problème technique est survenu lors de la vérification de l'existance du numero '%s' dans les opérations"),

	/**
			"TECH-OPE-0005",
			"Un problème technique est survenu lors de la recherche de toutes les opérations"), 
	 */
	TECH_RECHERCHE_OPERATION_TOUS(
			"TECH-OPE-0005",
			"Un problème technique est survenu lors de la recherche de toutes les opérations"), 

	/**
			"TECH-OPE-0006",
			"Un problème technique est survenu lors de la recherche des opérations de recette concernées par le compte d'id %s entre les dates %s et %s"),
	 */
	TECH_RECHERCHE_OPERATION_RECETTE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"TECH-OPE-0006",
			"Un problème technique est survenu lors de la recherche des opérations de recette concernées par le compte d'id %s entre les dates %s et %s"),

	/**
			"TECH-OPE-0007",
			"Un problème technique est survenu lors de la recherche des opérations de dépense concernées par le compte d'id %s entre les dates %s et %s"),
	 */
	TECH_RECHERCHE_OPERATION_DEPENSE_PAR_COMPTE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN(
			"TECH-OPE-0007",
			"Un problème technique est survenu lors de la recherche des opérations de dépense concernées par le compte d'id %s entre les dates %s et %s"),

	/**
	TECH_SUPPRESSION_OPERATION_TOUS(
			"TECH-OPE-0008",
			"Un problème technique est survenu lors de la suppression de toutes les opérations"),
	 */
	TECH_SUPPRESSION_OPERATION_TOUS(
			"TECH-OPE-0008",
			"Un problème technique est survenu lors de la suppression de toutes les opérations"),
	
	/**
			TypeErreur.TECHNIQUE,
			"TECH-OPE-0009",
			"Un problème technique est survenu lors de l'enregistrement de l'opération de numero '%s'"),
	 */
	TECH_ENREGISTREMENT_OPERATION(
			"TECH-OPE-0009",
			"Un problème technique est survenu lors de l'enregistrement de l'opération de numero '%s'"),
	
	/**
			"TECH-OPE-0010",
			"Un problème technique est survenu lors de la suppression de l'opération de numéro '%s'"),
	 */
	TECH_SUPPRESSION_OPERATION(
			"TECH-OPE-0010",
			"Un problème technique est survenu lors de la suppression de l'opération de numéro '%s'"),
	
	/** ------------------------------------------------------------ */
	/** -- CompteService ------------------------------------------- */
	
	/**
			"TECH-CPT-0001",
			"Un problème technique est survenu lors de la recherche du compte d'id %s dans les comptes de type %s"),
	 */
	TECH_RECHERCHE_COMPTE_PAR_ID(
			"TECH-CPT-0001",
			"Un problème technique est survenu lors de la recherche du compte d'id %s dans les comptes de type %s"),
	
	/**
			"TECH-CPT-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de l'id %s dans les comptes de type %s"),
	 */
	TECH_EXISTANCE_COMPTE_PAR_ID(
			"TECH-CPT-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de l'id %s dans les comptes de type %s"),
	
	/**
			"TECH-CPT-0003",
			"Un problème technique est survenu lors de la recherche du compte de type %s et d'identifiant '%s'"),
	 */
	TECH_RECHERCHE_COMPTE_PAR_IDENTIFIANT(
			"TECH-CPT-0003",
			"Un problème technique est survenu lors de la recherche du compte de type %s et d'identifiant '%s'"),

	/**
			"TECH-CPT-0004",
			"Un problème technique est survenu lors de la vérification de l'existance du compte de type %s et d'identifiant '%s'"),
	 */
	TECH_EXISTANCE_COMPTE_PAR_IDENTIFIANT(
			"TECH-CPT-0004",
			"Un problème technique est survenu lors de la vérification de l'existance du compte de type %s et d'identifiant '%s'"),

	/**
			"TECH-CPT-0005",
			"Un problème technique est survenu lors de la recherche de tous les comptes de type %s"),
	 */
	TECH_RECHERCHE_COMPTE_TOUS(
			"TECH-CPT-0005",
			"Un problème technique est survenu lors de la recherche de tous les comptes de type %s"),

	/**
			"TECH-CPT-0006",
			"Un problème technique est survenu lors de la suppression de tous les comptes de type %s"),
	 */
	TECH_SUPPRESSION_COMPTE_TOUS(
			"TECH-CPT-0006",
			"Un problème technique est survenu lors de la suppression de tous les comptes de type %s"),

	/**
			"TECH-CPT-0007",
			"Un problème technique est survenu lors de l'enregistrement du compte de type %s et d'identifiant '%s'"),
	 */
	TECH_ENREGISTREMENT_COMPTE(
			"TECH-CPT-0007",
			"Un problème technique est survenu lors de l'enregistrement du compte de type %s et d'identifiant '%s'"),

	/**
			"TECH-CPT-0008",
			"Un problème technique est survenu lors de la suppression du compte de type %s et d'identifiant '%s'"),
	 */
	TECH_SUPPRESSION_COMPTE(
			"TECH-CPT-0008",
			"Un problème technique est survenu lors de la suppression du compte de type %s et d'identifiant '%s'"),

	/**
			"TECH-CPT-0009",
			"Un problème technique est survenu lors de la recherche du nombre d'opération concernées par le compte d'id %s"),
	 */
	TECH_RECHERCHE_NOMBRE_OPERATION_PAR_COMPTE_ID(
			"TECH-CPT-0009",
			"Un problème technique est survenu lors de la recherche du nombre d'opération concernées par le compte d'id %s"),
	
	/** ------------------------------------------------------------ */
	/** -- ReferenceService ---------------------------------------- */

	/**
			"TECH-REF-0001",
			"Un problème technique est survenu lors de la recherche de la référence de type %s et d'id %s"),
	 */
	TECH_RECHERCHE_REFERENCE_PAR_ID(
			"TECH-REF-0001",
			"Un problème technique est survenu lors de la recherche de la référence de type %s et d'id %s"),
	
	/**
			"TECH-REF-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de la reference de type %s et d'id %s"),
	 */
	TECH_EXISTANCE_REFERENCE_PAR_ID(
			"TECH-REF-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de la reference de type %s et d'id %s"),

	/**
			"TECH-REF-0003",
			"Un problème technique est survenu lors de la recherche de la référence de type %s et de nom '%s'"),
	 */
	TECH_RECHERCHE_REFERENCE_PAR_NOM(
			"TECH-REF-0003",
			"Un problème technique est survenu lors de la recherche de la référence de type %s et de nom '%s'"),

	/**
			"TECH-REF-0004",
			"Un problème technique est survenu lors de la vérification de l'existance de la référence de type %s et de nom '%s'"),
	 */
	TECH_EXISTANCE_REFERENCE_PAR_NOM(
			"TECH-REF-0004",
			"Un problème technique est survenu lors de la vérification de l'existance de la référence de type %s et de nom '%s'"),

	/**
			"TECH-REF-0005",
			"Un problème technique est survenu lors de la recherche de toutes les références de type %s"),
	 */
	TECH_RECHERCHE_REFERENCE_TOUS(
			"TECH-REF-0005",
			"Un problème technique est survenu lors de la recherche de toutes les références de type %s"),

	/**
			"TECH-REF-0006",
			"Un problème technique est survenu lors de la suppression de toutes les références de type %s"),
	 */
	TECH_SUPPRESSION_REFERENCE_TOUS(
			"TECH-REF-0006",
			"Un problème technique est survenu lors de la suppression de toutes les références de type %s"),

	/**
			"TECH-REF-0007",
			"Un problème technique est survenu lors de l'enregistrement de la référence de type %s et de nom '%s'"),
	 */
	TECH_ENREGISTREMENT_REFERENCE(
			"TECH-REF-0007",
			"Un problème technique est survenu lors de l'enregistrement de la référence de type %s et de nom '%s'"),

	/**
			"TECH-REF-0008",
			"Un problème technique est survenu lors de la suppression de la référence de type %s et de nom '%s'"),
	 */
	TECH_SUPPRESSION_REFERENCE(
			"TECH-REF-0008",
			"Un problème technique est survenu lors de la suppression de la référence de type %s et de nom '%s'"),

	/**
			"TECH-REF-0009",
			"Un problème technique est survenu lors de la recherche du nombre de lignes de détail d'opération concernées par le bénéficiaire d'id %s"),
	 */
	TECH_RECHERCHE_NOMBRE_DETAIL_OPERATION_PAR_BENEFICIAIRE_ID(
			"TECH-REF-0009",
			"Un problème technique est survenu lors de la recherche du nombre de lignes de détail d'opération concernées par le bénéficiaire d'id %s"),

	/**
			"TECH-REF-0010",
			"Un problème technique est survenu lors de la recherche du nombre de lignes de détail d'opération concernées par la sous-catégorie d'id %s"),
	 */
	TECH_RECHERCHE_NOMBRE_DETAIL_OPERATION_PAR_SOUS_CATEGORIE_ID(
			"TECH-REF-0010",
			"Un problème technique est survenu lors de la recherche du nombre de lignes de détail d'opération concernées par la sous-catégorie d'id %s"),

	/** ------------------------------------------------------------ */
	/** -- BudgetService ------------------------------------------- */

	/**
			"TECH-BUD-0001",
			"Un problème technique est survenu lors de la recherche du budget d'id %s dans les budgets"),
	 */
	TECH_RECHERCHE_BUDGET_PAR_ID(
			"TECH-BUD-0001",
			"Un problème technique est survenu lors de la recherche du budget d'id %s dans les budgets"),

	/**
			"TECH-BUD-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de l'id %s dans les budgets"),
	 */
	TECH_EXISTANCE_BUDGET_PAR_ID(
			"TECH-BUD-0002",
			"Un problème technique est survenu lors de la vérification de l'existance de l'id %s dans les budgets"),

	/**
			"TECH-BUD-0003",
			"Un problème technique est survenu lors de la recherche de l'historique des budgets de la référence d'id %s"),
	 */
	TECH_RECHERCHE_LISTE_BUDGET_PAR_REFERENCE_ID(
			"TECH-BUD-0003",
			"Un problème technique est survenu lors de la recherche de l'historique des budgets de la référence d'id %s"),
	
	/**
			"TECH-BUD-0004",
			"Un problème technique est survenu lors de la vérification de l'existance d'un historique des budgets de la référence d'id %s"),
	 */
	TECH_EXISTANCE_LISTE_BUDGET_PAR_REFERENCE_ID(
			"TECH-BUD-0004",
			"Un problème technique est survenu lors de la vérification de l'existance d'un historique des budgets de la référence d'id %s"),
	
	/**
			"TECH-BUD-0005",
			"Un problème technique est survenu lors de la recherche du budget de la référence d'id %s pour la date %s"),
	 */
	TECH_RECHERCHE_BUDGET_PAR_REFERENCE_ID_ET_DATE_CIBLE(
			"TECH-BUD-0005",
			"Un problème technique est survenu lors de la recherche du budget de la référence d'id %s pour la date %s"),

	/**
			"TECH-BUD-0006",
			"Un problème technique est survenu lors de la vérification de l'existance du budget de la référence d'id %s pour la date %s"),
	 */
	TECH_EXISTANCE_BUDGET_PAR_REFERENCE_ID_ET_DATE_CIBLE(
			"TECH-BUD-0006",
			"Un problème technique est survenu lors de la vérification de l'existance du budget de la référence d'id %s pour la date %s"),

	/**
			"TECH-BUD-0007",
			"Un problème technique est survenu lors de l'enregistrement du budget de type '%s' pour la référence de type '%s' de nom '%s' et commençant le %s"),
	 */
	TECH_ENREGISTREMENT_BUDGET(
			"TECH-BUD-0007",
			"Un problème technique est survenu lors de l'enregistrement du budget de type '%s' pour la référence de type '%s' de nom '%s' et commençant le %s"),
	
	
	/**
			"TECH-BUD-0008",
			"Un problème technique est survenu lors de la suppression du budget de type '%s' pour la référence de type '%s' de nom '%s' et commençant le %s"),
	 */
	TECH_SUPPRESSION_BUDGET(
			"TECH-BUD-0008",
			"Un problème technique est survenu lors de la suppression du budget de type '%s' pour la référence de type '%s' de nom '%s' et commençant le %s"),

	/**
			"TECH-BUD-0009",
			"Un problème technique est survenu lors de la recherche de tous les budgets"),
	 */
	TECH_RECHERCHE_BUDGET_TOUS(
			"TECH-BUD-0009",
			"Un problème technique est survenu lors de la recherche de tous les budgets"),

	/**
			"TECH-BUD-0010",
			"Un problème technique est survenu lors de la suppression de tous les budgets"),
	 */
	TECH_SUPPRESSION_BUDGET_TOUS(
			"TECH-BUD-0010",
			"Un problème technique est survenu lors de la suppression de tous les budgets"),
	
	/**
			"TECH-BUD-0011",
			"Un problème technique est survenu lors de la création d'un nouveau budget"),
	 */
	TECH_INSTANCIATION_BUDGET(
			"TECH-BUD-0011",
			"Un problème technique est survenu lors de la création d'un nouveau budget"),
	
	;
	
	private final TypeErreur type = TypeErreur.TECHNIQUE;
	
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

	private ErreurTechnique(String code, String pattern) {

		this.code = code;
		this.pattern = pattern;
	}
}
