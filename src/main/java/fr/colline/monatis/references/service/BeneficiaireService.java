package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.exceptions.erreurs.ErreurTechnique;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.repository.BeneficiaireRepository;
import fr.colline.monatis.references.repository.ReferenceRepository;

@Service
public class BeneficiaireService extends ReferenceService<Beneficiaire> {

	@Autowired private BeneficiaireRepository repository;
	
	@Override
	protected ReferenceRepository<Beneficiaire> getRepository() {

		return repository;
	}

	@Override
	protected Class<Beneficiaire> getTClass() {
		
		return Beneficiaire.class;
	}
	
	@Override
	protected Beneficiaire controlerEtPreparerPourSuppression(Long beneficiaireId) 
			throws ServiceException {
		
		Beneficiaire beneficiaire = super.controlerEtPreparerPourSuppression(beneficiaireId);
		
		verifierAbsenceDetailOperationAssocie(beneficiaire);

		return beneficiaire;
	}
	
	private void verifierAbsenceDetailOperationAssocie(Beneficiaire beneficiaire) 
			throws ServiceException {

		int nombreDetailOperation;
		try {
			nombreDetailOperation = repository.compterDetailOperationParBeneficiaireId(beneficiaire.getId());
		}
		catch ( Throwable t ) {
			throw new ServiceException(
					ErreurTechnique.TECH_RECHERCHE_NOMBRE_DETAIL_OPERATION_PAR_BENEFICIAIRE_ID,
					beneficiaire.getId());
		}

		if ( nombreDetailOperation > 0 ) {
			throw new ServiceException(
					ErreurFonctionnelle.BENEFICIAIRE_SUPPRESSION_AVEC_DETAIL_OPERATION, 
					beneficiaire.getNom(),
					nombreDetailOperation);
		}
	}
}
