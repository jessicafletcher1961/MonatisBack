package fr.colline.monatis.rapports.controller.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CsvControllerTest {

	@Test
	void getCsvTypeRetourneLeCsvDesTypesOperation() throws IOException {
		csvController controller = new csvController();

		ResponseEntity<?> response = controller.getCsvType();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("text/csv", response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
		assertEquals("attachment; filename=type-operation.csv", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
		assertTrue(response.getBody() instanceof InputStreamResource);

		InputStreamResource body = (InputStreamResource) response.getBody();
		assertNotNull(body);
		String csv = new String(body.getInputStream().readAllBytes());

		assertTrue(csv.startsWith("type,code,libelle"));
		assertTrue(csv.contains("operation,RECETTE,"));
		assertTrue(csv.contains("operation,DEPENSE,"));
	}
}
