package fr.colline.monatis.rapports.model;

import java.time.LocalDate;

import fr.colline.monatis.comptes.model.CompteInterne;

public class PlusMoinsValue {

	private CompteInterne compteInterne;
	
	private LocalDate dateDebutEvaluation;
	
	private LocalDate dateFinEvaluation;
	
	private Long montantSoldeInitialEnCentimes;
	
	private Long montantSoldeFinalEnCentimes;
	
	private Long montantMouvementsEnCentimes;
	
	private Long montantsGainsEtFraisEnCentimes;
	
	private Long montantReevaluationEnCentimes;
	
	private Float montantPlusMoinsValueEnPourcentage;

	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public LocalDate getDateDebutEvaluation() {
		return dateDebutEvaluation;
	}

	public void setDateDebutEvaluation(LocalDate dateDebutEvaluation) {
		this.dateDebutEvaluation = dateDebutEvaluation;
	}

	public LocalDate getDateFinEvaluation() {
		return dateFinEvaluation;
	}

	public void setDateFinEvaluation(LocalDate dateFinEvaluation) {
		this.dateFinEvaluation = dateFinEvaluation;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public Long getMontantSoldeFinalEnCentimes() {
		return montantSoldeFinalEnCentimes;
	}

	public void setMontantSoldeFinalEnCentimes(Long montantSoldeFinalEnCentimes) {
		this.montantSoldeFinalEnCentimes = montantSoldeFinalEnCentimes;
	}

	public Long getMontantMouvementsEnCentimes() {
		return montantMouvementsEnCentimes;
	}

	public void setMontantMouvementsEnCentimes(Long montantOperationsEnCentimes) {
		this.montantMouvementsEnCentimes = montantOperationsEnCentimes;
	}

	public Long getMontantsGainsEtFraisEnCentimes() {
		return montantsGainsEtFraisEnCentimes;
	}

	public void setMontantsGainsEtFraisEnCentimes(Long montantOperationsEnCentimes) {
		this.montantsGainsEtFraisEnCentimes = montantOperationsEnCentimes;
	}

	public Long getMontantReevaluationEnCentimes() {
		return montantReevaluationEnCentimes;
	}

	public void setMontantReevaluationEnCentimes(Long montantOperationsEnCentimes) {
		this.montantReevaluationEnCentimes = montantOperationsEnCentimes;
	}

	public Float getMontantPlusMoinsValueEnPourcentage() {
		return montantPlusMoinsValueEnPourcentage;
	}

	public void setMontantPlusMoinsValueEnPourcentage(Float montantPlusMoinsValueEnPourcentage) {
		this.montantPlusMoinsValueEnPourcentage = montantPlusMoinsValueEnPourcentage;
	}
	
}
