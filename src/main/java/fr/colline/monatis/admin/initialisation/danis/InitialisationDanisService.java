package fr.colline.monatis.admin.initialisation.danis;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.model.TypeOperation;
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
public class InitialisationDanisService {

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
		creerComptesTechniques();
	}

	private void creerOperations() throws ServiceException {

		creerOperationsRecetteCompteJointReleve199();
		creerOperationsDepenseCompteJointReleve199();
		creerOperationsNuance3DOdile();
		creerOperationsEspaceInvest4Odile();
		creerOperationsLivretAOdile();
	}

	private void creerEvaluations() throws ServiceException {

		creerEvaluationsEspaceInvest5Odile();
		creerEvaluationsCompteJoint();
		creerEvaluationsNuance3D();
		creerEvaluationsEspaceInvest4Odile();
		creerEvaluationsMillevieOdile();
		creerEvaluationsLivretAOdile();
	}

	private void creerBanques() throws ServiceException {

		banqueService.creerReference(new Banque("CEIDF-OSNY", "Caisse d'Epargne - Agence : 42 T RUE ARISTIDE BRIAND, 95520 OSNY"));
		banqueService.creerReference(new Banque("LCL-LE-RAINCY", "Crédit Lyonnais - Agence : RAINCY PLACE GARE BP, 93340 LE RAINCY"));
		banqueService.creerReference(new Banque("LA-BANQUE-POSTALE", "PARIS"));
	}

	private void creerTitulaires() throws ServiceException {

		titulaireService.creerReference(new Titulaire("ODILE", "Odile Danis née Zirah le 14/10/1961"));
		titulaireService.creerReference(new Titulaire("THIERRY", "Thierry Danis né le 29/10/1962"));
	}

	private void creerBeneficiaires() throws ServiceException {

		beneficiaireService.creerReference(new Beneficiaire("ODILE", "Odile"));
		beneficiaireService.creerReference(new Beneficiaire("THIERRY", "Thierry"));
		beneficiaireService.creerReference(new Beneficiaire("FLORIAN", "Florian"));
		beneficiaireService.creerReference(new Beneficiaire("THIBAULT", "Thibault"));
		beneficiaireService.creerReference(new Beneficiaire("OISEAUX", "Les oiseaux du jardin"));

		beneficiaireService.creerReference(new Beneficiaire("VACANCES-2025-10", "Vacances en Dordogne du 30/09/2025 au 11/10/2025 - Gîte Savignac de Miremont"));

		beneficiaireService.creerReference(new Beneficiaire("GOLF-CF-830-MA", "La VW-Golf achetée en ...?"));
		beneficiaireService.creerReference(new Beneficiaire("FJR-", "La YAMAHA-FJR achetée en ...?"));

		beneficiaireService.creerReference(new Beneficiaire("MAISON-PATIS-OSNY", "Habitation principale 33 rue des Pâtis"));
		beneficiaireService.creerReference(new Beneficiaire("IMMEUBLE-COTOR-GRADIGNAN", "Appartement mis en location à Gradignan"));
		beneficiaireService.creerReference(new Beneficiaire("IMMEUBLE-CREATIV-CERGY", "Appartement mis en location à Cergy"));
		beneficiaireService.creerReference(new Beneficiaire("IMMEUBLE-BELAIR-PORNICHET", "Résidence secondaire à Pornichet"));

	}

	private void creerCategories() throws ServiceException {

		categorieService.creerReference(new Categorie("REVENUS", "Revenus du foyer (salaires, pensions, revenus locatifs, dividendes...)"));
		categorieService.creerReference(new Categorie("FONCTIONNEMENT", "Dépenses de fonctionnement du foyer (alimentation, consommation d'eau et d'électricité, loyers, charges récurrentes, petites fournitures, travaux d'entretien...)"));
		categorieService.creerReference(new Categorie("EQUIPEMENTS", "Achat d'équipements pour le foyer (électro-ménager, audio-visuel, cuisine équipée, mobilier...), travaux d'amélioration de l'habitat"));
		categorieService.creerReference(new Categorie("LOISIRS", "Dépense pour les loisirs (restaurants, spectacles, abonnements salle de sport, équipement sportif, hôtels, forfaits de ski...)"));
		categorieService.creerReference(new Categorie("HABILLEMENT", "Dépenses pour l'habillement des membres du foyer (vêtements, chaussures, accessoires, maquillage...)"));
		categorieService.creerReference(new Categorie("SANTE", "Dépenses de santé et remboursements des organismes sociaux ou des mutuelles"));
		categorieService.creerReference(new Categorie("TRANSPORT", "Achat ou vente de véhicules, entretien des véhicules, carburants, abonnements transports en commun, billets de train ou d'avion..."));
		categorieService.creerReference(new Categorie("DONS", "Dons faits à des personnes ou à des organismes"));
		categorieService.creerReference(new Categorie("IMPOTS-TAXES-AMENDES", "Impôts et taxes, amendes"));
	}

	private void creerSousCategories() throws ServiceException {

		Categorie revenus = categorieService.rechercherParNom("REVENUS");

		sousCategorieService.creerReference(new SousCategorie("SALAIRES", "Salaires", revenus));
		sousCategorieService.creerReference(new SousCategorie("RETRAITES", "Pensions de retraite", revenus));
		sousCategorieService.creerReference(new SousCategorie("ALLOCATIONS", "Allocations diverses", revenus));
		sousCategorieService.creerReference(new SousCategorie("LOCATIFS", "Revenus locatifs de biens immobiliers", revenus));
		sousCategorieService.creerReference(new SousCategorie("DIVIDENDES", "Revenus des placements financiers", revenus));
		sousCategorieService.creerReference(new SousCategorie("EMPRUNTS", "Emprunts souscrits (capitaux versés par des prêteurs)", revenus));

		Categorie fonctionnement = categorieService.rechercherParNom("FONCTIONNEMENT");

		sousCategorieService.creerReference(new SousCategorie("RAVITAILLEMENT", "Alimentation, petites fournitures, lessives...", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("CANTINE", "Cantines", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("ASSURANCES", "Assurances diverses", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("TELEPHONE", "Factures téléphoniques", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("ELECTRICITE", "Factures d'électricité", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("EAU", "Factures d'eau", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("PRESSING-LAVERIE", "Frais de nettoyage", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("MENAGE", "Service ménage à domicile", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("SECURITE", "Télésurveillance, systèmes d'alarme...", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("LOYER", "Loyer payé et charges", fonctionnement));
		sousCategorieService.creerReference(new SousCategorie("FRAIS-BANCAIRES", "Frais bancaires divers", fonctionnement));

		Categorie equipements = categorieService.rechercherParNom("EQUIPEMENTS");

		sousCategorieService.creerReference(new SousCategorie("ELECTRO-MENAGER", "Appareils électro-ménagers (lave-linge, aspirateur, tondeuse à gazon,...)", equipements));
		sousCategorieService.creerReference(new SousCategorie("VEHICULES", "Voitures, motos, trottinette et vélo électrique, vélo, patins à roulettes, patins à glace, traineaux,...)", equipements));


		Categorie loisirs = categorieService.rechercherParNom("LOISIRS");

		sousCategorieService.creerReference(new SousCategorie("RESTAURANTS", "Restaurants", loisirs));
		sousCategorieService.creerReference(new SousCategorie("JEUX-SOCIETE", "Jeux de société et figurines", loisirs));
		sousCategorieService.creerReference(new SousCategorie("ABONNEMENTS-INTERNET", "Abonnements à des services internet payants", loisirs));
		sousCategorieService.creerReference(new SousCategorie("ABONNEMENTS-PAPIER", "Abonnements à des journaux ou magazines papier", loisirs));
		sousCategorieService.creerReference(new SousCategorie("ABONNEMENTS-STREAMING", "Abonnements à des sites de streamming (Netflix, Canal+, Deezer...", loisirs));
		sousCategorieService.creerReference(new SousCategorie("LIVRES", "Livres (papier et numériques)", loisirs));
		sousCategorieService.creerReference(new SousCategorie("SPORT", "Abonnement à des clubs ou des associations sportives, équipement sportif", loisirs));

		Categorie habillement = categorieService.rechercherParNom("HABILLEMENT");

		sousCategorieService.creerReference(new SousCategorie("VETEMENTS", "Vêtements", habillement));


		Categorie sante = categorieService.rechercherParNom("SANTE");

		sousCategorieService.creerReference(new SousCategorie("IMAGERIE", "Imagerie médicale", sante));
		sousCategorieService.creerReference(new SousCategorie("ANALYSES", "Analyses médicales", sante));
		sousCategorieService.creerReference(new SousCategorie("GENERALISTE", "Médecin généraliste", sante));
		sousCategorieService.creerReference(new SousCategorie("SPECIALISTE", "Médecin spécialiste", sante));
		sousCategorieService.creerReference(new SousCategorie("MUTUELLES", "Cotisations et remboursements CPAM ou mutuelles", sante));
		sousCategorieService.creerReference(new SousCategorie("PHARMACIE", "Médicaments remboursables et non remboursables", sante));
		sousCategorieService.creerReference(new SousCategorie("HOPITAL", "Hospitalisations", sante));


		Categorie transport = categorieService.rechercherParNom("TRANSPORT");

		sousCategorieService.creerReference(new SousCategorie("CARBURANT", "Carburant", transport));
		sousCategorieService.creerReference(new SousCategorie("PARKING", "Stationnement", transport));
		sousCategorieService.creerReference(new SousCategorie("DEPLACEMENTS", "Billets transports en commun, stationnement, location de voitures, abonnements annuels, péages", fonctionnement));


		Categorie dons = categorieService.rechercherParNom("DONS");

		sousCategorieService.creerReference(new SousCategorie("FAMILLE", "Cadeaux à la famille ou à des amis", dons));
		sousCategorieService.creerReference(new SousCategorie("ORGANISMES", "Dons à des organismes caritatifs", dons));
		sousCategorieService.creerReference(new SousCategorie("ASSOCIATIONS", "Dons à des associations", dons));


		Categorie impots = categorieService.rechercherParNom("IMPOTS-TAXES-AMENDES");

		sousCategorieService.creerReference(new SousCategorie("IMPOT-REVENUS", "Impôt sur le revenu", impots));
		sousCategorieService.creerReference(new SousCategorie("IMPOT-FONCIER", "Impôt foncier", impots));

	}

	private void creerComptesExternes() throws ServiceException {

		compteExterneService.creerCompte(new CompteExterne(
				"CNAVTS",
				"Assurance retraite secteur privé"));
		compteExterneService.creerCompte(new CompteExterne(
				"CPAM-95",
				"CPAM du val d'Oise"));
		compteExterneService.creerCompte(new CompteExterne(
				"MGEFI",
				"Mutuelle Générale des Finances Publiques"));
		compteExterneService.creerCompte(new CompteExterne(
				"SAFRAN",
				"Safran Eragny"));
		compteExterneService.creerCompte(new CompteExterne(
				"CITYA",
				"GERANCE BURDIGALA BORD"));
		compteExterneService.creerCompte(new CompteExterne(
				"DRFIP-PAYS-DE-LA-LOIRE",
				"Assurance retraite fonction publique"));
		compteExterneService.creerCompte(new CompteExterne(
				"MALAKOFF-HUMANIS-AGIRC",
				"Retraite complémentaire secteur privé AGIRC-ARRCO"));
		compteExterneService.creerCompte(new CompteExterne(
				"LAVERIE-BEL-AIR",
				"Jetons de laverie de la résidence BEL-AIR"));
		compteExterneService.creerCompte(new CompteExterne(
				"MEDECINS",
				"Consultations médicales"));
		compteExterneService.creerCompte(new CompteExterne(
				"CHATGPT",
				"OPENAI *CHATGPT"));
		compteExterneService.creerCompte(new CompteExterne(
				"MIDJOURNEY",
				"MIDJOURNEY INC."));
		compteExterneService.creerCompte(new CompteExterne(
				"RESTAURANTS-DIVERS",
				"Restaurants divers, bars, brasserie et cantines"));
		compteExterneService.creerCompte(new CompteExterne(
				"HYPERS-DIVERS",
				"Hypermarchés et supermarchés"));
		compteExterneService.creerCompte(new CompteExterne(
				"PROXIMITE-DIVERS",
				"Commerces de bouche de proximité"));
		compteExterneService.creerCompte(new CompteExterne(
				"KOBO",
				"Contenu multimedia pour la liseuse"));
		compteExterneService.creerCompte(new CompteExterne(
				"GROUPAMA",
				"Assurances GROUPAMA"));
		compteExterneService.creerCompte(new CompteExterne(
				"HANDICAP-INTERNATIONAL",
				"Association d'aide aux personnes handicapées"));
		compteExterneService.creerCompte(new CompteExterne(
				"UNADEV",
				"Association d'aide aux personnes aveugles"));
		compteExterneService.creerCompte(new CompteExterne(
				"LPO",
				"Ligue de Protection des Oiseaux"));
		compteExterneService.creerCompte(new CompteExterne(
				"JEUX-DIVERS",
				"Magasins de jeux de société"));
		compteExterneService.creerCompte(new CompteExterne(
				"MULTIMEDIA-DIVERS",
				"Magasin multimedia : ordinateurs, consoles de jeu, téléviseurs, matériel audio..."));
		compteExterneService.creerCompte(new CompteExterne(
				"LE-MONDE-FR",
				"Quotidien \"Le Monde\""));
		compteExterneService.creerCompte(new CompteExterne(
				"DGFIP",
				"Impôts : taxes foncières, revenus, crédits d'impôts..."));
		compteExterneService.creerCompte(new CompteExterne(
				"FREE",
				"Free Telecom"));
		compteExterneService.creerCompte(new CompteExterne(
				"CYO",
				"Fournisseur d'eau"));
		compteExterneService.creerCompte(new CompteExterne(
				"WWF",
				"Association de protection de la nature"));
		compteExterneService.creerCompte(new CompteExterne(
				"SHIVA",
				"Prestation de ménage"));
		compteExterneService.creerCompte(new CompteExterne(
				"CANAL+",
				"Contenus vidéo"));
		compteExterneService.creerCompte(new CompteExterne(
				"OFAR",
				"Assureur pour la moto"));
		compteExterneService.creerCompte(new CompteExterne(
				"GUSTAVE-ROUSSY",
				"Recherche médicale contre les cancers"));
		compteExterneService.creerCompte(new CompteExterne(
				"VERISURE",
				"Télésurveillance"));
		compteExterneService.creerCompte(new CompteExterne(
				"CULLIGAN-95",
				"Traitement d'adoucissement d'eau"));
		compteExterneService.creerCompte(new CompteExterne(
				"DISTRIBUTEURS-BOISSON-DIVERS",
				"Distributeurs de boisson ou de friandises"));
		compteExterneService.creerCompte(new CompteExterne(
				"ORGANSIMES-CARITATIFS-DIVERS",
				"Organismes caritatifs divers"));
		compteExterneService.creerCompte(new CompteExterne(
				"CLINIQUE-SAINTE-MARIE",
				"Clinique Sainte-Marie OSNY (hospitalisations, consultations, imagerie...)"));
		compteExterneService.creerCompte(new CompteExterne(
				"PHARMACIE-GUIOT",
				"Pharmacie Guiot rue Aristide Briand à OSNY"));
		compteExterneService.creerCompte(new CompteExterne(
				"HOPITAL-PITIE-SALPETRIERE",
				"Hôpital Pitté-Salpêtrière - PARIS"));
		compteExterneService.creerCompte(new CompteExterne(
				"EDF",
				"Fournisseur d'électricité"));
		compteExterneService.creerCompte(new CompteExterne(
				"STATIONS-SERVICE-DIVERSES",
				"Fournisseur de carburant"));
		compteExterneService.creerCompte(new CompteExterne(
				"HERMES-INTERNATIONAL",
				"Société internationale de LUXE"));
		compteExterneService.creerCompte(new CompteExterne(
				"CAISSE-EPARGNE",
				"Services Caisse d'Épargne"));


	}

	private void creerComptesInternes() throws ServiceException {

		Banque caisseEpargne = banqueService.rechercherParNom("CEIDF-OSNY");
		Banque laPoste = banqueService.rechercherParNom("LA-BANQUE-POSTALE");

		Titulaire thierry = titulaireService.rechercherParNom("THIERRY");
		Titulaire odile = titulaireService.rechercherParNom("ODILE");

		compteInterneService.creerCompte(new CompteInterne(
				"COMPTE-JOINT", 
				"COMPTE DE DEPOT JOINT N° 17515 00092 04953671374 - BIC : CEPA FRPP 751 - IBAN : FR76 1751 5000 9204 9536 7137 415",
				null,
				TypeFonctionnement.COURANT,
				LocalDate.parse("2025-05-07"), 
				4817134L,
				caisseEpargne,
				thierry, odile));
		compteInterneService.creerCompte(new CompteInterne(
				"LIVRET-A-ODILE",
				"LIVRET A N° 17515 00092 00681246659",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2023-01-09"), 
				5086L,
				caisseEpargne,
				odile));
		compteInterneService.creerCompte(new CompteInterne(
				"AV-MILLEVIE-ODILE",
				"BPCE - Assurance-vie Millevie Infinie 2 Nº INFI2043728",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2023-10-14"), 
				10000000L,
				caisseEpargne,
				odile));
		compteInterneService.creerCompte(new CompteInterne(
				"LA-POSTE-ODILE",
				"Compte 19 611 20 H Paris",
				null,
				TypeFonctionnement.COURANT,
				LocalDate.parse("2025-06-03"), 
				11731L,
				laPoste,
				odile));
		compteInterneService.creerCompte(new CompteInterne(
				"AV-INVEST-5-ODILE",
				"GENERALI - Assurance-vie ESPACE INVEST 5 - N° du contrat : OC950101182",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2019-04-11"), 
				9250000L,
				caisseEpargne,
				odile));
		compteInterneService.creerCompte(new CompteInterne(
				"AV-INVEST-4-ODILE",
				"GENERALI - Assurance-vie  Espace Invest 4 - N° 90954679",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2013-11-09"), 
				6000000L,
				caisseEpargne,
				odile));
		compteInterneService.creerCompte(new CompteInterne(
				"AV-NUANCE-3D",
				"CNP - Assurance-vie NUANCE-3D - référence client : 06842324900, numéro adhésion : 617 768274 04",
				null,
				TypeFonctionnement.FINANCIER,
				LocalDate.parse("2007-03-21"), 
				410000L,
				caisseEpargne,
				odile));
		compteInterneService.creerCompte(new CompteInterne(
				"IMMEUBLE-CREATIV-CERGY",
				"Appartement de Cergy",
				null,
				TypeFonctionnement.BIEN,
				LocalDate.parse("2014-03-05"),
				24850000L,
				caisseEpargne,
				odile, thierry));
		compteInterneService.creerCompte(new CompteInterne(
				"IMMEUBLE-COTOR-GRADIGNAN",
				"Appartement de Gradignan",
				null,
				TypeFonctionnement.BIEN,
				LocalDate.parse("2005-03-05"),
				11000000L,
				caisseEpargne,
				odile, thierry));
		compteInterneService.creerCompte(new CompteInterne(
				"EMPRUNT-CREATIV-CERGY",
				"Emprunt pour l'achat de l'appartement locatif de Cergy",
				null,
				TypeFonctionnement.COURANT,
				LocalDate.parse("2014-03-05"),
				-24850000L,
				caisseEpargne,
				odile, thierry));

	}

	private void creerComptesTechniques() throws ServiceException {


	}

	private void creerOperationsRecetteCompteJointReleve199() throws ServiceException {

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");

		Beneficiaire odile = beneficiaireService.rechercherParNom("ODILE");
		Beneficiaire thierry = beneficiaireService.rechercherParNom("THIERRY");
		Beneficiaire gradignanCotor = beneficiaireService.rechercherParNom("GRADIGNAN-COTOR");

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Dividendes Hermès International", 
				LocalDate.parse("2025-05-07"), 
				1575L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("HERMES-INTERNATIONAL"),
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Dividendes Hermès International", 
						LocalDate.parse("2025-05-07"), 
						1575L, 
						sousCategorieService.rechercherParNom("DIVIDENDES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Versement retraite", 
				LocalDate.parse("2025-05-09"), 
				24133L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("CNAVTS"),
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement retraite", 
						LocalDate.parse("2025-05-09"), 
						24133L, 
						sousCategorieService.rechercherParNom("RETRAITES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Remboursement CPAM", 
				LocalDate.parse("2025-05-12"), 
				3736L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("CPAM-95"),
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Remboursement CPAM", 
						LocalDate.parse("2025-05-12"), 
						3736L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Remboursement CPAM", 
				LocalDate.parse("2025-05-13"), 
				3467L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("CPAM-95"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Remboursement CPAM", 
						LocalDate.parse("2025-05-13"), 
						3467L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Remboursement mutuelle", 
				LocalDate.parse("2025-05-13"), 
				1672L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("MGEFI"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Remboursement mutuelle", 
						LocalDate.parse("2025-05-13"), 
						1672L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Remboursement mutuelle", 
				LocalDate.parse("2025-05-13"), 
				2036L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("MGEFI"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Remboursement mutuelle", 
						LocalDate.parse("2025-05-13"), 
						2036L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Remboursement CPAM", 
				LocalDate.parse("2025-05-23"), 
				3000L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("CPAM-95"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Remboursement CPAM", 
						LocalDate.parse("2025-05-23"), 
						3000L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Salaire mai 2025", 
				LocalDate.parse("2025-05-23"), 
				578073L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("SAFRAN"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Salaire mai 2025", 
						LocalDate.parse("2025-05-23"), 
						578073L, 
						sousCategorieService.rechercherParNom("SALAIRES"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Versement des loyers Gradignan", 
				LocalDate.parse("2025-05-27"), 
				66841L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("CITYA"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Loyer mensuel", 
						LocalDate.parse("2025-05-27"), 
						66841L, 
						sousCategorieService.rechercherParNom("LOCATIFS"),
						gradignanCotor)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Retraite mai 2025", 
				LocalDate.parse("2025-05-29"), 
				106627L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("DRFIP-PAYS-DE-LA-LOIRE"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Retraite mai 2025", 
						LocalDate.parse("2025-05-29"), 
						106627L, 
						sousCategorieService.rechercherParNom("RETRAITES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Retraite juin 2025", 
				LocalDate.parse("2025-06-02"), 
				28664L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("MALAKOFF-HUMANIS-AGIRC"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Retraite juin 2025", 
						LocalDate.parse("2025-06-02"), 
						28664L, 
						sousCategorieService.rechercherParNom("RETRAITES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TRANSFERT,
				"Clôture d'office d'un compte inutilisé", 
				LocalDate.parse("2025-06-03"), 
				11731L, 
				compteJoint, 
				compteInterneService.rechercherParIdentifiant("LA-POSTE-ODILE"),
				Boolean.FALSE,
				new OperationLigne(
						0, 
						"Clôture d'office d'un compte inutilisé", 
						LocalDate.parse("2025-06-03"), 
						11731L, 
						null)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RECETTE,
				"Versement retraite juin 2025", 
				LocalDate.parse("2025-06-06"), 
				24133L, 
				compteJoint, 
				compteExterneService.rechercherParIdentifiant("CNAVTS"),
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement retraite juin 2025", 
						LocalDate.parse("2025-06-06"), 
						24133L, 
						sousCategorieService.rechercherParNom("RETRAITES"),
						odile)
				));
	}

	private void creerOperationsDepenseCompteJointReleve199() throws ServiceException {

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");

		Beneficiaire odile = beneficiaireService.rechercherParNom("ODILE");
		Beneficiaire thierry = beneficiaireService.rechercherParNom("THIERRY");
		Beneficiaire thibault = beneficiaireService.rechercherParNom("THIBAULT");

		Beneficiaire golf = beneficiaireService.rechercherParNom("GOLF-CF-830-MA");
		Beneficiaire moto = beneficiaireService.rechercherParNom("FJR-");

		Beneficiaire pornichetBelair = beneficiaireService.rechercherParNom("PORNICHET-BELAIR");

		operationService.creerOperation(new Operation(
				"CHQ-8197515", 
				TypeOperation.DEPENSE,
				"Jetons laverie Pornichet", 
				LocalDate.parse("2025-05-19"), 
				1200L, 
				compteExterneService.rechercherParIdentifiant("LAVERIE-BEL-AIR"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Jetons laverie Pornichet", 
						LocalDate.parse("2025-05-19"), 
						1200L, 
						sousCategorieService.rechercherParNom("PRESSING-LAVERIE"),
						odile,
						pornichetBelair)
				));
		operationService.creerOperation(new Operation(
				"CHQ-8197519", 
				TypeOperation.DEPENSE,
				"Consultation", 
				LocalDate.parse("2025-05-22"), 
				3000L, 
				compteExterneService.rechercherParIdentifiant("MEDECINS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Consultation", 
						LocalDate.parse("2025-05-22"), 
						3000L, 
						sousCategorieService.rechercherParNom("GENERALISTE"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"* COTIS FORMULE CONFORT", 
				LocalDate.parse("2025-05-15"), 
				1775L, 
				compteExterneService.rechercherParIdentifiant("CAISSE-EPARGNE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"* COTIS FORMULE CONFORT", 
						LocalDate.parse("2025-05-15"), 
						1775L, 
						sousCategorieService.rechercherParNom("FRAIS-BANCAIRES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"*COM CB INT OPENAI *CHATGPT", 
				LocalDate.parse("2025-06-05"), 
				159L, 
				compteExterneService.rechercherParIdentifiant("CHATGPT"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"*COM CB INT OPENAI *CHATGPT", 
						LocalDate.parse("2025-06-05"), 
						159L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-INTERNET"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"*COM CB INT MIDJOURNEY INC.", 
				LocalDate.parse("2025-06-05"), 
				189L, 
				compteExterneService.rechercherParIdentifiant("MIDJOURNEY"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"*COM CB INT MIDJOURNEY INC.", 
						LocalDate.parse("2025-06-05"), 
						189L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-INTERNET"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *LUDUM S FACT 280425", 
				LocalDate.parse("2025-06-04"), 
				3040L, 
				compteExterneService.rechercherParIdentifiant("JEUX-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *LUDUM S FACT 280425", 
						LocalDate.parse("2025-06-04"), 
						3040L, 
						sousCategorieService.rechercherParNom("JEUX-SOCIETE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB CHRISTOPHE CREV FACT 300425", 
				LocalDate.parse("2025-06-04"), 
				500L, 
				compteExterneService.rechercherParIdentifiant("DISTRIBUTEURS-BOISSON-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB CHRISTOPHE CREV FACT 300425", 
						LocalDate.parse("2025-06-04"), 
						500L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUX PAINS DES I FACT 040525", 
				LocalDate.parse("2025-06-04"), 
				565L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUX PAINS DES I FACT 040525", 
						LocalDate.parse("2025-06-04"), 
						565L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB LEON DE BRUXELL FACT 020525", 
				LocalDate.parse("2025-06-04"), 
				6610L, 
				compteExterneService.rechercherParIdentifiant("RESTAURANTS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB LEON DE BRUXELL FACT 020525", 
						LocalDate.parse("2025-06-04"), 
						6610L, 
						sousCategorieService.rechercherParNom("RESTAURANTS"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB SODEXO SAGEM FACT 020525", 
				LocalDate.parse("2025-06-04"), 
				365L, 
				compteExterneService.rechercherParIdentifiant("RESTAURANTS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB LEON DE BRUXELL FACT 020525", 
						LocalDate.parse("2025-06-04"), 
						365L, 
						sousCategorieService.rechercherParNom("CANTINE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN OSNY FACT 030525", 
				LocalDate.parse("2025-06-04"), 
				14485L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN OSNY FACT 030525", 
						LocalDate.parse("2025-06-04"), 
						14485L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN DAC FACT 030525", 
				LocalDate.parse("2025-06-04"), 
				8409L, 
				compteExterneService.rechercherParIdentifiant("STATIONS-SERVICE-DIVERSES"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN DAC FACT 030525", 
						LocalDate.parse("2025-06-04"), 
						8409L, 
						sousCategorieService.rechercherParNom("CARBURANT"),
						golf)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *PHILIBE FACT 300425", 
				LocalDate.parse("2025-06-04"), 
				11658L, 
				compteExterneService.rechercherParIdentifiant("JEUX-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *PHILIBE FACT 300425", 
						LocalDate.parse("2025-06-04"), 
						11658L, 
						sousCategorieService.rechercherParNom("JEUX-SOCIETE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB CHRISTOPHE CREV FACT 090525", 
				LocalDate.parse("2025-06-04"), 
				500L, 
				compteExterneService.rechercherParIdentifiant("DISTRIBUTEURS-BOISSON-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB CHRISTOPHE CREV FACT 090525", 
						LocalDate.parse("2025-06-04"), 
						500L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB SODEXO SAGEM FACT 090525", 
				LocalDate.parse("2025-06-04"), 
				284L, 
				compteExterneService.rechercherParIdentifiant("RESTAURANTS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB SODEXO SAGEM FACT 090525", 
						LocalDate.parse("2025-06-04"), 
						284L, 
						sousCategorieService.rechercherParNom("CANTINE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN OSNY FACT 100525", 
				LocalDate.parse("2025-06-04"), 
				10878L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN OSNY FACT 100525", 
						LocalDate.parse("2025-06-04"), 
						10878L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB LMFR OSNY FACT 100525", 
				LocalDate.parse("2025-06-04"), 
				500L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB LMFR OSNY FACT 100525", 
						LocalDate.parse("2025-06-04"), 
						500L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUX PAINS DES I FACT 040525", 
				LocalDate.parse("2025-06-04"), 
				2580L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUX PAINS DES I FACT 040525", 
						LocalDate.parse("2025-06-04"), 
						2580L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB OPENAI *CHATGPT FACT 090525 (EN USD 24,00)", 
				LocalDate.parse("2025-06-05"), 
				2141L, 
				compteExterneService.rechercherParIdentifiant("CHATGPT"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB OPENAI *CHATGPT FACT 090525 (EN USD 24,00)", 
						LocalDate.parse("2025-06-05"), 
						2141L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-INTERNET"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB CHRISTOPHE CREV FACT 150525", 
				LocalDate.parse("2025-06-04"), 
				500L, 
				compteExterneService.rechercherParIdentifiant("DISTRIBUTEURS-BOISSON-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB CHRISTOPHE CREV FACT 150525", 
						LocalDate.parse("2025-06-04"), 
						500L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUX PAINS DES I FACT 180525", 
				LocalDate.parse("2025-06-04"), 
				555L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUX PAINS DES I FACT 180525", 
						LocalDate.parse("2025-06-04"), 
						555L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN OSNY FACT 170525", 
				LocalDate.parse("2025-06-04"), 
				16479L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN OSNY FACT 170525", 
						LocalDate.parse("2025-06-04"), 
						16479L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *GAMEFOU FACT 170525", 
				LocalDate.parse("2025-06-04"), 
				6600L, 
				compteExterneService.rechercherParIdentifiant("JEUX-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *GAMEFOU FACT 170525", 
						LocalDate.parse("2025-06-04"), 
						6600L, 
						sousCategorieService.rechercherParNom("JEUX-SOCIETE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB CHRISTOPHE CREV FACT 220525", 
				LocalDate.parse("2025-06-04"), 
				500L, 
				compteExterneService.rechercherParIdentifiant("DISTRIBUTEURS-BOISSON-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB CHRISTOPHE CREV FACT 220525", 
						LocalDate.parse("2025-06-04"), 
						500L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB APG DES BELLEVU FACT 220525", 
				LocalDate.parse("2025-06-04"), 
				3000L, 
				compteExterneService.rechercherParIdentifiant("RESTAURANTS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB APG DES BELLEVU FACT 220525", 
						LocalDate.parse("2025-06-04"), 
						3000L, 
						sousCategorieService.rechercherParNom("CANTINE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PROXI FACT 250525", 
				LocalDate.parse("2025-06-04"), 
				689L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PROXI FACT 250525", 
						LocalDate.parse("2025-06-04"), 
						689L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUX PAINS DES I FACT 250525", 
				LocalDate.parse("2025-06-04"), 
				505L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUX PAINS DES I FACT 250525", 
						LocalDate.parse("2025-06-04"), 
						505L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN 0571C FACT 230525", 
				LocalDate.parse("2025-06-04"), 
				3307L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN 0571C FACT 230525", 
						LocalDate.parse("2025-06-04"), 
						3307L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB Boulanger FACT 240525", 
				LocalDate.parse("2025-06-04"), 
				4598L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB Boulanger FACT 240525", 
						LocalDate.parse("2025-06-04"), 
						4598L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB Fonds de dotati FACT 240525", 
				LocalDate.parse("2025-06-04"), 
				100L, 
				compteExterneService.rechercherParIdentifiant("ORGANSIMES-CARITATIFS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB Fonds de dotati FACT 240525", 
						LocalDate.parse("2025-06-04"), 
						100L, 
						sousCategorieService.rechercherParNom("ORGANISMES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN OSNY FACT 240525", 
				LocalDate.parse("2025-06-04"), 
				17655L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN OSNY FACT 240525", 
						LocalDate.parse("2025-06-04"), 
						17655L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB MIDJOURNEY INC. FACT 280525 (EN USD 36,00)", 
				LocalDate.parse("2025-06-05"), 
				3180L, 
				compteExterneService.rechercherParIdentifiant("MIDJOURNEY"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB MIDJOURNEY INC. FACT 280525 (EN USD 36,00)", 
						LocalDate.parse("2025-06-05"), 
						3180L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-INTERNET"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *GAMEFOU FACT 280525", 
				LocalDate.parse("2025-06-04"), 
				3170L, 
				compteExterneService.rechercherParIdentifiant("JEUX-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *GAMEFOU FACT 280525", 
						LocalDate.parse("2025-06-04"), 
						3170L, 
						sousCategorieService.rechercherParNom("JEUX-SOCIETE"),
						thierry)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *LPO SYL FACT 020525 - Abonnement 1 an \"L'Oiseaumag\"", 
				LocalDate.parse("2025-06-04"), 
				7000L, 
				compteExterneService.rechercherParIdentifiant("LPO"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *LPO SYL FACT 020525 - Abonnement 1 an \"L'Oiseaumag\"", 
						LocalDate.parse("2025-06-04"), 
						7000L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-PAPIER"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB IMAGERIE MEDICA FACT 020525 - IRM Fracture pied droit - Dr Anne Saison", 
				LocalDate.parse("2025-06-04"), 
				13000L, 
				compteExterneService.rechercherParIdentifiant("CLINIQUE-SAINTE-MARIE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB IMAGERIE MEDICA FACT 020525 - IRM Fracture pied droit - Dr Anne Saison", 
						LocalDate.parse("2025-06-04"), 
						13000L, 
						sousCategorieService.rechercherParNom("IMAGERIE"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB DE LAROUSSILHE FACT 060525 - Consultation ophtalmologie", 
				LocalDate.parse("2025-06-04"), 
				5503L, 
				compteExterneService.rechercherParIdentifiant("CLINIQUE-SAINTE-MARIE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB DE LAROUSSILHE FACT 060525 - Consultation ophtalmologie", 
						LocalDate.parse("2025-06-04"), 
						5503L, 
						sousCategorieService.rechercherParNom("SPECIALISTE"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUX PAINS DES I FACT 070525", 
				LocalDate.parse("2025-06-04"), 
				975L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUX PAINS DES I FACT 070525", 
						LocalDate.parse("2025-06-04"), 
						975L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PHARMACIE GUIOT FACT 070525", 
				LocalDate.parse("2025-06-04"), 
				3145L, 
				compteExterneService.rechercherParIdentifiant("PHARMACIE-GUIOT"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PHARMACIE GUIOT FACT 070525", 
						LocalDate.parse("2025-06-04"), 
						3145L, 
						sousCategorieService.rechercherParNom("PHARMACIE"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB BOUCHERIE DU FACT 070525", 
				LocalDate.parse("2025-06-04"), 
				4066L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB BOUCHERIE DU FACT 070525", 
						LocalDate.parse("2025-06-04"), 
						4066L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB LEMONDE.FR FACT 090525 - versement mensuel", 
				LocalDate.parse("2025-06-04"), 
				2080L, 
				compteExterneService.rechercherParIdentifiant("LE-MONDE-FR"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB LEMONDE.FR FACT 090525", 
						LocalDate.parse("2025-06-04"), 
						2080L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-INTERNET"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB 3 BRASSEURS FACT 160525", 
				LocalDate.parse("2025-06-04"), 
				6700L, 
				compteExterneService.rechercherParIdentifiant("RESTAURANTS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB 3 BRASSEURS FACT 160525", 
						LocalDate.parse("2025-06-04"), 
						6700L, 
						sousCategorieService.rechercherParNom("RESTAURANTS"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PICARD SA 418 FACT 160525", 
				LocalDate.parse("2025-06-04"), 
				5800L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PICARD SA 418 FACT 160525", 
						LocalDate.parse("2025-06-04"), 
						5800L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *KOBO FACT 190525", 
				LocalDate.parse("2025-06-04"), 
				1998L, 
				compteExterneService.rechercherParIdentifiant("KOBO"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *KOBO FACT 190525", 
						LocalDate.parse("2025-06-04"), 
						1998L, 
						sousCategorieService.rechercherParNom("LIVRES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN OSNY FACT 220525", 
				LocalDate.parse("2025-06-04"), 
				9389L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN OSNY FACT 220525", 
						LocalDate.parse("2025-06-04"), 
						9389L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB PAYPAL *KOBO FACT 190525", 
				LocalDate.parse("2025-06-04"), 
				2997L, 
				compteExterneService.rechercherParIdentifiant("KOBO"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB PAYPAL *KOBO FACT 190525", 
						LocalDate.parse("2025-06-04"), 
						2997L, 
						sousCategorieService.rechercherParNom("LIVRES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB CANNELLE ORANGE FACT 260525", 
				LocalDate.parse("2025-06-04"), 
				1650L, 
				compteExterneService.rechercherParIdentifiant("PROXIMITE-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB CANNELLE ORANGE FACT 260525", 
						LocalDate.parse("2025-06-04"), 
						1650L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB AUCHAN OSNY FACT 260525", 
				LocalDate.parse("2025-06-04"), 
				1600L, 
				compteExterneService.rechercherParIdentifiant("HYPERS-DIVERS"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB AUCHAN OSNY FACT 260525", 
						LocalDate.parse("2025-06-04"), 
						1600L, 
						sousCategorieService.rechercherParNom("RAVITAILLEMENT"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB APHP TIPI FACT 260525", 
				LocalDate.parse("2025-06-04"), 
				1400L, 
				compteExterneService.rechercherParIdentifiant("HOPITAL-PITIE-SALPETRIERE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB APHP TIPI FACT 260525", 
						LocalDate.parse("2025-06-04"), 
						1400L, 
						sousCategorieService.rechercherParNom("IMAGERIE"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"CB REGIE HORODATEU FACT 270525", 
				LocalDate.parse("2025-06-04"), 
				270L, 
				compteExterneService.rechercherParIdentifiant("HOPITAL-PITIE-SALPETRIERE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"CB REGIE HORODATEU FACT 270525", 
						LocalDate.parse("2025-06-04"), 
						270L, 
						sousCategorieService.rechercherParNom("PARKING"),
						golf)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV GROUPAMA 401374 - P086666319 16700481200076 - mutuelle Thibault", 
				LocalDate.parse("2025-05-09"), 
				6486L, 
				compteExterneService.rechercherParIdentifiant("GROUPAMA"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV GROUPAMA 401374 - P086666319 16700481200076 - mutuelle Thibault", 
						LocalDate.parse("2025-05-09"), 
						6486L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						thibault)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV HANDICAP INTERNATIONAL FRAN P.A au 20250510 , Pour Le 1225286", 
				LocalDate.parse("2025-05-12"), 
				1400L, 
				compteExterneService.rechercherParIdentifiant("HANDICAP-INTERNATIONAL"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV HANDICAP INTERNATIONAL FRAN P.A au 20250510 , Pour Le 1225286", 
						LocalDate.parse("2025-05-12"), 
						1400L, 
						sousCategorieService.rechercherParNom("ORGANISMES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV DON UNADEV MD - CEAN", 
				LocalDate.parse("2025-05-15"), 
				3200L, 
				compteExterneService.rechercherParIdentifiant("UNADEV"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV DON UNADEV MD - CEAN", 
						LocalDate.parse("2025-05-15"), 
						3200L, 
						sousCategorieService.rechercherParNom("ORGANISMES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV DIRECTION GENERALE DES FINA PRELEVEMENT A LA SOURCE REVENUS 2025", 
				LocalDate.parse("2025-05-15"), 
				11800L, 
				compteExterneService.rechercherParIdentifiant("DGFIP"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV DIRECTION GENERALE DES FINA PRELEVEMENT A LA SOURCE REVENUS 2025", 
						LocalDate.parse("2025-05-15"), 
						11800L, 
						sousCategorieService.rechercherParNom("IMPOT-REVENUS"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.INVESTISSEMENT,
				"PRLV 54376D - CNP EV NUANCES 3D 6177682740561776827431751500092 NUANCES 3D", 
				LocalDate.parse("2025-05-16"), 
				3000L, 
				compteInterneService.rechercherParIdentifiant("AV-NUANCE-3D"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV 54376D - CNP EV NUANCES 3D 6177682740561776827431751500092 NUANCES 3D", 
						LocalDate.parse("2025-05-16"), 
						3000L, 
						null)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV FREE MOBILE", 
				LocalDate.parse("2025-06-02"), 
				4824L, 
				compteExterneService.rechercherParIdentifiant("FREE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV FREE MOBILE", 
						LocalDate.parse("2025-06-02"), 
						4824L, 
						sousCategorieService.rechercherParNom("TELEPHONE"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV EDF clients particuliers DANIS ODILE Numero de client : 5007391419 - Numero de compte : 7007202963", 
				LocalDate.parse("2025-06-02"), 
				11225L, 
				compteExterneService.rechercherParIdentifiant("EDF"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV EDF clients particuliers DANIS ODILE Numero de client : 5007391419 - Numero de compte : 7007202963", 
						LocalDate.parse("2025-06-02"), 
						11225L, 
						sousCategorieService.rechercherParNom("ELECTRICITE"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV CYO - 131,96 RUM:XX405790000SEPA/ICS:FR67ZZZ543071-95181791502025105 63922006", 
				LocalDate.parse("2025-06-02"), 
				13196L, 
				compteExterneService.rechercherParIdentifiant("CYO"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV CYO - 131,96 RUM:XX405790000SEPA/ICS:FR67ZZZ543071-95181791502025105 63922006", 
						LocalDate.parse("2025-06-02"), 
						13196L, 
						sousCategorieService.rechercherParNom("EAU"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV WWF", 
				LocalDate.parse("2025-06-03"), 
				500L, 
				compteExterneService.rechercherParIdentifiant("WWF"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV WWF", 
						LocalDate.parse("2025-06-03"), 
						500L, 
						sousCategorieService.rechercherParNom("ORGANISMES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV SHIVA GROUPE SHIVA GROUPE PRELEV SHIVA 874209718", 
				LocalDate.parse("2025-06-03"), 
				21550L, 
				compteExterneService.rechercherParIdentifiant("SHIVA"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV SHIVA GROUPE SHIVA GROUPE PRELEV SHIVA 874209718", 
						LocalDate.parse("2025-06-03"), 
						21550L, 
						sousCategorieService.rechercherParNom("MENAGE"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV CANAL+ France PRLV CANAL Abonnement mensuel", 
				LocalDate.parse("2025-06-04"), 
				12100L, 
				compteExterneService.rechercherParIdentifiant("CANAL+"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV CANAL+ France PRLV CANAL Abonnement mensuel", 
						LocalDate.parse("2025-06-04"), 
						12100L, 
						sousCategorieService.rechercherParNom("ABONNEMENTS-STREAMING"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TRANSFERT,
				"ECH PRET 9355184 DU 05/06/25", 
				LocalDate.parse("2025-06-05"), 
				57054L, 
				compteInterneService.rechercherParIdentifiant("EMPRUNT-CREATIV-CERGY"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Remboursement capital", 
						LocalDate.parse("2025-06-05"), 
						0L, 
						null),
				new OperationLigne(
						1,
						"Intérêts d'emprunt", 
						LocalDate.parse("2025-06-05"), 
						40588L, 
						sousCategorieService.rechercherParNom("FRAIS-BANCAIRES")),
				new OperationLigne(
						2,
						"Assurance emprunteur", 
						LocalDate.parse("2025-06-05"), 
						16466L,
						sousCategorieService.rechercherParNom("ASSURANCES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV OFAR 0625051512924438023100216425777", 
				LocalDate.parse("2025-06-05"), 
				7440L, 
				compteExterneService.rechercherParIdentifiant("OFAR"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV OFAR 0625051512924438023100216425777", 
						LocalDate.parse("2025-06-05"), 
						7440L, 
						sousCategorieService.rechercherParNom("ASSURANCES"),
						moto)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV GUSTAVE ROUSSY DON GUSTAVE ROUSSY", 
				LocalDate.parse("2025-06-05"), 
				1000L, 
				compteExterneService.rechercherParIdentifiant("GUSTAVE-ROUSSY"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV GUSTAVE ROUSSY DON GUSTAVE ROUSSY", 
						LocalDate.parse("2025-06-05"), 
						1000L, 
						sousCategorieService.rechercherParNom("ORGANISMES"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV MGEFI", 
				LocalDate.parse("2025-06-05"), 
				5518L, 
				compteExterneService.rechercherParIdentifiant("MGEFI"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV MGEFI", 
						LocalDate.parse("2025-06-05"), 
						5518L, 
						sousCategorieService.rechercherParNom("MUTUELLES"),
						odile)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV Free Telecom Free HautDebit 1382832549", 
				LocalDate.parse("2025-06-05"), 
				2999L, 
				compteExterneService.rechercherParIdentifiant("FREE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV Free Telecom Free HautDebit 1382832549", 
						LocalDate.parse("2025-06-05"), 
						2999L, 
						sousCategorieService.rechercherParNom("TELEPHONE")
						)
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV VERISURE 85210026", 
				LocalDate.parse("2025-06-06"), 
				5519L, 
				compteExterneService.rechercherParIdentifiant("VERISURE"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV VERISURE 85210026", 
						LocalDate.parse("2025-06-06"), 
						5519L, 
						sousCategorieService.rechercherParNom("SECURITE"))
				));
		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPENSE,
				"PRLV ATS CULLIGAN VAL D OISE-ATS", 
				LocalDate.parse("2025-06-06"), 
				2367L, 
				compteExterneService.rechercherParIdentifiant("CULLIGAN-95"), 
				compteJoint,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"PRLV ATS CULLIGAN VAL D OISE-ATS", 
						LocalDate.parse("2025-06-06"), 
						2367L, 
						sousCategorieService.rechercherParNom("EAU"))
				));

	}

	private void creerOperationsNuance3DOdile() throws ServiceException {

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");
		CompteInterne nuance3D = compteInterneService.rechercherParIdentifiant("AV-NUANCE-3D");

		CompteTechnique compteFrais = compteTechniqueService.rechercherOuCreerCompteTechniqueFrais();
		CompteTechnique compteRemuneration = compteTechniqueService.rechercherOuCreerCompteTechniqueRemuneration();

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Frais bancaires versement initial CNP EV NUANCES 3D", 
				LocalDate.parse("2007-03-21"), 
				15989L,
				compteFrais,
				nuance3D, 
				Boolean.TRUE,
				new OperationLigne(
						0,
						"Frais bancaires versement initial CNP EV NUANCES 3D", 
						LocalDate.parse("2007-03-21"), 
						15989L,
						null)
				));

		LocalDate dateVersement30 = LocalDate.parse("2007-05-16");
		LocalDate dateFin = LocalDate.now();

		while ( dateVersement30.isBefore(dateFin) ) {

			operationService.creerOperation(new Operation(
					null, 
					TypeOperation.INVESTISSEMENT,
					"PRLV CNP EV NUANCES 3D", 
					dateVersement30, 
					3000L, 
					nuance3D, 
					compteJoint,
					Boolean.TRUE,
					new OperationLigne(
							0,
							"PRLV CNP EV NUANCES 3D", 
							dateVersement30, 
							3000L, 
							null)
					));

			operationService.creerOperation(new Operation(
					null, 
					TypeOperation.TECHNIQUE,
					"Frais bancaires sur PRLV CNP EV NUANCES 3D", 
					dateVersement30, 
					117L,
					compteFrais,
					nuance3D,
					Boolean.TRUE,
					new OperationLigne(
							0,
							"Frais bancaires sur PRLV CNP EV NUANCES 3D", 
							dateVersement30, 
							117L,
							null)
					));

			dateVersement30 = dateVersement30.plus(1, ChronoUnit.MONTHS);
		}

		operationService.creerOperation(new Operation(
			null, 
			TypeOperation.TECHNIQUE,
			"Revalorisation", 
			LocalDate.parse("2007-12-31"), 
			1237L, 
			nuance3D, 
			compteRemuneration,
			Boolean.TRUE,
			new OperationLigne(
					0,
					"Revalorisation", 
					LocalDate.parse("2007-12-31"), 
					1237L, 
					null)
			));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2008-04-17"), 
				3145L, 
				nuance3D, 
				compteRemuneration,
				Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2008-04-17"), 
						3145L,
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2008-12-31"), 
				738L, 
				nuance3D, 
				compteRemuneration,
				Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2008-12-31"), 
						738L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2009-04-16"), 
				4226L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2009-04-16"), 
						4226L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2009-12-31"), 
				3634L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2009-12-31"), 
						3634L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2010-04-15"), 
				3360L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2010-04-15"), 
						3360L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2010-12-31"), 
				3506L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2010-12-31"), 
						3506L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2011-04-26"), 
				5205L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2011-04-26"), 
						5205L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2011-12-31"), 
				2681L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2011-12-31"), 
						2681L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2012-04-17"), 
				5343L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2012-04-17"), 
						5343L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2012-12-31"), 
				3156L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2012-12-31"), 
						3156L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2013-03-28"), 
				4225L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2013-03-28"), 
						4225L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2013-12-31"), 
				4708L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2013-12-31"), 
						4708L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2014-03-26"), 
				4524L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2014-03-26"), 
						4524L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2014-12-31"), 
				3700L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2014-12-31"), 
						3700L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2015-03-09"), 
				4589L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2015-03-09"), 
						4589L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2015-12-31"), 
				6515L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2015-12-31"), 
						6515L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2016-03-15"), 
				3661L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2016-03-15"), 
						3661L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2016-12-31"), 
				5951L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2016-12-31"), 
						5951L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2017-03-10"), 
				3071L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2017-03-10"), 
						3071L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2017-12-31"), 
				8086L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2017-12-31"), 
						8086L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2018-03-03"), 
				838L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2018-03-03"), 
						838L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2018-12-31"), 
				9687L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2018-12-31"), 
						9687L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2019-03-29"), 
				1247L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2019-03-29"), 
						1247L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2019-12-31"), 
				7092L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2019-12-31"), 
						7092L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2020-03-31"), 
				2039L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2020-03-31"), 
						2039L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2020-12-31"), 
				6633L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2020-12-31"), 
						6633L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2021-03-16"), 
				2051L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2021-03-16"), 
						2051L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2021-12-31"), 
				6519L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2021-12-31"), 
						6519L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2022-03-22"), 
				2582L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2022-03-22"), 
						2582L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2022-12-31"), 
				12274L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2022-12-31"), 
						12274L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2023-03-22"), 
				1304L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2023-03-22"), 
						1304L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2023-12-31"), 
				20033L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2023-12-31"), 
						20033L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2024-03-20"), 
				1138L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2024-03-20"), 
						1138L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Revalorisation", 
				LocalDate.parse("2024-12-31"), 
				20111L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Revalorisation", 
						LocalDate.parse("2024-12-31"), 
						20111L, 
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.TECHNIQUE,
				"Réinvestissement des dividendes", 
				LocalDate.parse("2025-03-13"), 
				3878L, 
				nuance3D, 
				compteRemuneration, Boolean.TRUE,
				new OperationLigne(
						0,
						"Réinvestissement des dividendes", 
						LocalDate.parse("2025-03-13"), 
						3878L, 
						null)
				));

	//		operationService.creerOperation(new Operation(
	//				null, 
	//				TypeOperation.FRAIS_GESTION,
	//				"Frais bancaires cumulés depuis 2007 sur revalorisations et réinvestissements des dividendes", 
	//				LocalDate.parse("2025-12-09"), 
	//				10822L,
	//				compteFraisGestion,
	//				nuance3D, 
	//				new OperationLigne(
	//						0,
	//						"Frais bancaires cumulés depuis 2007 sur revalorisations et réinvestissements des dividendes", 
	//						LocalDate.parse("2025-12-09"), 
	//						10822L,
	//						categorieFraisGestion)
	//				));
	//
	//	}

	}

	private void creerOperationsEspaceInvest4Odile() throws ServiceException {

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");
		CompteInterne generali = compteInterneService.rechercherParIdentifiant("AV-INVEST-4-ODILE");

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.INVESTISSEMENT,
				"Versement exceptionnel sans frais", 
				LocalDate.parse("2018-02-07"), 
				2500000L,
				generali,
				compteJoint, 
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement exceptionnel sans frais", 
						LocalDate.parse("2018-02-07"), 
						2500000L,
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.INVESTISSEMENT,
				"Versement exceptionnel sans frais", 
				LocalDate.parse("2018-07-07"), 
				2500000L,
				generali,
				compteJoint, 
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement exceptionnel sans frais", 
						LocalDate.parse("2018-07-07"), 
						2500000L,
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.INVESTISSEMENT,
				"Versement libre", 
				LocalDate.parse("2019-02-02"), 
				14150000L,
				generali,
				compteJoint, 
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement libre", 
						LocalDate.parse("2019-02-02"), 
						14150000L,
						null)
				));

	}

	private void creerOperationsLivretAOdile() throws ServiceException {

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");
		CompteInterne livretA = compteInterneService.rechercherParIdentifiant("LIVRET-A-ODILE");

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPOT,
				"Versement libre", 
				LocalDate.parse("2023-10-14"), 
				2000000L,
				livretA,
				compteJoint, 
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement libre", 
						LocalDate.parse("2023-10-14"), 
						2000000L,
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPOT,
				"Versement libre", 
				LocalDate.parse("2024-10-05"), 
				277265L,
				livretA,
				compteJoint, 
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement libre", 
						LocalDate.parse("2024-10-05"), 
						277265L,
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.RETRAIT,
				"Versement libre", 
				LocalDate.parse("2024-10-25"), 
				500000L,
				compteJoint, 
				livretA,
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement libre", 
						LocalDate.parse("2024-10-25"), 
						500000L,
						null)
				));

		operationService.creerOperation(new Operation(
				null, 
				TypeOperation.DEPOT,
				"Versement libre", 
				LocalDate.parse("2024-12-28"), 
				500000L,
				livretA,
				compteJoint, 
				Boolean.FALSE,
				new OperationLigne(
						0,
						"Versement libre", 
						LocalDate.parse("2024-12-28"), 
						500000L,
						null)
				));
	}

	private void creerEvaluationsEspaceInvest5Odile() throws ServiceException {

		CompteInterne generali = compteInterneService.rechercherParIdentifiant("AV-INVEST-5-ODILE");
		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2019-12-31"), 
				9525072L, 
				"Relevé de situation annuel 2019"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2020-12-31"), 
				9337149L, 
				"Relevé de situation annuel 2020"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2021-12-31"), 
				9496733L, 
				"Relevé de situation annuel 2021"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2022-12-31"), 
				9461014L, 
				"Relevé de situation annuel 2022"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2023-12-31"), 
				9932746L, 
				"Relevé de situation annuel 2023"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2024-12-31"), 
				10038474L, 
				"Relevé de situation annuel 2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				generali, 
				compteEvaluations, 
				LocalDate.parse("2026-01-01"), 
				10404277L, 
				"Situation au 01/01/2026"));

	}

	private void creerEvaluationsCompteJoint() throws ServiceException {

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE-JOINT");
		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-01-06"), 
				3103657L, 
				"Relevé de compte décembre 2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-02-06"), 
				3413044L, 
				"Relevé de compte janvier 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-03-06"), 
				4071867L, 
				"Relevé de compte février 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-04-06"), 
				4621325L, 
				"Relevé de compte mars 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-05-06"), 
				4817134L, 
				"Relevé de compte avril 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-06-06"), 
				5304395L, 
				"Relevé de compte mai 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-07-06"), 
				5981470L, 
				"Relevé de compte juin 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-08-06"), 
				6338625L, 
				"Relevé de compte juillet 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-09-07"), 
				6699426L, 
				"Relevé de compte août 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-10-06"), 
				7254438L, 
				"Relevé de compte septembre 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-11-06"), 
				6771157L, 
				"Relevé de compte octobre 2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null, 
				compteJoint, 
				compteEvaluations, 
				LocalDate.parse("2025-12-07"), 
				7219339L, 
				"Relevé de compte novembre 2025"));

	}

	private void creerEvaluationsNuance3D() throws ServiceException {

		CompteInterne nuance3D = compteInterneService.rechercherParIdentifiant("AV-NUANCE-3D");
		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2008-12-31"),
				463619L,
				"Relevé semestriel 31/12/2008"));
		
		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2009-12-31"),
				513769L,
				"Relevé semestriel 31/12/2009"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2010-06-30"),
				530289L,
				"Relevé semestriel 30/06/2010"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2010-12-31"),
				549426L,
				"Relevé semestriel 31/12/2010"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2011-06-30"),
				571632L,
				"Relevé semestriel 30/06/2011"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2011-12-31"),
				599513L,
				"Relevé semestriel 31/12/2011"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2012-06-30"),
				624979L,
				"Relevé semestriel 30/06/2012"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2012-12-31"),
				648457L,
				"Relevé semestriel 31/12/2012"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2013-06-30"),
				666515L,
				"Relevé semestriel 30/06/2013"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2013-12-31"),
				688726L,
				"Relevé semestriel 31/12/2013"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2014-06-30"),
				708295L,
				"Relevé semestriel 30/06/2014"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2014-12-31"),
				728252L,
				"Relevé semestriel 31/12/2014"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2015-06-30"),
				748952L,
				"Relevé semestriel 30/06/2015"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2015-12-31"),
				774176L,
				"Relevé semestriel 31/12/2015"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2016-06-30"),
				793113L,
				"Relevé semestriel 30/06/2016"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2016-12-31"),
				819255L,
				"Relevé semestriel 31/12/2016"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2017-06-30"),
				839959L,
				"Relevé semestriel 30/06/2017"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2017-12-31"),
				865683L,
				"Relevé semestriel 31/12/2017"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2018-12-31"),
				910965L,
				"Relevé annuel 31/12/2018"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2019-12-31"),
				953918L,
				"Relevé annuel 31/12/2019"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2020-12-31"),
				997196L,
				"Relevé annuel 31/12/2020"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2021-12-31"),
				1038108L,
				"Relevé annuel 31/12/2021"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2022-12-31"),
				1088853L,
				"Relevé annuel 31/12/2022"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2023-12-31"),
				1125244L,
				"Relevé annuel 31/12/2023"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2024-12-31"),
				1170103L,
				"Relevé annuel 31/12/2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				nuance3D,
				compteEvaluations,
				LocalDate.parse("2026-01-24"),
				1229117L,
				"Vérification solde en ligne"));

	}

	private void creerEvaluationsEspaceInvest4Odile() throws ServiceException {

		CompteInterne generali = compteInterneService.rechercherParIdentifiant("AV-INVEST-4-ODILE");
		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2017-12-31"),
				6515492L,
				"Relevé trimestriel 31/12/2017"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2018-12-31"),
				11570118L,
				"Relevé trimestriel 31/12/2018"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2019-12-31"),
				26771299L,
				"Relevé trimestriel 31/12/2019"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2020-12-31"),
				26275936L,
				"Relevé trimestriel 31/12/2020"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2021-12-31"),
				26622181L,
				"Relevé trimestriel 31/12/2021"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2022-12-31"),
				26820891L,
				"Relevé trimestriel 31/12/2022"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2023-12-31"),
				27961761L,
				"Relevé trimestriel 31/12/2023"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2024-12-31"),
				28223079L,
				"Relevé trimestriel 31/12/2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2025-03-31"),
				28673435L,
				"Relevé trimestriel 31/03/2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2025-06-30"),
				28876548L,
				"Relevé trimestriel 30/06/2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				generali,
				compteEvaluations,
				LocalDate.parse("2025-09-30"),
				29039229L,
				"Relevé trimestriel 30/09/2025"));

	}

	private void creerEvaluationsMillevieOdile() throws ServiceException {

		CompteInterne bpce = compteInterneService.rechercherParIdentifiant("AV-MILLEVIE-ODILE");
		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();

		evaluationService.creerEvaluation(new Evaluation(
				null,
				bpce,
				compteEvaluations,
				LocalDate.parse("2024-12-31"),
				10039349L,
				"Reprise feuille de calcul colonne 2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				bpce,
				compteEvaluations,
				LocalDate.parse("2025-05-05"),
				10616369L,
				"Reprise feuille de calcul colonne 05/05/2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				bpce,
				compteEvaluations,
				LocalDate.parse("2026-01-06"),
				10694156L,
				"Information trimestrielle 06/01/2026"));

	}

	private void creerEvaluationsLivretAOdile() throws ServiceException {

		CompteInterne livretA = compteInterneService.rechercherParIdentifiant("LIVRET-A-ODILE");
		CompteTechnique compteEvaluations = compteTechniqueService.rechercherOuCreerCompteTechniqueEvaluation();

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2024-01-09"),
				2017735L,
				"Relevé au 09/01/2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2024-07-09"),
				2017735L,
				"Relevé au 09/07/2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2024-10-09"),
				2295000L,
				"Relevé au 09/10/2024"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2025-01-09"),
				2354140L,
				"Relevé au 09/01/2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2025-07-09"),
				2354140L,
				"Relevé au 09/07/2025"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2025-12-31"),
				2404950L,
				"Relevé en ligne"));

		evaluationService.creerEvaluation(new Evaluation(
				null,
				livretA,
				compteEvaluations,
				LocalDate.parse("2026-01-09"),
				2404950L,
				"Relevé de comptes N° 40 au 09/01/2026"));

	}
}
