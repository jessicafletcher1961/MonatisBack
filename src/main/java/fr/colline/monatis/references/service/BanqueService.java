package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurFonctionnelle;
import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.references.repository.BanqueRepository;
import fr.colline.monatis.references.repository.ReferenceRepository;

@Service
public class BanqueService extends ReferenceService<Banque> {

	@Autowired private BanqueRepository repository;

	@Override
	protected ReferenceRepository<Banque> getRepository() {
		
		return repository;
	}

	@Override
	protected Class<Banque> getTClass() {
		
		return Banque.class;
	}

	/**
	 * Vérifie qu'aucun compte interne n'est rattaché à cette banque
	 *  
	 * @param banqueId l'id de la banque à supprimer
	 * @return la banque à supprimer récupérée en base
	 * @throws ServiceException si le titulaire ne peut être supprimé
	 */
	@Override
	protected Banque controlerEtPreparerPourSuppression(Long banqueId) throws ServiceException {
	
		Banque banque = super.controlerEtPreparerPourSuppression(banqueId);
		
		verifierAbsenceCompteInterneAssocie(banque);
		
		return banque;
	}
	
	private void verifierAbsenceCompteInterneAssocie(Banque banque) throws ServiceException {

		if ( banque.getComptesInternes() != null
				&& ! banque.getComptesInternes().isEmpty() ) {
			throw new ServiceException(
					ErreurFonctionnelle.BANQUE_SUPPRESSION_AVEC_COMPTES_INTERNES,
					banque.getNom(),
					banque.getComptesInternes().size());
		}
	}
}
