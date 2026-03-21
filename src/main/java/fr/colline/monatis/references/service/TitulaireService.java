package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.repository.ReferenceRepository;
import fr.colline.monatis.references.repository.TitulaireRepository;

@Service
public class TitulaireService extends ReferenceService<Titulaire>{

	@Autowired private TitulaireRepository repository;
	
	@Override 
	protected ReferenceRepository<Titulaire> getRepository() {
		
		return repository;
	}

	@Override
	protected Class<Titulaire> getTClass() {
		
		return Titulaire.class;
	}

	/**
	 * Vérifie qu'aucun compte interne n'est rattachée à ce titulaire
	 *  
	 * @param titulaireId l'id du titulaire à supprimer
	 * @return le titulaire à supprimer récupéré en base
	 * @throws ServiceTException si le titulaire ne peut pas être supprimé
	 */
	@Override
	protected Titulaire controlerEtPreparerPourSuppression(Long titulaireId) throws ServiceException {
		
		Titulaire titulaire = super.controlerEtPreparerPourSuppression(titulaireId);

		verifierAbsenceCompteInterneAssocie(titulaire);
		
		return titulaire;
	}
	
	private void verifierAbsenceCompteInterneAssocie(Titulaire titulaire) throws ServiceException {
		
		if ( titulaire.getComptesInternes() != null 
				&& ! titulaire.getComptesInternes().isEmpty() ) {
			throw new ServiceException(
					ErreurFonctionnelle.TITULAIRE_SUPPRESSION_AVEC_COMPTES_INTERNES,
					titulaire.getNom(),
					titulaire.getComptesInternes().size());
		}
	}
}
