package fr.colline.monatis.comptes.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.CompteControleErreur;
import fr.colline.monatis.comptes.CompteFonctionnelleErreur;
import fr.colline.monatis.comptes.CompteTechniqueErreur;
import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.CompteTechnique;
import fr.colline.monatis.comptes.model.TypeCompte;
import fr.colline.monatis.comptes.model.TypeFonctionnement;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.references.model.Titulaire;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/csv/comptes")
@Transactional
public class CompteCsvController {

    @Autowired private CompteExterneService compteExterneService;
    @Autowired private CompteInterneService compteInterneService;
    @Autowired private CompteTechniqueService compteTechniqueService;
    
	@GetMapping(value = "/types", produces = "text/csv")
	public ResponseEntity<?> getCsvTypePeriode() {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("type", "code", "libelle"));
		for ( TypeCompte type : TypeCompte.values() ) {
			csvBody.add(Arrays.asList("compte", type.getCode(), type.getLibelle()));
		}
		for ( TypeFonctionnement type : TypeFonctionnement.values() ) {
			csvBody.add(Arrays.asList("fonctionnement", type.getCode(), type.getLibelle()));
		}

		String csvFileName = "comptes-types.csv";

		return genererContenu(csvBody, csvFileName);
	}

	@GetMapping(value = "/erreurs", produces = "text/csv")
	public ResponseEntity<?> getCsvErreurs() {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("erreur", "code", "pattern"));
		for ( CompteControleErreur erreur : CompteControleErreur.values() ) {
			csvBody.add(Arrays.asList(erreur.getTypeErreur().getPrefixe(), erreur.getCode(), erreur.getPattern()));
		}
		for ( CompteFonctionnelleErreur erreur : CompteFonctionnelleErreur.values() ) {
			csvBody.add(Arrays.asList(erreur.getTypeErreur().getPrefixe(), erreur.getCode(), erreur.getPattern()));
		}
		for ( CompteTechniqueErreur erreur : CompteTechniqueErreur.values() ) {
			csvBody.add(Arrays.asList(erreur.getTypeErreur().getPrefixe(), erreur.getCode(), erreur.getPattern()));
		}

		String csvFileName = "comptes-erreurs.csv";

		return genererContenu(csvBody, csvFileName);
	}

	@GetMapping(value = "/tables", produces = "text/csv")
	public ResponseEntity<?> getCsvTables() throws ServiceException {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("table", "identifiant", "libelle", "date_solde_initial", "solde_initial_en_centimes", "type_fonctionnement", "nom_banque", "noms_titulaires"));
		for ( CompteExterne compte : compteExterneService.rechercherTous() ) {
			csvBody.add(Arrays.asList(
					"compte_externe", 
					compte.getIdentifiant(), 
					compte.getLibelle()));
		}
		for ( CompteInterne compte : compteInterneService.rechercherTous() ) {
			String nomBanque = compte.getBanque() == null ? null : compte.getBanque().getNom();
			String nomsTitulaires = null;
			if ( compte.getTitulaires() != null && ! compte.getTitulaires().isEmpty() ) {
				nomsTitulaires = "";
				boolean premier = true;
				for ( Titulaire titulaire : compte.getTitulaires() ) {
					if ( ! premier ) {
						nomsTitulaires = nomsTitulaires.concat(", ");
					}
					nomsTitulaires = nomsTitulaires.concat(titulaire.getNom());
					premier = false;
				}
			}
			csvBody.add(Arrays.asList(
					"compte_interne", 
					compte.getIdentifiant(), 
					compte.getLibelle(),
					compte.getDateSoldeInitial().toString(),
					compte.getMontantSoldeInitialEnCentimes().toString(),
					compte.getTypeFonctionnement().getCode(),
					nomBanque,
					nomsTitulaires));
		}
		for ( CompteTechnique compte : compteTechniqueService.rechercherTous() ) {
			csvBody.add(Arrays.asList(
					"compte_technique", 
					compte.getIdentifiant(), 
					compte.getLibelle()));
		}

		String csvFileName = "comptes-tables.csv";

		return genererContenu(csvBody, csvFileName);
	}

	private ResponseEntity<?> genererContenu(List<List<String>> csvBody, String csvFileName) {
		
		ByteArrayInputStream byteArrayOutputStream;

		// closing resources by using a try with resources
		try (
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				// defining the CSV printer
				CSVPrinter csvPrinter = new CSVPrinter(
						new PrintWriter(out),
						// withHeader is optional
						CSVFormat.DEFAULT
						);
				) {
			// populating the CSV content
			for (List<String> record : csvBody)
				csvPrinter.printRecord(record);

			// writing the underlying stream
			csvPrinter.flush();

			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
		headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

		return new ResponseEntity<>(
				fileInputStream,
				headers,
				HttpStatus.OK);
	}

}
