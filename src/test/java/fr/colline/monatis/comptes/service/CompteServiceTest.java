package fr.colline.monatis.comptes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.exceptions.ServiceException;

public class CompteServiceTest {

	@Autowired private CompteInterneService compteInterneService;
	
	@BeforeTestClass
	public void initialiserReferentielEtComptes() {
		
	}

	@Test
	public void testControlerEtPreparerPourSuppression() {
		
		CompteInterne compte = new CompteInterne();
		
		try {
			compteInterneService.controlerEtPreparerPourSuppression(compte);
		} 
		catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("This is a test");
	}
}
