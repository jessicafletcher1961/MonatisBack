package fr.colline.monatis.rapports.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.colline.monatis.comptes.model.TypeFonctionnement;

public class ListeResumeCompteInterne {

	Map<TypeFonctionnement, List<ResumeCompteInterne>> map = new HashMap<>();
	
	public Map<TypeFonctionnement, List<ResumeCompteInterne>> getMap() {
		return map;
	}

	public void setMap(Map<TypeFonctionnement, List<ResumeCompteInterne>> map) {
		this.map = map;
	}

	public ListeResumeCompteInterne () {
		
		for ( TypeFonctionnement typeFonctionnement : TypeFonctionnement.values() ) {
			map.put(typeFonctionnement, new ArrayList<ResumeCompteInterne>());
		}
	}
	
}
