package fr.colline.monatis.operations.controller.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import fr.colline.monatis.references.controller.ReferenceResponseDto;

public class OperationLigneSimpleResponseDto implements Serializable {

	private static final long serialVersionUID = -6345449274731381005L;
	
	public Integer numeroLigne;
	public LocalDate dateComptabilisation;
	public Long montantEnCentimes;
	public String libelle;
	public ReferenceResponseDto sousCategorie;
	public List<ReferenceResponseDto> beneficiaires;
}
