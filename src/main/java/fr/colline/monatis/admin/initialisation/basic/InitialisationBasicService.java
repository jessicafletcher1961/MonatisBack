package fr.colline.monatis.admin.initialisation.basic;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.service.BanqueService;
import fr.colline.monatis.references.service.BeneficiaireService;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.references.service.SousCategorieService;
import fr.colline.monatis.references.service.TitulaireService;

@Service
public class InitialisationBasicService {

	@Autowired private BanqueService banqueService;
	@Autowired private TitulaireService titulaireService;
	@Autowired private BeneficiaireService beneficiaireService;
	@Autowired private CategorieService categorieService;
	@Autowired private SousCategorieService sousCategorieService;

	@Autowired private CompteExterneService compteExterneService;
	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteTechniqueService compteTechniqueService;
	
	@Autowired private OperationService operationService;
	@Autowired private EvaluationService evaluationService;

	public void supprimerTous() throws ServiceException {

		evaluationService.supprimerTous();
		operationService.supprimerTous();
		
		compteTechniqueService.supprimerTous();
		compteInterneService.supprimerTous();
		compteExterneService.supprimerTous();

		sousCategorieService.supprimerTous();
		categorieService.supprimerTous();
		beneficiaireService.supprimerTous();
		titulaireService.supprimerTous();
		banqueService.supprimerTous();
	}

	public void initialiser() throws ServiceException {

		creerReferences();
		creerComptes();
		creerOperations();
		creerEvaluations();
	}


	private void creerReferences() throws ServiceException {

		creerBanques();
		creerTitulaires();
		creerBeneficiaires();
		creerCategories();
		creerSousCategories();

	}

	private void creerComptes() throws ServiceException {

		creerComptesExternes();
		creerComptesInternes();
	}
	
	private void creerBanques() throws ServiceException {

		banqueService.creerReference(new Banque("BNP", "BNP PARIBAS - Agence : 168 avenue Victor Hugo, 75016 Paris"));
		banqueService.creerReference(new Banque("LA-POSTE", "La Banque Postale - Agence : 6 allée Empereur 82000 Montauban "));
		banqueService.creerReference(new Banque("CREDIT-AGRICOLE", "Crédit Agricole De Champagne-Bourgogne - Agence : 72 avenue Drapeau 21000 Dijon"));
	}

	private void creerTitulaires() throws ServiceException {

		titulaireService.creerReference(new Titulaire("KEVIN", "Kevin Martin né le 28/03/1995"));
		titulaireService.creerReference(new Titulaire("JESSICA", "Jessica Dupont née le 05/02/1996"));
	}

	private void creerBeneficiaires() throws ServiceException {

		// Les personnes
		beneficiaireService.creerReference(new Beneficiaire("KEVIN", "Kevin"));
		beneficiaireService.creerReference(new Beneficiaire("JESSICA", "Jessica"));
		beneficiaireService.creerReference(new Beneficiaire("ROBIN", "Robin fils de Kevin et Jessica"));
		beneficiaireService.creerReference(new Beneficiaire("MATHILDE", "Maman de Jessica"));
		beneficiaireService.creerReference(new Beneficiaire("HERVE", "Copain de Kevin"));
		beneficiaireService.creerReference(new Beneficiaire("OISEAUX", "Les oiseaux du jardin"));

		// Les évènements
		beneficiaireService.creerReference(new Beneficiaire("VACANCES-2025-10", "Vacances en Dordogne du 30/09/2025 au 11/10/2025"));

		// Les objets
		beneficiaireService.creerReference(new Beneficiaire("YARIS-XX-111-YY", "La TOYOTA-YARIS achetée en 2021"));
		beneficiaireService.creerReference(new Beneficiaire("FJR-AA-222-BB", "La YAMAHA-FJR achetée en 2019"));
		beneficiaireService.creerReference(new Beneficiaire("APPART-LA-ROCHELLE", "Habitation principale au 29 allée des sources La Rochelle"));
		
	}

	private void creerCategories() throws ServiceException {

		categorieService.creerReference(new Categorie("REVENUS", "Revenus du foyer (salaires, pensions, revenus locatifs, dividendes...)"));
		categorieService.creerReference(new Categorie("FONCTIONNEMENT", "Dépenses de fonctionnement du foyer (alimentation, consommation d'eau et d'électricité, loyers, charges récurrentes, petites fournitures, travaux d'entretien...)"));
		categorieService.creerReference(new Categorie("EQUIPEMENTS", "Equipements du le foyer (électro-ménager, audio-visuel, cuisine équipée, mobilier, véhicules, travaux d'amélioration de l'habitat,...)"));
		categorieService.creerReference(new Categorie("LOISIRS", "Dépense pour les loisirs (restaurants, spectacles, abonnements salle de sport, équipement sportif, hôtels, forfaits de ski...)"));
		categorieService.creerReference(new Categorie("HABILLEMENT", "Dépenses pour l'habillement des membres du foyer (vêtements, chaussures, accessoires, maquillage...)"));
		categorieService.creerReference(new Categorie("SANTE", "Dépenses de santé et remboursements des organismes sociaux ou des mutuelles"));
		categorieService.creerReference(new Categorie("DONS", "Dons faits à des personnes ou à des organismes (ou à des oiseaux)"));
		categorieService.creerReference(new Categorie("IMPOTS", "Impôts et taxes, amendes"));
	}

	private void creerSousCategories() throws ServiceException {

		Categorie revenus = categorieService.rechercherParNom("REVENUS");

		sousCategorieService.creerReference(new SousCategorie("SALAIRES", "Salaires", revenus));
		sousCategorieService.creerReference(new SousCategorie("RETRAITES", "Pensions de retraite", revenus));
		sousCategorieService.creerReference(new SousCategorie("ALLOCATIONS", "Allocations diverses", revenus));
		sousCategorieService.creerReference(new SousCategorie("LOCATIFS", "Revenus locatifs", revenus));
		sousCategorieService.creerReference(new SousCategorie("PLACEMENTS", "Revenus des placements financiers", revenus));

		Categorie fonctionnement = categorieService.rechercherParNom("FONCTIONNEMENT");

		sousCategorieService.creerReference(new SousCategorie("RAVITAILLEMENT", "Alimentation, petites fournitures, lessives...", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("CANTINE", "Cantines diverses", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("ASSURANCES", "Assurances diverses", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("IMPOTS", "Impôts divers", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("TELEPHONE", "Factures téléphoniques", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("ELECTRICITE", "Factures d'électricité", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("EAU", "Factures d'eau", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("DEPLACEMENTS", "Carburants, billets et abonnements transports en commun, stationnement, location de voitures", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("PRESSING-LAVERIE", "Frais de nettoyage", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("MENAGE", "Service ménage à domicile", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("SECURITE", "Télésurveillance, systèmes d'alarme...", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("LOYER", "Loyer payé et charges", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("CHARGES-COPOROPRIETE", "Charges de copropriété pour les biens possédés", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("FRAIS-BANCAIRES", "Frais bancaires", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("LOCATION-VOITURES", "Location de voitures", fonctionnement));

		Categorie equipements = categorieService.rechercherParNom("EQUIPEMENTS");

		sousCategorieService.creerReference(new SousCategorie("ELECTRO-MENAGER", "Appareils électro-ménagers (lave-linge, aspirateurs, tondeuse à gazon,...)", equipements));


		Categorie loisirs = categorieService.rechercherParNom("LOISIRS");

		sousCategorieService.creerReference(new SousCategorie("RESTAURANTS", "Restaurants", loisirs));
		sousCategorieService.creerReference(new SousCategorie("JEUX-SOCIETE", "Jeux de société et figurines", loisirs));
		sousCategorieService.creerReference(new SousCategorie("JEUX-VIDEO", "Consoles et de jeux video", loisirs));
		sousCategorieService.creerReference(new SousCategorie("ABONNEMENTS-INTERNET", "Abonnements à des services internet payants", loisirs));
		sousCategorieService.creerReference(new SousCategorie("ABONNEMENTS-JOURNAUX", "Abonnements à des journaux papier", loisirs));
		sousCategorieService.creerReference(new SousCategorie("LIVRES", "Livres (papier et numériques)", loisirs));

		Categorie habillement = categorieService.rechercherParNom("HABILLEMENT");

		sousCategorieService.creerReference(new SousCategorie("VETEMENTS", "Vêtements de ville", habillement));


		Categorie sante = categorieService.rechercherParNom("SANTE");

		sousCategorieService.creerReference(new SousCategorie("IMAGERIE", "Imagerie médicale", sante));
		sousCategorieService.creerReference(new SousCategorie("ANALYSES", "Analyses médicales", sante));
		sousCategorieService.creerReference(new SousCategorie("GENERALISTE", "Médecin généraliste", sante));
		sousCategorieService.creerReference(new SousCategorie("SPECIALISTE", "Médecin spécialiste", sante));
		sousCategorieService.creerReference(new SousCategorie("MUTUELLES", "Cotisations et remboursements CPAM ou mutuelles", sante));
		sousCategorieService.creerReference(new SousCategorie("PHARMACIE", "Médicaments remboursables et non remboursables", sante));
		sousCategorieService.creerReference(new SousCategorie("HOPITAL", "Hospitalisations", sante));


		Categorie dons = categorieService.rechercherParNom("DONS");

		sousCategorieService.creerReference(new SousCategorie("FAMILLE", "Cadeaux à la famille ou à des amis", dons));
		sousCategorieService.creerReference(new SousCategorie("ORGANISMES", "Dons à des organismes caritatifs", dons));
		sousCategorieService.creerReference(new SousCategorie("ASSOCIATIONS", "Dons à des associations", dons));

		
		Categorie impots = categorieService.rechercherParNom("IMPOTS");

		sousCategorieService.creerReference(new SousCategorie("IMPOT-REVENUS", "Impôt sur le revenu", impots));
		sousCategorieService.creerReference(new SousCategorie("IMPOT-FONCIER", "Impôt foncier", impots));

	}

	private void creerComptesExternes() throws ServiceException {

		compteExterneService.creerCompte(new CompteExterne(
				"FOURNISSEURS-DIVERS",
				"Fournisseurs divers"));

	}

	private void creerComptesInternes() throws ServiceException {

		Banque bnp = banqueService.rechercherParNom("BNP");
		Banque laPoste = banqueService.rechercherParNom("LA-POSTE");
		Banque ca = banqueService.rechercherParNom("CREDIT-AGRICOLE");
		
		Titulaire kevin = titulaireService.rechercherParNom("KEVIN");
		Titulaire jessica = titulaireService.rechercherParNom("JESSICA");
		
		compteInterneService.creerCompte(new CompteInterne(
				"COMPTE-JOINT", 
				"COMPTE DE DEPOT N° 11111 11111 11111111111 - BIC : CEPA FRPP 222 - IBAN : FR76 3333 3333 3333 3333 3333 333",
				null,
				TypeFonctionnement.COURANT,
				LocalDate.parse("2024-01-02"), 
				266832L,
				bnp,
				kevin, jessica));
		compteInterneService.creerCompte(new CompteInterne(
				"LIVRET-A-JESSICA",
				"LIVRET A 99999999999",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2018-12-22"), 
				50000L,
				laPoste,
				jessica));
		compteInterneService.creerCompte(new CompteInterne(
				"COMPTE-TITRE-KEVIN",
				"CACE - Compte-titre nº INF88888888",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2023-10-14"), 
				200000L,
				ca,
				kevin));
		compteInterneService.creerCompte(new CompteInterne(
				"EMPRUNT-LA-ROCHELLE",
				"BNP - Emprunt n° 8888888888 de 230 000,00 € sur 18 ans pour l'appartement de La Rochelle. Première echéance le 08/07/2024 et dernière échéance le 08/06/2042",
				LocalDate.parse("2042-06-08"), 
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2024-06-08"), 
				-23000000L,
				bnp,
				kevin, jessica));
		compteInterneService.creerCompte(new CompteInterne(
				"APPART-LA-ROCHELLE",
				"Habitation pribcipale au 29 allée des sources La Rochelle",
				null, 
				TypeFonctionnement.BIEN,
				LocalDate.parse("2024-06-29"), 
				23000000L,
				null,
				kevin, jessica));
	}
	
	private void creerOperations() throws ServiceException {

//		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");
//		Beneficiaire jessica = beneficiaireService.rechercherParNom("JESSICA");
		
//		operationService.creerOperation(new Operation(
//				"CHQ-8197519", 
//				TypeOperation.DEPENSE,
//				"Consultation Dr Rivière", 
//				LocalDate.parse("2025-05-22"), 
//				3000L, 
//				compteExterneService.rechercherParIdentifiant("MEDECINS"), 
//				compteJoint,
//				new OperationLigne(
//						0,
//						"Consultation Dr Rivière", 
//						LocalDate.parse("2025-05-22"), 
//						3000L, 
//						sousCategorieService.rechercherParNom("GENERALISTE"),
//						jessica)
//				));

//		operationService.creerOperation(new Operation(
//				null, 
//				TypeOperation.RECETTE,
//				"Remboursement CPAM", 
//				LocalDate.parse("2025-06-12"), 
//				3736L, 
//				compteJoint, 
//				compteExterneService.rechercherParIdentifiant("CPAM-95"),
//				new OperationLigne(
//						0,
//						"Remboursement CPAM", 
//						LocalDate.parse("2025-06-12"), 
//						3736L, 
//						sousCategorieService.rechercherParNom("MUTUELLES"),
//						jessica)
//				));
		
	}

	private void creerEvaluations() throws ServiceException {

//		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");
//		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueOperationsReevaluation();

//		evaluationService.creerEvaluation(new Evaluation(
//				null, 
//				compteJoint, 
//				compteEvaluations, 
//				LocalDate.parse("2024-12-31"), 
//				256166L, 
//				"Relevé de compte décembre 2024"));


	}

}
