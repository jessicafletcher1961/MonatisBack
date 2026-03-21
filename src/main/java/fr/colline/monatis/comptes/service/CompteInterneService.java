package fr.colline.monatis.comptes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.repository.CompteInterneRepository;
import fr.colline.monatis.comptes.repository.CompteRepository;

@Service 
public class CompteInterneService extends CompteService<CompteInterne> {

	@Autowired private CompteInterneRepository compteInterneRepository;

	@Override
	protected CompteRepository<CompteInterne> getRepository() {

		return compteInterneRepository;
	}

	@Override
	protected Class<CompteInterne> getTClass() {

		return CompteInterne.class;
	}
}
