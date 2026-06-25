package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.rapports.model.Echeancier;
import fr.colline.monatis.rapports.model.EtatBilanPatrimoine;
import fr.colline.monatis.rapports.model.EtatDepenseRecette;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValue;
import fr.colline.monatis.rapports.model.EtatRemunerationsFrais;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.model.ReleveNonCategorise;
import fr.colline.monatis.rapports.model.ReleveSousCategorie;
import fr.colline.monatis.rapports.model.ResumeCompteInterne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoinePeriode;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.bilan_patrimoine.BilanPatrimoineTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecetteCategorieLigne;
import fr.colline.monatis.rapports.model.composants.depense_recette.DepenseRecettePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValuePeriode;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.plus_moins_value.PlusMoinsValueTypeFonctionnementPeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisPeriode;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementLigne;
import fr.colline.monatis.rapports.model.composants.remunerations_frais.RemunerationsFraisTypeFonctionnementPeriode;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypePeriode;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

/**
 * Liste des rapports
 * <ul><b>relevé de compte</b> : liste de toutes les opérations ayant affecté ce compte en dépense comme en recette, entre deux dates.
 * Les opérations enregistrées avant la date de solde initial sont ignorées.</ul> 
 */
@Service
public class RapportService {

	@Autowired private OperationService operationService;

	@Autowired private SoldeService soldeService;
	@Autowired private PlusMoinsValueService plusMoinsValueService;
	@Autowired private DepenseRecetteService depenseRecetteService;
	@Autowired private RemunerationsFraisService remunerationsFraisService;
	@Autowired private BilanPatrimoineService bilanPatrimoineService;
	@Autowired private EcheancierService echeancierService;

	public ReleveCompte rechercherReleveOperationCompte(
			Compte compte,
			LocalDate dateDebutReleve,
			LocalDate dateFinReleve) throws ServiceException {
		
		Long montantSoldeDebutReleveEnCentimes;
		Long montantSoldeFinReleveEnCentimes;
		Long montantTotalOperationsRecetteEnCentimes;
		Long montantTotalOperationsDepenseEnCentimes;
		Long montantTotalEcartEnCentimems;
		List<Operation> operationsRecette;
		List<Operation> operationsDepense;

		montantSoldeDebutReleveEnCentimes = soldeService.rechercherSolde(compte, dateDebutReleve.minus(1, ChronoUnit.DAYS));
		montantSoldeFinReleveEnCentimes = soldeService.rechercherSolde(compte, dateFinReleve);

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {

			CompteInterne compteInterne = (CompteInterne) compte;

			// Tout à 0 si le relevé est situé entièrement avant ou après la période de "vie" du compte interne.  
			if ( dateFinReleve.isBefore(compteInterne.getDateSoldeInitial()) 
					|| (compteInterne.getDateCloture() != null && dateDebutReleve.isAfter(compteInterne.getDateCloture())) )  {
				ReleveCompte releve = new ReleveCompte();

				releve.setCompte(compte);
				releve.setDateDebutReleve(dateDebutReleve);
				releve.setDateFinReleve(dateFinReleve);
				releve.setMontantSoldeDebutReleveEnCentimes(montantSoldeDebutReleveEnCentimes);
				releve.setMontantSoldeFinReleveEnCentimes(montantSoldeFinReleveEnCentimes);
				releve.setMontantTotalOperationsRecetteEnCentimes(0L);
				releve.setMontantTotalOperationsDepenseEnCentimes(0L);
				releve.setMontantEcartEnCentimes(0L);
				releve.setOperationsRecette(new ArrayList<Operation>());
				releve.setOperationsDepense(new ArrayList<Operation>());
				
				return releve;
			}
		}

		operationsRecette = operationService.rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(
				compte, 
				dateDebutReleve, 
				dateFinReleve);
		
		operationsDepense = operationService.rechercherOperationsDepenseParCompteEntreDateDebutEtDateFin(
				compte, 
				dateDebutReleve, 
				dateFinReleve);

		montantTotalOperationsRecetteEnCentimes = operationsRecette
				.stream()
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();

		montantTotalOperationsDepenseEnCentimes = operationsDepense
				.stream()
				.mapToLong((o) -> {
					return o.getMontantEnCentimes();})
				.sum();

		montantTotalEcartEnCentimems = montantSoldeFinReleveEnCentimes - (montantSoldeDebutReleveEnCentimes + montantTotalOperationsRecetteEnCentimes - montantTotalOperationsDepenseEnCentimes);
		
		ReleveCompte releve = new ReleveCompte();

		releve.setCompte(compte);
		releve.setDateDebutReleve(dateDebutReleve);
		releve.setDateFinReleve(dateFinReleve);
		releve.setMontantSoldeDebutReleveEnCentimes(montantSoldeDebutReleveEnCentimes);
		releve.setMontantSoldeFinReleveEnCentimes(montantSoldeFinReleveEnCentimes);
		releve.setMontantTotalOperationsRecetteEnCentimes(montantTotalOperationsRecetteEnCentimes);
		releve.setMontantTotalOperationsDepenseEnCentimes(montantTotalOperationsDepenseEnCentimes);
		releve.setMontantEcartEnCentimes(montantTotalEcartEnCentimems);
		releve.setOperationsRecette(operationsRecette);
		releve.setOperationsDepense(operationsDepense);
		
		return releve;
	}

	public ReleveNonCategorise rechercherReleveOperationNonCategorise(LocalDate dateDebut, LocalDate dateFin) throws ServiceException {
		
		List<OperationLigne> operationsLignes = operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(
				null,
				null,
				dateDebut, 
				dateFin)
				.toList();
		
		ReleveNonCategorise releve = new ReleveNonCategorise();
		
		releve.setDateDebutReleve(dateDebut);
		releve.setDateFinReleve(dateFin);
		releve.setOperationsLignes(operationsLignes);
		
		return releve;
	}

	public ReleveSousCategorie rechercherReleveOperationSousCategorie(SousCategorie sousCategorie, LocalDate dateDebut, LocalDate dateFin) throws ServiceException {
		
		List<OperationLigne> operationsLignes = operationService.rechercherOperationsLignesParSousCategorieIdEtCriteres(
				sousCategorie.getId(), 
				null,
				dateDebut, 
				dateFin)
				.toList();
		
		ReleveSousCategorie releve = new ReleveSousCategorie();
		
		releve.setDateDebutReleve(dateDebut);
		releve.setDateFinReleve(dateFin);
		releve.setSousCategorie(sousCategorie);
		releve.setOperationsLignes(operationsLignes);
		
		return releve;
	}

	public ResumeCompteInterne rechercherResumeCompteInterne(
			CompteInterne compteInterne, 
			LocalDate dateSolde) throws ServiceException {

		ResumeCompteInterne resume = new ResumeCompteInterne();
		
		resume.setCompteInterne(compteInterne);
		resume.setDateSolde(dateSolde);
		resume.setMontantSoldeEnCentimes(soldeService.rechercherSolde(compteInterne, dateSolde));

		return resume;
	}
	
	public Echeancier rechercherEcheancier(
			Emprunt emprunt,
			LocalDate dateCible) throws ServiceException {
		
		return echeancierService.calculerEcheancier(emprunt, dateCible);
	}
	
	public EtatDepenseRecette rechercherEtatDepenseRecette(
			List<SousCategorie> sousCategories,
			Beneficiaire beneficiaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat,
			TypePeriode typePeriode) throws ServiceException {
		
		List<DepenseRecetteCategorieLigne> lignesCategorie = new ArrayList<DepenseRecetteCategorieLigne>();
		DepenseRecettePeriode[] cumulEtat = depenseRecetteService.initialiserPeriodes(dateDebutEtat, dateFinEtat, typePeriode);

		for ( DepenseRecetteCategorieLigne ligneCategorie : depenseRecetteService.rechercherDepenseRecetteCategorieLignes(
					sousCategories,
					beneficiaire,
					dateDebutEtat, 
					dateFinEtat,
					typePeriode) ) {

			lignesCategorie.add(ligneCategorie);
			cumulEtat = depenseRecetteService.cumulerPeriodes(cumulEtat, ligneCategorie.getCumulCategorie());
		}

		EtatDepenseRecette etat = new EtatDepenseRecette();

		etat.setDateDebutEtat(dateDebutEtat);
		etat.setDateFinEtat(dateFinEtat);
		etat.setTypePeriode(typePeriode);
		etat.setSousCategories(sousCategories);
		etat.setBeneficiaire(beneficiaire);
		
		etat.setLignesCategorie(lignesCategorie);
		etat.setCumulEtat(cumulEtat);

		return etat;
	}
	
	public EtatPlusMoinsValue rechercherEtatPlusMoinsValue(
			List<CompteInterne> comptesInternes,
			List<TypeFonctionnement> typesFonctionnements,
			Banque banque,
			Titulaire titulaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat,
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		List<PlusMoinsValueTypeFonctionnementLigne> lignesTypeFonctionnement = new ArrayList<PlusMoinsValueTypeFonctionnementLigne>();
		PlusMoinsValuePeriode[] cumulsEtat = new PlusMoinsValuePeriode[nombrePeriodes];

		for ( TypeFonctionnement typeFonctionnement : typesFonctionnements ) {

			PlusMoinsValueTypeFonctionnementLigne ligneTypeFonctionnement = plusMoinsValueService.rechercherPlusMoinsValueTypeFonctionnementLigne(
					comptesInternes,
					typeFonctionnement,
					banque,
					titulaire,
					dateDebutEtat, 
					dateFinEtat,
					typePeriode);

			lignesTypeFonctionnement.add(ligneTypeFonctionnement);
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				PlusMoinsValueTypeFonctionnementPeriode cumulTypeFonctionnementPeriode = ligneTypeFonctionnement.getCumulsPeriodes()[numeroPeriode];
				PlusMoinsValuePeriode cumulEtatPeriode;
				if ( cumulsEtat[numeroPeriode] == null ) {
					cumulEtatPeriode = new PlusMoinsValuePeriode();
					
					cumulEtatPeriode.setDateDebutPeriode(cumulTypeFonctionnementPeriode.getDateDebutPeriode());
					cumulEtatPeriode.setDateFinPeriode(cumulTypeFonctionnementPeriode.getDateFinPeriode());
					
					cumulEtatPeriode.setMontantSoldeInitialEnCentimes(cumulTypeFonctionnementPeriode.getMontantSoldeInitialEnCentimes());
					cumulEtatPeriode.setMontantOperationsEnCentimes(cumulTypeFonctionnementPeriode.getMontantOperationsEnCentimes());
					cumulEtatPeriode.setMontantPlusMoinsValueNetteEnCentimes(cumulTypeFonctionnementPeriode.getMontantPlusMoinsValueNetteEnCentimes());
					cumulEtatPeriode.setTauxPlusMoinsValueNette(cumulTypeFonctionnementPeriode.getTauxPlusMoinsValueNette());
					cumulEtatPeriode.setMontantSoldeFinalEnCentimes(cumulTypeFonctionnementPeriode.getMontantSoldeFinalEnCentimes());
					cumulEtatPeriode.setMontantFraisEnCentimes(cumulTypeFonctionnementPeriode.getMontantFraisEnCentimes());
					cumulEtatPeriode.setTauxFrais(cumulTypeFonctionnementPeriode.getTauxFrais());

					cumulsEtat[numeroPeriode] = cumulEtatPeriode;
				}
				else {
					cumulEtatPeriode = cumulsEtat[numeroPeriode];
					
					cumulEtatPeriode.setMontantPlusMoinsValueNetteEnCentimes(cumulEtatPeriode.getMontantPlusMoinsValueNetteEnCentimes() + cumulTypeFonctionnementPeriode.getMontantPlusMoinsValueNetteEnCentimes());
					
					cumulEtatPeriode.setMontantSoldeInitialEnCentimes(cumulEtatPeriode.getMontantSoldeInitialEnCentimes() + cumulTypeFonctionnementPeriode.getMontantSoldeInitialEnCentimes());
					cumulEtatPeriode.setMontantOperationsEnCentimes(cumulEtatPeriode.getMontantOperationsEnCentimes() + cumulTypeFonctionnementPeriode.getMontantOperationsEnCentimes());
					cumulEtatPeriode.setMontantPlusMoinsValueNetteEnCentimes(cumulEtatPeriode.getMontantPlusMoinsValueNetteEnCentimes() + cumulTypeFonctionnementPeriode.getMontantPlusMoinsValueNetteEnCentimes());
					cumulEtatPeriode.setMontantSoldeFinalEnCentimes(cumulEtatPeriode.getMontantSoldeFinalEnCentimes() + cumulTypeFonctionnementPeriode.getMontantSoldeFinalEnCentimes());
					cumulEtatPeriode.setMontantFraisEnCentimes(cumulEtatPeriode.getMontantFraisEnCentimes() + cumulTypeFonctionnementPeriode.getMontantFraisEnCentimes());

					Double tauxPlusMoinsValueNette = null;
					Long soldeDebutAvecOperations = cumulEtatPeriode.getMontantSoldeInitialEnCentimes() + cumulEtatPeriode.getMontantOperationsEnCentimes();
					Long montantPlusMoinsValueNetteEnCentimes = cumulEtatPeriode.getMontantPlusMoinsValueNetteEnCentimes();
					if ( soldeDebutAvecOperations != 0 ) {
						tauxPlusMoinsValueNette = (double) (100.00 * montantPlusMoinsValueNetteEnCentimes / soldeDebutAvecOperations);
					}
					cumulEtatPeriode.setTauxPlusMoinsValueNette(tauxPlusMoinsValueNette);

					Double tauxFrais = null;
					Long soldeFinPeriode = cumulEtatPeriode.getMontantSoldeFinalEnCentimes();
					Long montantFraisEnCentimes = cumulEtatPeriode.getMontantFraisEnCentimes();
					if ( soldeFinPeriode != 0 ) {
						tauxFrais = (double) (100.00 * montantFraisEnCentimes / soldeFinPeriode);
					}
					cumulEtatPeriode.setTauxFrais(tauxFrais);

					
				}
			}
		}

		EtatPlusMoinsValue etat = new EtatPlusMoinsValue();

		etat.setDateDebutEtat(dateDebutEtat);
		etat.setDateFinEtat(dateFinEtat);
		etat.setTypePeriode(typePeriode);
		etat.setComptesInternes(comptesInternes);
		etat.setBanque(banque);
		etat.setTitulaire(titulaire);
		etat.setTypesFonctionnements(typesFonctionnements);
		etat.setLignesTypeFonctionnement(lignesTypeFonctionnement);
		etat.setCumuls(cumulsEtat);

		return etat;
	}

	public EtatRemunerationsFrais rechercherEtatRemunerationsFrais(
			List<CompteInterne> comptesInternes,
			List<TypeFonctionnement> typesFonctionnements,
			Titulaire titulaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat,
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		List<RemunerationsFraisTypeFonctionnementLigne> lignesTypeFonctionnement = new ArrayList<RemunerationsFraisTypeFonctionnementLigne>();
		RemunerationsFraisPeriode[] cumulsEtat = new RemunerationsFraisPeriode[nombrePeriodes];

		for ( TypeFonctionnement typeFonctionnement : typesFonctionnements ) {

			RemunerationsFraisTypeFonctionnementLigne ligneTypeFonctionnement = remunerationsFraisService.rechercherRemunerationsFraisTypeFonctionnementLigne(
					comptesInternes,
					typeFonctionnement,
					titulaire,
					dateDebutEtat, 
					dateFinEtat,
					typePeriode);

			lignesTypeFonctionnement.add(ligneTypeFonctionnement);
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				RemunerationsFraisTypeFonctionnementPeriode typeFonctionnementPeriode = ligneTypeFonctionnement.getCumuls()[numeroPeriode];
				RemunerationsFraisPeriode cumulPeriode;
				if ( cumulsEtat[numeroPeriode] == null ) {
					cumulPeriode = new RemunerationsFraisPeriode();
					
					cumulPeriode.setDateDebutPeriode(typeFonctionnementPeriode.getDateDebutPeriode());
					cumulPeriode.setDateFinPeriode(typeFonctionnementPeriode.getDateFinPeriode());
					cumulPeriode.setMontantRemunerationsEnCentimes(typeFonctionnementPeriode.getMontantRemunerationsEnCentimes());
					cumulPeriode.setMontantFraisEnCentimes(typeFonctionnementPeriode.getMontantFraisEnCentimes());
					cumulPeriode.setSoldeRemunerationsFraisEnCentimes(typeFonctionnementPeriode.getSoldeRemunerationsFraisEnCentimes());
					
					cumulsEtat[numeroPeriode] = cumulPeriode;
				}
				else {
					cumulPeriode = cumulsEtat[numeroPeriode];
					
					cumulPeriode.setMontantRemunerationsEnCentimes(cumulPeriode.getMontantRemunerationsEnCentimes() + typeFonctionnementPeriode.getMontantRemunerationsEnCentimes());
					cumulPeriode.setMontantFraisEnCentimes(cumulPeriode.getMontantFraisEnCentimes() + typeFonctionnementPeriode.getMontantFraisEnCentimes());
					cumulPeriode.setSoldeRemunerationsFraisEnCentimes(cumulPeriode.getSoldeRemunerationsFraisEnCentimes() + typeFonctionnementPeriode.getSoldeRemunerationsFraisEnCentimes());
				}
			}
		}

		EtatRemunerationsFrais etat = new EtatRemunerationsFrais();

		etat.setDateDebutEtat(dateDebutEtat);
		etat.setDateFinEtat(dateFinEtat);
		etat.setTypePeriode(typePeriode);
		etat.setComptesInternes(comptesInternes);
		etat.setTitulaire(titulaire);
		etat.setComptesInternes(comptesInternes);
		etat.setTypesFonctionnements(typesFonctionnements);
		etat.setLignesTypeFonctionnement(lignesTypeFonctionnement);
		etat.setCumuls(cumulsEtat);

		return etat;
	}

	public EtatBilanPatrimoine rechercherEtatBilanPatrimoine(
			List<CompteInterne> comptesInternes,
			List<TypeFonctionnement> typesFonctionnements,
			Titulaire titulaire,
			LocalDate dateDebutEtat,
			LocalDate dateFinEtat,
			TypePeriode typePeriode) throws ServiceException {

		int nombrePeriodes = DateEtPeriodeUtils.calculerNombrePeriodesEntreDateDebutEtDateFin(typePeriode, dateDebutEtat, dateFinEtat);

		List<BilanPatrimoineTypeFonctionnementLigne> lignesTypeFonctionnement = new ArrayList<BilanPatrimoineTypeFonctionnementLigne>();
		Long montantSoldeInitialEnCentimes = 0L;
		BilanPatrimoinePeriode[] cumulsEtat = new BilanPatrimoinePeriode[nombrePeriodes];

		for ( TypeFonctionnement typeFonctionnement : typesFonctionnements ) {

			BilanPatrimoineTypeFonctionnementLigne ligneTypeFonctionnement = bilanPatrimoineService.rechercherBilanPatrimoineTypeFonctionnementLigne(
					comptesInternes,
					typeFonctionnement,
					titulaire,
					dateDebutEtat, 
					dateFinEtat,
					typePeriode);

			lignesTypeFonctionnement.add(ligneTypeFonctionnement);
			montantSoldeInitialEnCentimes += ligneTypeFonctionnement.getMontantSoldeInitialEnCentimes();
			for ( int numeroPeriode = 0 ; numeroPeriode < nombrePeriodes ; numeroPeriode++ ) {
				BilanPatrimoineTypeFonctionnementPeriode typeFonctionnementPeriode = ligneTypeFonctionnement.getCumulsPeriodes()[numeroPeriode];
				BilanPatrimoinePeriode cumulPeriode;
				if ( cumulsEtat[numeroPeriode] == null ) {
					cumulPeriode = new BilanPatrimoinePeriode();
					
					cumulPeriode.setDateDebutPeriode(typeFonctionnementPeriode.getDateDebutPeriode());
					cumulPeriode.setDateFinPeriode(typeFonctionnementPeriode.getDateFinPeriode());
					cumulPeriode.setMontantSoldeFinalEnCentimes(typeFonctionnementPeriode.getMontantSoldeFinalEnCentimes());
					cumulPeriode.setMontantTotalRecetteEnCentimes(typeFonctionnementPeriode.getMontantTotalRecetteEnCentimes());
					cumulPeriode.setMontantTotalDepenseEnCentimes(typeFonctionnementPeriode.getMontantTotalDepenseEnCentimes());
					cumulPeriode.setSoldeTotalTechniqueEnCentimes(typeFonctionnementPeriode.getSoldeTotalTechniqueEnCentimes());
					cumulPeriode.setMontantEcartNonJustifieEnCentimes(typeFonctionnementPeriode.getMontantEcartNonJustifieEnCentimes());
					
					cumulsEtat[numeroPeriode] = cumulPeriode;
				}
				else {
					cumulPeriode = cumulsEtat[numeroPeriode];
					cumulPeriode.setMontantSoldeFinalEnCentimes(cumulPeriode.getMontantSoldeFinalEnCentimes() + typeFonctionnementPeriode.getMontantSoldeFinalEnCentimes());
					cumulPeriode.setMontantTotalRecetteEnCentimes(cumulPeriode.getMontantTotalRecetteEnCentimes() + typeFonctionnementPeriode.getMontantTotalRecetteEnCentimes());
					cumulPeriode.setMontantTotalDepenseEnCentimes(cumulPeriode.getMontantTotalDepenseEnCentimes() + typeFonctionnementPeriode.getMontantTotalDepenseEnCentimes());
					cumulPeriode.setSoldeTotalTechniqueEnCentimes(cumulPeriode.getSoldeTotalTechniqueEnCentimes() + typeFonctionnementPeriode.getSoldeTotalTechniqueEnCentimes());
					cumulPeriode.setMontantEcartNonJustifieEnCentimes(cumulPeriode.getMontantEcartNonJustifieEnCentimes() + typeFonctionnementPeriode.getMontantEcartNonJustifieEnCentimes());
				}
			}
		}

		EtatBilanPatrimoine etat = new EtatBilanPatrimoine();

		etat.setDateDebutEtat(dateDebutEtat);
		etat.setDateFinEtat(dateFinEtat);
		etat.setTypePeriode(typePeriode);
		etat.setComptesInternes(comptesInternes);
		etat.setTypesFonctionnements(typesFonctionnements);
		etat.setTitulaire(titulaire);
		etat.setLignesTypeFonctionnement(lignesTypeFonctionnement);
		etat.setMontantSoldeInitialEnCentimes(montantSoldeInitialEnCentimes);
		etat.setCumuls(cumulsEtat);

		return etat;
	}
}
