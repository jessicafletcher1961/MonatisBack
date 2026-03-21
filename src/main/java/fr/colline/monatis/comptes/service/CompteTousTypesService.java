package fr.colline.monatis.comptes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.repository.CompteRepository;

@Service
public class CompteTousTypesService extends CompteService<Compte> {

	@Autowired private CompteRepository<Compte> repository;

	@Override
	protected Class<Compte> getTClass() {

		return Compte.class;
	}

	@Override
	public CompteRepository<Compte> getRepository() {

		return repository;
	}
}
