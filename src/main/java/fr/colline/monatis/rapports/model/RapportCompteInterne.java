package fr.colline.monatis.rapports.model;

import java.time.ZonedDateTime;
import java.util.List;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.operations.model.Operation;

public class RapportCompteInterne {

	private CompteInterne compteInterne;
	
	private ZonedDateTime dateSoldeInitial;
	
	private Long montantSoldeInitialEnCentimes;

	private Long montantTotalRecetteEnCentimes;
	
	private Long montantTotalDepenseEnCentimes;
	
	private ZonedDateTime dateSoldeFinal;
	
	private Long montantSoldeFinalEnCentimes;
	
	private Long montantDeltaEnCentimes;
	
	private List<Operation> operationsDepense;

	private List<Operation> operationsRecette;
	
	public CompteInterne getCompteInterne() {
		return compteInterne;
	}

	public void setCompteInterne(CompteInterne compteInterne) {
		this.compteInterne = compteInterne;
	}

	public ZonedDateTime getDateSoldeInitial() {
		return dateSoldeInitial;
	}

	public void setDateSoldeInitial(ZonedDateTime dateSoldeInitial) {
		this.dateSoldeInitial = dateSoldeInitial;
	}

	public Long getMontantSoldeInitialEnCentimes() {
		return montantSoldeInitialEnCentimes;
	}

	public void setMontantSoldeInitialEnCentimes(Long montantSoldeInitialEnCentimes) {
		this.montantSoldeInitialEnCentimes = montantSoldeInitialEnCentimes;
	}

	public Long getMontantTotalRecetteEnCentimes() {
		return montantTotalRecetteEnCentimes;
	}

	public void setMontantTotalRecetteEnCentimes(Long montantTotalRecetteEnCentimes) {
		this.montantTotalRecetteEnCentimes = montantTotalRecetteEnCentimes;
	}

	public Long getMontantTotalDepenseEnCentimes() {
		return montantTotalDepenseEnCentimes;
	}

	public void setMontantTotalDepenseEnCentimes(Long montantTotalDepenseEnCentimes) {
		this.montantTotalDepenseEnCentimes = montantTotalDepenseEnCentimes;
	}

	public ZonedDateTime getDateSoldeFinal() {
		return dateSoldeFinal;
	}

	public void setDateSoldeFinal(ZonedDateTime dateSoldeFinal) {
		this.dateSoldeFinal = dateSoldeFinal;
	}

	public Long getMontantSoldeFinalEnCentimes() {
		return montantSoldeFinalEnCentimes;
	}

	public void setMontantSoldeFinalEnCentimes(Long montantSoldeFinalEnCentimes) {
		this.montantSoldeFinalEnCentimes = montantSoldeFinalEnCentimes;
	}

	public Long getMontantDeltaEnCentimes() {
		return montantDeltaEnCentimes;
	}

	public void setMontantDeltaEnCentimes(Long montantDeltaEnCentimes) {
		this.montantDeltaEnCentimes = montantDeltaEnCentimes;
	}

	public List<Operation> getOperationsDepense() {
		return operationsDepense;
	}

	public void setOperationsDepense(List<Operation> operationsDepense) {
		this.operationsDepense = operationsDepense;
	}

	public List<Operation> getOperationsRecette() {
		return operationsRecette;
	}

	public void setOperationsRecette(List<Operation> operationsRecette) {
		this.operationsRecette = operationsRecette;
	}
}
