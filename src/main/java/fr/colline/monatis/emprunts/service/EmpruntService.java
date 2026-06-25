package fr.colline.monatis.emprunts.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.emprunts.EmpruntFonctionnelleErreur;
import fr.colline.monatis.emprunts.EmpruntTechniqueErreur;
import fr.colline.monatis.emprunts.model.ConditionEmprunt;
import fr.colline.monatis.emprunts.model.Echeance;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.emprunts.repository.EmpruntRepository;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
public class EmpruntService {

	record EnchainementRevision (ConditionEmprunt revisionPrecedente, ConditionEmprunt revisionSuivante) {};

	private final static int NOMBRE_JOURS_INTERVALLE_DATE_ECHEANCE = 3;

	@Autowired private EmpruntRepository empruntRepository;
	@Autowired private OperationService operationService;

	public Emprunt rechercherParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la recherche d'un emprunt est obligatoire");

		try {
			Optional<Emprunt> optional = empruntRepository.findById(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EmpruntTechniqueErreur.RECHERCHE_PAR_ID,
					id );
		}
	}

	public boolean isExistantParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la vérification de l'existence d'un emprunt est obligatoire");

		try {
			return empruntRepository.existsById(id);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EmpruntTechniqueErreur.EXISTENCE_PAR_ID,
					id);
		}
	}

	public Emprunt rechercherParCle(String cle) throws ServiceException {

		Assert.notNull(cle, () -> "La CLE pour la recherche d'un emprunt est obligatoire");

		try {
			Optional<Emprunt> optional = empruntRepository.findByCle(cle);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EmpruntTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL,
					cle);
		}
	}

	public boolean isExistantParCle(String cle) throws ServiceException {

		Assert.notNull(cle, () -> "La CLE pour la vérification de l'existence d'un emprunt est obligatoire");

		try {
			return empruntRepository.existsByCle(cle);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					EmpruntTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL,
					cle);
		}
	}

	public List<Emprunt> rechercherTous() throws ServiceException {

		try {
			return empruntRepository.findAll();
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EmpruntTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public void supprimerTous() throws ServiceException {

		try {
			empruntRepository.deleteAll();
			
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EmpruntTechniqueErreur.SUPPRESSION_TOUS);
		}
	}

	public List<Echeance> genererEcheances(Emprunt emprunt) throws ServiceException {
		
		Assert.notNull(emprunt, () -> "L'EMPRUNT pour lequel la génération des échéances est demandée est obligatoire");
		
		List<Echeance> echeances = calculerEcheances(emprunt);
		
		return echeances;
	}

	public Emprunt creerEmprunt(Emprunt emprunt) throws ServiceException {
		
		Assert.notNull(emprunt, () -> "L'EMPRUNT à créer est obligatoire");

		emprunt = controlerEtPreparerPourCreation(emprunt);

		return enregistrer(emprunt);
	}

	public Emprunt modifierEmprunt(Emprunt emprunt) throws ServiceException {
		
		Assert.notNull(emprunt, () -> "L'EMPRUNT à modifier est obligatoire");

		emprunt = controlerEtPreparerPourModification(emprunt);

		return enregistrer(emprunt);
	}

	public void supprimerEmprunt(Emprunt emprunt) throws ServiceException {
		
		Assert.notNull(emprunt, () -> "L'EMPRUNT à supprimer est obligatoire");

		emprunt = controlerEtPreparerPourSuppression(emprunt);

		supprimer(emprunt);
	}
	
	private Emprunt enregistrer(Emprunt emprunt) throws ServiceException {

		try {
			emprunt = empruntRepository.save(emprunt);
			if ( emprunt.getCle() == null ) {
				emprunt.setCle(String.format("EMPR-%010d", emprunt.getId()));
				emprunt = empruntRepository.save(emprunt);
			}
			return emprunt;
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EmpruntTechniqueErreur.ENREGISTREMENT);
		}
	}
	
	private void supprimer(Emprunt emprunt) throws ServiceException {

		try {
			empruntRepository.delete(emprunt);
		}
		catch (Throwable t) {
			throw new ServiceException (
					t,
					EmpruntTechniqueErreur.SUPPRESSION,
					emprunt.getCle());
		}
	}

	private Emprunt controlerEtPreparerPourCreation(Emprunt emprunt) throws ServiceException {

		verifierCompteInterneFinancier(emprunt.getCompteInterne());
		verifierEnchainementRevisions(emprunt);

		return emprunt;
	}

	private Emprunt controlerEtPreparerPourModification(Emprunt emprunt) throws ServiceException {

		verifierCompteInterneFinancier(emprunt.getCompteInterne());
		verifierEnchainementRevisions(emprunt);
		
		return emprunt;
	}

	private Emprunt controlerEtPreparerPourSuppression(Emprunt emprunt) throws ServiceException {

		return emprunt;
	}

	private void verifierCompteInterneFinancier(CompteInterne compteInterne) throws ServiceException {
	
		if ( compteInterne != null ) {
			if ( compteInterne.getTypeFonctionnement() != TypeFonctionnement.FINANCIER ) {
				throw new ServiceException(
						EmpruntFonctionnelleErreur.COMPTE_INTERNE_TYPE_FONCTIONNEMENT_INVALIDE,
						compteInterne.getIdentifiant());
			}
		}
	}
	
	private void verifierEnchainementRevisions(Emprunt emprunt) throws ServiceException {

		List<ConditionEmprunt> conditionsEmpruntOrdonnees = emprunt.getConditionsEmprunt()
				.stream()
				.sorted((ce1, ce2) -> {return Integer.compare(ce1.getNumeroPremiereEcheance(), ce2.getNumeroPremiereEcheance());})
				.toList();

		if ( conditionsEmpruntOrdonnees.size() < 1 ) {
			throw new ServiceException(
					EmpruntFonctionnelleErreur.CONDITION_EMPRUNT_INITIALE_OBLIGATOIRE);
		}
		
		ConditionEmprunt conditionEmpruntInitiale = conditionsEmpruntOrdonnees.get(0);
		verifierConditionEmprunt(conditionEmpruntInitiale);
		
		List<EnchainementRevision> enchainements = new ArrayList<EnchainementRevision>();
		ConditionEmprunt conditionEmpruntPrecedente = conditionEmpruntInitiale;
		for ( ConditionEmprunt revision : emprunt.getRevisions() ) {
			verifierConditionEmprunt(revision);
			if ( revision.getNumeroPremiereEcheance() <= conditionEmpruntPrecedente.getNumeroPremiereEcheance() ) {
				throw new ServiceException(
						EmpruntFonctionnelleErreur.REVISION_NUMERO_PREMIERE_ECHEANCE_HORS_SEQUENCE,
						revision.getNumeroPremiereEcheance(),
						conditionEmpruntPrecedente.getNumeroPremiereEcheance() + 1);
			}
			if ( ! revision.getDatePremiereEcheance().isAfter(conditionEmpruntPrecedente.getDatePremiereEcheance()) ) {
				throw new ServiceException(
						EmpruntFonctionnelleErreur.REVISION_DATE_PREMIERE_ECHEANCE_HORS_SEQUENCE,
						revision.getDatePremiereEcheance(),
						conditionEmpruntPrecedente.getDatePremiereEcheance());
			}
			EnchainementRevision nouvelEnchainement = new EnchainementRevision(conditionEmpruntPrecedente, revision);
			enchainements.add(nouvelEnchainement);
			conditionEmpruntPrecedente = revision;
		}
		EnchainementRevision nouvelEnchainement = new EnchainementRevision(conditionEmpruntPrecedente, null);
		enchainements.add(nouvelEnchainement);
		
		// Vérifier la validités de toutes les révisions : montant de l'échéance suffisant pour couvrir au moins les intérêts
		// et les frais fixes, nombre d'échéances adapté, absence de chevauchement entre les dates de révision.
		
		long montantCapitalDejaRembourseEmprunt = 0L;
		
		for ( EnchainementRevision enchainement : enchainements ) {

			ConditionEmprunt conditionEmpruntEtudiee = enchainement.revisionPrecedente;
			ConditionEmprunt revisionSuivante = enchainement.revisionSuivante;
			
			int numeroDerniereEcheanceEmprunt = conditionEmpruntEtudiee.getDuree() + conditionEmpruntInitiale.getNumeroPremiereEcheance() - 1;
			int numeroDerniereEcheanceRevision = revisionSuivante == null ? numeroDerniereEcheanceEmprunt : revisionSuivante.getNumeroPremiereEcheance() - 1;

			long montantCapitalRestantDuEmprunt = conditionEmpruntEtudiee.getCapitalEmprunteEnCentimes() - montantCapitalDejaRembourseEmprunt;

			int nombreMoisPeriode = DateEtPeriodeUtils.calculerNombreMoisPeriode(conditionEmpruntEtudiee.getTypePeriodeEcheances());
			double tauxInteretApplicablePeriode = (double) (conditionEmpruntEtudiee.getTauxAnnuel() / (12 / nombreMoisPeriode));
			long montantPaiement = conditionEmpruntEtudiee.getMontantTotalEcheanceEnCentimes();
			long partFraisFixes = conditionEmpruntEtudiee.getMontantFraisFixesEcheanceEnCentimes();
			
			int numeroEcheance = conditionEmpruntEtudiee.getNumeroPremiereEcheance();
			LocalDate dateEcheance = conditionEmpruntEtudiee.getDatePremiereEcheance();
			while ( numeroEcheance <= numeroDerniereEcheanceRevision ) {

				// Vérification de l'absence de chevauchement des dates d'échéance entre les révisions
				
				if ( revisionSuivante != null && ! dateEcheance.isBefore(revisionSuivante.getDatePremiereEcheance()) ) {
					throw new ServiceException(
							EmpruntFonctionnelleErreur.REVISION_DATE_ECHEANCE_HORS_SEQUENCE,
							numeroEcheance,
							dateEcheance,
							revisionSuivante.getDatePremiereEcheance());
				}

				// Calcul des intérêts de l'échéance
				
				Long montantInteretsEcheance = Math.round(montantCapitalRestantDuEmprunt * tauxInteretApplicablePeriode / 100.00);
				if ( montantInteretsEcheance + partFraisFixes > montantPaiement ) {
					throw new ServiceException(
							EmpruntFonctionnelleErreur.MONTANT_TOTAL_ECHEANCE_INSUFFISANT,
							numeroEcheance,
							montantInteretsEcheance + partFraisFixes,
							montantPaiement);
				}
				
				// Calcul du capital remboursé de l'échéance
				
				Long montantCapitalRembourseEcheance = montantPaiement - (montantInteretsEcheance + partFraisFixes);
				if ( montantCapitalRembourseEcheance > montantCapitalRestantDuEmprunt ) {
					if ( numeroEcheance < numeroDerniereEcheanceEmprunt ) {
						throw new ServiceException(
								EmpruntFonctionnelleErreur.NOMBRE_TOTAL_ECHEANCES_EXCESSIF,
								numeroEcheance,
								numeroDerniereEcheanceEmprunt);
					}
					montantCapitalRembourseEcheance = montantCapitalRestantDuEmprunt;
				}
				if ( numeroEcheance == numeroDerniereEcheanceEmprunt ) {
					// Tout le capital restant du est payé à la dernière échéance. Ce n'est pas un problème.
					montantCapitalRembourseEcheance = montantCapitalRestantDuEmprunt;
				}
				
				montantCapitalRestantDuEmprunt -= montantCapitalRembourseEcheance;
				montantCapitalDejaRembourseEmprunt += montantCapitalRembourseEcheance;
				
				numeroEcheance++;
				dateEcheance = dateEcheance.plus(nombreMoisPeriode, ChronoUnit.MONTHS);
			}
		}
	}
		
	private void verifierConditionEmprunt(ConditionEmprunt conditionEmprunt) throws ServiceException {

	}

	private List<Echeance> calculerEcheances(Emprunt emprunt) throws ServiceException {
		
		List<Echeance> echeancesEmprunt = new ArrayList<Echeance>();
		
		List<ConditionEmprunt> conditionsEmpruntOrdonnees = emprunt.getConditionsEmprunt()
				.stream()
				.sorted((ce1, ce2) -> {return Integer.compare(ce1.getNumeroPremiereEcheance(), ce2.getNumeroPremiereEcheance());})
				.toList();

		List<EnchainementRevision> enchainements = new ArrayList<EnchainementRevision>();
		
		ConditionEmprunt conditionEmpruntInitiale = conditionsEmpruntOrdonnees.get(0);
		
		// Constitution de la liste des enchaînements de révisions
		
		ConditionEmprunt conditionEmpruntPrecedente = conditionEmpruntInitiale;
		for ( ConditionEmprunt revision : emprunt.getRevisions() ) {
			EnchainementRevision nouvelEnchainement = new EnchainementRevision(conditionEmpruntPrecedente, revision);
			enchainements.add(nouvelEnchainement);
			conditionEmpruntPrecedente = revision;
		}
		EnchainementRevision nouvelEnchainement = new EnchainementRevision(conditionEmpruntPrecedente, null);
		enchainements.add(nouvelEnchainement);

		// Calcul des échéances
		
		long montantCapitalDejaRembourseEmprunt = 0L;
		
		for ( EnchainementRevision enchainement : enchainements ) {

			ConditionEmprunt conditionEmpruntEtudiee = enchainement.revisionPrecedente;
			ConditionEmprunt revisionSuivante = enchainement.revisionSuivante;
			
			int numeroDerniereEcheanceEmprunt = conditionEmpruntEtudiee.getDuree() + conditionEmpruntInitiale.getNumeroPremiereEcheance() - 1;
			int numeroDerniereEcheanceRevision = revisionSuivante == null ? numeroDerniereEcheanceEmprunt : revisionSuivante.getNumeroPremiereEcheance() - 1;

			Long montantCapitalRestantDuEmprunt = conditionEmpruntEtudiee.getCapitalEmprunteEnCentimes() - montantCapitalDejaRembourseEmprunt;

			int nombreMoisPeriode = DateEtPeriodeUtils.calculerNombreMoisPeriode(conditionEmpruntEtudiee.getTypePeriodeEcheances());
			double tauxInteretApplicablePeriode = (double) (conditionEmpruntEtudiee.getTauxAnnuel() / (12 / nombreMoisPeriode));
			long montantPaiement = conditionEmpruntEtudiee.getMontantTotalEcheanceEnCentimes();
			long partFraisFixes = conditionEmpruntEtudiee.getMontantFraisFixesEcheanceEnCentimes();
			
			int numeroEcheance = conditionEmpruntEtudiee.getNumeroPremiereEcheance();
			LocalDate dateEcheance = conditionEmpruntEtudiee.getDatePremiereEcheance();
			List<Echeance> echeancesConditionEmpruntEtudiee = new ArrayList<Echeance>();
			while ( numeroEcheance <= numeroDerniereEcheanceRevision ) {
				
				// Calcul des intérêts de l'échéance
				
				long partInterets = Math.round(montantCapitalRestantDuEmprunt * tauxInteretApplicablePeriode / 100.00);
				
				// Calcul du capital remboursé de l'échéance
				
				Long partCapital = montantPaiement - (partInterets + partFraisFixes);
				if ( partCapital > montantCapitalRestantDuEmprunt ) {
					partCapital = montantCapitalRestantDuEmprunt;
				}
				if ( numeroEcheance == numeroDerniereEcheanceEmprunt ) {
					// Tout le capital restant du est payé à la dernière échéance. Ce n'est pas un problème.
					partCapital = montantCapitalRestantDuEmprunt;
				}
				
				// Cumuls
				
				montantCapitalRestantDuEmprunt -= partCapital;
				montantCapitalDejaRembourseEmprunt += partCapital;
				long montantTotalEcheanceRecalcule = partCapital + partInterets + partFraisFixes;

				// Création de l'échéance
				
				Echeance echeance = new Echeance();
				echeance.setConditionEmprunt(conditionEmpruntEtudiee);
				echeance.setNumeroEcheance(numeroEcheance);
				echeance.setDateEcheance(dateEcheance);
				echeance.setMontantPaiementEnCentimes(montantTotalEcheanceRecalcule);
				echeance.setPartCapitalEnCentimes(partCapital);
				echeance.setPartInteretsEnCentimes(partInterets);
				echeance.setPartFraisFixesEnCentimes(partFraisFixes);
				echeance.setCapitalEmprunteDejaRembourseEnCentimes(montantCapitalDejaRembourseEmprunt);
				echeance.setCapitalEmprunteRestantDuEnCentimes(montantCapitalRestantDuEmprunt);
				rapprocherOperationsEcheance(echeance);
				
				echeancesConditionEmpruntEtudiee.add(echeance);
				
				// Echeance suivante 
				
				numeroEcheance++;
				dateEcheance = dateEcheance.plus(nombreMoisPeriode, ChronoUnit.MONTHS);
			}
			
			// Enregistrement des échéances au niveau de la révision examinée

			conditionEmpruntEtudiee.setEcheances(echeancesConditionEmpruntEtudiee);
			
			// Ajout des mêmes échéances au niveau global de l'emprunt
			
			echeancesEmprunt.addAll(echeancesConditionEmpruntEtudiee);
		}
		
		return echeancesEmprunt;
	}
	
	private void rapprocherOperationsEcheance(Echeance echeance) throws ServiceException {

		CompteInterne compteInterne = echeance.getConditionEmprunt().getEmprunt().getCompteInterne();
		if ( compteInterne == null ) {
			return;
		}

		List<Operation> operationsRecette = operationService.rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(
				compteInterne, 
				echeance.getDateEcheance().minus(NOMBRE_JOURS_INTERVALLE_DATE_ECHEANCE, ChronoUnit.DAYS), 
				echeance.getDateEcheance().plus(NOMBRE_JOURS_INTERVALLE_DATE_ECHEANCE, ChronoUnit.DAYS)); 
		List<Operation> operationsDepense = operationService.rechercherOperationsDepenseParCompteEntreDateDebutEtDateFin(
				compteInterne, 
				echeance.getDateEcheance().minus(NOMBRE_JOURS_INTERVALLE_DATE_ECHEANCE, ChronoUnit.DAYS), 
				echeance.getDateEcheance().plus(NOMBRE_JOURS_INTERVALLE_DATE_ECHEANCE, ChronoUnit.DAYS));

		for ( Operation operation : operationsRecette ) {
			if ( operation.getTypeOperation() == TypeOperation.DEPOT 
					&& operation.getMontantEnCentimes().equals(echeance.getMontantPaiementEnCentimes()) ) {
				echeance.setOperationPaiement(operation);
				break;
			}
		}

		for ( Operation operation : operationsDepense ) {
			if ( operation.getTypeOperation() == TypeOperation.FRAIS_COMPTE_FINANCIER 
					&& operation.getMontantEnCentimes().equals(echeance.getPartInteretsEnCentimes()) ) {
				echeance.setOperationPartInterets(operation);
				break;
			}
		}

		for ( Operation operation : operationsDepense ) {
			if ( operation.getTypeOperation() == TypeOperation.FRAIS_COMPTE_FINANCIER 
					&& (echeance.getOperationPartInterets() == null || ! echeance.getOperationPartInterets().getId().equals(operation.getId()))
					&& operation.getMontantEnCentimes().equals(echeance.getPartFraisFixesEnCentimes()) ) {
				echeance.setOperationPartFraisFixes(operation);
				break;
			}
		}
	}

}
