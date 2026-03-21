package fr.colline.monatis.exceptions.erreurs;

import org.springframework.http.HttpStatus;

import fr.colline.monatis.exceptions.interfaces.MonatisErreurWithHttpStatusInterface;

public enum ErreurControle implements MonatisErreurWithHttpStatusInterface {

	/**
			HttpStatus.BAD_REQUEST,
			"REF-0001",
			"Le paramètre /{nom} est obligatoire à la fin de l'url de la requête"),
	 */
	PATH_VARIABLE_NOM_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"REF-0001",
			"Le paramètre /{nom} est obligatoire à la fin de l'url de la requête"),

	/**
			HttpStatus.NOT_FOUND,
			"BNQ-0001",
			"Aucune banque de nom '%s' n'a été trouvée"),
	 */
	BANQUE_NON_TROUVEE_PAR_NOM(
			HttpStatus.NOT_FOUND,
			"BNQ-0001",
			"Aucune banque de nom '%s' n'a été trouvée"),

	/**
			HttpStatus.BAD_REQUEST,
			"BNQ-0002",
			"La banque de nom '%s' ne peut être consultée"),
	 */
	BANQUE_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BNQ-0002",
			"La banque de nom '%s' ne peut être consultée"),

	/**
			HttpStatus.BAD_REQUEST,
			"BNQ-0003",
			"La banque ne peut être créée"),
	 */
	BANQUE_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BNQ-0003",
			"La banque ne peut être créée"),

	/**
			HttpStatus.BAD_REQUEST,
			"BNQ-0004",
			"La banque de nom '%s' ne peut être modifiée"),
	 */
	BANQUE_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BNQ-0004",
			"La banque de nom '%s' ne peut être modifiée"),

	/**
			HttpStatus.BAD_REQUEST,
			"RQST-BNQ-0005",
			"La banque de nom '%s' ne peut être supprimée"),
	 */
	BANQUE_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BNQ-0005",
			"La banque de nom '%s' ne peut être supprimée"),

	/**
			HttpStatus.BAD_REQUEST,
			"BNQ-0006",
			"Une banque doit obligatoirement avoir un nom"),
	 */
	BANQUE_NOM_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"BNQ-0006",
			"Une banque doit obligatoirement avoir un nom"),

	/**
			HttpStatus.NOT_FOUND,
			"BEN-0001",
			"Aucun bénéficiaire de nom '%s' n'a été trouvé"),
	 */
	BENEFICIAIRE_NON_TROUVE_PAR_NOM(
			HttpStatus.NOT_FOUND,
			"BEN-0001",
			"Aucun bénéficiaire de nom '%s' n'a été trouvé"),

	/**
			HttpStatus.BAD_REQUEST,
			"BEN-0002",
			"Le bénéficiaire de nom '%s' ne peut être consulté"),
	 */
	BENEFICIAIRE_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BEN-0002",
			"Le bénéficiaire de nom '%s' ne peut être consulté"),

	/**
			HttpStatus.BAD_REQUEST,
			"BEN-0003",
			"Le bénéficiaire ne peut être créé"),
	 */
	BENEFICIAIRE_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BEN-0003",
			"Le bénéficiaire ne peut être créé"),

	/**
			HttpStatus.BAD_REQUEST,
			"BEN-0004",
			"Le bénéficiaire de nom '%s' ne peut être modifié"),
	 */
	BENEFICIAIRE_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BEN-0004",
			"Le bénéficiaire de nom '%s' ne peut être modifié"),

	/**
			HttpStatus.BAD_REQUEST,
			"BEN-0005",
			"Le bénéficiaire de nom '%s' ne peut être supprimé"),
	 */
	BENEFICIAIRE_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"BEN-0005",
			"Le bénéficiaire de nom '%s' ne peut être supprimé"),

	/**
			HttpStatus.BAD_REQUEST,
			"BEN-0006",
			"Un bénéficiaire doit obligatoirement avoir un nom"),
	 */
	BENEFICIAIRE_NOM_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"BEN-0006",
			"Un bénéficiaire doit obligatoirement avoir un nom"),

	/**
			HttpStatus.NOT_FOUND,
			"CAT-0001",
			"Aucune categorie de nom '%s' n'a été trouvée"),
	 */
	CATEGORIE_NON_TROUVEE_PAR_NOM(
			HttpStatus.NOT_FOUND,
			"CAT-0001",
			"Aucune categorie de nom '%s' n'a été trouvée"),

	/**
			HttpStatus.BAD_REQUEST,
			"CAT-0002",
			"La catégorie de nom '%s' ne peut être consultée"),
	 */
	CATEGORIE_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CAT-0002",
			"La catégorie de nom '%s' ne peut être consultée"),

	/**
			HttpStatus.BAD_REQUEST,
			"CAT-0003",
			"La catégorie de nom '%s' ne peut être créée"),
	 */
	CATEGORIE_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CAT-0003",
			"La catégorie de nom '%s' ne peut être créée"),

	/**
			HttpStatus.BAD_REQUEST,
			"CAT-0004",
			"La catégorie de nom '%s' ne peut être modifiée"),
	 */
	CATEGORIE_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CAT-0004",
			"La catégorie de nom '%s' ne peut être modifiée"),

	/**
			HttpStatus.BAD_REQUEST,
			"CAT-0005",
			"La catégorie de nom '%s' ne peut être supprimée"),
	 */
	CATEGORIE_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CAT-0005",
			"La catégorie de nom '%s' ne peut être supprimée"),

	/**
			HttpStatus.BAD_REQUEST,
			"CAT-0006",
			"Une catégorie doit obligatoirement avoir un nom"),
	 */
	CATEGORIE_NOM_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"CAT-0006",
			"Une catégorie doit obligatoirement avoir un nom"),

	/**
			HttpStatus.NOT_FOUND,
			"SSC-0001",
			"Aucune sous-categorie de nom '%s' n'a été trouvée"), 
	 */
	SOUS_CATEGORIE_NON_TROUVEE_PAR_NOM(
			HttpStatus.NOT_FOUND,
			"SSC-0001",
			"Aucune sous-categorie de nom '%s' n'a été trouvée"), 

	/**
			HttpStatus.BAD_REQUEST,
			"SSC-0002",
			"La sous-catégorie de nom '%s' ne peut être consultée"),
	 */
	SOUS_CATEGORIE_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"SSC-0002",
			"La sous-catégorie de nom '%s' ne peut être consultée"),

	/**
			HttpStatus.BAD_REQUEST,
			"SSC-0003",
			"La sous-catégorie de nom '%s' ne peut être créée"),
	 */
	SOUS_CATEGORIE_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"SSC-0003",
			"La sous-catégorie de nom '%s' ne peut être créée"),

	/**
			HttpStatus.BAD_REQUEST,
			"SSC-0004",
			"La sous-catégorie de nom '%s' ne peut être modifiée"),
	 */
	SOUS_CATEGORIE_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"SSC-0004",
			"La sous-catégorie de nom '%s' ne peut être modifiée"),

	/**
			HttpStatus.BAD_REQUEST,
			"SSC-0005",
			"La sous-categorie de nom '%s' ne peut être supprimée"),
	 */
	SOUS_CATEGORIE_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"SSC-0005",
			"La sous-categorie de nom '%s' ne peut être supprimée"),

	/**
			HttpStatus.BAD_REQUEST,
			"SSC-0006",
			"Une sous-catégorie doit obligatoirement avoir un nom"),
	 */
	SOUS_CATEGORIE_NOM_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"SSC-0006",
			"Une sous-catégorie doit obligatoirement avoir un nom"),

	/**
			HttpStatus.BAD_REQUEST,
			"SSC-0007",
			"Une sous-catégorie doit obligatoirement faire partie d'une catégorie"),
	 */
	SOUS_CATEGORIE_NOM_CATEGORIE_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"SSC-0007",
			"Une sous-catégorie doit obligatoirement faire partie d'une catégorie"),

	/**
			HttpStatus.NOT_FOUND,
			"TIT-0001",
			"Aucun titulaire de nom '%s' n'a été trouvé"),
	 */
	TITULAIRE_NON_TROUVE_PAR_NOM(
			HttpStatus.NOT_FOUND,
			"TIT-0001",
			"Aucun titulaire de nom '%s' n'a été trouvé"),

	/**
			HttpStatus.BAD_REQUEST,
			"TIT-0002",
			"Le titulaire de nom '%s' ne peut être consulté"),
	 */
	TITULAIRE_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"TIT-0002",
			"Le titulaire de nom '%s' ne peut être consulté"),

	/**
			HttpStatus.BAD_REQUEST,
			"TIT-0003",
			"Le titulaire de nom '%s' ne peut être créé"), 
	 */
	TITULAIRE_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"TIT-0003",
			"Le titulaire de nom '%s' ne peut être créé"), 

	/**
			HttpStatus.BAD_REQUEST,
			"TIT-0004",
			"Le titulaire de nom '%s' ne peut être modifié"),
	 */
	TITULAIRE_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"TIT-0004",
			"Le titulaire de nom '%s' ne peut être modifié"),

	/**
			HttpStatus.BAD_REQUEST,
			"TIT-0005",
			"Le titulaire de nom '%s' ne peut être supprimé"),
	 */
	TITULAIRE_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"TIT-0005",
			"Le titulaire de nom '%s' ne peut être supprimé"),

	/**
			HttpStatus.BAD_REQUEST,
			"TIT-0006",
			"Un titulaire doit obligatoirement avoir un nom"),
	 */
	TITULAIRE_NOM_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"TIT-0006",
			"Un titulaire doit obligatoirement avoir un nom"),

	/** ------------------------------------------- */

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-0001",
			"Le paramètre /{identifiant} est obligatoire à la fin de l'url de la requête"),
	 */
	PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"CPT-0001",
			"Le paramètre /{identifiant} est obligatoire à la fin de l'url de la requête"),

	/**
			HttpStatus.NOT_FOUND,
			"CPT-0002",
			"Aucun compte (ni interne, ni tiers) d'identifiant '%s' n'a été trouvé"),
	 */
	COMPTE_NON_TROUVE_PAR_IDENTIFIANT(
			HttpStatus.NOT_FOUND,
			"CPT-0002",
			"Aucun compte (ni interne, ni tiers) d'identifiant '%s' n'a été trouvé"),

	/**
			HttpStatus.NOT_FOUND,
			"CPT-I-0001",
			"Aucun compte interne d'identifiant '%s' n'a été trouvé"),
	 */
	COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT(
			HttpStatus.NOT_FOUND,
			"CPT-I-0001",
			"Aucun compte interne d'identifiant '%s' n'a été trouvé"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0002",
			"Le compte interne d'identifiant '%s' ne peut être consulté"),
	 */
	COMPTE_INTERNE_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0002",
			"Le compte interne d'identifiant '%s' ne peut être consulté"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0003",
			"Le compte interne d'identifiant '%s' ne peut être créé"), 
	 */
	COMPTE_INTERNE_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0003",
			"Le compte interne d'identifiant '%s' ne peut être créé"), 

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0004",
			"Le compte interne d'identifiant '%s' ne peut être modifié"), 
	 */
	COMPTE_INTERNE_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0004",
			"Le compte interne d'identifiant '%s' ne peut être modifié"), 

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0005",
			"Le compte interne d'identifiant '%s' ne peut être supprimé"), 
	 */
	COMPTE_INTERNE_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0005",
			"Le compte interne d'identifiant '%s' ne peut être supprimé"), 

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0006",
			"Un compte interne doit obligatoirement avoir un identifiant"),
	 */
	COMPTE_INTERNE_IDENTIFIANT_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0006",
			"Un compte interne doit obligatoirement avoir un identifiant"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0007",
			"Un compte interne doit obligatoirement avoir un type"),
	 */
	COMPTE_INTERNE_TYPE_COMPTE_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"CPT-I-0007",
			"Un compte interne doit obligatoirement avoir un type"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0008",
			"Aucun type de compte interne de code '%s' n'a été trouvé"),
	 */
	COMPTE_INTERNE_TYPE_COMPTE_NON_TROUVE_PAR_CODE(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0008",
			"Aucun type de compte interne de code '%s' n'a été trouvé"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0009",
			"Le compte interne d'identifiant '%s' doit avoir une banque"),
	 */
	COMPTE_INTERNE_BANQUE_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0009",
			"Le compte interne d'identifiant '%s' doit avoir une banque"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-I-0010",
			"Le compte interne doit avoir au moins un titulaire"),
	 */
	COMPTE_INTERNE_AU_MOINS_UN_TITULAIRE_REQUIS(
			HttpStatus.BAD_REQUEST,
			"CPT-I-0010",
			"Le compte interne doit avoir au moins un titulaire"),

	
	/**
			HttpStatus.NOT_FOUND,
			"CPT-T-0001",
			"Aucun compte tiers d'identifiant '%s' n'a été trouvé"), 
	 */
	COMPTE_TIERS_NON_TROUVE_PAR_IDENTIFIANT(
			HttpStatus.NOT_FOUND,
			"CPT-T-0001",
			"Aucun compte tiers d'identifiant '%s' n'a été trouvé"), 

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-T-0002",
			"Le compte tiers d'identifiant '%s' ne peut être consulté"),
	 */
	COMPTE_TIERS_CONSULTATION_PROBLEME(			
			HttpStatus.BAD_REQUEST,
			"CPT-T-0002",
			"Le compte tiers d'identifiant '%s' ne peut être consulté"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-T-0003",
			"Le compte tiers d'identifiant '%s' ne peut être créé"), 
	 */
	COMPTE_TIERS_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"CPT-T-0003",
			"Le compte tiers d'identifiant '%s' ne peut être créé"), 

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-T-0004",
			"Le compte tiers d'identifiant '%s' ne peut être modifié"), 
	 */
	COMPTE_TIERS_MODIFICATION_PROBLEME(			
			HttpStatus.BAD_REQUEST,
			"CPT-T-0004",
			"Le compte tiers d'identifiant '%s' ne peut être modifié"), 

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-T-0005",
			"Le compte interne d'identifiant '%s' ne peut être supprimé"),
	 */
	COMPTE_TIERS_SUPPRESSION_PROBLEME(		
			HttpStatus.BAD_REQUEST,
			"CPT-T-0005",
			"Le compte interne d'identifiant '%s' ne peut être supprimé"),

	/**
			HttpStatus.BAD_REQUEST,
			"CPT-T-0006",
			"Un compte tiers doit obligatoirement avoir un identifiant"),
	 */
	COMPTE_TIERS_IDENTIFIANT_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"CPT-T-0006",
			"Un compte tiers doit obligatoirement avoir un identifiant"),


	/** ------------------------------------------- */

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0001",
			"Le paramètre /{numero} est obligatoire à la fin de l'url de la requête"),
	 */
	PATH_VARIABLE_NUMERO_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"OPE-0001",
			"Le paramètre /{numero} est obligatoire à la fin de l'url de la requête"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0002",
			"L'operation de numéro '%s' doit avoir au moins une ligne de détail"),
	 */
	OPERATION_NON_TROUVEE_PAR_NUMERO(
			HttpStatus.BAD_REQUEST,
			"OPE-0002",
			"Aucune opération de numéro '%s' n'a été trouvée"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0003",
			"L'opération de numéro '%s' ne peut être consultée"),
	 */
	OPERATION_CONSULTATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"OPE-0003",
			"L'opération de numéro '%s' ne peut être consultée"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0004",
			"L'opération ne peut être créée"),
	 */
	OPERATION_CREATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"OPE-0004",
			"L'opération ne peut être créée"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0005",
			"L'opération de numéro '%s' ne peut être modifiée"),
	 */
	OPERATION_MODIFICATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"OPE-0005",
			"L'opération de numéro '%s' ne peut être modifiée"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0006",
			"L'opération de numéro '%s' ne peut être supprimée"),
	 */
	OPERATION_SUPPRESSION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"OPE-0006",
			"L'opération de numéro '%s' ne peut être supprimée"),

//	/**
//			HttpStatus.BAD_REQUEST,
//			"OPE-0007",
//			"Une opération doit obligatoirement avoir un numero"), 
//	 */
//	OPERATION_NUMERO_OBLIGATOIRE(			
//			HttpStatus.BAD_REQUEST,
//			"OPE-0007",
//			"Une opération doit obligatoirement avoir un numero"), 

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0008",
			"Une opération doit obligatoirement avoir un type"),
	 */
	OPERATION_TYPE_OPERATION_OBLIGATOIRE(			
			HttpStatus.BAD_REQUEST,
			"OPE-0008",
			"Une opération doit obligatoirement avoir un type"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0009",
			"Aucun type d'opération de code '%s' n'a été trouvé"),
	 */
	TYPE_OPERATION_NON_TROUVE_PAR_CODE(			
			HttpStatus.BAD_REQUEST,
			"OPE-0009",
			"Aucun type d'opération de code '%s' n'a été trouvé"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0010",
			"L'operation de numéro '%s' doit avoir un montant valide"),
	 */
	OPERATION_MONTANT_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"OPE-0010",
			"L'operation de numéro '%s' doit avoir un montant valide"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0011",
			"Une opération doit avoir un compte de recette sur lequel imputer le montant"),	
	 */
	OPERATION_COMPTE_RECETTE_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"OPE-0011",
			"Une opération doit avoir un compte de recette sur lequel imputer le montant"),	

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0012",
			"Une opération doit avoir un compte de dépense sur lequel imputer le montant"),
	 */
	OPERATION_COMPTE_DEPENSE_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"OPE-0012",
			"Une opération doit avoir un compte de dépense sur lequel imputer le montant"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0013",
			"L'operation de numéro '%s' doit avoir au moins une ligne de détail"),
	 */
	OPERATION_AU_MOINS_UNE_LIGNE_DE_DETAIL_REQUISE(
			HttpStatus.BAD_REQUEST,
			"OPE-0013",
			"L'operation de numéro '%s' doit avoir au moins une ligne de détail"),


	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0014",
			"L'opération de numéro '%s' n'a pas de ligne de détail correspondant à la séquence %s"), 
	 */
	DETAIL_OPERATION_NON_TROUVE_PAR_SEQUENCE(
			HttpStatus.BAD_REQUEST,
			"OPE-0014",
			"L'opération de numéro '%s' n'a pas de ligne de détail correspondant à la séquence %s"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0015",
			"Une opération d'ajustement doit obligatoirement mentionner le solde actuel"),
	 */
	OPERATION_AJUSTEMENT_COMPTE_SOLDE_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"OPE-0015",
			"Une opération d'ajustement doit obligatoirement mentionner le solde actuel"),

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0016",
			"L'opération d'ajustement pour le compte interne d'identifiant '%s' ne peut être effectuée"), 
	 */
	OPERATION_AJUSTEMENT_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"OPE-0016",
			"L'opération d'ajustement pour le compte interne d'identifiant '%s' ne peut être effectuée"), 

	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0017",
			"Une opération d'actualisation ou d'évaluation de plus value du compte d'investissement '%s' doit obligatoirement mentionner une valeur globale de référence"),
	 */
	OPERATION_ACTUALISATION_COMPTE_SOLDE_OBLIGATOIRE(
			HttpStatus.BAD_REQUEST,
			"OPE-0017",
			"Une opération d'actualisation ou d'évaluation de plus value du compte d'investissement '%s' doit obligatoirement mentionner une valeur globale de référence"),
	
	/**
			HttpStatus.BAD_REQUEST,
			"OPE-0018",
			"L'opération d'actualisation ou d'évaluation de plus value du compte d'investissement '%s' ne peut être effectuée"),
	 */
	OPERATION_ACTUALISATION_PROBLEME(
			HttpStatus.BAD_REQUEST,
			"OPE-0018",
			"L'opération d'actualisation ou d'évaluation de plus value du compte d'investissement '%s' ne peut être effectuée"),
	
	;

	private final TypeErreur type = TypeErreur.CONTROLE;

	private HttpStatus status;
	private String code;
	private String pattern;

	@Override
	public TypeErreur getType() {
		return type;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage(Object[] values) {
		return String.format(pattern, values);
	}

	private ErreurControle(
			HttpStatus status,
			String code,
			String pattern) {

		this.status = status;
		this.code = code;
		this.pattern = pattern;
	}
}
