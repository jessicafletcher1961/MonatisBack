package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.repository.CategorieRepository;
import fr.colline.monatis.references.repository.ReferenceRepository;

@Service
public class CategorieService extends ReferenceService<Categorie>{

	@Autowired private CategorieRepository repository;

	@Override
	protected ReferenceRepository<Categorie> getRepository() {

		return repository;
	}

	@Override
	protected Class<Categorie> getTClass() {
		
		return Categorie.class;
	}

	@Override
	protected Categorie controlerEtPreparerPourSuppression(Long categorieId) throws ServiceException {
		
		Categorie categorie = super.controlerEtPreparerPourSuppression(categorieId);

		verifierAbsenceSousCategorieAssociee(categorie);
		
		return categorie;
	}
	
	private void verifierAbsenceSousCategorieAssociee(Categorie categorie) throws ServiceException {
		
		if ( categorie.getSousCategories() != null
				&& ! categorie.getSousCategories().isEmpty() ) {
			throw new ServiceException(
					ErreurFonctionnelle.CATEGORIE_SUPPRESSION_AVEC_SOUS_CATEGORIES,
					categorie.getNom(),
					categorie.getSousCategories().size());
		}
	}
}
