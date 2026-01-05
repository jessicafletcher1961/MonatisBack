package fr.colline.monatis.rapports.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.budgets.model.TypePeriode;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.erreurs.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.rapports.RapportControleErreur;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueRequestDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.EtatPlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.HistoriquePlusMoinsValueRequestDto;
import fr.colline.monatis.rapports.controller.plus_moins_values.HistoriquePlusMoinsValueResponseDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteRequestDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.model.EtatPlusMoinsValues;
import fr.colline.monatis.rapports.model.HistoriquePlusMoinsValues;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.service.RapportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/rapports")
@Transactional
public class RapportController {

	private final boolean OBLIGATOIRE = true;
	private final boolean FACULTATIF = false;

	@Autowired private ControllerVerificateurService verificateur; 
	@Autowired private RapportService rapportService;

	@GetMapping("/releve_compte")
	public ReleveCompteResponseDto getReleveCompte(
			@RequestBody ReleveCompteRequestDto requestDto) throws ControllerException, ServiceException {

		Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte, OBLIGATOIRE);
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());

		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		ReleveCompte releve = rapportService.rechercherReleveCompte(compte, dateDebut, dateFin);
		
		return CompteRapportModelToResponseDtoMapper.mapperReleveCompte(releve);
	}

	@GetMapping(value = "/releve_compte/pdf", produces = "application/pdf")
	public void getReleveComptePdf(
			HttpServletResponse response,
			@RequestBody ReleveCompteRequestDto requestDto) throws ControllerException, ServiceException, IOException {

		Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte, OBLIGATOIRE);
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, OBLIGATOIRE, null);
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());

		if ( dateFin.isBefore(dateDebut) ) {
			throw new ControllerException(
					RapportControleErreur.DATE_FIN_AVANT_DATE_DEBUT, 
					dateFin,
					dateDebut);
		}

		ReleveCompte releve = rapportService.rechercherReleveCompte(compte, dateDebut, dateFin);
		
		CompteRapportModelToResponseDtoMapper.mapperReleveCompteToPdf(releve, response.getOutputStream());
	}

	@GetMapping("/plus_moins_value/historique")
	public HistoriquePlusMoinsValueResponseDto getHistoriquePlusMoinsValue(
			@RequestBody HistoriquePlusMoinsValueRequestDto requestDto) throws ControllerException, ServiceException {

		Compte compte = verificateur.verifierCompte(requestDto.identifiantCompte, OBLIGATOIRE);
		if ( ! CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			throw new ControllerException(
					RapportControleErreur.RECHERCHE_EVALUATION_SUR_COMPTE_PAS_INTERNE,
					TypeCompte.INTERNE.getCode(),
					compte.getIdentifiant(),	
					compte.getTypeCompte().getCode());
		}
		CompteInterne compteInterne = (CompteInterne) compte;
		TypePeriode typePeriode = verificateur.verifierTypePeriode(requestDto.codeTypePeriode, FACULTATIF, null);
		LocalDate dateDebut = verificateur.verifierDate(requestDto.dateDebut, FACULTATIF, compteInterne.getDateSoldeInitial());
		LocalDate dateFin = verificateur.verifierDate(requestDto.dateFin, FACULTATIF, LocalDate.now());
		
		HistoriquePlusMoinsValues historique = rapportService.rechercherHistoriquePlusMoinsValue(
				compteInterne, 
				dateDebut,
				dateFin,
				typePeriode);
		
		return CompteRapportModelToResponseDtoMapper.mapperHistoriquePlusMoinsValue(historique);
	}
	
	@GetMapping("/plus_moins_value/etat")
	public List<EtatPlusMoinsValueResponseDto> getEtatPlusMoinsValue(
			@RequestBody EtatPlusMoinsValueRequestDto requestDto) throws ControllerException, ServiceException {

		TypePeriode typePeriode = verificateur.verifierTypePeriode(requestDto.codeTypePeriode, FACULTATIF, null);
		LocalDate dateCible = verificateur.verifierDate(requestDto.dateCible, FACULTATIF, LocalDate.now());
		
		List<EtatPlusMoinsValues> etats = rapportService.rechercherEtatsPlusMoinsValue(
				null,
				dateCible,
				typePeriode);
		
		List<EtatPlusMoinsValueResponseDto> dto = new ArrayList<EtatPlusMoinsValueResponseDto>();
		for ( EtatPlusMoinsValues etat : etats ) {
			dto.add(CompteRapportModelToResponseDtoMapper.mapperEtatPlusMoinsValue(etat));
		}
		
		return dto;
	}

}
