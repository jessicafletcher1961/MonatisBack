	package fr.colline.monatis.operations.service;
	
	import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationFonctionnelleErreur;
import fr.colline.monatis.operations.OperationTechniqueErreur;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.OperationLigne;
import fr.colline.monatis.operations.repository.OperationLigneRepository;
import fr.colline.monatis.operations.repository.OperationRepository;
import fr.colline.monatis.typologies.model.TypeOperation;
	
	@Service
	public class OperationService {
	
		@Autowired private OperationRepository operationRepository;
		@Autowired private OperationLigneRepository operationLigneRepository;
	
	    @Autowired private CompatibiliteService compatibiliteService;
	
		public Operation rechercherParId(Long id) throws ServiceException {
	
			Assert.notNull(id, () -> "L'ID pour la recherche d'une opération est obligatoire");
	
			try {
				Optional<Operation> optional = operationRepository.findById(id);
				return optional.isEmpty() ? null : optional.get();
			}
			catch (Throwable t) {
				throw new ServiceException(
						t,
						OperationTechniqueErreur.RECHERCHE_PAR_ID,
						id );
			}
		}
	
		public boolean isExistantParId(Long id) throws ServiceException {
	
			Assert.notNull(id, () -> "L'ID pour la vérification de l'existence d'une opération est obligatoire");
	
			try {
				return operationRepository.existsById(id);
			}
			catch (Throwable t) {
				throw new ServiceException(
						t,
						OperationTechniqueErreur.EXISTENCE_PAR_ID,
						id);
			}
		}
	
		public Operation rechercherParNumero(String numero) throws ServiceException {
	
			Assert.notNull(numero, () -> "Le NUMERO pour la recherche d'une opération est obligatoire");
	
			try {
				Optional<Operation> optional = operationRepository.findByNumero(numero);
				return optional.isEmpty() ? null : optional.get();
			}
			catch (Throwable t) {
				throw new ServiceException(
						t,
						OperationTechniqueErreur.RECHERCHE_PAR_IDENTIFIANT_FONCTIONNEL,
						numero);
			}
		}
	
		public boolean isExistantParNumero(String numero) throws ServiceException {
	
			Assert.notNull(numero, () -> "Le NUMERO pour la vérification de l'existence d'une opération est obligatoire");
	
			try {
				return operationRepository.existsByNumero(numero);
			}
			catch (Throwable t) {
				throw new ServiceException(
						t,
						OperationTechniqueErreur.EXISTENCE_PAR_IDENTIFIANT_FONCTIONNEL,
						Operation.class.getSimpleName(),
						numero);
			}
		}
	
		public Stream<Operation> rechercherTous() throws ServiceException {
	
			try {
				return operationRepository.findAll().stream();
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.RECHERCHE_TOUS);
			}
		}
	
		public void supprimerTous() throws ServiceException {
	
			try {
				operationRepository.deleteAll();
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.SUPPRESSION_TOUS);
			}
		}
		
		/**
		 * Recherche toutes les opérations du compte indiqué entre une date début (incluse)
		 * et une date fin (incluse).</br>
		 * Pour les comptes internes, la recherche est bornée par la date de solde initial du compte et
		 * la date de clôture du compte (s'il y en a une).<br>
		 * Trié par date de valeur de l'opération.
		 * 
		 * @param compte
		 * @param dateDebut
		 * @param dateFin
		 * @return
		 * @throws ServiceException
		 */
		public List<Operation> rechercherOperationsParCompteEntreDateDebutEtDateFin(
				Compte compte,
				LocalDate dateDebut, 
				LocalDate dateFin) throws ServiceException {
	
			try {
				
				Specification<Operation> filtre = OperationFiltreConstructeur.initialiserFiltre();
				
				filtre = OperationFiltreConstructeur.ajouterFiltreCompte(filtre, compte.getId());
				filtre = OperationFiltreConstructeur.ajouterFiltreDateValeur(filtre, dateDebut, dateFin);
				filtre = OperationFiltreConstructeur.ajouterFiltreVisibilite(filtre);
	
				Sort tri = Sort.by(Sort.Direction.DESC, "dateValeur", "id");
	
				return operationRepository.findAll(filtre, tri);
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_COMPTE_ENTRE_DATE_DEBUT_ET_DATE_FIN,
						compte == null ? "non spécifié" : compte.getIdentifiant(),
						dateDebut,
						dateFin);
			}
		}
	
		/**
		 * Recherche toutes les opérations de tous les comptes entre deux date.</br>
		 * Trié par date de valeur de l'opération en ordre descendant.
		 * 
		 * @return un flux d'opérations
		 * @throws ServiceException
		 */
		public List<Operation> rechercherOperationsParDateValeur(LocalDate dateDebut, LocalDate dateFin) throws ServiceException {
	
			return rechercherOperationsParCompteEntreDateDebutEtDateFin(null, dateDebut, dateFin);
		}
		
		public List<Operation> rechercherOperationsParCompteDepuisDateDebut(
				Compte compte,
				LocalDate dateDebut) throws ServiceException {
	
			return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, dateDebut, null);
		}
	
		/**
		 * Recherche des opération de dépense du compte indiqué entre date début (incluse)
		 * et date fin (incluse).</br>
		 * Comprends les opérations de dépense réelles et les opérations techniques (de frais).</br>
		 * Trié par date de valeur de l'opération.
		 * @param compte
		 * @param dateDebut
		 * @param dateFin
		 * @return
		 */
		public List<Operation> rechercherOperationsDepenseParCompteEntreDateDebutEtDateFin(
				Compte compte,
				LocalDate dateDebut, 
				LocalDate dateFin) throws ServiceException {
	
			return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, dateDebut, dateFin)
					.stream()
					.filter((o) -> {return o.getCompteDepense().getId() == compte.getId();})
					.toList();
		}
	
		/**
		 * Recherche des opération de dépense du compte indiqué jusqu'à la date fin (incluse).</br>
		 * Trié par date de valeur de l'opération.
		 * @param compte
		 * @param dateFin
		 * @return
		 */
		public List<Operation> rechercherOperationsDepenseParCompteJusqueDateFin(
				Compte compte, 
				LocalDate dateFin) throws ServiceException {
	
			return rechercherOperationsDepenseParCompteEntreDateDebutEtDateFin(compte, null, dateFin);
		}
	
		/**
		 * Recherche des opération de recette du compte indiqué entre date début (incluse)
		 * et date fin (incluse).</br>
		 * Comprends les opérations réelles et les opérations techniques.</br>
		 * Trié par date de valeur de l'opération.
		 * @param compteId
		 * @param dateDebut
		 * @param dateFin
		 * @return
		 */
		public List<Operation> rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(
				Compte compte,
				LocalDate dateDebut, 
				LocalDate dateFin) throws ServiceException {
	
			return rechercherOperationsParCompteEntreDateDebutEtDateFin(compte, dateDebut, dateFin)
					.stream()
					.filter((o) -> {return o.getCompteRecette().getId().equals(compte.getId());})
					.toList();
	
		}
	
		/**
		 * Recherche des opérations de recette du compte indiqué jusqu'à la date fin (incluse).</br>
		 * Trié par date de valeur de l'opération.
		 * @param compteId
		 * @param dateFin
		 * @return
		 */
		public List<Operation> rechercherOperationsRecetteParCompteJusqueDateFin(
				Compte compte, 
				LocalDate dateFin) throws ServiceException {
	
			return rechercherOperationsRecetteParCompteEntreDateDebutEtDateFin(compte, null, dateFin);
		}
		
		/**
		 * Recherche des opérations correspondant à l'exemple indiqué.<ul>
		 * li>par les mots les plus significatifs du libellé indiqué</li>
		 * <li>avec le compte en dépense s'il est indiqué</li>
		 * <li>avec le compte en recette s'il est indiqué</li>
		 * <li>avec le type d'opération s'il est indiqué</li><
		 * /ul></br>
		 * Si l'on recgherche des doublons, il faut également indiquer un montant et une date de valeur.
		 *  
		 * @param exempleOperation
		 * @param pagination
		 * @return
		 * @throws ServiceException
		 */
		public Page<Operation> rechercherOperationsParExemple(
				Operation exempleOperation,
				Pageable pagination) throws ServiceException {

			try {
				Specification<Operation> filtre = OperationFiltreConstructeur.initialiserFiltre();
				
				if ( exempleOperation.getLibelle() != null && !exempleOperation.getLibelle().isBlank() ) {
					List<String> mots = extraireMotsSignifiants(exempleOperation.getLibelle());
					for ( String mot : mots ) {
						filtre = OperationFiltreConstructeur.ajouterFiltreLibelle(filtre, mot);
					}
				}
				
				if ( exempleOperation.getCompteDepense() != null ) {
					filtre = OperationFiltreConstructeur.ajouterFiltreCompteDepense(filtre, exempleOperation.getCompteDepense().getId());
				}
				
				if ( exempleOperation.getCompteRecette() != null ) {
					filtre = OperationFiltreConstructeur.ajouterFiltreCompteRecette(filtre, exempleOperation.getCompteDepense().getId());
				}
				
				if ( exempleOperation.getTypeOperation() != null ) {
					filtre = OperationFiltreConstructeur.ajouterFiltreTypeOperation(filtre, exempleOperation.getTypeOperation()); 
				}
				
				if ( exempleOperation.getMontantEnCentimes() != null ) {
					filtre = OperationFiltreConstructeur.ajouterFiltreMontant(filtre, exempleOperation.getMontantEnCentimes(), exempleOperation.getMontantEnCentimes()); 
				}

				if ( exempleOperation.getDateValeur() != null ) {
					filtre = OperationFiltreConstructeur.ajouterFiltreDateValeur(filtre, exempleOperation.getDateValeur(), exempleOperation.getDateValeur()); 
				}

				filtre = OperationFiltreConstructeur.ajouterFiltreVisibilite(filtre);

				return operationRepository.findAll(filtre, pagination);
			}
			catch ( Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_EXEMPLE);
			}
		}
		
		/**
		 * Recherche générique d'opérations avec un filtre de sélection constitué manuellement
		 * 
		 * @param filtre
		 * @param pagination
		 * @return
		 * @throws ServiceException
		 */
		public Page<Operation> rechercherOperationsParFiltre(
				Specification<Operation> filtre,
				Pageable pagination) throws ServiceException {
	
			try { 
				filtre = OperationFiltreConstructeur.ajouterFiltreVisibilite(filtre);
				return operationRepository.findAll(filtre, pagination);
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.RECHERCHE_OPERATION_PAR_PAGE_FILTREES);
			}
		}
		
		public Stream<OperationLigne> rechercherOperationsLignesParSousCategorieIdEtCriteres(
				Long sousCategorieId, 
				Long beneficiaireId, 
				LocalDate dateDebut, 
				LocalDate dateFin) throws ServiceException {
	
			try {
				return operationLigneRepository.findBySousCategorieIdAndFilters(
						sousCategorieId,
						beneficiaireId,
						dateDebut,
						dateFin)
						.filter((o) -> {return o.getOperation().getTypeOperation().isCategorisable();});
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.RECHERCHE_OPERATION_LIGNE_PAR_REFERENCE_ID_ENTRE_DATE_DEBUT_ET_DATE_FIN,
						sousCategorieId,
						dateDebut,
						dateFin);
			}
		}
		
		public Operation creerOperation(Operation operation) throws ServiceException {
	
			Assert.notNull(operation, () -> "L'OPERATION à créer est obligatoire");
	
			operation = controlerEtPreparerPourCreation(operation);
	
			return enregistrer(operation);
		}
	
		public Operation modifierOperation(Operation operation) throws ServiceException {
	
			Assert.notNull(operation, () -> "L'OPERATION à modifier est obligatoire");
	
			operation = controlerEtPreparerPourModification(operation);
	
			return enregistrer(operation);
		}
	
		public void supprimerOperation(Operation operation) throws ServiceException {
	
			Assert.notNull(operation, () -> "L'OPERATION à supprimer est obligatoire");
	
			operation = controlerEtPreparerPourSuppression(operation);
	
			supprimer(operation);
		}
	
		private Operation enregistrer(Operation operation) throws ServiceException {
	
			try {
				operation = operationRepository.save(operation);
				if ( operation.getDateCreation() == null ) {
					operation.setDateCreation(operation.getDateValeur());
					operation = operationRepository.save(operation);
				}
				if ( operation.getNumero() == null ) {
					operation.setNumero(String.format("AUTO-%010d", operation.getId()));
					operation = operationRepository.save(operation);
				}
				return operation;
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.ENREGISTREMENT,
						operation.getNumero());
			}
		}
	
		private void supprimer(Operation operation) throws ServiceException {
	
			try {
				operationRepository.delete(operation);
			}
			catch (Throwable t) {
				throw new ServiceException (
						t,
						OperationTechniqueErreur.SUPPRESSION,
						operation.getNumero());
			}
		}
	
		private Operation controlerEtPreparerPourCreation(Operation operation) throws ServiceException {
	
			verifierCompatibiliteDepense(operation.getTypeOperation(), operation.getCompteDepense());
			verifierCompatibiliteRecette(operation.getTypeOperation(), operation.getCompteRecette());
			verifierDateCloture(operation.getDateValeur(), operation.getCompteDepense());
			verifierDateCloture(operation.getDateValeur(), operation.getCompteRecette());
			verifierListeOperationLigne(operation.getLignes(), operation.getMontantEnCentimes());
	
			return operation;
		}
	
		private Operation controlerEtPreparerPourModification(Operation operation) throws ServiceException {
	
			if ( operation.getLignes() != null && operation.getLignes().size() == 1 ) {
				
				// S'il n'y a qu'une seule ligne détail, celle-ci doit avoir le même montant
				
				operation.getLignes().forEach((od) -> { 
					od.setMontantEnCentimes(od.getOperation().getMontantEnCentimes());});
			}
	
			verifierCompatibiliteDepense(operation.getTypeOperation(), operation.getCompteDepense());
			verifierCompatibiliteRecette(operation.getTypeOperation(), operation.getCompteRecette());
			verifierDateCloture(operation.getDateValeur(), operation.getCompteDepense());
			verifierDateCloture(operation.getDateValeur(), operation.getCompteRecette());
			verifierListeOperationLigne(operation.getLignes(), operation.getMontantEnCentimes());
	
			return operation;
		}
	
		private Operation controlerEtPreparerPourSuppression(Operation operation) throws ServiceException  {
	
			return operation;
		}
	
		private void verifierCompatibiliteDepense(
				TypeOperation typeOperation, 
				Compte compteDepense) throws ServiceException {
	
			if ( ! compatibiliteService.isCompatibiliteDepense(compteDepense, typeOperation) ) {
				throw new ServiceException(
						OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_DEPENSE_INCOMPATIBLES,
						typeOperation.getCode(), 
						compteDepense.getIdentifiant());
			}
		}
	
		private void verifierCompatibiliteRecette(
				TypeOperation typeOperation, 
				Compte compteRecette) throws ServiceException {
	
			if ( ! compatibiliteService.isCompatibiliteRecette(compteRecette, typeOperation) ) {
				throw new ServiceException(
						OperationFonctionnelleErreur.TYPE_OPERATION_ET_COMPTE_RECETTE_INCOMPATIBLES,
						typeOperation.getCode(), 
						compteRecette.getIdentifiant());
			}
		}
	
		private void verifierDateCloture(
				LocalDate dateValeur, 
				Compte compte) throws ServiceException {
			
			if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
				CompteInterne compteInterne = (CompteInterne) compte;
				if ( compteInterne.getDateCloture() != null && dateValeur.isAfter(compteInterne.getDateCloture()) ) {
					throw new ServiceException(
							OperationFonctionnelleErreur.DATE_VALEUR_APRES_CLOTURE_COMPTE,
							dateValeur, 
							compteInterne.getIdentifiant(),
							compteInterne.getDateCloture());
				}
			}
		}
		
		private void verifierListeOperationLigne(Set<OperationLigne> operationDetails, Long montantOperationEnCentimes) throws ServiceException {
	
			if ( operationDetails == null || operationDetails.isEmpty() ) {
				throw new ServiceException(
						OperationFonctionnelleErreur.OPERATION_AU_MOINS_UN_DETAIL_REQUIS);
			}
			
			Set<Integer> numerosDetails = new HashSet<>();
			Long sommeMontantDetailEnCentimes = 0L;
			for ( OperationLigne operationDetail : operationDetails ) {
				
				if ( numerosDetails.contains(operationDetail.getNumeroLigne()) ) {
					throw new ServiceException(
							OperationFonctionnelleErreur.OPERATION_LISTE_DETAIL_NUMERO_DUPLIQUE,
							operationDetail.getNumeroLigne());
				}
				numerosDetails.add(operationDetail.getNumeroLigne());
				sommeMontantDetailEnCentimes += operationDetail.getMontantEnCentimes();
			}
	
			if ( ! sommeMontantDetailEnCentimes.equals(montantOperationEnCentimes) ) {
				
				throw new ServiceException(
						OperationFonctionnelleErreur.OPERATION_LISTE_DETAIL_SOMME_MONTANTS_ERRONEE, 
						sommeMontantDetailEnCentimes,
						montantOperationEnCentimes);
			}
		}
	
		private List<String> extraireMotsSignifiants(String libelle) {

			if ( libelle == null || libelle.isBlank() ) {
				return new ArrayList<String>();
			}

			final int NOMBRE_MOTS_SIGNIFIANTS_A_RETENIR = 2;
			record Score(String mot, Long occurences) {};
			
			Score[] scoresEnregistres = new Score[NOMBRE_MOTS_SIGNIFIANTS_A_RETENIR];
			String[] decoupage = libelle.split(" ");
			
			for ( String mot : decoupage ) {
				mot = mot.replaceAll("\\d+", "");
				if ( mot.isBlank() ) {
					continue;
				}
				mot = mot.trim();	
				if ( mot.length() <= 2 ) {
					continue;
				}
	
				// Recherche du nombre d'occurences du mot dans le libellé des opérations
				
				Operation operation = new Operation();
				operation.setLibelle(mot);
				Example<Operation> example = Example.of(operation, ExampleMatcher.matchingAny()
						.withIgnoreCase()
						.withStringMatcher(StringMatcher.CONTAINING));
				Long nombreOccurences = operationRepository.count(example);
				
				Score nouveauScore = new Score(mot, nombreOccurences);
				
				// Calcul des mots ayant le meilleur score (à la truelle)
	
				// On commence par remplir le tableau avec les premières valeurs
				boolean nouveauScoreEnregistre = false;
				for ( int i=0 ; i<scoresEnregistres.length ; i++ ) {
					if ( scoresEnregistres[i] == null ) {
						scoresEnregistres[i] = nouveauScore;
						nouveauScoreEnregistre = true;
						break;
					}
				}
				
				if ( ! nouveauScoreEnregistre ) {
					// Ensuite, quand le tableau est plein, on y echerche le pire score enregistré 
					Score pireScore = scoresEnregistres[0];
					int indexPireScore = 0;
					for (  int i=0 ; i<scoresEnregistres.length ; i++ ) {
						Score scoreEnregistre = scoresEnregistres[i];
						if ( scoreEnregistre.occurences > pireScore.occurences ) {
							pireScore = scoreEnregistre;
							indexPireScore = i; 
						}
					}
					// Quand le nouveau score est meilleur que le pire score enregistré, 
					// il le remplace
					if ( nouveauScore.occurences < pireScore.occurences ) {
						scoresEnregistres[indexPireScore] = nouveauScore;
					}
				}
			}
	
			return Arrays.stream(scoresEnregistres)
					.filter((s) -> {return s != null;} )
					.map((s) -> {return s.mot;}) 
					.toList();
		}
	}
