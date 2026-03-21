package fr.colline.monatis.rapports.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurControle;
import fr.colline.monatis.rapports.controller.dto.RapportCompteInterneBasicResponseDto;
import fr.colline.monatis.rapports.controller.dto.RapportCompteInterneDetailedResponseDto;
import fr.colline.monatis.rapports.controller.mapper.RapportCompteInterneDtoMapper;
import fr.colline.monatis.rapports.service.RapportService;

@RestController
@RequestMapping("/monatis/rapports")
@Transactional
public class RapportController {

	@Autowired private RapportService rapportService;

	@Autowired private CompteInterneService compteInterneService;

	@GetMapping("/comptes/interne/all")
	public List<RapportCompteInterneBasicResponseDto> getAllRapportsCompteInterne(
			@RequestParam(name = "dateDebut", required = false) ZonedDateTime paramDateDebut,
			@RequestParam(name = "dateFin", required = false) ZonedDateTime paramDateFin)
					throws ServiceException {

		List<RapportCompteInterneBasicResponseDto> resultat = new ArrayList<>();

		ZonedDateTime dateFin = paramDateFin == null ? ZonedDateTime.now() : paramDateFin;

		Sort tri = Sort.by("identifiant");
		List<CompteInterne> liste = compteInterneService.rechercherTous(tri);
		for ( CompteInterne compteInterne : liste ) {
			ZonedDateTime dateDebut = paramDateDebut == null ? compteInterne.getDateSoldeInitial() : paramDateDebut;
			resultat.add(RapportCompteInterneDtoMapper.modelToBasicResponseDto(
					rapportService.calculerRapportCompteInterne(
							compteInterne,
							dateDebut,
							dateFin)));
		}

		return resultat;
	}

	@GetMapping("/comptes/interne/get/{identifiant}")
	public RapportCompteInterneDetailedResponseDto getRapportCompte(
			@PathVariable (name = "identifiant") String identifiant,
			@RequestParam(name = "dateDebut", required = false) ZonedDateTime paramDateDebut,
			@RequestParam(name = "dateFin", required = false) ZonedDateTime paramDateFin)
					throws ControllerException, ServiceException {

		if ( identifiant == null || identifiant.isBlank() ) {
			throw new ControllerException(
					ErreurControle.PATH_VARIABLE_IDENTIFIANT_OBLIGATOIRE);
		}

		CompteInterne compteInterne = compteInterneService.rechercherParIdentifiant(identifiant);
		if ( compteInterne == null ) {
			throw new ControllerException(
					ErreurControle.COMPTE_INTERNE_NON_TROUVE_PAR_IDENTIFIANT,
					identifiant);
		}

		ZonedDateTime dateFin = paramDateFin == null ? ZonedDateTime.now() : paramDateFin;
		ZonedDateTime dateDebut = paramDateDebut == null ? compteInterne.getDateSoldeInitial() : paramDateDebut;

		return RapportCompteInterneDtoMapper.modelToDetailedResponseDto(
				rapportService.calculerRapportCompteInterne(
						compteInterne,
						dateDebut,
						dateFin));
	}
}
