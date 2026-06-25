package fr.colline.monatis.rapports.controller;

import static fr.colline.monatis.rapports.RapportTestFixtures.compteInterne;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.references.service.CategorieService;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteRequestDto;
import fr.colline.monatis.rapports.controller.releve_compte.ReleveCompteResponseDto;
import fr.colline.monatis.rapports.model.ReleveCompte;
import fr.colline.monatis.rapports.service.RapportService;

@ExtendWith(MockitoExtension.class)
class RapportControllerTest {

	@Mock
	private ControllerVerificateurService verificateur;

	@Mock
	private RapportService rapportService;

	@Mock
	private CompteInterneService compteInterneService;

	@Mock
	private CategorieService categorieService;

	@InjectMocks
	private RapportController rapportController;

	@Test
	void getReleveCompteVerifieLesParametresPuisRetourneLeDto() throws Exception {
		CompteInterne compte = compteInterne(1L, "COURANT");
		ReleveCompteRequestDto request = new ReleveCompteRequestDto();
		request.identifiantCompte = "courant";
		request.dateDebut = LocalDate.of(2026, 5, 1);
		request.dateFin = LocalDate.of(2026, 5, 31);
		ReleveCompte releve = new ReleveCompte();
		releve.setCompte(compte);
		releve.setDateDebutReleve(request.dateDebut);
		releve.setDateFinReleve(request.dateFin);
		releve.setMontantSoldeDebutReleveEnCentimes(10_000L);
		releve.setMontantSoldeFinReleveEnCentimes(11_000L);
		releve.setMontantTotalOperationsRecetteEnCentimes(1_000L);
		releve.setMontantTotalOperationsDepenseEnCentimes(0L);
		releve.setMontantEcartEnCentimes(0L);
		releve.setOperationsRecette(List.of());
		releve.setOperationsDepense(List.of());
		when(verificateur.verifierDate(request.dateDebut, true, null)).thenReturn(request.dateDebut);
		when(verificateur.verifierDate(request.dateFin, false, LocalDate.now())).thenReturn(request.dateFin);
		when(verificateur.verifierCompte("courant", true)).thenReturn(compte);
		when(rapportService.rechercherReleveOperationCompte(compte, request.dateDebut, request.dateFin)).thenReturn(releve);

		ReleveCompteResponseDto dto = rapportController.getReleveCompte(request);

		assertEquals(request.dateDebut, dto.dateDebutReleve);
		assertEquals(100D, dto.montantSoldeDebutReleveEnEuros);
		assertEquals(110D, dto.montantSoldeFinReleveEnEuros);
		verify(rapportService).rechercherReleveOperationCompte(compte, request.dateDebut, request.dateFin);
	}

	@Test
	void getReleveCompteRefuseUneDateFinAvantLaDateDebut() throws Exception {
		ReleveCompteRequestDto request = new ReleveCompteRequestDto();
		request.dateDebut = LocalDate.of(2026, 5, 31);
		request.dateFin = LocalDate.of(2026, 5, 1);
		when(verificateur.verifierDate(request.dateDebut, true, null)).thenReturn(request.dateDebut);
		when(verificateur.verifierDate(request.dateFin, false, LocalDate.now())).thenReturn(request.dateFin);

		assertThrows(ControllerException.class, () -> rapportController.getReleveCompte(request));
	}
}
