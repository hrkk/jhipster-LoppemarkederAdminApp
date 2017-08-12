package dk.roninit.lopadminapp.repository;

import dk.roninit.lopadminapp.domain.Marked;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Marked entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarkedRepository extends JpaRepository<Marked,Long> {
    
}
