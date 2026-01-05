package fr.colline.monatis.budgets.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.budgets.BudgetControleErreur;
import fr.colline.monatis.budgets.BudgetFonctionnelleErreur;
import fr.colline.monatis.budgets.BudgetTechniqueErreur;
import fr.colline.monatis.budgets.model.Budget;
import fr.colline.monatis.budgets.model.TypePeriode;
import fr.colline.monatis.budgets.service.BudgetService;
import fr.colline.monatis.exceptions.ServiceException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/csv/budgets")
@Transactional
public class BudgetCsvController {

    private final BudgetService budgetService;

    BudgetCsvController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }
	
	@GetMapping(value = "/types", produces = "text/csv")
	public ResponseEntity<?> getCsvTypePeriode() {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("type", "code", "libelle"));
		for ( TypePeriode type : TypePeriode.values() ) {
			csvBody.add(Arrays.asList("periode", type.getCode(), type.getLibelle()));
		}

		String csvFileName = "budgets-types.csv";

		return genererContenu(csvBody, csvFileName);
	}

	@GetMapping(value = "/erreurs", produces = "text/csv")
	public ResponseEntity<?> getCsvErreurs() {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("erreur", "code", "pattern"));
		for ( BudgetControleErreur erreur : BudgetControleErreur.values() ) {
			csvBody.add(Arrays.asList(erreur.getTypeErreur().getPrefixe(), erreur.getCode(), erreur.getPattern()));
		}
		for ( BudgetFonctionnelleErreur erreur : BudgetFonctionnelleErreur.values() ) {
			csvBody.add(Arrays.asList(erreur.getTypeErreur().getPrefixe(), erreur.getCode(), erreur.getPattern()));
		}
		for ( BudgetTechniqueErreur erreur : BudgetTechniqueErreur.values() ) {
			csvBody.add(Arrays.asList(erreur.getTypeErreur().getPrefixe(), erreur.getCode(), erreur.getPattern()));
		}

		String csvFileName = "budget-erreurs.csv";

		return genererContenu(csvBody, csvFileName);
	}

	@GetMapping(value = "/tables", produces = "text/csv")
	public ResponseEntity<?> getCsvTables() throws ServiceException {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("table", "code_type_reference", "nom_reference", "code_type_periode", "date_debut", "date_fin", "montant_en_centimes"));
		for ( Budget budget : budgetService.rechercherTous() ) {
			csvBody.add(Arrays.asList(
					"budget", 
					budget.getReference().getTypeReference().getCode(), 
					budget.getReference().getNom(), 
					budget.getTypePeriode().getCode(), 
					budget.getDateDebut().toString(), 
					budget.getDateFin().toString(), 
					budget.getMontantEnCentimes().toString()));
		}

		String csvFileName = "budget-tables.csv";

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
