package fr.colline.monatis.rapports.controller;

import java.util.ArrayList;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import fr.colline.monatis.budgets.controller.BudgetResponseDtoMapper;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.erreurs.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.operations.model.Operation;
import fr.colline.monatis.rapports.RapportControleErreur;
import fr.colline.monatis.rapports.controller.budgets.EtatAvancementBudgetResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.HistoriquePlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.PlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.EnteteCompteExterneResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.EnteteCompteInterneResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.EnteteCompteResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteOperationResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.model.EtatAvancementBudget;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValues;
import fr.colline.monatis.rapports.model.HistoriquePlusMoinsValues;
import fr.colline.monatis.rapports.model.PlusMoinsValue;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.references.controller.souscategorie.SousCategorieResponseDtoMapper;
import fr.colline.monatis.references.model.Banque;
import fr.colline.monatis.references.model.Titulaire;
import jakarta.servlet.ServletOutputStream;

public class CompteRapportModelToResponseDtoMapper {

	public static ReleveCompteResponseDto mapperReleveCompte(ReleveCompte releve) throws ControllerException {

		ReleveCompteResponseDto dto = new ReleveCompteResponseDto();

		dto.enteteCompte = mapperEnteteCompte(releve.getCompte());

		dto.dateDebutReleve = releve.getDateDebutReleve();
		dto.dateFinReleve = releve.getDateFinReleve();
		dto.montantSoldeDebutReleveEnEuros = (float) (releve.getMontantSoldeDebutReleveEnCentimes() / 100.00);
		dto.montantSoldeFinReleveEnEuros = (float) (releve.getMontantSoldeFinReleveEnCentimes() / 100.00);
		dto.montantTotalOperationsRecetteEnEuros = (float) (releve.getMontantTotalOperationsRecetteEnCentimes() / 100.00);
		dto.montantTotalOperationsDepenseEnEuros = (float) (releve.getMontantTotalOperationsDepenseEnCentimes() / 100.00);

		dto.operationsRecette = new ArrayList<ReleveCompteOperationResponseDto>();
		for ( Operation operation : releve.getOperationsRecette() ) {
			dto.operationsRecette.add(mapperOperationRecette(operation));
		}
		dto.operationsDepense = new ArrayList<ReleveCompteOperationResponseDto>();
		for ( Operation operation : releve.getOperationsDepense() ) {
			dto.operationsDepense.add(mapperOperationDepense(operation));
		}

		return dto;
	}

	public static void mapperReleveCompteToPdf(ReleveCompte releve, ServletOutputStream stream) throws ControllerException {

		try {
			PdfWriter pdfWriter = new PdfWriter(stream);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument);

			switch ( releve.getCompte().getTypeCompte() ) {
			case INTERNE:
				CompteInterne compteInterne = (CompteInterne) releve.getCompte();

				if ( compteInterne.getBanque() != null ) {
					Banque banque = compteInterne.getBanque();
					String parBanque = "BANQUE : "
							.concat(banque.getNom())
							.concat(" - ")
							.concat(banque.getLibelle());
					document.add(new Paragraph(parBanque));
				}

				String parCompteInterne = "COMPTE : "
						.concat(compteInterne.getIdentifiant())
						.concat(" - ")
						.concat(compteInterne.getLibelle());
				document.add(new Paragraph(parCompteInterne));

				if ( compteInterne.getTitulaires() != null && ! compteInterne.getTitulaires().isEmpty() ) {
					for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
						String parTitulaire = "TITULAIRE : "
								.concat(titulaire.getNom())
								.concat(" - ")
								.concat(titulaire.getLibelle());
						document.add(new Paragraph(parTitulaire));
					}
				}

				break;
			default:
				String parCompte = "COMPTE : "
						.concat(releve.getCompte().getIdentifiant())
						.concat(" - ")
						.concat(releve.getCompte().getLibelle());
				document.add(new Paragraph(parCompte));
				break;
			}

			document.close();
		}
		catch ( Throwable t ) {
			throw new ControllerException(
					RapportControleErreur.GENERATION_PDF_EN_ECHEC,
					t);
		}
	}

	public static HistoriquePlusMoinsValueResponseDto mapperHistoriquePlusMoinsValue(HistoriquePlusMoinsValues historique) throws ControllerException {

		HistoriquePlusMoinsValueResponseDto dto = new HistoriquePlusMoinsValueResponseDto();

		dto.enteteCompte = mapperEnteteCompte(historique.getCompteInterne());

		dto.plusMoinsValues = new ArrayList<>();
		for ( PlusMoinsValue plusMoinsValue : historique.getPlusMoinsValues() ) {
			dto.plusMoinsValues.add(mapperPlusMoinsValueResponseDto(plusMoinsValue));
		}

		return dto;
	}

	public static EtatPlusMoinsValueResponseDto mapperEtatPlusMoinsValue(
			EtatPlusMoinsValues etat) throws ControllerException {

		EtatPlusMoinsValueResponseDto dto = new EtatPlusMoinsValueResponseDto();

		dto.enteteCompte = mapperEnteteCompte(etat.getCompteInterne());
		dto.plusMoinsValue = CompteRapportModelToResponseDtoMapper.mapperPlusMoinsValueResponseDto(etat.getPlusMoinsValue());

		return dto;
	}

	public static EtatAvancementBudgetResponseDto mapperEtatAvancementBudget(
			EtatAvancementBudget etatAvancement) {

		EtatAvancementBudgetResponseDto dto = new EtatAvancementBudgetResponseDto();

		dto.reference = SousCategorieResponseDtoMapper.mapperModelToBasicResponseDto(etatAvancement.getSousCategorie());
		dto.budget = BudgetResponseDtoMapper.mapperModelToResponseDto(etatAvancement.getBudget());

		dto.montantBudgeteEnEuros = (float) (etatAvancement.getMontantBudgetEnCentimes() / 100.00);
		dto.montantExecutionEnEuros = (float) (etatAvancement.getMontantExecutionEnCentimes() / 100.00);
		dto.montantExecutionEnPourcentage = etatAvancement.getMontantExecutionEnPourcentage();
		dto.resteADepenserEnEuros = (float) (etatAvancement.getResteADepenserEnCentimes() / 100.00);

		dto.montantTotalLignesDepenseEnEuros = (float) (etatAvancement.getMontantTotalLignesDepenseEnCentimes() / 100.00);
		dto.montantTotalLignesRecetteEnEuros = (float) (etatAvancement.getMontantTotalLignesRecetteEnCentimes() / 100.00);
		dto.montantTotalLignesExcluesEnEuros = (float) (etatAvancement.getMontantTotalLignesExcluesEnCentimes() / 100.00);

		return dto;
	}

	private static ReleveCompteOperationResponseDto mapperOperationRecette(Operation operation) {

		ReleveCompteOperationResponseDto dto = new ReleveCompteOperationResponseDto();

		dto.numero = operation.getNumero();
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnEuros = (float) (operation.getMontantEnCentimes() / 100.00);
		dto.libelle = operation.getLibelle();
		dto.identifiantAutreCompte = operation.getCompteDepense().getIdentifiant();
		dto.libelleAutreCompte = operation.getCompteDepense().getLibelle();
		dto.codeTypeAutreCompte = operation.getCompteDepense().getTypeCompte().getCode();

		return dto;
	}

	private static ReleveCompteOperationResponseDto mapperOperationDepense(Operation operation) {

		ReleveCompteOperationResponseDto dto = new ReleveCompteOperationResponseDto();

		dto.numero = operation.getNumero();
		dto.codeTypeOperation = operation.getTypeOperation().getCode();
		dto.dateValeur = operation.getDateValeur();
		dto.montantEnEuros = 0 - (float) (operation.getMontantEnCentimes() / 100.00);
		dto.libelle = operation.getLibelle();
		dto.identifiantAutreCompte = operation.getCompteRecette().getIdentifiant();
		dto.libelleAutreCompte = operation.getCompteRecette().getLibelle();
		dto.codeTypeAutreCompte = operation.getCompteRecette().getTypeCompte().getCode();

		return dto;
	}

	private static EnteteCompteResponseDto mapperEnteteCompte(Compte compte) throws ControllerException {

		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {

			EnteteCompteInterneResponseDto dto = new EnteteCompteInterneResponseDto();

			dto.identifiantCompte = compte.getIdentifiant();
			dto.libelleCompte = compte.getLibelle();
			dto.codeTypeCompte = compte.getTypeCompte().getCode();

			CompteInterne compteInterne = (CompteInterne) compte;

			dto.codeTypeFonctionnement = compteInterne.getTypeFonctionnement().getCode();
			dto.libelleBanque = compteInterne.getBanque() == null ? null : compteInterne.getBanque().getLibelle();
			if ( compteInterne.getTitulaires() != null && !compteInterne.getTitulaires().isEmpty() ) {
				dto.libellesTitulaires = new ArrayList<>();
				for ( Titulaire titulaire : compteInterne.getTitulaires() ) {
					dto.libellesTitulaires.add(titulaire.getLibelle());
				}
			}
			dto.dateSoldeInitial = compteInterne.getDateSoldeInitial();
			dto.montantSoldeInitialEnEuros = (float) (compteInterne.getMontantSoldeInitialEnCentimes() / 100.00);

			return dto;
		}
		else if ( CompteExterne.class.isAssignableFrom(compte.getClass()) ) {

			EnteteCompteExterneResponseDto dto = new EnteteCompteExterneResponseDto();

			dto.identifiantCompte = compte.getIdentifiant();
			dto.libelleCompte = compte.getLibelle();
			dto.codeTypeCompte = compte.getTypeCompte().getCode();

			return dto;
		}
		else if ( CompteTechnique.class.isAssignableFrom(compte.getClass()) ) {

			EnteteCompteExterneResponseDto dto = new EnteteCompteExterneResponseDto();

			dto.identifiantCompte = compte.getIdentifiant();
			dto.libelleCompte = compte.getLibelle();
			dto.codeTypeCompte = compte.getTypeCompte().getCode();

			return dto;
		}
		else {
			throw new ControllerException(
					GeneriqueTechniqueErreur.CLASSE_NON_TRAITEE,
					compte.getClass().getSimpleName());
		}
	}

	private static PlusMoinsValueResponseDto mapperPlusMoinsValueResponseDto(PlusMoinsValue plusMoinsValue) {

		PlusMoinsValueResponseDto dto = new PlusMoinsValueResponseDto();

		dto.dateDebutEvaluation = plusMoinsValue.getDateDebutEvaluation();
		dto.dateFinEvaluation = plusMoinsValue.getDateFinEvaluation();
		dto.montantSoldeInitialEnEuros = (float) (plusMoinsValue.getMontantSoldeInitialEnCentimes() / 100.00);
		dto.montantSoldeFinalEnEuros = (float) (plusMoinsValue.getMontantSoldeFinalEnCentimes() / 100.00);
		dto.montantMouvementsEnEuros = (float) (plusMoinsValue.getMontantMouvementsEnCentimes() / 100.00);
		dto.montantRemunerationEnEuros = (float) (plusMoinsValue.getMontantsGainsEtFraisEnCentimes() / 100.00);
		dto.montantReevaluationEnEuros = (float) (plusMoinsValue.getMontantReevaluationEnCentimes() / 100.00);
		dto.montantPlusMoinsValueEnPourcentage = plusMoinsValue.getMontantPlusMoinsValueEnPourcentage();

		return dto;
	}

}
