package fr.colline.monatis.comptes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteExterne;
import fr.colline.monatis.comptes.repository.CompteExterneRepository;
import fr.colline.monatis.comptes.repository.CompteRepository;

@Service
public class CompteExterneService extends CompteService<CompteExterne>{

	@Autowired private CompteExterneRepository compteExterneRepository;
	
	@Override
	public Class<CompteExterne> getTClass() {
		return CompteExterne.class;
	}

	@Override
	public CompteRepository<CompteExterne> getRepository() {
		return compteExterneRepository;
	}

}
