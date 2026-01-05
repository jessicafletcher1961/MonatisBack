package fr.colline.monatis.rapports;

import fr.colline.monatis.erreurs.MonatisErreur;
import fr.colline.monatis.erreurs.TypeDomaine;
import fr.colline.monatis.erreurs.TypeErreur;

public enum RapportFonctionnelleErreur implements MonatisErreur {
	
	;
		
	private final TypeDomaine typeDomaine = TypeDomaine.RAPPORT;
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

	private RapportFonctionnelleErreur(String pattern) {
		this.pattern = pattern;
	}

}
