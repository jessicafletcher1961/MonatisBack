package fr.colline.monatis.comptes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.CompteFonctionnelleErreur;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.comptes.repository.CompteRepository;
import fr.colline.monatis.comptes.repository.CompteTechniqueRepository;
import fr.colline.monatis.exceptions.ServiceException;

@Service
public class CompteTechniqueService extends CompteService<CompteTechnique> {

	@Autowired private CompteTechniqueRepository compteTechniqueRepository;
	@Autowired private CompteGeneriqueService compteGeneriqueService;
	
	@Override
	public Class<CompteTechnique> getTClass() {
		return CompteTechnique.class;
	}

	@Override
	public CompteRepository<CompteTechnique> getRepository() {
		return compteTechniqueRepository;
	}

	public CompteTechnique rechercherOuCreerCompteTechniqueEvaluation() throws ServiceException {
		return rechercherOuCreerCompteTechnique("TECH-EVALUATIONS", "Contrepartie des opérations virtuelles");
	}

	public CompteTechnique rechercherOuCreerCompteTechniqueFrais() throws ServiceException {
		return rechercherOuCreerCompteTechnique("TECH-FRAIS", "Contrepartie des opérations de frais prélevés directement sur un compte par le gestionnaire de ce compte (agios, frais divers...)");
	}

	public CompteTechnique rechercherOuCreerCompteTechniqueRemuneration() throws ServiceException {
		return rechercherOuCreerCompteTechnique("TECH-REMUNERATION", "Contrepartie des rémunérations directement versées sur un compte financier par le gestionnaire de ce compte (dividendes, intérêts...)");
	}

	private CompteTechnique rechercherOuCreerCompteTechnique(String identifiant, String libelle) throws ServiceException {

		Compte compte = compteGeneriqueService.rechercherParIdentifiant(identifiant);
		if ( compte != null ) {
			if ( CompteTechnique.class.isAssignableFrom(compte.getClass()) ) {
				// Un compte technique avec cet identifiant existe déjà
				return (CompteTechnique) compte;
			}
			else {
				// Un compte avec cet identifiant existe déjà mais ce n'est pas un compte technique
				throw new ServiceException(
						CompteFonctionnelleErreur.IDENTIFIANT_COMPTE_TECHNIQUE_DEJA_UTILISE,
						compte.getIdentifiant(),
						compte.getClass().getSimpleName());
			}
		}
		else {
			// Création du compte technique
			CompteTechnique compteTechnique = new CompteTechnique();
			
			compteTechnique.setIdentifiant(identifiant);
			compteTechnique.setLibelle(libelle);
			
			return creerCompte(compteTechnique);
		}
	}

}
