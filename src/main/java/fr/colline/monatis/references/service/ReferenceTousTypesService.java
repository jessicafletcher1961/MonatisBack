package fr.colline.monatis.references.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.exceptions.erreurs.ErreurProgrammation;
import fr.colline.monatis.model.references.Reference;
import fr.colline.monatis.references.repository.ReferenceRepository;

@Service
public class ReferenceTousTypesService extends ReferenceService<Reference> {

	@Autowired private ReferenceRepository<Reference> repository;

	@Override
	protected Class<Reference> getTClass() {

		return Reference.class;
	}

	@Override
	protected ReferenceRepository<Reference> getRepository() {
		
		return repository;
	}
	
	@Override
	public Reference rechercherParNom(String nom) throws ServiceException {
		
		throw new ServiceException(
				ErreurProgrammation.RECHERCHE_PAR_NOM_IMPOSSIBLE,
				getTClass().getSimpleName());
	}
	
	@Override
	public boolean isExistantParNom(String nom) throws ServiceException {
		
		throw new ServiceException(
				ErreurProgrammation.RECHERCHE_PAR_NOM_IMPOSSIBLE,
				getTClass().getSimpleName());

	}
}
