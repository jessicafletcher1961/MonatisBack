package fr.colline.monatis.operations.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.typologies.model.TypeOperation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class OperationFiltreConstructeur {

	public static Specification<Operation> initialiserFiltre() {
		return (root, query, builder) -> builder.conjunction();
	}

	public static Specification<Operation> filtrer(
			String recherche,
			String numeroContient,
			String libelleContient,
			LocalDate dateCreationApproximative,
			LocalDate dateValeurDepuisLe,
			LocalDate dateValeurJusqueAu,
			Long compte1Id,
			Long compte2Id,
			Boolean pointee,
			Long montantPlancherEnCentimes,
			Long montantPlafondEnCentimes,
			Set<Long> sousCategoriesIds,
			Set<Long> beneficiairesIds,
			Set<TypeOperation> typesOperation) {

		Specification<Operation> filtre = initialiserFiltre();

		filtre = OperationFiltreConstructeur.ajouterFiltreNumero(filtre, numeroContient);
		filtre = OperationFiltreConstructeur.ajouterFiltreLibelle(filtre, libelleContient);
		filtre = OperationFiltreConstructeur.ajouterFiltreDateCreationApproximative(filtre, dateCreationApproximative);
		filtre = OperationFiltreConstructeur.ajouterFiltreDateValeur(filtre, dateValeurDepuisLe, dateValeurJusqueAu);
		filtre = OperationFiltreConstructeur.ajouterFiltreCompte(filtre, compte1Id);
		filtre = OperationFiltreConstructeur.ajouterFiltreCompte(filtre, compte2Id);
		filtre = OperationFiltreConstructeur.ajouterFiltrePointee(filtre, pointee);
		filtre = OperationFiltreConstructeur.ajouterFiltreMontant(filtre, montantPlancherEnCentimes, montantPlafondEnCentimes);
		filtre = OperationFiltreConstructeur.ajouterFiltreSousCCategories(filtre, sousCategoriesIds);
		filtre = OperationFiltreConstructeur.ajouterFiltreBeneficiaires(filtre, beneficiairesIds);
		filtre = OperationFiltreConstructeur.ajouterFiltreTypesOperation(filtre, typesOperation);

		return filtre;
	}
	
	@Deprecated
	public static Specification<Operation> filtrer(
			String recherche,
			Set<TypeOperation> typesOperation,
			LocalDate dateDebut,
			LocalDate dateFin,
			Long montantEnCentimes,
			Long compteRecetteOuDepenseId,
			Long compteRecetteId,
			Long compteDepenseId,
			Boolean pointee) {
		
		Specification<Operation> specification = initialiserFiltre();
		
		specification = OperationFiltreConstructeur.ajouterFiltreRecherche(specification, recherche);
		specification = OperationFiltreConstructeur.ajouterFiltreTypesOperation(specification, typesOperation);
		specification = OperationFiltreConstructeur.ajouterFiltreDateValeur(specification, dateDebut, dateFin);
		specification = OperationFiltreConstructeur.ajouterFiltreMontant(specification, montantEnCentimes, montantEnCentimes);
		specification = OperationFiltreConstructeur.ajouterFiltrePointee(specification, pointee);
		if ( compteRecetteOuDepenseId != null ) {
			// Toutes les opérations de ce compte
			specification = OperationFiltreConstructeur.ajouterFiltreCompte(specification, compteRecetteOuDepenseId);
		}
		else {
			// Seulement les opérations entre ces deux comptes 
			specification = OperationFiltreConstructeur.ajouterFiltreCompte(specification, compteRecetteId);
			specification = OperationFiltreConstructeur.ajouterFiltreCompte(specification, compteDepenseId);
		}
		
		return specification;
	}

	/**
	 * Constitution du filtre permettant de rechercher un texte partout dans les opérations<ul>
	 * <li>dans le numero ou le libelle de l'opération</li>
	 * <li>OU dans l'identifiant ou le libellé des comptes</li>
	 * <li>OU dans le code, le libellé court ou les libellés du type de l'opération</li>
	 * </ul>
	 * 
	 * @param filtre principal auquel ajouter le filtre déterminé dans la méthode
	 * @param recherche le texte à rechercher. 
	 * @return le filtre principal auquel on a ajouté (ou pas) le filtre généré
	 */
	public static Specification<Operation> ajouterFiltreRecherche(
			Specification<Operation> filtre, 
			String recherche) {

		if ( recherche == null || recherche.isBlank()) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			String pattern = "%" + normalize(recherche) + "%";

			ArrayList<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.like(normalize(builder, root.get("numero")), pattern));
			predicates.add(builder.like(normalize(builder, root.get("libelle")), pattern));
			predicates.add(builder.like(normalize(builder, root.get("compteDepense").get("identifiant")), pattern));
			predicates.add(builder.like(normalize(builder, root.get("compteDepense").get("libelle")), pattern));
			predicates.add(builder.like(normalize(builder, root.get("compteRecette").get("identifiant")), pattern));
			predicates.add(builder.like(normalize(builder, root.get("compteRecette").get("libelle")), pattern));

//			Set<String> typesTrouves = trouverTypesOperationsRecherche(recherche);
//			if (!typesTrouves.isEmpty()) {
//				predicates.add(root.get("typeOperation").in(typesTrouves));
//			}

			return builder.or(predicates.toArray(Predicate[]::new));
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de rechercher une opération par une partie de son numéro
	 * 
	 * @param filtre principal auquel ajouter le filtre déterminé dans la méthode
	 * @param numero le bout de numéro à rechercher. 
	 * @return le filtre principal auquel on a ajouté (ou pas) le filtre généré
	 */
	public static Specification<Operation> ajouterFiltreNumero(Specification<Operation> filtre, String numero) {

		if ( numero == null || numero.isBlank() ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			return builder.and(builder.like(normalize(builder, root.get("numero")), '%' + normalize(numero) + '%'));
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de rechercher une opération par une partie de son libellé
	 * 
	 * @param filtre principal auquel ajouter le filtre déterminé dans la méthode
	 * @param libelle le libellé dont on recherche des exemples 
	 * @return le filtre principal auquel on a ajouté (ou pas) le filtre généré
	 */
	public static Specification<Operation> ajouterFiltreLibelle(Specification<Operation> filtre, String libelle) {

		if ( libelle == null || libelle.isBlank() ) {
			return filtre;
		}
		
		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			return builder.like(normalize(builder, root.get("libelle")), '%' + normalize(libelle) + '%');
		};

		return filtre.and(filtreRecherche);
	}
	
	/**
	 * Constitution du filtre permettant de rechercher une opération par sa date de création
	 * avec unr marge de plus ou moins 3 jours
	 * 
	 * @param filtre
	 * @param dateCreationApproximative
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreDateCreationApproximative(
			Specification<Operation> filtre,
			LocalDate dateCreationApproximative) {

		if ( dateCreationApproximative == null ) {
			return filtre;
		}

		LocalDate dateDebut = dateCreationApproximative.minus(3, ChronoUnit.DAYS);
		LocalDate dateFin = dateCreationApproximative.plus(3, ChronoUnit.DAYS);

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			ArrayList<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.greaterThanOrEqualTo(root.get("dateCreation"), dateDebut));
			predicates.add(builder.lessThanOrEqualTo(root.get("dateCreation"), dateFin));

			return builder.and(predicates.toArray(Predicate[]::new));
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de rechercher les opérations ayant une date
	 * de valeur située entre les deux bornes indiquées (incluses).<br>
	 * 
	 * @param filtre
	 * @param dateValeurDebut facultative (si omise, depuis la première)
	 * @param dateValeurFin facultative (si omise, jusqu'à la dernière)
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreDateValeur(
			Specification<Operation> filtre,
			LocalDate dateValeurDebut, 
			LocalDate dateValeurFin) {

		if ( dateValeurDebut == null && dateValeurFin == null ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			ArrayList<Predicate> predicates = new ArrayList<>();

			if (dateValeurDebut != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("dateValeur"), dateValeurDebut));
			}

			if (dateValeurFin != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dateValeur"), dateValeurFin));
			}

			return builder.and(predicates.toArray(Predicate[]::new));
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de rechercher les opérations dont le compte en
	 * recette ou le compte en dépense est le compte indiqué.
	 * 
	 * @param filtre
	 * @param compte
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreCompte(
			Specification<Operation> filtre,
			Long compteId) {

		if ( compteId == null ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			Predicate compteRecette;
			Predicate compteDepense;

			compteRecette = builder.equal(root.get("compteRecette").get("id"), compteId);
			compteDepense = builder.equal(root.get("compteDepense").get("id"), compteId);

			return builder.or(compteRecette, compteDepense);
		};

		return filtre.and(filtreRecherche);
	}

	public static Specification<Operation> ajouterFiltreCompteDepense(
			Specification<Operation> filtre,
			Long compteId) {

		if ( compteId == null ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			return builder.equal(root.get("compteDepense").get("id"), compteId);
		};

		return filtre.and(filtreRecherche);
	}

	public static Specification<Operation> ajouterFiltreCompteRecette(
			Specification<Operation> filtre,
			Long compteId) {

		if ( compteId == null ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			return builder.equal(root.get("compteRecette").get("id"), compteId);
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de ne rechercher que les opérations pointées ou les opérations
	 * non pointées
	 *  
	 * @param filtre
	 * @param pointee
	 * @return
	 */
	public static Specification<Operation> ajouterFiltrePointee(Specification<Operation> filtre, Boolean pointee) {

		if ( pointee == null ) {
			return filtre;
		}
		
		return filtre.and((root, query, builder) -> builder.equal(root.get("pointee"), pointee));
	}

	/**
	 * Constitution du filtre permettant de rechercher les opérations ayant un montant
	 * de valeur située entre les deux bornes indiquées (incluses).<br>
	 * 
	 * @param filtre
	 * @param montantEnCentimesPlancher
	 * @param montantEnCentimesPlafond
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreMontant(
			Specification<Operation> filtre,
			Long montantEnCentimesPlancher,
			Long montantEnCentimesPlafond) {

		if ( montantEnCentimesPlancher == null && montantEnCentimesPlafond == null ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			ArrayList<Predicate> predicates = new ArrayList<>();

			if ( montantEnCentimesPlancher != null ) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("montantEnCentimes"), montantEnCentimesPlancher));
			}

			if (montantEnCentimesPlafond != null ) {
				predicates.add(builder.lessThanOrEqualTo(root.get("montantEnCentimes"), montantEnCentimesPlafond));
			}

			return builder.and(predicates.toArray(Predicate[]::new));
		};

		return filtre.and(filtreRecherche);
	}

	public static Specification<Operation> ajouterFiltreSousCCategories(
			Specification<Operation> filtre,
			Set<Long> sousCategoriesIds) {
		
		if ( sousCategoriesIds == null || sousCategoriesIds.isEmpty() ) {
			return filtre;
		}

		Set<String> typeOperationCategorisables = trouverTypeOperationCategorisables(); 
		
		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			Predicate isOperationCategorisable = root.get("typeOperation").in(typeOperationCategorisables);
			Predicate isSousCategorieTrouvee = root.get("lignes").get("sousCategorie").get("id").in(sousCategoriesIds);

			return builder.and(isOperationCategorisable, isSousCategorieTrouvee);
		};
		
		return filtre.and(filtreRecherche);
	}

	public static Specification<Operation> ajouterFiltreBeneficiaires(
			Specification<Operation> filtre,
			Set<Long> beneficiairesIds) {

		if ( beneficiairesIds == null || beneficiairesIds.isEmpty() ) {
			return filtre;
		}

		Set<String> typeOperationCategorisables = trouverTypeOperationCategorisables(); 
		
		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			Predicate isOperationCategorisable = root.get("typeOperation").in(typeOperationCategorisables);
			Predicate isBeneficiaireTrouve = root.get("lignes").get("beneficiaires").get("id").in(beneficiairesIds);

			return builder.and(isOperationCategorisable, isBeneficiaireTrouve);
		};
		
		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de rechercher les opérations ayant l'un des
	 * types d'opération indiqués
	 *
	 * @param filtre
	 * @param typesOperation
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreTypesOperation(
			Specification<Operation> filtre,
			Set<TypeOperation> typesOperation) {

		if ( typesOperation == null || typesOperation.isEmpty() ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			return root.get("typeOperation").in(typesOperation);
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Constitution du filtre permettant de rechercher les opérations ayant le
	 * type d'opération indiqué
	 *
	 * @param filtre
	 * @param typeOperation
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreTypeOperation(
			Specification<Operation> filtre,
			TypeOperation typeOperation) {

		if ( typeOperation == null ) {
			return filtre;
		}

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			return builder.equal(root.get("typeOperation"), typeOperation.getCode());
		};

		return filtre.and(filtreRecherche);
	}

	/**
	 * Prépare une condition pour que l'opération soit visible par l'un ou l'autre
	 * des comptes de recette ou de dépense.<br>
	 * Ne concerne que les comptes internes, vérification 
	 * par rapport à la date de solde initial et éventuellement à la date de clôture.
	 * 
	 * @param filtre
	 * @return
	 */
	public static Specification<Operation> ajouterFiltreVisibilite(
			Specification<Operation> filtre) {

		Specification<Operation> filtreRecherche = (root, query, builder) -> {

			ArrayList<Predicate> predicates = new ArrayList<>();

			Predicate compteIsNotInterne;
			Predicate dateDebutIsOk;
			Predicate dateClotureIsNull;
			Predicate dateFinIsOk;

			// vérification visibilite compte recette
			compteIsNotInterne = builder.isNull(root.get("compteRecette").get("dateSoldeInitial"));
			dateDebutIsOk = builder.greaterThanOrEqualTo(root.get("dateValeur"), root.get("compteRecette").get("dateSoldeInitial"));
			dateClotureIsNull = builder.isNull(root.get("compteRecette").get("dateCloture"));
			dateFinIsOk = builder.or(dateClotureIsNull, builder.lessThanOrEqualTo(root.get("dateValeur"), root.get("compteRecette").get("dateCloture")));
			predicates.add(builder.or(compteIsNotInterne, builder.and(dateDebutIsOk, dateFinIsOk)));

			// vérification visibilite compte dépense
			compteIsNotInterne = builder.isNull(root.get("compteDepense").get("dateSoldeInitial"));
			dateDebutIsOk = builder.greaterThanOrEqualTo(root.get("dateValeur"), root.get("compteDepense").get("dateSoldeInitial"));
			dateClotureIsNull = builder.isNull(root.get("compteDepense").get("dateCloture"));
			dateFinIsOk = builder.or(dateClotureIsNull, builder.lessThanOrEqualTo(root.get("dateValeur"), root.get("compteDepense").get("dateCloture")));
			predicates.add(builder.or(compteIsNotInterne, builder.and(dateDebutIsOk, dateFinIsOk)));

			// on vérifie que l'opération fait bien partie des opérations visibles 
			// par l'un ou l'autre compte
			return builder.or(predicates.toArray(Predicate[]::new));
		};

		return filtre.and(filtreRecherche);
	}
	
	private static Set<String> trouverTypeOperationCategorisables() {

		ArrayList<String> resultats = new ArrayList<>();

		for ( TypeOperation typeOperation : TypeOperation.values() ) {
			if ( typeOperation.isCategorisable() ) {
				resultats.add(typeOperation.getCode());
			}
		}

		return Set.copyOf(resultats);
	}
//	
//	private static Set<String> trouverTypesOperationsRecherche(String recherche) {
//
//		String rechercheNormalisee = normalize(recherche);
//
//		ArrayList<String> resultats = new ArrayList<>();
//
//		for (TypeOperation typeOperation : TypeOperation.values()) {
//			if (normalize(typeOperation.getCode()).contains(rechercheNormalisee)
//					|| normalize(typeOperation.getLibelleCourt()).contains(rechercheNormalisee)
//					|| normalize(typeOperation.getLibelle()).contains(rechercheNormalisee)) {
//				resultats.add(typeOperation.getCode());
//			}
//		}
//
//		return Set.copyOf(resultats);
//	}
	
	private static Expression<String> normalize(CriteriaBuilder builder, Expression<String> colonne) {

		final String CAR_ACCENTUES = "áãàâäçéèëêùûüúóôöïîí";
		final String CAR_REMPLACMT = "aaaaaceeeeuuuuoooiii";

		Expression<String> colonneMinuscule = builder.lower(colonne);
		for (int i = 0; i < CAR_ACCENTUES.length(); i++) {
			colonneMinuscule = builder.function("REPLACE", 
					String.class, 
					colonneMinuscule, 
					builder.literal(CAR_ACCENTUES.charAt(i)), 
					builder.literal(CAR_REMPLACMT.charAt(i)));
		}
		return colonneMinuscule;

	}
	
	private static String normalize(String valeur) {
		
		return valeur != null ? Normalizer.normalize(valeur, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase().trim() : valeur;
	}
}
