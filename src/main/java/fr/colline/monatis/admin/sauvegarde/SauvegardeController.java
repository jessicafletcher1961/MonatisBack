package fr.colline.monatis.admin.sauvegarde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.exceptions.ServiceException;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/admin")
@Transactional
public class SauvegardeController {

	@Autowired SauvegardeService sauvegardeService;
	
	@GetMapping("/save")
	public void getAll() throws ServiceException {

		sauvegardeService.dumpTables();
		
	}

}
