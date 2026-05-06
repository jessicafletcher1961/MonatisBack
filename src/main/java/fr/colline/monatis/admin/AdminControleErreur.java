package fr.colline.monatis.admin;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum AdminControleErreur implements MonatisErreur {
	
	/**
			"Le fichier '%s' n'a pas été trouvé"),
	 */
	FICHIER_NON_TROUVE(
			"Le fichier '%s' n'a pas été trouvé"),
	
	/**
			"Le fichier '%s' existe déjà"),
	 */
	FICHIER_DEJA_PRESENT(
			"Le fichier '%s' existe déjà"),
	
	/**
	"		Le fichier de sauvegarde '%s' n'a pas été trouvé"),
	 */
	FICHIER_SAUVEGARDE_INEXISTANT(
			"Le fichier de sauvegarde '%s' n'a pas été trouvé"),
	
	/**
			"Le fichier de sauvegarde '%s' existe déjà"),
	 */
	FICHIER_SAUVEGARDE_EXISTANT(
			"Le fichier de sauvegarde '%s' existe déjà"),
	
	/**
			"Le répertoire de sauvegarde '%s' n'a pas été trouvé"),
	 */
	REPERTOIRE_SAUVEGARDE_INEXISTANT(
			"Le répertoire de sauvegarde '%s' n'a pas été trouvé"),

	;

	private final TypeDomaine typeDomaine = TypeDomaine.ADMIN;
	private final TypeErreur typeErreur = TypeErreur.CONTROLE;

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
	
	private AdminControleErreur(String pattern) {
		this.pattern = pattern;
	}

}
