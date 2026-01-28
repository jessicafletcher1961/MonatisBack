package fr.colline.monatis.references;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum ReferenceFonctionnelleErreur implements MonatisErreur {

	/**
			"Aucune référence de type '%s' et d'ID %s n'a été trouvée"),
	 */
	NON_TROUVE_PAR_TYPE_ET_ID(
			"Aucune référence de type '%s' et d'ID %s n'a été trouvée"),

	/**
			"Suppression de la banque de nom '%s' : cette banque ne peut être supprimée car elle est associée à %s comptes internes"),
	 */
	SUPPRESSION_BANQUE_AVEC_COMPTES_INTERNES(
			"Suppression de la banque de nom '%s' : cette banque ne peut être supprimée car elle est associée à %s comptes internes"),

	/**
			"Suppression du bénéficiaire de nom '%s' : ce bénéficiaire ne peut être supprimé car il est associé à %s opérations"),
	 */
	SUPPRESSION_BENEFICIAIRE_AVEC_OPERATION(
			"Suppression du bénéficiaire de nom '%s' : ce bénéficiaire ne peut être supprimé car il est associé à %s opérations"),
	
	/**
			"Suppression de la catégorie de nom '%s' : cette catégorie ne peut être supprimée car elle est associée à %s sous-catégories"), 
	 */
	SUPPRESSION_CATEGORIE_AVEC_SOUS_CATEGORIES(
			"Suppression de la catégorie de nom '%s' : cette catégorie ne peut être supprimée car elle est associée à %s sous-catégories"), 
	
	/**
			"Suppression de la sous-catégorie de nom '%s' : cette sous-catégorie ne peut être supprimée car elle est associée à %s opérations"), 
	 */
	SUPPRESSION_SOUS_CATEGORIE_AVEC_OPERATION(
			"Suppression de la sous-catégorie de nom '%s' : cette sous-catégorie ne peut être supprimée car elle est associée à %s opérations"), 
	
	/**
			"Suppression du titulaire de nom '%s' : ce titulaire ne peut être supprimée car il est associé à %s comptes internes"), 
	 */
	SUPPRESSION_TITULAIRE_AVEC_COMPTES_INTERNES(
			"Suppression du titulaire de nom '%s' : ce titulaire ne peut être supprimée car il est associé à %s comptes internes"), 

	;

	private final TypeDomaine typeDomaine = TypeDomaine.REFERENCE;
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
	
	private ReferenceFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}
	
}
