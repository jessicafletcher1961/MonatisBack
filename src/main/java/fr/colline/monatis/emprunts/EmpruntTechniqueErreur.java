package fr.colline.monatis.emprunts;

import fr.colline.monatis.exceptions.MonatisErreur;
import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

public enum EmpruntTechniqueErreur implements MonatisErreur {
	
	RECHERCHE_PAR_ID(
			"Un problème technique est survenu lors de la recherche de l'emprunt d'ID %s"),
	
	EXISTENCE_PAR_ID(
			"Un problème technique est survenu lors de la vérification de l'existence de l'emprunt d'ID %s"),
	
	RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la recherche de l'emprunt '%s'"),
	
	EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL(
			"Un problème technique est survenu lors de la vérification de l'existence de l'emprunt '%s'"),
	
	RECHERCHE_TOUS(
			"Un problème technique est survenu lors de la recherche de toua les emprunts"),
	
	SUPPRESSION_TOUS(
			"Un problème technique est survenu lors de la suppression de tous les emprunts"),
	
	ENREGISTREMENT(
			"Un problème technique est survenu lors de l'enregistrement de l'emprunt"),
	
	SUPPRESSION(
			"Un problème technique est survenu lors de la suppression de l'emprunt '%s'")
	
	;

	private final TypeDomaine typeDomaine = TypeDomaine.EMPRUNT;
	private final TypeErreur typeErreur = TypeErreur.TECHNIQUE;

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
	
	private EmpruntTechniqueErreur(String pattern) {
		this.pattern = pattern;
	}
}
