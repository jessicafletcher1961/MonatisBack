package fr.colline.monatis.comptes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteTiers;
import fr.colline.monatis.comptes.repository.CompteTiersRepository;

@Service public class CompteTiersService extends CompteService<CompteTiers>{

	@Autowired private CompteTiersRepository compteTiersRepository;

	@Override
	protected Class<CompteTiers> getTClass() {

		return CompteTiers.class;
	}
	
	public CompteTiersRepository getRepository() {
		
		return compteTiersRepository;
	}
}
