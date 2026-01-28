package fr.colline.monatis.admin.initialisation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.admin.initialisation.basic.InitialisationBasicService;
import fr.colline.monatis.admin.initialisation.danis.InitialisationDanisService;
import fr.colline.monatis.exceptions.ServiceException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/admin")
@Transactional
public class IntialisationController {

	@Autowired InitialisationDanisService initialisationService;
	
	@GetMapping("/delete/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAll() throws ServiceException {
		
		initialisationService.supprimerTous();
	}
	
	@GetMapping("/init/basic")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void initialiserBasic() throws ServiceException {
		
		initialisationService.initialiser();
	}
	
}
