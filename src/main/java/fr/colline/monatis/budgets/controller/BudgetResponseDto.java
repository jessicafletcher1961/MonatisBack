package fr.colline.monatis.budgets.controller;

import java.io.Serializable;
import java.time.LocalDate;

import fr.colline.monatis.references.controller.ReferenceResponseDto;
import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class BudgetResponseDto implements Serializable {

	private static final long serialVersionUID = -449293913563674883L;

	public String cle;
	public String libelle;
	public ReferenceResponseDto reference;
	public TypologieResponseDto typePeriode;
	public LocalDate dateDebut;
	public LocalDate dateFin;
	public Long montantBudgetEnCentimes;

}
