package fr.colline.monatis.rapports.controller.csv;

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

import fr.colline.monatis.operations.model.TypeOperation;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/csv/type")
@Transactional
public class csvController {

	@GetMapping(value = "/operation", produces = "text/csv")
	public ResponseEntity<?> getCsvType() {

		List<List<String>> csvBody = new ArrayList<>();
		csvBody.add(Arrays.asList("type", "code", "libelle"));
		for ( TypeOperation type : TypeOperation.values() ) {
			csvBody.add(Arrays.asList("operation", type.getCode(), type.getLibelle()));
		}

		String csvFileName = "type-operation.csv";

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
