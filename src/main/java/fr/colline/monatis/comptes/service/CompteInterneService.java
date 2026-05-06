package fr.colline.monatis.comptes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.CompteFonctionnelleErreur;
import fr.colline.monatis.comptes.CompteTechniqueErreur;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.repository.CompteInterneRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.typologies.model.TypeFonctionnement;

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

	public List<CompteInterne> rechercherParTypeFonctionnement(TypeFonctionnement typeFonctionnement) throws ServiceException {

		try {
			return compteInterneRepository.findByTypeFonctionnementOrderByIdentifiant(typeFonctionnement);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					CompteTechniqueErreur.RECHERCHE_PAR_TYPE_FONCTIONNEMENT,
					typeFonctionnement.getCode(),
					typeFonctionnement.getLibelle());
		}
	}
	
	protected CompteInterne controlerEtPreparerPourCreation(CompteInterne compte) throws ServiceException {
		
		super.controlerEtPreparerPourCreation(compte);
		
		verifierDateCloture(compte);
		
		return compte;
	}
	
	protected CompteInterne controlerEtPreparerPourModification(CompteInterne compte) throws ServiceException {
		
		super.controlerEtPreparerPourModification(compte);
		
		verifierDateCloture(compte);
		
		return compte;
	}
	
	protected CompteInterne controlerEtPreparerPourSuppression(CompteInterne compte) throws ServiceException {

		super.controlerEtPreparerPourSuppression(compte);
		
		verifierAbsenceEvaluationAssociee(compte.getId());
		
		return compte;
	}
	
	private void verifierDateCloture(CompteInterne compte) throws ServiceException {

		if ( compte.getDateCloture() != null ) {

			if ( compte.getDateCloture().isBefore(compte.getDateSoldeInitial()) ) {
				throw new ServiceException (
						CompteFonctionnelleErreur.DATE_CLOTURE_AVANT_DATE_SOLDE_INITIAL,
						compte.getDateCloture(),
						compte.getIdentifiant(),
						compte.getDateSoldeInitial());
			}
			
			try {
				int nombreOperationApresDateCloture = getRepository().compterOperationByCompteIdApresDate(
						compte.getId(), 
						compte.getDateCloture());
				if ( nombreOperationApresDateCloture > 0 ) {
					throw new ServiceException (
							CompteFonctionnelleErreur.OPERATIONS_APRES_DATE_CLOTURE,
							compte.getDateCloture(),
							compte.getIdentifiant(),
							nombreOperationApresDateCloture);
				}
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						CompteTechniqueErreur.COMPTAGE_OPERATION_APRES_DATE_CLOTURE,
						compte.getDateCloture(),
						compte.getIdentifiant());
			}
		}
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
