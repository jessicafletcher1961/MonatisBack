package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.ReferenceFonctionnelleErreur;
import fr.colline.monatis.references.ReferenceTechniqueErreur;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.repository.BeneficiaireRepository;

@Service
public class BeneficiaireService extends ReferenceService<Beneficiaire>{

	@Autowired private BeneficiaireRepository beneficiaireRepository;

	@Override
	public Class<Beneficiaire> getTClass() {
		return Beneficiaire.class;
	}

	@Override
	public BeneficiaireRepository getRepository() {
		return beneficiaireRepository;
	}
	
	@Override
	protected Beneficiaire controlerEtPreparerPourSuppression(Beneficiaire beneficiaire) 
			throws ServiceException {
		
		beneficiaire = super.controlerEtPreparerPourSuppression(beneficiaire);
		
		verifierAbsenceDetailOperationAssocie(beneficiaire);

		return beneficiaire;
	}
	
	private void verifierAbsenceDetailOperationAssocie(Beneficiaire beneficiaire) 
			throws ServiceException {

		int nombreDetailOperation;
		try {
			nombreDetailOperation = beneficiaireRepository.compterOperationDetailParBeneficiaireId(beneficiaire.getId());
		}
		catch ( Throwable t ) {
			throw new ServiceException(
					ReferenceTechniqueErreur.COMPTAGE_USAGE_PAR_ID,
					Beneficiaire.class.getSimpleName(),
					beneficiaire.getId());
		}

		if ( nombreDetailOperation > 0 ) {
			throw new ServiceException(
					ReferenceFonctionnelleErreur.SUPPRESSION_BENEFICIAIRE_AVEC_OPERATION_DETAILS, 
					beneficiaire.getNom(),
					nombreDetailOperation);
		}
	}

}
