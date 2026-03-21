package fr.colline.monatis.comptes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.comptes.controller.dto.response.CompteResponseDto;
import fr.colline.monatis.comptes.controller.mapper.CompteDtoMapper;
import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.service.CompteTousTypesService;
import fr.colline.monatis.exceptions.ServiceException;

@RestController
@RequestMapping("/monatis/comptes")
@Transactional
public class CompteTousTypeController {

	@Autowired private CompteTousTypesService compteTousTypesService;

	@GetMapping("/all")
	public List<CompteResponseDto> getAllComptes() 
			throws ServiceException {

		List<CompteResponseDto> resultat = new ArrayList<>();
		Sort tri = Sort.by("identifiant");
		List<Compte> liste = compteTousTypesService.rechercherTous(tri);
		for ( Compte compte : liste ) {
			resultat.add(CompteDtoMapper.modelToBasicResponseDto(compte));
		}
		
		return resultat;		
	}
}
