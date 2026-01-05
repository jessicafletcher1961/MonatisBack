package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.ReferenceFonctionnelleErreur;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.repository.BanqueRepository;

@Service
public class BanqueService extends ReferenceService<Banque> {

	@Autowired private BanqueRepository banqueRepository;
	
	@Override
	public Class<Banque> getTClass() {
		return Banque.class;
	}

	@Override
	public BanqueRepository getRepository() {
		return banqueRepository;
	}

	@Override
	protected Banque controlerEtPreparerPourSuppression(Banque banque) throws ServiceException {
	
		banque = super.controlerEtPreparerPourSuppression(banque);
		
		verifierAbsenceCompteInterneAssocie(banque);
		
		return banque;
	}
	
	private void verifierAbsenceCompteInterneAssocie(Banque banque) throws ServiceException {

		if ( banque.getComptesInternes() != null
				&& ! banque.getComptesInternes().isEmpty() ) {
			throw new ServiceException(
					ReferenceFonctionnelleErreur.SUPPRESSION_BANQUE_AVEC_COMPTES_INTERNES,
					banque.getNom(),
					banque.getComptesInternes().size());
		}
	}
}
