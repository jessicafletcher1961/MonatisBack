package fr.colline.monatis.typologies.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.colline.monatis.typologies.model.TypeCompte;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;
import fr.colline.monatis.typologies.model.TypePeriode;
import fr.colline.monatis.typologies.model.TypeProgrammation;
import fr.colline.monatis.typologies.model.TypeReference;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/monatis/typologies")
@Transactional
public class TypologieController {

	@GetMapping("/fonctionnement")
	public List<TypologieResponseDto> getTypesFonctionnements() {

		return Arrays.asList(TypeFonctionnement.values())
				.stream()
				.map((t) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(t);})
				.toList();
		
	}

	@GetMapping("/compte")
	public List<TypologieResponseDto> getAllTypesComptes() {

		return Arrays.asList(TypeCompte.values())
				.stream()
				.map((t) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(t);})
				.toList();
		
	}
	
	@GetMapping("/operation")
	public List<TypologieResponseDto> getTypesOperations() {
		
		return Arrays.asList(TypeOperation.values())
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();
	}
	
	@GetMapping("/periode")
	public List<TypologieResponseDto> getTypesPeriodes() {
		
		return Arrays.asList(TypePeriode.values())
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();
	}
	
	@GetMapping("/programmation")
	public List<TypologieResponseDto> getTypesProgrammations() {
		
		return Arrays.asList(TypeProgrammation.values())
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();
	}
	
	@GetMapping("/reference")
	public List<TypologieResponseDto> getTypesReferences() {
		
		return Arrays.asList(TypeReference.values())
				.stream()
				.map((to) -> {return TypologieResponseDtoMapper.mapperModelToResponseDto(to);})
				.toList();
	}

}
