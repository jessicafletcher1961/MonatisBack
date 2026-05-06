package fr.colline.monatis.admin;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.exceptions.ControllerException;
import fr.colline.monatis.exceptions.ControllerVerificateurService;
import fr.colline.monatis.exceptions.ServiceException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/admin")
@Transactional
public class AdminController {

	public final static String REPERTOIRE_SAUVEGARDE = "sauvegardes";
	public final static String REPERTOIRE_ECHANGE = "echanges";
	
	@Autowired private ControllerVerificateurService verificateur;
	@Autowired private AdminService adminService;
	
	@GetMapping("/execution/{nomFichierScript}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void executerScript(@PathVariable String nomFichierScript) throws ControllerException, ServiceException {
		
		nomFichierScript = REPERTOIRE_ECHANGE
				+ "/" 
				+ nomFichierScript;
		File fichierScript = new File(nomFichierScript);
		if ( ! fichierScript.exists() ) {
			throw new ControllerException(AdminControleErreur.FICHIER_NON_TROUVE, fichierScript.getAbsolutePath());
		}
		
		adminService.desactiverContraintes();
		adminService.executerScript(fichierScript);
		adminService.reactiverContraintes();
	}

	@GetMapping("/export/{nomTable}/{nomFichierCsv}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void exporterCsv(
			@PathVariable String nomTable,
			@PathVariable String nomFichierCsv) throws ControllerException, ServiceException {
		
		nomFichierCsv = REPERTOIRE_ECHANGE
				+ "/" 
				+ nomFichierCsv;
		File fichierCsv = new File(nomFichierCsv);
		if ( fichierCsv.exists() ) {
			throw new ControllerException(AdminControleErreur.FICHIER_DEJA_PRESENT, fichierCsv.getAbsolutePath());
		}
		
		adminService.exporterToCsv(nomTable, fichierCsv);
	}

	@GetMapping("/import/{nomFichierCsv}/{nomTable}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void importerCsv(
			@PathVariable String nomFichierCsv,
			@PathVariable String nomTable) throws ControllerException, ServiceException {
		
		nomFichierCsv = REPERTOIRE_ECHANGE
				+ "/" 
				+ nomFichierCsv;
		File fichierCsv = new File(nomFichierCsv);
		if ( ! fichierCsv.exists() ) {
			throw new ControllerException(AdminControleErreur.FICHIER_NON_TROUVE, fichierCsv.getAbsolutePath());
		}
		
		adminService.desactiverContraintes(nomTable);
		adminService.importerFromCsv(fichierCsv, nomTable);
		adminService.reactiverContraintes(nomTable);
	}

	@GetMapping("/sauvegarde/show")
	public List<AdminSauvegardeResponseDto> montrerSauvegardesEnregistrees() throws ControllerException {
		
		String nomRepertoire = REPERTOIRE_SAUVEGARDE;
		File repertoire = new File(nomRepertoire);
		
		if ( ! repertoire.exists() || ! repertoire.isDirectory() ) {

			throw new ControllerException(
					AdminControleErreur.REPERTOIRE_SAUVEGARDE_INEXISTANT,
					repertoire.getAbsolutePath());
		}
		
		return AdminResponseDtoMapper.mapperListAdminSauvegarde(adminService.rechercherSauvegardesEnregistrees(repertoire));
	}
	
	@GetMapping("/sauvegarde")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void sauvegarder() throws ControllerException, ServiceException {
		
		sauvegarderBase("MONATIS");
	}

	@GetMapping("/sauvegarde/{nomSauvegarde}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void sauvegarderBase(@PathVariable String nomSauvegarde) throws ControllerException, ServiceException {
		
		nomSauvegarde = verificateur.verifierNom(nomSauvegarde, true);

		LocalDateTime today = LocalDateTime.now();
		String nomFichierZip = REPERTOIRE_SAUVEGARDE
				+ "/" 
				+ nomSauvegarde
				+ "-"
				+ today.format(DateTimeFormatter.BASIC_ISO_DATE)
				+ "-"
				+ Integer.toString(today.get(ChronoField.SECOND_OF_DAY))
				+ ".zip";
		File fichierZip =  new File(nomFichierZip);
		
		if ( fichierZip.exists() ) {
			throw new ControllerException(
					AdminControleErreur.FICHIER_SAUVEGARDE_EXISTANT,
					fichierZip.getAbsolutePath());
		}

		adminService.sauvegarderBase(fichierZip);
	}

	@GetMapping("/restauration/{nomFichierZip}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void restaurerBase(@PathVariable String nomFichierZip) throws ControllerException, ServiceException {
		
		nomFichierZip = REPERTOIRE_SAUVEGARDE
				+ "/" 
				+ nomFichierZip;
		File fichierZip =  new File(nomFichierZip) ;
		
		if ( ! fichierZip.exists() ) {
			throw new ControllerException(
					AdminControleErreur.FICHIER_SAUVEGARDE_INEXISTANT,
					fichierZip.getAbsolutePath());
		}

		adminService.desactiverContraintes();
		adminService.restaurerBase(fichierZip);
		adminService.reactiverContraintes();
	}

	@GetMapping("/vidange")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void viderBase() throws ServiceException {
		
		adminService.desactiverContraintes();
		adminService.vidangerBase();
		adminService.reactiverContraintes();
	}

}
