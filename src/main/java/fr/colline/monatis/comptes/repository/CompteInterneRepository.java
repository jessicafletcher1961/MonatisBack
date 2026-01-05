package fr.colline.monatis.comptes.repository;

import java.util.Set;

import org.springframework.stereotype.Repository;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;

@Repository
public interface CompteInterneRepository extends CompteRepository<CompteInterne> {

	public Set<CompteInterne> findByTypeFonctionnement(TypeFonctionnement typeFonctionnement);

}
