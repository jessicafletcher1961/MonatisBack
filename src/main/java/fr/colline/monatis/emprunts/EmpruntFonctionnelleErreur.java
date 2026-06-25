package fr.colline.monatis.emprunts;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum EmpruntFonctionnelleErreur implements MonatisErreur {
	
	/**
			"Le compte interne d'identifiant '%s' lié à cet emprunt n'est pas un compte financier."), 
	 */
	COMPTE_INTERNE_TYPE_FONCTIONNEMENT_INVALIDE(
			"Le compte interne d'identifiant '%s' lié à cet emprunt n'est pas un compte financier."), 
	
	/**
			"Pour l'échéance n° %s, la somme des intérêts calculés et des frais fixes (%s) dépasse le montant total déclaré d'une échéance (%s)."),
	 */
	MONTANT_TOTAL_ECHEANCE_INSUFFISANT(
			"Pour l'échéance n° %s, la somme des intérêts calculés et des frais fixes (%s) dépasse le montant total déclaré d'une échéance (%s)."),
	
	/**
			"Le prêt est entièrement remboursé à l'échéance n° %s, or le nombre total d'échéances déclaré est de %s."),
	 */
	NOMBRE_TOTAL_ECHEANCES_EXCESSIF(
			"Le prêt est entièrement remboursé à l'échéance n° %s, or le nombre total d'échéances déclaré est de %s."),
	
	/**
			"La révision des conditions de l'emprunt est définie comme applicable à partir de l'échéance %s, ce qui est impossible car il ne peut y avoir de révision avant l'échéance n° %s."),
	 */
	REVISION_NUMERO_PREMIERE_ECHEANCE_HORS_SEQUENCE(
			"La révision des conditions de l'emprunt est définie comme applicable à partir de l'échéance %s, ce qui est impossible car il ne peut y avoir de révision avant l'échéance n° %s."),
	
	/**
			"La révision des conditions de l'emprunt applicable à partir de l'échéance %s est définie comme applicable au %s, ce qui est impossible car l'échéance précédente est applicable au %s."),
	 */
	REVISION_DATE_PREMIERE_ECHEANCE_HORS_SEQUENCE(
			"La révision des conditions de l'emprunt applicables à partir de l'échéance %s est définie comme applicable au %s, ce qui est impossible car l'échéance précédente est applicable au %s."),
	
	/**
			"L'échéance n° %s ne peut être calculée car sa date d'échéance (%s) serait postérieure à la première date d'échéance de la révision déclarée suivante s'appliquant à partir du %s"),
	*/
	REVISION_DATE_ECHEANCE_HORS_SEQUENCE(
			"L'échéance n° %s ne peut être calculée car sa date d'échéance (%s) serait postérieure à la première date d'échéance de la révision déclarée suivante s'appliquant à partir du %s"),
	 
	/**
			"Les conditions financières de l'emprunt sont obligatoires : montant de l'emprunt, durée des remboursements etc..."), 
	 */
	CONDITION_EMPRUNT_INITIALE_OBLIGATOIRE(
			"Les conditions financières de l'emprunt sont obligatoires : montant de l'emprunt, durée des remboursements etc..."), 
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.EMPRUNT;
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
	
	private EmpruntFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}

}
