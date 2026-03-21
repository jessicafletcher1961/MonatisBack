package fr.colline.monatis.exceptions.erreurs;

import fr.colline.monatis.exceptions.interfaces.MonatisErreurInterface;

public enum ErreurFonctionnelle implements MonatisErreurInterface {

	// ------------------------- //
	// --- DOMAINE REFERENCE --- //
	// ------------------------- //

	/**
			"REF-0001",
			"Une référence du type [%s] doit obligatoirement avoir un nom"),
	 */
	REFERENCE_NOM_INVALIDE(
			"REF-0001",
			"Une référence du type [%s] doit obligatoirement avoir un nom"),

	/**
			"REF-0002",
			"Une autre référence du type [%s] utilise déjà le nom '%s'"),
	 */
	REFERENCE_NOM_DEJA_UTILISE(
			"REF-0002",
			"Une autre référence du type [%s] utilise déjà le nom '%s'"),

	/**
			"REF-0003",
			"Aucune référence du type [%s] ne correspond à l'id %s"),
	 */
	REFERENCE_NON_ENREGISTREE_PAR_ID (
			"REF-0003",
			"Aucune référence du type [%s] ne correspond à l'id %s"),

	/**
			"REF-0004",
			"Une autre référence du type [%s] utilise déjà l'id %s"),
	 */
	REFERENCE_DEJA_ENREGISTREE_PAR_ID(			
			"REF-0004",
			"Une autre référence du type [%s] utilise déjà l'id %s"),
	
	// --- DOMAINE BANQUE

	/**
			"BNQ-0001",
			"La banque '%s' ne peut être supprimée car elle est encore référencée par %s comptes internes"),
	 */
	BANQUE_SUPPRESSION_AVEC_COMPTES_INTERNES(
			"BNQ-0001",
			"La banque '%s' ne peut être supprimée car elle est encore référencée par %s comptes internes"),
	
	// --- DOMAINE BENEFICIARE

	/**
			"BEN-0001",
			"Le bénéficiaire '%s' ne peut être supprimé car il est encore référencé par %s comptes internes"),
	 */
	BENEFICIAIRE_SUPPRESSION_AVEC_DETAIL_OPERATION(
			"BEN-0001",
			"Le bénéficiaire '%s' ne peut être supprimé car il est encore référencé par %s comptes internes"),
	 
	// --- DOMAINE CATEGORIE

	/**
			"CAT-0001",
			"La catégorie '%s' ne peut être supprimée car elle est encore référencée par %s sous-categories"),
	 */
	CATEGORIE_SUPPRESSION_AVEC_SOUS_CATEGORIES(
			"CAT-0001",
			"La catégorie '%s' ne peut être supprimée car elle est encore référencée par %s sous-categories"),
	
	// --- DOMAINE SOUS-CATEGORIE
	
	/**
			"SSC-0001",
			"La sous-catégorie '%s' ne peut être supprimée car elle est encore référencée par %s lignes de détail d'opérations"),
	 */
	SOUS_CATEGORIE_SUPPRESSION_AVEC_DETAIL_OPERATION(
			"SSC-0001",
			"La sous-catégorie '%s' ne peut être supprimée car elle est encore référencée par %s lignes de détail d'opérations"),

	// --- DOMAINE TITULAIRE

	/**
			"TIT-0001",
			"Le titulaire '%s' ne peut être supprimé car il est encore référencé par %s comptes internes"),
	 */
	TITULAIRE_SUPPRESSION_AVEC_COMPTES_INTERNES(
			"TIT-0001",
			"Le titulaire '%s' ne peut être supprimé car il est encore référencé par %s comptes internes"),
	
	// ----------------------- //
	// --- DOMAINE COMPTES --- //
	// ----------------------- //

	/**
			"CPT-0001",
			"Un compte du type [%s] doit obligatoirement avoir un identifiant"),
	 */
	COMPTE_IDENTIFIANT_INVALIDE(
			"CPT-0001",
			"Un compte du type [%s] doit obligatoirement avoir un identifiant"),
	
	/**
			"CPT-0002",
			"Un autre compte du type [%s] utilise déjà l'identifiant '%s'"),
	 */
	COMPTE_IDENTIFIANT_DEJA_UTILISE(
			"CPT-0002",
			"Un autre compte du type [%s] utilise déjà l'identifiant '%s'"),

	/**
			"CPT-0003",
			"Aucun compte du type [%s] ne correspond à l'id %s"),
	 */
	COMPTE_NON_ENREGISTRE_PAR_ID(
			"CPT-0003",
			"Aucun compte du type [%s] ne correspond à l'id %s"),

	/**
			"CPT-0004",
			"Un autre compte du type [%s] utilise déjà l'id %s"),
	 */
	COMPTE_DEJA_ENREGISTRE_PAR_ID(
			"CPT-0004",
			"Un autre compte du type [%s] utilise déjà l'id %s"),

	/**
			"CPT-0005",
			"Le compte '%s' ne peut être supprimé car il est encore référencé par %s opérations"),
	 */
	COMPTE_SUPPRESSION_AVEC_OPERATION(
			"CPT-0005",
			"Le compte '%s' ne peut pas être ajusté"),

	/**
			"CPT-0006",
			"Le compte '%s' de fonctionnement '%s' ne peut pas être ajusté"),
	 */
	COMPTE_INTERNE_AJUSTEMENT_INCOMPATIBLE(
			"CPT-0006",
			"Le compte '%s' de fonctionnement '%s' ne peut pas être ajusté"),

	/**
			"CPT-0007",
			"Le compte '%s' de fonctionnement '%s' ne peut pas être actualisé"),
	 */
	COMPTE_INTERNE_ACTUALISATION_INCOMPATIBLE(
			"CPT-0007",
			"Le compte '%s' de fonctionnement '%s' ne peut pas être actualisé"),
	
	// -------------------------- //
	// --- DOMAINE OPERATIONS --- //
	// -------------------------- //

	/**
			"OPE-0001",
			"Une opération doit obligatoirement avoir un numéro"),
	 */
	OPERATION_NUMERO_INVALIDE(
			"OPE-0001",
			"Une opération doit obligatoirement avoir un numéro"),

	/**
			"OPE-0002",
			"Une autre opération utilise déjà le numéro '%s'"),
	 */
	OPERATION_NUMERO_DEJA_UTILISE(
			"OPE-0002",
			"Une autre opération utilise déjà le numéro '%s'"),

	/**
			"OPE-0003",
			"Aucune opération ne correspond à l'id %s"),
	 */
	OPERATION_NON_ENREGISTREE_PAR_ID(
			"OPE-0003",
			"Aucune opération ne correspond à l'id %s"),
	
	/**
			"OPE-0004",
			"Une autre opération utilise déjà l'id %s"),
	 */
	OPERATION_DEJA_ENREGISTREE_PAR_ID(
			"OPE-0004",
			"Une autre opération utilise déjà l'id %s"),

	/**
			"OPE-0005",
			"L'opération doit avoir au moins une ligne de détail"),	
	 */
	OPERATION_AU_MOINS_UN_DETAIL_OPERATION_REQUIS(
			"OPE-0005",
			"L'opération doit avoir au moins une ligne de détail"),	
	 
	/**
			"OPE-0006",
			"L'opération contient au moins deux lignes de détail avec le numéro de séquence %s"),
	 */
	OPERATION_LISTE_DETAIL_NUMERO_SEQUENCE_DUPLIQUEE(
			"OPE-0006",
			"L'opération contient au moins deux lignes de détail avec le numéro de séquence %s"),

	/**
			"OPE-0007",
			"Le montant de l'opération (%s) ne correspond pas à la somme des montants de ses lignes de détail (%s)"),
	 */
	OPERATION_LISTE_DETAIL_SOMME_MONTANTS_ERRONEE(
			"OPE-0007",
			"Le montant de l'opération (%s) ne correspond pas à la somme des montants de ses lignes de détail (%s)"),

	/**
			"OPE-0008",
			"Une opération de type '%s' est incompatible avec le type de fonctionnement '%s' du compte de dépense"),
	 */
	OPERATION_TYPE_COMPTE_DEPENSE_INCOMPATIBLE(
			"OPE-0008",
			"Une opération de type '%s' est incompatible avec le type de fonctionnement '%s' du compte de dépense"),

	/**
			"OPE-0009",
			"Une opération de type %s est incompatible avec le type de fonctionnement '%s' du compte de recette"),
	 */
	OPERATION_TYPE_COMPTE_RECETTE_INCOMPATIBLE(
			"OPE-0009",
			"Une opération de type %s est incompatible avec le type de fonctionnement '%s' du compte de recette"),
	
	// ---------------------- //
	// --- DOMAINE BUDGET --- //
	// ---------------------- //

	/**
			"BUD-0001",
			"Création d'un budget '%s' pour la référence de type '%s' de nom '%s' : au moins un budget a déjà été déterminé pour cette référence"),
	 */
	BUDGET_CREATION_AVEC_HISTORIQUE(
			"BUD-0001",
			"Création d'un budget '%s' pour la référence de type '%s' de nom '%s' : au moins un budget a déjà été déterminé pour cette référence"),

	/**
			"BUD-0002",
			"Reconduction d'un budget '%s' pour la référence de type '%s' de nom '%s' : aucun budget n'a encore été déterminé pour cette référence"),
	 */
	BUDGET_RECONDUCTION_SANS_HISTORIQUE(
			"BUD-0002",
			"Reconduction d'un budget '%s' pour la référence de type '%s' de nom '%s' : aucun budget n'a encore été déterminé pour cette référence"),
	
	/**
			"BUD-0003",
			"Reconduction d'un budget '%s' pour la référence de type '%s' de nom '%s' : un budget a déjà été déterminé pour la date %s pour cette référence"),
	 */
	BUDGET_RECONDUCTION_INUTILE(
			"BUD-0003",
			"Reconduction d'un budget '%s' pour la référence de type '%s' de nom '%s' : un budget a déjà été déterminé pour la date %s pour cette référence"),
	
	;
	
	private final TypeErreur type = TypeErreur.FONCTIONNELLE;
	
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

	private ErreurFonctionnelle(String code, String pattern) {
		this.code = code;
		this.pattern = pattern;
	}
}
