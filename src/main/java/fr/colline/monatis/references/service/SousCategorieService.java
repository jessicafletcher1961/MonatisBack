package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.exceptions.erreurs.ErreurTechnique;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.repository.ReferenceRepository;
import fr.colline.monatis.references.repository.SousCategorieRepository;

@Service 
public class SousCategorieService extends ReferenceService<SousCategorie> {

	@Autowired private SousCategorieRepository repository;

	@Override
	protected ReferenceRepository<SousCategorie> getRepository() {

		return repository;
	}

	@Override
	protected Class<SousCategorie> getTClass() {

		return SousCategorie.class;
	}

	@Override
	protected SousCategorie controlerEtPreparerPourSuppression(Long sousCategorieId) 
			throws ServiceException {

		SousCategorie sousCategorie = super.controlerEtPreparerPourSuppression(sousCategorieId);

		verifierAbsenceDetailOperationAssocie(sousCategorie);

		return sousCategorie;
	}

	private void verifierAbsenceDetailOperationAssocie(SousCategorie sousCategorie) throws ServiceException {

		int nombreDetailOperationAssocie;
		try {
			nombreDetailOperationAssocie = repository.countDetailOperationParSousCategorieId(sousCategorie.getId());
		}
		catch ( Throwable t ) {
			throw new ServiceException(
					t,
					ErreurTechnique.TECH_RECHERCHE_NOMBRE_DETAIL_OPERATION_PAR_SOUS_CATEGORIE_ID,
					sousCategorie.getId());
		}

		if ( nombreDetailOperationAssocie > 0 ) {
			throw new ServiceException(
					ErreurFonctionnelle.SOUS_CATEGORIE_SUPPRESSION_AVEC_DETAIL_OPERATION, 
					sousCategorie.getNom(),
					nombreDetailOperationAssocie);
		}
	}
}