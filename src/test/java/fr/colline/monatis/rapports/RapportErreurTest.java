package fr.colline.monatis.rapports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import fr.colline.monatis.exceptions.TypeDomaine;
import fr.colline.monatis.exceptions.TypeErreur;

class RapportErreurTest {

	@Test
	void erreursDeControleExposentLeDomaineLeTypeLeCodeEtLePattern() {
		for (RapportControleErreur erreur : RapportControleErreur.values()) {
			assertEquals(TypeDomaine.RAPPORT, erreur.getTypeDomaine());
			assertEquals(TypeErreur.CONTROLE, erreur.getTypeErreur());
			assertNotNull(erreur.getCode());
			assertFalse(erreur.getCode().isBlank());
			assertNotNull(erreur.getPrefixe());
			assertFalse(erreur.getPrefixe().isBlank());
			assertNotNull(erreur.getPattern());
			assertFalse(erreur.getPattern().isBlank());
		}
	}

	@Test
	void erreursFonctionnellesExposentLeDomaineLeTypeLeCodeEtLePattern() {
		for (RapportFonctionnelleErreur erreur : RapportFonctionnelleErreur.values()) {
			assertEquals(TypeDomaine.RAPPORT, erreur.getTypeDomaine());
			assertEquals(TypeErreur.FONCTIONNELLE, erreur.getTypeErreur());
			assertNotNull(erreur.getCode());
			assertFalse(erreur.getCode().isBlank());
			assertNotNull(erreur.getPrefixe());
			assertFalse(erreur.getPrefixe().isBlank());
			assertNotNull(erreur.getPattern());
			assertFalse(erreur.getPattern().isBlank());
		}
	}
}
