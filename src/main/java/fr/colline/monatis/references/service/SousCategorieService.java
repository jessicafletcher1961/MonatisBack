package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.ReferenceFonctionnelleErreur;
import fr.colline.monatis.references.ReferenceTechniqueErreur;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.repository.SousCategorieRepository;

@Service
public class SousCategorieService extends ReferenceService<SousCategorie> {

	@Autowired private SousCategorieRepository sousCategorieRepository;

	@Override
	public Class<SousCategorie> getTClass() {
		return SousCategorie.class;
	}

	@Override
	public SousCategorieRepository getRepository() {
		return sousCategorieRepository;
	}

	@Override
	protected SousCategorie controlerEtPreparerPourSuppression(SousCategorie sousCategorie) 
			throws ServiceException {

		sousCategorie = super.controlerEtPreparerPourSuppression(sousCategorie);

		verifierAbsenceDetailOperationAssocie(sousCategorie);

		return sousCategorie;
	}

	private void verifierAbsenceDetailOperationAssocie(SousCategorie sousCategorie) throws ServiceException {

		int nombreDetailOperationAssocie;
		try {
			nombreDetailOperationAssocie = sousCategorieRepository.compterOperationLigneParSousCategorieId(sousCategorie.getId());
		}
		catch ( Throwable t ) {
			throw new ServiceException(
					t,
					ReferenceTechniqueErreur.COMPTAGE_USAGE_PAR_ID,
					SousCategorie.class.getSimpleName(),
					sousCategorie.getId());
		}

		if ( nombreDetailOperationAssocie > 0 ) {
			throw new ServiceException(
					ReferenceFonctionnelleErreur.SUPPRESSION_SOUS_CATEGORIE_AVEC_OPERATION, 
					sousCategorie.getNom(),
					nombreDetailOperationAssocie);
		}
	}

}
