package fr.colline.monatis.emprunts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.emprunts.model.Emprunt;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {

	Optional<Emprunt> findByCle(String cle);

	boolean existsByCle(String cle);

}
