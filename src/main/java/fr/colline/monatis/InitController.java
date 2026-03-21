package fr.colline.monatis;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTiers;
import fr.colline.monatis.comptes.model.TypeCompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTiersService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.model.references.Banque;
import fr.colline.monatis.operations.controller.OperationController;
import fr.colline.monatis.operations.controller.dto.request.OperationCreationRequestDto;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.service.BanqueService;
import fr.colline.monatis.references.service.BeneficiaireService;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.references.service.SousCategorieService;
import fr.colline.monatis.references.service.TitulaireService;

@RestController
@RequestMapping("/monatis/init")
public class InitController {

	@Autowired private CategorieService categorieService; 
	@Autowired private SousCategorieService sousCategorieService; 
	@Autowired private BanqueService banqueService; 
	@Autowired private TitulaireService titulaireService; 
	@Autowired private BeneficiaireService beneficiaireService; 

	@Autowired private CompteTiersService compteTiersService; 
	@Autowired private CompteInterneService compteInterneService; 

	@Autowired private OperationController operationController; 
	@Autowired private OperationService operationService; 

	@GetMapping("/insertall")
	public void initialisation() throws ServiceException, ControllerException {

		supprimerTout();

		initialiseReferences();
		initialiseComptes();
		initialiseOperations();
	}

	@GetMapping("/deleteall")
	public void suppression() throws ServiceException {

		supprimerTout();
	}

	private void supprimerTout() throws ServiceException {

		// OPERATIONS
		operationService.supprimerTous();

		// COMPTES
		compteTiersService.supprimerTous();
		compteInterneService.supprimerTous();

		// REFERENCES
		titulaireService.supprimerTous();
		sousCategorieService.supprimerTous();
		categorieService.supprimerTous();
		banqueService.supprimerTous();
		beneficiaireService.supprimerTous();
	}

	private void initialiserCompteTiers() throws ServiceException {
		
		compteTiersService.creerCompte(new CompteTiers(
				"DISTRIBUTEURS",
				"Tous distributeurs automatiques"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"ALIMENTATION-PROXIMITE",
				"Tous commerces de bouche de proximité"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"CNAVTS",
				"Assurance retraite civile"));

		compteTiersService.creerCompte(new CompteTiers(
				"CITYA",
				"Gérance COTOR-GRADIGNAN"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"SAFRAN",
				"SAFRAN ELECTRONICS"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"DRFIP PAYS-DE-LA-LOIRE",
				"Assurance retraite fonction publique"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"AGIRC-ARRCO",
				"HUMANIS RETRAITE"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"CAISSE-EPARGNE",
				"Caisse d'Epargne"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"CANTINE DES BELLEVUES",
				"Cantine SAFRAN"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"HYPERS",
				"Tous Super et Hyper-marchés"));
		
		compteTiersService.creerCompte(new CompteTiers(
				"HELLO ASSO",
				"????"));
		
		
	}
	
	private void initialiseReferences() throws ServiceException {

		// CATEGORIES ET SOUS-CATEGORIES
		//
		SousCategorie sousCategorie;

		Categorie cadeauxEtDons = categorieService.creerReference(new Categorie(
				"CADEAUX_ET_DONS",
				"Cadeaux donnés ou reçus"));
		sousCategorie = new SousCategorie(
				"CADEAUX_RECUS",
				"Cadeaux reçus par le foyer");
		sousCategorie.changerCategorie(cadeauxEtDons);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"CADEAUX_DONNES_PERSONNES",
				"Cadeaux donnés à des personnes");
		sousCategorie.changerCategorie(cadeauxEtDons);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"DONS_ORGANISMES",
				"Dons à des organismes, susceptibles d'être déduits des revenus ");
		sousCategorie.changerCategorie(cadeauxEtDons);
		sousCategorieService.creerReference(sousCategorie);

		Categorie revenus = categorieService.creerReference(new Categorie(
				"REVENUS",
				"Revenus divers : salaires, retraite, gains au jeu, héritages..."));
		sousCategorie = new SousCategorie(
				"SALAIRES",
				"Salaires versés par l'employeur");
		sousCategorie.changerCategorie(revenus);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"RETRAITES",
				"Pensions de retraite");
		sousCategorie.changerCategorie(revenus);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"REVENUS_LOCATIFS",
				"Loyers nets perçus");
		sousCategorie.changerCategorie(revenus);
		sousCategorieService.creerReference(sousCategorie);

		Categorie loisirs = categorieService.creerReference(new Categorie(
				"LOISIRS",
				"Loisirs : restaurants, location de vacances..."));
		sousCategorie = new SousCategorie(
				"RESTAURANT",
				"Restaurant");
		sousCategorie.changerCategorie(loisirs);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"ABONNEMENTS_INTERNET",
				"Abonnements Internet : canal+, prime vidéo, deezer, midjourney, journeaux numériques...");
		sousCategorie.changerCategorie(loisirs);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"ABONNEMENTS_ACTIVITES",
				"Abonnements : théâtre, cinema, salle de sport...");
		sousCategorie.changerCategorie(loisirs);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"COTISATIONS_ASSOCIATIONS",
				"Cotisations : associations...");
		sousCategorie.changerCategorie(loisirs);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"LOCATION_HEBERGEMENT",
				"Hôtels, gîtes, appart'hôtels, B&B...");
		sousCategorie.changerCategorie(loisirs);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"DEPLACEMENTS_LOISIRS",
				"Location de voiture, billets de train ou d'avion");
		sousCategorie.changerCategorie(loisirs);
		sousCategorieService.creerReference(sousCategorie);

		Categorie equipement = categorieService.creerReference(new Categorie(
				"EQUIPEMENT",
				"Achat et vente d'équipements durables de valeur supérieure à 200€..."));
		sousCategorie = new SousCategorie(
				"ELECTROMENAGER",
				"Machine à laver, sèche-linge, aspirateur, Kärcher, four, micro-onde...");
		sousCategorie.changerCategorie(equipement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"MULTIMEDIA",
				"Ordinateurs, imprimantes, TV, amplificateurs, enceintes, appareils photo...");
		sousCategorie.changerCategorie(equipement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"OUTILLAGE",
				"Gros outillages comme une défonceuse, une perceuse...");
		sousCategorie.changerCategorie(equipement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"VEHICULES",
				"Voitures, motos, vélos...");
		sousCategorie.changerCategorie(equipement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"MOBILIER",
				"Meubles et décorations...");
		sousCategorie.changerCategorie(equipement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"TRAVAUX_TRANSFORMATION",
				"Travaux de transformation et d'amélioration");
		sousCategorie.changerCategorie(equipement);
		sousCategorieService.creerReference(sousCategorie);

		Categorie fonctionnement = categorieService.creerReference(new Categorie(
				"FONCTIONNEMENT",
				"Frais de fonctionnement : factures d'eau, factures de téléphones, carburants, ménage à domicile, vêtements..."));
		sousCategorie = new SousCategorie(
				"RAVITAILLEMENT",
				"Courses hebdomadaires de ravitaillement : alimentation mais aussi lessives, petites fournitures comme des ampoules, dentifrice...");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"BRICOLAGE",
				"Petits outils, fournitures");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"EAU",
				"Facture d'eau");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"ELECTRICITE",
				"Facture d'électricité");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"BOX",
				"Facture mensuelle pour la box");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"TELEPHONE",
				"Facture mensuelle pour le téléphone");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"CARBURANT",
				"Carburant des véhicules");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"HABILLEMENT",
				"Vêtements, chaussures, ceintures, chapeaux, maillots de bain...");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"LINGE_DE_MAISON",
				"Draps, serviettes de bain, plaids, coussins, nappes, torchons...");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"HYGIENE_BEAUTE",
				"Produits cosmétiques, savons, petite pharmacie...");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"TRAVAUX_REPARATIONS",
				"Travaux d'entretien, réparations diverses...");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorie = new SousCategorie(
				"MENAGE",
				"Service ménage à domicile");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);
		sousCategorie = new SousCategorie(
				"CHARGES_COPROPRIETE",
				"Charges de copropriétés");
		sousCategorie.changerCategorie(fonctionnement);
		sousCategorieService.creerReference(sousCategorie);

		Categorie medical = categorieService.creerReference(new Categorie(
				"MEDICAL",
				"Paiements aux services médicaux et remboursements par la Sécurité Sociale"));
		Categorie impotsEtTaxes = categorieService.creerReference(new Categorie(
				"IMPOTS_TAXES",
				"Impôts et taxes divers, amendes, taxes de séjour..."));
		Categorie fraisFinanciers = categorieService.creerReference(new Categorie(
				"FRAIS_FINANCIERS",
				"Frais facturés pas les banques, agios, intérêts d'emprunt..."));
		Categorie assurances = categorieService.creerReference(new Categorie(
				"ASSURANCES",
				"Primes d'assurance"));
		Categorie logement = categorieService.creerReference(new Categorie(
				"LOGEMENT",
				"Dépenses liées au maintien dans le logement : loyers, charges..."));
		Categorie fraisProfessionnels = categorieService.creerReference(new Categorie(
				"FRAIS_PROFESSIONNELS",
				"Frais occasionnés par la vie professionnelle avancés par le foyer et éventuellement remboursés par l'employeur : restaurants, locations de voitures, hôtels, billets de train ou d'avion..."));

		// BANQUES 
		//
		banqueService.creerReference(new Banque(
				"CEIDF",
				"Caisse d'épargne Ile de France"));

		// TITULAIRES
		//
		titulaireService.creerReference(new Titulaire(
				"ODILE",
				"Odile Zirah Danis"));
		titulaireService.creerReference(new Titulaire(
				"THIERRY",
				"Thierry Danis"));

		// BENEFICIAIRES
		//
		beneficiaireService.creerReference(new Beneficiaire(
				"ODILE", 
				"Odile Zirah Danis"));
		beneficiaireService.creerReference(new Beneficiaire(
				"THIERRY", 
				"Thierry Danis"));
		beneficiaireService.creerReference(new Beneficiaire(
				"NICOLE", 
				"Nicole Schmitt Zirah"));
		beneficiaireService.creerReference(new Beneficiaire(
				"VOITURE_CF-830-MA", 
				"VW Golf n°CF-830-MA"));
	}

	private void initialiseComptes() throws ServiceException {

		// COMPTES DE TIERS
		//
		compteTiersService.creerCompte(new CompteTiers(
				"SHIVA",
				"SHIVA - Service d'aide à la personne"));
		compteTiersService.creerCompte(new CompteTiers(
				"IMPOTS",
				"Trésor public - Impôts divers : revenus, taxes foncières, ..."));
		compteTiersService.creerCompte(new CompteTiers(
				"AMENDES",
				"Trésor public - Amendes diverses"));
		compteTiersService.creerCompte(new CompteTiers(
				"PENSION",
				"Pension - Tous les organismes chargés du versement des pensions"));
		compteTiersService.creerCompte(new CompteTiers(
				"HYPER",
				"Hyper - Tous les hypermarchés"));

		// COMPTES INTERNES
		//
		CompteInterne compteInterne;

		Banque caisseEpargne = banqueService.rechercherParNom("CEIDF");
		Titulaire odile = titulaireService.rechercherParNom("ODILE");
		Titulaire thierry = titulaireService.rechercherParNom("THIERRY");

		//			// Création Livret A Odile
		//			compteInterne = new CompteInterne(
		//					"LA_O", 
		//					"Livret A Odile n°00681246659",
		//					TypeCompteInterne.LIVRET,
		//					Timestamp.valueOf("2025-01-01 00:00:00"), 
		//					1500000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//			
		//			// Création Livret A Thierry
		//			compteInterne = new CompteInterne(
		//					"LA_T", 
		//					"Livret A Thierry",
		//					TypeCompteInterne.LIVRET,
		//					Timestamp.valueOf("2025-01-01 00:00:00"), 
		//					1500000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Compte joint
		//			compteInterne = new CompteInterne(
		//					"CC-J", 
		//					"Compte joint n°04953671374",
		//					TypeCompteInterne.COURANT,
		//					Timestamp.valueOf("2025-08-31 01:00:00"), 
		//					7057018L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création porte-monnaie Odile
		//			compteInterne = new CompteInterne(
		//					"LIQ-O", 
		//					"Porte-monnaie ODILE",
		//					TypeCompteInterne.LIQUIDE,
		//					Timestamp.valueOf("2025-08-31 01:00:00"), 
		//					6750L);
		////			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création PEA numeraire Odile
		//			compteInterne = new CompteInterne(
		//					"PEA-NUM-O", 
		//					"Compte courant associé au PEA ODILE n°21015135377",
		//					TypeCompteInterne.COURANT,
		//					Timestamp.valueOf("2025-08-31 01:00:00"), 
		//					2901L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//			
		//			// Création PEA
		//			compteInterne = new CompteInterne(
		//					"PEA-O", 
		//					"PEA Odile n°34015135302",
		//					TypeCompteInterne.PEA,
		//					Timestamp.valueOf("2025-08-31 01:00:00"), 
		//					1378000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//			
		//			// Création Compte titre ordinaire
		//			compteInterne = new CompteInterne(
		//					"CT-J", 
		//					"Compte titres ordinaire n°30763182109",
		//					TypeCompteInterne.COMPTE_TITRES,
		//					Timestamp.valueOf("2025-08-31 01:00:00"), 
		//					225392L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-T-01", 
		//					"GENERALI - Espace Invest 4 n°90954678",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2013-11-09 01:00:00"), 
		//					15400000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-T-02", 
		//					"GENERALI - Espace Invest 4 N° OC709500034",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2016-12-06 01:00:00"), 
		//					10000000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-T-03", 
		//					"GENERALI - ESPACE INVEST 5 N° OC950101183",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2019-04-11 01:00:00"), 
		//					9250000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-O-01", 
		//					"Nuances 3D n°617768274",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2007-03-30 01:00:00"), 
		//					394011L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-O-02", 
		//					"Espace Invest 4 n°90954679",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2013-11-09 01:00:00"), 
		//					6000000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-O-03", 
		//					"ESPACE INVEST 5 n°OC950101182",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2019-04-11 01:00:00"), 
		//					6000000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Assurance-vie
		//			compteInterne = new CompteInterne(
		//					"AV-O-04", 
		//					"BPCE - Millevie Infinie 2 nºINFI2043728",
		//					TypeCompteInterne.ASSURANCE_VIE,
		//					Timestamp.valueOf("2023-10-14 01:00:00"), 
		//					10000000L);
		//			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Compte immobilier
		//			compteInterne = new CompteInterne(
		//					"PATIS-OSNY", 
		//					"Habitation principale [33 rue des Pâtis 95520 OSNY]",
		//					TypeCompteInterne.BIEN_IMMOBILIER,
		//					Timestamp.valueOf("2025-05-05 01:00:00"), 
		//					42600000L);
		////			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(thierry);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Compte immobilier
		//			compteInterne = new CompteInterne(
		//					"BEL-AIR_PORNICHET", 
		//					"Habitation secondaire [24 rue Gilbert Vaillant PORNICHET]",
		//					TypeCompteInterne.BIEN_IMMOBILIER,
		//					Timestamp.valueOf("2025-05-05 01:00:00"), 
		//					45630000L);
		////			compteInterne.changerBanque(caisseEpargne);
		//			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Compte immobilier
		//			compteInterne = new CompteInterne(
		//					"COTOR_GRADIGNAN", 
		//					"Bien locatif [GRADIGNAN]",
		//					TypeCompteInterne.BIEN_IMMOBILIER,
		//					Timestamp.valueOf("2025-05-05 01:00:00"), 
		//					22020000L);
		////			compteInterne.changerBanque(caisseEpargne);
		////			compteInterne.ajouterTitulaire(thierry);
		////			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);
		//
		//			// Création Compte immobilier
		//			compteInterne = new CompteInterne(
		//					"CREATIV_CERGY", 
		//					"Bien locatif [CERGY]",
		//					TypeCompteInterne.BIEN_IMMOBILIER,
		//					Timestamp.valueOf("2025-05-05 01:00:00"), 
		//					25950000L);
		////			compteInterne.changerBanque(caisseEpargne);
		////			compteInterne.ajouterTitulaire(thierry);
		////			compteInterne.ajouterTitulaire(odile);
		//			compteInterneService.creerCompte(compteInterne);

		ZonedDateTime dateSoldeInitial = ZonedDateTime.parse(
				"2024-12-31T23:00:00Z"); 

		// Creation d'un compte bancaire
		compteInterne = new CompteInterne(
				"COMPTE_COURANT_1", 
				"Compte courant joint en euros n°***",
				TypeCompteInterne.BANCAIRE,
				dateSoldeInitial, 
				200000L);
		compteInterne.changerBanque(caisseEpargne);
		compteInterne.ajouterTitulaire(thierry);
		compteInterne.ajouterTitulaire(odile);
		compteInterneService.creerCompte(compteInterne);

		compteInterne = new CompteInterne(
				"PORTE_MONNAIE_1", 
				"Porte-monnaie de ***",
				TypeCompteInterne.LIQUIDE,
				dateSoldeInitial, 
				5000L);
		compteInterne.ajouterTitulaire(odile);
		compteInterneService.creerCompte(compteInterne);

		compteInterne = new CompteInterne(
				"LIVRET_1", 
				"Livret A n°***",
				TypeCompteInterne.LIVRET,
				dateSoldeInitial, 
				100000L);
		compteInterne.changerBanque(caisseEpargne);
		compteInterne.ajouterTitulaire(odile);
		compteInterneService.creerCompte(compteInterne);

		// Création Compte titre ordinaire
		compteInterne = new CompteInterne(
				"COMPTE_TITRES_1", 
				"Compte titres ordinaire n°***",
				TypeCompteInterne.COMPTE_TITRES,
				dateSoldeInitial, 
				225392L);
		compteInterne.changerBanque(caisseEpargne);
		compteInterne.ajouterTitulaire(thierry);
		compteInterne.ajouterTitulaire(odile);
		compteInterneService.creerCompte(compteInterne);

		// Création Assurance-vie
		compteInterne = new CompteInterne(
				"ASSURANCE-VIE_1", 
				"BPCE - Millevie Infinie 2 nº***",
				TypeCompteInterne.ASSURANCE_VIE,
				dateSoldeInitial, 
				10000000L);
		compteInterne.changerBanque(caisseEpargne);
		compteInterne.ajouterTitulaire(odile);
		compteInterneService.creerCompte(compteInterne);
	}

	private void initialiseOperations() 
			throws ServiceException, ControllerException {

		// REFERENCES ET COMPTES UTILISES
		SousCategorie menage =  sousCategorieService.rechercherParNom("MENAGE");
		SousCategorie pension = sousCategorieService.rechercherParNom("RETRAITES");

		CompteInterne compteJoint = compteInterneService.rechercherParIdentifiant("COMPTE_COURANT_1");	
		CompteInterne livretAOdile = compteInterneService.rechercherParIdentifiant("LIVRET_1");	

		CompteTiers shiva = compteTiersService.rechercherParIdentifiant("SHIVA");
		CompteTiers retraitePension = compteTiersService.rechercherParIdentifiant("PENSION");

		Beneficiaire odile = beneficiaireService.rechercherParNom("ODILE");
		Beneficiaire thierry = beneficiaireService.rechercherParNom("THIERRY");
		Beneficiaire nicole = beneficiaireService.rechercherParNom("NICOLE");

		// OPERATIONS : CREATION

		OperationCreationRequestDto operationCreationRequestDto;

		operationCreationRequestDto = new OperationCreationRequestDto();
		operationCreationRequestDto.dateValeur = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris"));
		operationCreationRequestDto.identifiantCompteDepense = compteJoint.getIdentifiant();
		operationCreationRequestDto.identifiantCompteRecette = shiva.getIdentifiant();
		operationCreationRequestDto.libelle = "Paiement mensuel SHIVA Osny";
		operationCreationRequestDto.montantTotalEnCentimes = 30000L;
		operationCreationRequestDto.nomsBeneficiaires = new ArrayList<>();
		operationCreationRequestDto.nomsBeneficiaires.add(odile.getNom());
		operationCreationRequestDto.nomsBeneficiaires.add(thierry.getNom());
		operationCreationRequestDto.nomSousCategorie = menage.getNom();
		operationCreationRequestDto.numero = "CHQ-00000001";
		operationCreationRequestDto.codeTypeOperation = TypeOperation.DEPENSE.getCode();
		operationController.creerOperation(operationCreationRequestDto);

		operationCreationRequestDto = new OperationCreationRequestDto();
		operationCreationRequestDto.dateValeur = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris"));
		operationCreationRequestDto.identifiantCompteDepense = compteJoint.getIdentifiant();
		operationCreationRequestDto.identifiantCompteRecette = shiva.getIdentifiant();
		operationCreationRequestDto.libelle = "Paiement mensuel SHIVA Le Raincy";
		operationCreationRequestDto.montantTotalEnCentimes = 45000L;
		operationCreationRequestDto.nomsBeneficiaires = new ArrayList<>();
		operationCreationRequestDto.nomsBeneficiaires.add(nicole.getNom());
		operationCreationRequestDto.nomSousCategorie = menage.getNom();
		operationCreationRequestDto.numero = "CBO-665206406A";
		operationCreationRequestDto.codeTypeOperation = TypeOperation.DEPENSE.getCode();
		operationController.creerOperation(operationCreationRequestDto);

		operationCreationRequestDto = new OperationCreationRequestDto();
		operationCreationRequestDto.dateValeur = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris"));
		operationCreationRequestDto.identifiantCompteDepense = retraitePension.getIdentifiant();
		operationCreationRequestDto.identifiantCompteRecette = compteJoint.getIdentifiant();
		operationCreationRequestDto.libelle = "Virement pension ETAT";
		operationCreationRequestDto.montantTotalEnCentimes = 150000L;
		operationCreationRequestDto.nomsBeneficiaires = new ArrayList<>();
		operationCreationRequestDto.nomsBeneficiaires.add(odile.getNom());
		operationCreationRequestDto.nomSousCategorie = pension.getNom();
		operationCreationRequestDto.numero = "00000004";
		operationCreationRequestDto.codeTypeOperation = TypeOperation.RECETTE.getCode();
		operationController.creerOperation(operationCreationRequestDto);
	}
}
