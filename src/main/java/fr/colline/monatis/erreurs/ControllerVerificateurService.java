package fr.colline.monatis.erreurs;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.budgets.model.TypePeriode;
import fr.colline.monatis.comptes.CompteControleErreur;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteGeneriqueService;
import fr.colline.monatis.evaluations.EvaluationControleErreur;
import fr.colline.monatis.evaluations.model.Evaluation;
import fr.colline.monatis.evaluations.service.EvaluationService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.operations.OperationControleErreur;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.operations.model.TypeOperation;
import fr.colline.monatis.operations.service.OperationService;
import fr.colline.monatis.references.ReferenceControleErreur;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Beneficiaire;
import fr.colline.monatis.references.model.Categorie;
import fr.colline.monatis.references.model.Reference;
import fr.colline.monatis.references.model.SousCategorie;
import fr.colline.monatis.references.model.Titulaire;
import fr.colline.monatis.references.model.TypeReference;
import fr.colline.monatis.references.service.BanqueService;
import fr.colline.monatis.references.service.BeneficiaireService;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.references.service.SousCategorieService;
import fr.colline.monatis.references.service.TitulaireService;

@Service
public class ControllerVerificateurService {

	@Autowired private BanqueService banqueService;
	@Autowired private BeneficiaireService beneficiaireService;
	@Autowired private CategorieService categorieService;
	@Autowired private SousCategorieService sousCategorieService;
	@Autowired private TitulaireService titulaireService;
	@Autowired private CompteGeneriqueService compteGeneriqueService;
	@Autowired private OperationService operationService;
	@Autowired private EvaluationService evaluationService;

	public String verifierNom(String nom, boolean obligatoire) throws ControllerException {

		if ( nom == null || nom.isBlank() ) {
			if ( obligatoire ) {
				throw new ControllerException(
						ReferenceControleErreur.NOM_OBLIGATOIRE);
			}
			return null;
		}

		return nom.trim().toUpperCase().replaceAll(" ", "-").replaceAll("_", "-");
	}

	public String verifierNomValideEtUnique(TypeReference typeReference, String nomReference, Long id, boolean obligatoire) throws ControllerException, ServiceException {

		nomReference = verifierNom(nomReference, obligatoire);
		if ( nomReference == null ) {
			return null;
		}

		Reference reference;

		switch ( typeReference ) {
		case BANQUE:
			reference = banqueService.rechercherParNom(nomReference);
			break;
		case BENEFICIAIRE:
			reference = beneficiaireService.rechercherParNom(nomReference);
			break;
		case CATEGORIE:
			reference = categorieService.rechercherParNom(nomReference);
			break;
		case SOUS_CATEGORIE:
			reference = sousCategorieService.rechercherParNom(nomReference);
			break;
		case TITULAIRE:
			reference = titulaireService.rechercherParNom(nomReference);
			break;
		default:
			throw new ControllerException(
					GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeReference.class.getSimpleName(),
					typeReference.getCode(),
					typeReference.getLibelle());
		}

		if ( reference != null && !reference.getId().equals(id) ) {
			throw new ControllerException(
					ReferenceControleErreur.NOM_DEJA_UTILISE,
					nomReference,
					reference.getClass().getSimpleName());
		}

		return nomReference;
	}

	public String verifierIdentifiant(String identifiant, boolean obligatoire) throws ControllerException {

		if ( identifiant == null || identifiant.isBlank() ) {
			if ( obligatoire ) {
				throw new ControllerException(
						CompteControleErreur.IDENTIFIANT_OBLIGATOIRE);
			}
			return null;
		}

		return identifiant.trim().toUpperCase().replaceAll(" ", "-").replaceAll("_", "-");
	}

	public String verifierIdentifiantValideEtUnique(String identifiant, Long id, boolean obligatoire) throws ControllerException, ServiceException {

		identifiant = verifierIdentifiant(identifiant, obligatoire);
		if ( identifiant == null ) {
			return null;
		}

		Compte compte = compteGeneriqueService.rechercherParIdentifiant(identifiant);
		if ( compte != null && ! compte.getId().equals(id) ) {
			throw new ControllerException(
					CompteControleErreur.IDENTIFIANT_DEJA_UTILISE,
					identifiant,
					compte.getClass().getSimpleName());
		}

		return identifiant;
	}

	public String verifierNumero(String numero, boolean obligatoire) throws ControllerException {

		if ( numero == null || numero.isBlank() ) {
			if ( obligatoire ) {
				throw new ControllerException(
						OperationControleErreur.NUMERO_OBLIGATOIRE);
			}
			return null;
		}

		return numero.trim().toUpperCase().replaceAll(" ", "-").replaceAll("_", "-");
	}

	public String verifierNumeroValideEtUnique(String numero, Long id, boolean obligatoire) throws ControllerException, ServiceException {

		numero = verifierNumero(numero, obligatoire);
		if ( numero == null ) {
			return null;
		}

		Operation operation = operationService.rechercherParNumero(numero);
		if ( operation != null && ! operation.getId().equals(id) ) {
			throw new ControllerException(
					OperationControleErreur.NUMERO_DEJA_UTILISE,
					operation,
					operation.getClass().getSimpleName());
		}

		return numero;
	}

	public String verifierCle(String cle, boolean obligatoire) throws ControllerException {

		if ( cle == null || cle.isBlank() ) {
			if ( obligatoire ) {
				throw new ControllerException(
						EvaluationControleErreur.CLE_OBLIGATOIRE);
			}
			return null;
		}

		return cle.trim().toUpperCase().replaceAll(" ", "-").replaceAll("_", "-");
	}

	public String verifierCleValideEtUnique(String cle, Long id, boolean obligatoire) throws ControllerException, ServiceException {

		cle = verifierCle(cle, obligatoire);
		if ( cle == null ) {
			return null;
		}

		Evaluation evaluation = evaluationService.rechercherParCle(cle);
		if ( evaluation != null && ! evaluation.getId().equals(id) ) {
			throw new ControllerException(
					EvaluationControleErreur.CLE_DEJA_UTILISE,
					evaluation,
					evaluation.getClass().getSimpleName());
		}

		return cle;
	}

	public String verifierLibelle(String libelle, boolean obligatoire, String valeurParDefaut) throws ControllerException {

		if ( libelle == null || libelle.isBlank() ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.LIBELLE_OBLIGATOIRE);
			}
			return valeurParDefaut; 
		}

		return libelle.trim();
	}

	public LocalDate verifierDate(LocalDate date, boolean obligatoire, LocalDate valeurParDefaut) throws ControllerException {

		if ( date == null ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.DATE_OBLIGATOIRE);
			}
			return valeurParDefaut;
		}

		return date;
	}

	public LocalDate verifierDate(String date, boolean obligatoire, LocalDate valeurParDefaut) throws ControllerException {

		if ( date == null || date.isBlank() ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.DATE_OBLIGATOIRE);
			}
			return valeurParDefaut;
		}
		
		try {
			return LocalDate.parse(date);
		}
		catch ( DateTimeParseException e) {
			if ( obligatoire ) {
				throw new ControllerException(
						e,
						GeneriqueControleErreur.DATE_INVALIDE,
						date);
			}
			return valeurParDefaut;
		}
	}

	public Long verifierMontantEnCentimes(Long montantEnCentimes, boolean obligatoire, Long valeurParDefaut) throws ControllerException {

		if ( montantEnCentimes == null ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.MONTANT_OBLIGATOIRE,
						CompteInterne.class.getSimpleName());
			}
			return valeurParDefaut;
		}

		return montantEnCentimes;
	}

	public TypePeriode verifierTypePeriode(String codeTypePeriode, boolean obligatoire, TypePeriode valeurParDefaut) throws ControllerException {

		TypePeriode type = TypePeriode.findByCode(codeTypePeriode);
		if ( type == null ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.NON_TROUVE_PAR_CODE,
						TypePeriode.class.getSimpleName(),
						codeTypePeriode);
			}
			return valeurParDefaut;
		}
		return type;
	}


	public TypeOperation verifierTypeOperation(String codeTypeOperation, boolean obligatoire, TypeOperation valeurParDefaut) throws ControllerException {

		TypeOperation type = TypeOperation.findByCode(codeTypeOperation);
		if ( type == null ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.NON_TROUVE_PAR_CODE,
						TypePeriode.class.getSimpleName(),
						codeTypeOperation);
			}
			return valeurParDefaut;
		}
		return type;
	}

	public TypeFonctionnement verifierTypeFonctionnement(String codeTypeFonctionnement, boolean obligatoire, TypeFonctionnement valeurParDefaut) throws ControllerException {

		TypeFonctionnement type = TypeFonctionnement.findByCode(codeTypeFonctionnement);
		if ( type == null ) {
			if ( obligatoire ) {
				throw new ControllerException(
						GeneriqueControleErreur.NON_TROUVE_PAR_CODE,
						TypeFonctionnement.class.getSimpleName(),
						codeTypeFonctionnement);
			}
			return valeurParDefaut;
		}
		return type;
	}

	public Banque verifierBanque(String nomBanque, boolean obligatoire) throws ServiceException, ControllerException {

		nomBanque = verifierNom(nomBanque, obligatoire);

		if ( nomBanque == null ) {
			return null;
		}
		else {
			Banque banque = banqueService.rechercherParNom(nomBanque);
			if ( banque == null ) {
				throw new ControllerException(
						ReferenceControleErreur.NON_TROUVE_PAR_NOM,
						Banque.class.getSimpleName(),
						nomBanque);
			}
			return banque;
		}
	}

	public Beneficiaire verifierBeneficiaire(String nomBeneficiaire, boolean obligatoire) throws ServiceException, ControllerException {

		nomBeneficiaire = verifierNom(nomBeneficiaire, obligatoire);

		if ( nomBeneficiaire == null ) {
			return null;
		}
		else {
			Beneficiaire beneficiaire = beneficiaireService.rechercherParNom(nomBeneficiaire);
			if ( beneficiaire == null ) {
				throw new ControllerException(
						ReferenceControleErreur.NON_TROUVE_PAR_NOM,
						Beneficiaire.class.getSimpleName(),
						nomBeneficiaire);
			}
			return beneficiaire;
		}
	}

	public Categorie verifierCategorie(String nomCategorie, boolean obligatoire) throws ControllerException, ServiceException {

		nomCategorie = verifierNom(nomCategorie, obligatoire);

		if ( nomCategorie == null ) {
			return null;
		}
		else {
			Categorie categorie = categorieService.rechercherParNom(nomCategorie);
			if ( categorie == null ) {
				throw new ControllerException(
						ReferenceControleErreur.NON_TROUVE_PAR_NOM,
						Categorie.class.getSimpleName(),
						nomCategorie);
			}
			return categorie;
		}
	}

	public SousCategorie verifierSousCategorie(String nomSousCategorie, boolean obligatoire) throws ControllerException, ServiceException {

		nomSousCategorie = verifierNom(nomSousCategorie, obligatoire);

		if ( nomSousCategorie == null ) {
			return null;
		}
		else {
			SousCategorie sousCategorie = sousCategorieService.rechercherParNom(nomSousCategorie);
			if ( sousCategorie == null ) {
				throw new ControllerException(
						ReferenceControleErreur.NON_TROUVE_PAR_NOM,
						SousCategorie.class.getSimpleName(),
						nomSousCategorie);
			}
			return sousCategorie;
		}
	}

	public Titulaire verifierTitulaire(String nomTitulaire, boolean obligatoire) throws ControllerException, ServiceException {

		nomTitulaire = verifierNom(nomTitulaire, obligatoire);

		if ( nomTitulaire == null ) {
			return null;
		}
		else {
			Titulaire titulaire = titulaireService.rechercherParNom(nomTitulaire);
			if ( titulaire == null ) {
				throw new ControllerException(
						ReferenceControleErreur.NON_TROUVE_PAR_NOM,
						Titulaire.class.getSimpleName(),
						nomTitulaire);
			}
			return titulaire;
		}
	}

	public Compte verifierCompte(String identifiant, boolean obligatoire) throws ServiceException, ControllerException {

		identifiant = verifierIdentifiant(identifiant, obligatoire);

		if ( identifiant == null ) {
			return null;
		}
		else {
			Compte compte = compteGeneriqueService.rechercherParIdentifiant(identifiant);
			if ( compte == null ) {
				throw new ControllerException(
						CompteControleErreur.NON_TROUVE_PAR_IDENTIFIANT,
						identifiant);
			}
			return compte;
		}
	}

	public Compte verifierCompteEtTypeCompte(TypeCompte typeCompte, String identifiant, boolean obligatoire) throws ServiceException, ControllerException {

		Compte compte = verifierCompte(identifiant, obligatoire);

		if ( compte == null ) {
			return null;
		}
		else {
			if ( compte.getTypeCompte() != typeCompte ) {
				throw new ControllerException(
						CompteControleErreur.IDENTIFIANT_DEJA_UTILISE,
						compte.getIdentifiant(),
						compte.getClass().getSimpleName());
			}
			return compte;
		}
	}

	public Operation verifierOperation(String numero, boolean obligatoire) throws ControllerException, ServiceException {

		numero = verifierNumero(numero, obligatoire);

		if ( numero == null ) {
			return null;
		}
		else {
			Operation operation = operationService.rechercherParNumero(numero);
			if ( operation == null ) {
				throw new ControllerException(
						OperationControleErreur.NON_TROUVE_PAR_NUMERO,
						numero);
			}
			return operation;
		}
	}

	public Evaluation verifierEvaluation(String cle, boolean obligatoire) throws ServiceException, ControllerException {

		cle = verifierCle(cle, obligatoire);

		if ( cle == null ) {
			return null;
		}
		else {
			Evaluation evaluation = evaluationService.rechercherParCle(cle);
			if ( evaluation == null ) {
				throw new ControllerException(
						EvaluationControleErreur.NON_TROUVE_PAR_CLE,
						cle);
			}
			return evaluation;
		}
	}
}

