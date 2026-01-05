package fr.colline.monatis.operations.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.operations.model.OperationLigne;

@Repository
public interface OperationLigneRepository extends JpaRepository<OperationLigne, Long> {

	public List<OperationLigne> findBySousCategorieIdAndDateComptabilisationBetweenOrderByDateComptabilisation(
			Long sousCategorieId, 
			LocalDate dateDebut,
			LocalDate dateFin);

}
