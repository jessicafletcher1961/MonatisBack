package fr.colline.monatis.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.colline.monatis.comptes.repository.CompteTiersRepository;
import fr.colline.monatis.comptes.service.CompteTiersService;

@RunWith(MockitoJUnitRunner.class)
public class CompteTiersServiceTests {

	@Mock private CompteTiersRepository compteTiersRepository;
	
	@InjectMocks private CompteTiersService compteTiersService; 
	
	@Test public void testCompteTiers() {
	}	
}
