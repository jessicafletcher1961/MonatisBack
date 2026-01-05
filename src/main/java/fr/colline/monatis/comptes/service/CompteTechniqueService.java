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

	private final String IDENTIFIANT_OPERATIONS_REEVALUATIONS = "TECH-OPERATIONS-REEVALUATIONS"; 
	private final String IDENTIFIANT_OPERATIONS_REMUNERATION_ET_FRAIS = "TECH-OPERATIONS-REMUNERATION";
	private final String LIBELLE_COMPTE_REEVALUATION_SOLDE = "Contreparties pour les opérations virtuelles, comme l'enregistrement des plus-values, de moins values ou d'ajustement des soldes";
	
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

	public CompteTechnique rechercherOuCreerCompteTechniqueOperationsReevaluation() throws ServiceException {
		return rechercherOuCreerCompteTechnique(IDENTIFIANT_OPERATIONS_REEVALUATIONS);
	}

	public CompteTechnique rechercherOuCreerCompteTechniqueOperationsRemunerationEtFrais() throws ServiceException {
		return rechercherOuCreerCompteTechnique(IDENTIFIANT_OPERATIONS_REMUNERATION_ET_FRAIS);
	}

	private CompteTechnique rechercherOuCreerCompteTechnique(String identifiantCompteTechnique) throws ServiceException {

		if ( identifiantCompteTechnique == null ) {
			identifiantCompteTechnique = IDENTIFIANT_OPERATIONS_REEVALUATIONS;
		}
		
		Compte compte = compteGeneriqueService.rechercherParIdentifiant(identifiantCompteTechnique);
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
			
			compteTechnique.setIdentifiant(identifiantCompteTechnique);
			compteTechnique.setLibelle(LIBELLE_COMPTE_REEVALUATION_SOLDE);
			
			return creerCompte(compteTechnique);
		}
	}

}
