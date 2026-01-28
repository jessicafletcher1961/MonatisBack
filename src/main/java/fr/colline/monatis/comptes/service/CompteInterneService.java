package fr.colline.monatis.comptes.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.CompteFonctionnelleErreur;
import fr.colline.monatis.comptes.CompteTechniqueErreur;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.repository.CompteInterneRepository;
import fr.colline.monatis.exceptions.ServiceException;

@Service
public class CompteInterneService extends CompteService<CompteInterne> {

	@Autowired private CompteInterneRepository compteInterneRepository;
	
	@Override
	public Class<CompteInterne> getTClass() {
		
		return CompteInterne.class;
	}

	@Override
	public CompteInterneRepository getRepository() {

		return compteInterneRepository;
	}

	public Set<CompteInterne> rechercherParTypeFonctionnement(TypeFonctionnement typeFonctionnement) throws ServiceException {

		try {
			return compteInterneRepository.findByTypeFonctionnement(typeFonctionnement);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					CompteTechniqueErreur.RECHERCHE_PAR_TYPE_FONCTIONNEMENT,
					typeFonctionnement.getCode(),
					typeFonctionnement.getLibelle());
		}
	}

	protected CompteInterne controlerEtPreparerPourSuppression(CompteInterne compte) throws ServiceException {

		super.controlerEtPreparerPourSuppression(compte);
		
		verifierAbsenceEvaluationAssociee(compte.getId());
		
		return compte;
	}
	
	private void verifierAbsenceEvaluationAssociee(Long id) throws ServiceException {
	
		int nombreEvaluationAssociee;
		try {
			nombreEvaluationAssociee = getRepository().compterEvaluationByCompteId(id);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					CompteTechniqueErreur.COMPTAGE_USAGE_EVALUATION_PAR_ID,
					id);
		}
		
		if ( nombreEvaluationAssociee > 0 ) {
			throw new ServiceException (
					CompteFonctionnelleErreur.SUPPRESSION_COMPTE_AVEC_EVALUATION,
					id,
					nombreEvaluationAssociee);
		}
	}
}
