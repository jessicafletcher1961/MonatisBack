package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.ReferenceFonctionnelleErreur;
import fr.colline.monatis.references.ReferenceTechniqueErreur;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.repository.CategorieRepository;

@Service
public class CategorieService extends ReferenceService<Categorie> {

	@Autowired private CategorieRepository categorieRepository;

	@Override
	public Class<Categorie> getTClass() {
		return Categorie.class;
	}

	@Override
	public CategorieRepository getRepository() {
		return categorieRepository;
	}

	@Override
	protected Categorie controlerEtPreparerPourSuppression(Categorie categorie) throws ServiceException {
		
		categorie = super.controlerEtPreparerPourSuppression(categorie);

		verifierAbsenceSousCategorieAssociee(categorie);
		verifierAbsenceBudgetAssocie(categorie);
		
		return categorie;
	}
	
	private void verifierAbsenceSousCategorieAssociee(Categorie categorie) throws ServiceException {
		
		if ( categorie.getSousCategories() != null
				&& ! categorie.getSousCategories().isEmpty() ) {
			throw new ServiceException(
					ReferenceFonctionnelleErreur.SUPPRESSION_CATEGORIE_AVEC_SOUS_CATEGORIES,
					categorie.getNom(),
					categorie.getSousCategories().size());
		}
	}

	private void verifierAbsenceBudgetAssocie(Categorie categorie) throws ServiceException {

		int nombreBudgetAssocie;
		try {
			nombreBudgetAssocie = categorieRepository.compterBudgetParReferenceId(categorie.getId());
		}
		catch ( Throwable t ) {
			throw new ServiceException(
					t,
					ReferenceTechniqueErreur.COMPTAGE_USAGE_PAR_ID,
					SousCategorie.class.getSimpleName(),
					categorie.getId());
		}

		if ( nombreBudgetAssocie > 0 ) {
			throw new ServiceException(
					ReferenceFonctionnelleErreur.SUPPRESSION_SOUS_CATEGORIE_AVEC_BUDGET, 
					categorie.getNom(),
					nombreBudgetAssocie);
		}
	}

}
