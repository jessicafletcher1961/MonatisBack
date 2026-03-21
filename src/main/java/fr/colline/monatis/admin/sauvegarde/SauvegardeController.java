package fr.colline.monatis.admin.sauvegarde;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/admin")
@Transactional
public class SauvegardeController {

//	@Autowired private SauvegardeService sauvegardeService;
//	@Autowired private BanqueService banqueService;
//	
//	@GetMapping(value = "/save", produces = "text/csv")
//	public ResponseEntity<?> saveAll() throws ServiceException {
//
//		LocalDateTime date = LocalDateTime.now();
//		
//		String dateNomFichier = date.format(DateTimeFormatter.BASIC_ISO_DATE);
//		String timeNomFichier = date.format(DateTimeFormatter.ISO_TIME).replaceAll(":", "").substring(0, 4);
//		S
//		String identifiantSauvegarde = dateNomFichier.concat("_").concat(timeNomFichier).concat("_");
//		
//		String nomTable = "banque";
//		List<List<String>> csvBody = new ArrayList<>();
//		csvBody.add(Arrays.asList("table", "nom", "libelle"));
//		for ( Banque banque : banqueService.rechercherTous() ) {
//			csvBody.add(Arrays.asList("banque", banque.getNom(), banque.getLibelle()));
//		}
//		String csvFileName = identifiantSauvegarde.concat(nomTable).concat(".csv");
//
//		return genererContenu(csvBody, csvFileName);
//
//	}
//	
//	private ResponseEntity<?> genererContenu(List<List<String>> csvBody, String csvFileName) {
//		
//		ByteArrayInputStream byteArrayOutputStream;
//
//		// closing resources by using a try with resources
//		try (
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				// defining the CSV printer
//				CSVPrinter csvPrinter = new CSVPrinter(
//						new PrintWriter(out),
//						// withHeader is optional
//						CSVFormat.DEFAULT
//						);
//				) {
//			// populating the CSV content
//			for (List<String> record : csvBody)
//				csvPrinter.printRecord(record);
//
//			// writing the underlying stream
//			csvPrinter.flush();
//
//			byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
//	
//		} catch (IOException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//
//		InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
//		headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
//
//		return new ResponseEntity<>(
//				fileInputStream,
//				headers,
//				HttpStatus.OK);
//	}

}
