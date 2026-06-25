/*
 * Chemin original depuis la racine du projet: MonatisBack-main\src\main\java\fr\colline\monatis\importsreleves\service\RegleImportReleveService.java
 * Chemin de cette copie documentaire depuis la racine du projet: MonatisFront-codex-monatis-front-ui-refresh\Doc_Front_Creation\Classes_Java_Crees\MonatisBack-main\src\main\java\fr\colline\monatis\importsreleves\service\RegleImportReleveService.java
 */
package fr.colline.monatis.importsreleves.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteGeneriqueService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.importsreleves.ImportReleveFonctionnelleErreur;
import fr.colline.monatis.importsreleves.ImportReleveTechniqueErreur;
import fr.colline.monatis.importsreleves.model.RegleImportReleve;
import fr.colline.monatis.importsreleves.model.RoleCompteContrepartie;
import fr.colline.monatis.importsreleves.repository.RegleImportReleveRepository;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.service.BeneficiaireService;
import fr.colline.monatis.references.service.SousCategorieService;
import fr.colline.monatis.typologies.model.TypeOperation;

@Service
public class RegleImportReleveService {

	@Autowired private RegleImportReleveRepository regleImportReleveRepository;
	@Autowired private LibelleImportReleveNormalizer libelleImportReleveNormalizer;
	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteGeneriqueService compteContrepartieService;
	@Autowired private SousCategorieService sousCategorieService;
	@Autowired private BeneficiaireService beneficiaireService;

	public static class CritereSuggestion {
		public int index;
		public String operationImportId;
		public String libelle;
		public String groupKey;
		public RoleCompteContrepartie roleCompteContrepartie;
		public Long compteInterneReleveId;
		public String identifiantCompteInterneReleve;
	}

	public static class ResultatSuggestion {
		public CritereSuggestion critere;
		public String cleLibelleNormalisee;
		public RegleImportReleve regle;
	}

	public static class Apprentissage {
		public String libelle;
		public String groupKey;
		public RoleCompteContrepartie roleCompteContrepartie;
		public TypeOperation typeOperation;
		public Long compteInterneReleveId;
		public String identifiantCompteInterneReleve;
		public Long compteContrepartieId;
		public String identifiantCompteContrepartie;
		public Long sousCategorieId;
		public String nomSousCategorie;
		public List<Long> beneficiaireIds;
		public List<String> nomsBeneficiaires;
	}

	public RegleImportReleve rechercherParId(Long id) throws ServiceException {

		Assert.notNull(id, () -> "L'ID pour la recherche d'une regle d'import de releve est obligatoire");

		try {
			Optional<RegleImportReleve> optional = regleImportReleveRepository.findByIdAndActiveTrue(id);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ImportReleveTechniqueErreur.RECHERCHE_PAR_ID,
					id);
		}
	}

	public List<RegleImportReleve> rechercherToutesActives() throws ServiceException {

		try {
			return regleImportReleveRepository.findByActiveTrueOrderByDateDerniereUtilisationDesc();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ImportReleveTechniqueErreur.RECHERCHE_TOUS);
		}
	}

	public List<ResultatSuggestion> rechercherSuggestions(List<CritereSuggestion> criteres) throws ServiceException {

		if (criteres == null || criteres.isEmpty()) {
			return List.of();
		}

		Map<Integer, String> clesParIndex = new LinkedHashMap<>();
		for (CritereSuggestion critere : criteres) {
			clesParIndex.put(critere.index, construireCle(critere.libelle, critere.groupKey));
		}

		Set<String> cles = clesParIndex.values()
				.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		List<RegleImportReleve> regles;
		try {
			regles = cles.isEmpty()
					? List.of()
					: regleImportReleveRepository.findByActiveTrueAndCleLibelleNormaliseeIn(cles);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ImportReleveTechniqueErreur.RECHERCHE_SUGGESTIONS);
		}

		List<ResultatSuggestion> resultats = new ArrayList<>();
		for (CritereSuggestion critere : criteres) {
			ResultatSuggestion resultat = new ResultatSuggestion();
			resultat.critere = critere;
			resultat.cleLibelleNormalisee = clesParIndex.get(critere.index);
			resultat.regle = rechercherMeilleureRegle(critere, resultat.cleLibelleNormalisee, regles);
			resultats.add(resultat);
		}

		return resultats;
	}

	public List<RegleImportReleve> apprendre(List<Apprentissage> apprentissages) throws ServiceException {

		if (apprentissages == null || apprentissages.isEmpty()) {
			return List.of();
		}

		List<RegleImportReleve> regles = new ArrayList<>();
		for (Apprentissage apprentissage : apprentissages) {
			regles.add(apprendre(apprentissage));
		}
		return regles;
	}

	public RegleImportReleve apprendre(Apprentissage apprentissage) throws ServiceException {

		String cleLibelleNormalisee = construireCle(apprentissage.libelle, apprentissage.groupKey);
		CompteInterne compteInterneReleve = rechercherCompteInterneReleve(
				apprentissage.compteInterneReleveId,
				apprentissage.identifiantCompteInterneReleve);
		Compte compteContrepartie = rechercherCompte(
				apprentissage.compteContrepartieId,
				apprentissage.identifiantCompteContrepartie);
		SousCategorie sousCategorie = rechercherSousCategorie(
				apprentissage.sousCategorieId,
				apprentissage.nomSousCategorie);
		Set<Beneficiaire> beneficiaires = rechercherBeneficiaires(
				apprentissage.beneficiaireIds,
				apprentissage.nomsBeneficiaires);

		RegleImportReleve regle = rechercherParClePourModification(
				compteInterneReleve,
				apprentissage.roleCompteContrepartie,
				cleLibelleNormalisee);

		if (regle == null) {
			regle = new RegleImportReleve();
			regle.setCleLibelleNormalisee(cleLibelleNormalisee);
			regle.setCompteInterneReleve(compteInterneReleve);
			regle.setRoleCompteContrepartie(apprentissage.roleCompteContrepartie);
		}

		regle.setLibelleExemple(apprentissage.libelle);
		regle.setTypeOperation(apprentissage.typeOperation);
		regle.setCompteContrepartie(compteContrepartie);
		regle.setSousCategorie(sousCategorie);
		regle.changerBeneficiaires(beneficiaires);
		regle.setActive(Boolean.TRUE);
		regle.incrementerUtilisation();

		return enregistrer(regle);
	}

	public void desactiver(Long id) throws ServiceException {

		RegleImportReleve regle = rechercherParId(id);
		if (regle == null) {
			throw new ServiceException(
					ImportReleveFonctionnelleErreur.REGLE_NON_TROUVEE,
					id);
		}

		try {
			regle.setActive(Boolean.FALSE);
			regleImportReleveRepository.save(regle);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ImportReleveTechniqueErreur.DESACTIVATION,
					id);
		}
	}

	public String construireCle(String libelle, String groupKey) {
		return libelleImportReleveNormalizer.normaliser(libelle, groupKey);
	}

	private RegleImportReleve rechercherMeilleureRegle(
			CritereSuggestion critere,
			String cleLibelleNormalisee,
			List<RegleImportReleve> regles) throws ServiceException {

		if (cleLibelleNormalisee == null || critere.roleCompteContrepartie == null) {
			return null;
		}

		CompteInterne compteInterneReleve = rechercherCompteInterneReleve(
				critere.compteInterneReleveId,
				critere.identifiantCompteInterneReleve);
		Long compteInterneReleveId = compteInterneReleve == null ? null : compteInterneReleve.getId();

		return regles.stream()
				.filter((regle) -> Boolean.TRUE.equals(regle.isActive()))
				.filter((regle) -> cleLibelleNormalisee.equals(regle.getCleLibelleNormalisee()))
				.filter((regle) -> critere.roleCompteContrepartie == regle.getRoleCompteContrepartie())
				.filter((regle) -> compteInterneReleveId == null
						|| regle.getCompteInterneReleve() == null
						|| regle.getCompteInterneReleve().getId().equals(compteInterneReleveId))
				.sorted(comparateurPertinence(compteInterneReleveId))
				.findFirst()
				.orElse(null);
	}

	private Comparator<RegleImportReleve> comparateurPertinence(Long compteInterneReleveId) {
		return Comparator
				.comparing((RegleImportReleve regle) -> scoreContexte(regle, compteInterneReleveId))
				.thenComparing(
						RegleImportReleve::getDateDerniereUtilisation,
						Comparator.nullsLast(Comparator.reverseOrder()))
				.thenComparing(
						RegleImportReleve::getNombreUtilisations,
						Comparator.nullsLast(Comparator.reverseOrder()));
	}

	private int scoreContexte(RegleImportReleve regle, Long compteInterneReleveId) {
		if (compteInterneReleveId != null
				&& regle.getCompteInterneReleve() != null
				&& compteInterneReleveId.equals(regle.getCompteInterneReleve().getId())) {
			return 0;
		}
		return regle.getCompteInterneReleve() == null ? 1 : 2;
	}

	private RegleImportReleve rechercherParClePourModification(
			CompteInterne compteInterneReleve,
			RoleCompteContrepartie roleCompteContrepartie,
			String cleLibelleNormalisee) throws ServiceException {

		try {
			if (compteInterneReleve == null) {
				Optional<RegleImportReleve> optional = regleImportReleveRepository.findByCompteInterneReleveIsNullAndRoleCompteContrepartieAndCleLibelleNormalisee(
						roleCompteContrepartie,
						cleLibelleNormalisee);
				return optional.isEmpty() ? null : optional.get();
			}

			Optional<RegleImportReleve> optional = regleImportReleveRepository.findByCompteInterneReleveIdAndRoleCompteContrepartieAndCleLibelleNormalisee(
					compteInterneReleve.getId(),
					roleCompteContrepartie,
					cleLibelleNormalisee);
			return optional.isEmpty() ? null : optional.get();
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ImportReleveTechniqueErreur.RECHERCHE_PAR_CLE,
					cleLibelleNormalisee);
		}
	}

	private RegleImportReleve enregistrer(RegleImportReleve regle) throws ServiceException {

		try {
			return regleImportReleveRepository.save(regle);
		}
		catch (Throwable t) {
			throw new ServiceException(
					t,
					ImportReleveTechniqueErreur.ENREGISTREMENT,
					regle.getCleLibelleNormalisee());
		}
	}

	private CompteInterne rechercherCompteInterneReleve(Long id, String identifiant) throws ServiceException {

		if (id == null && (identifiant == null || identifiant.isBlank())) {
			return null;
		}

		CompteInterne compteInterne = id != null
				? compteInterneService.rechercherParId(id)
				: compteInterneService.rechercherParIdentifiant(identifiant);

		if (compteInterne == null) {
			throw new ServiceException(
					ImportReleveFonctionnelleErreur.COMPTE_INTERNE_RELEVE_NON_TROUVE,
					id == null ? identifiant : id);
		}

		return compteInterne;
	}

	private Compte rechercherCompte(Long id, String identifiant) throws ServiceException {

		if (id == null && (identifiant == null || identifiant.isBlank())) {
			return null;
		}

		Compte compteContrepartie = id != null
				? compteContrepartieService.rechercherParId(id)
				: compteContrepartieService.rechercherParIdentifiant(identifiant);

		if (compteContrepartie == null) {
			throw new ServiceException(
					ImportReleveFonctionnelleErreur.COMPTE_CONTREPARTIE_NON_TROUVE,
					id == null ? identifiant : id);
		}

		return compteContrepartie;
	}

	private SousCategorie rechercherSousCategorie(Long id, String nom) throws ServiceException {

		if (id == null && (nom == null || nom.isBlank())) {
			return null;
		}

		SousCategorie sousCategorie = id != null
				? sousCategorieService.rechercherParId(id)
				: sousCategorieService.rechercherParNom(nom);

		if (sousCategorie == null) {
			throw new ServiceException(
					ImportReleveFonctionnelleErreur.SOUS_CATEGORIE_NON_TROUVEE,
					id == null ? nom : id);
		}

		return sousCategorie;
	}

	private Set<Beneficiaire> rechercherBeneficiaires(List<Long> ids, List<String> noms) throws ServiceException {

		Set<Beneficiaire> beneficiaires = new HashSet<>();

		if (ids != null) {
			for (Long id : ids) {
				if (id == null) {
					continue;
				}
				Beneficiaire beneficiaire = beneficiaireService.rechercherParId(id);
				if (beneficiaire == null) {
					throw new ServiceException(
							ImportReleveFonctionnelleErreur.BENEFICIAIRE_NON_TROUVE,
							id);
				}
				beneficiaires.add(beneficiaire);
			}
		}

		if (noms != null) {
			for (String nom : noms) {
				if (nom == null || nom.isBlank()) {
					continue;
				}
				Beneficiaire beneficiaire = beneficiaireService.rechercherParNom(nom);
				if (beneficiaire == null) {
					throw new ServiceException(
							ImportReleveFonctionnelleErreur.BENEFICIAIRE_NON_TROUVE,
							nom);
				}
				beneficiaires.add(beneficiaire);
			}
		}

		return beneficiaires;
	}
}

