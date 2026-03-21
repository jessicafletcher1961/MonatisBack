package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.ReferenceFonctionnelleErreur;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.repository.TitulaireRepository;

@Service
public class TitulaireService extends ReferenceService<Titulaire> {

	@Autowired private TitulaireRepository titulaireRepository; 

	@Override
	public Class<Titulaire> getTClass() {
		return Titulaire.class;
	}

	@Override
	public TitulaireRepository getRepository() {
		return titulaireRepository;
	}

	@Override
	protected Titulaire controlerEtPreparerPourSuppression(Titulaire titulaire) throws ServiceException {
		
		titulaire = super.controlerEtPreparerPourSuppression(titulaire);

		verifierAbsenceCompteInterneAssocie(titulaire);
		
		return titulaire;
	}
	
	private void verifierAbsenceCompteInterneAssocie(Titulaire titulaire) throws ServiceException {
		
		if ( titulaire.getComptesInternes() != null 
				&& ! titulaire.getComptesInternes().isEmpty() ) {
			throw new ServiceException(
					ReferenceFonctionnelleErreur.SUPPRESSION_TITULAIRE_AVEC_COMPTES_INTERNES,
					titulaire.getNom(),
					titulaire.getComptesInternes().size());
		}
	}

}
