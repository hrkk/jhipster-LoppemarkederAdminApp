package dk.roninit.lopadminapp.repository;

import dk.roninit.lopadminapp.domain.Organizer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Organizer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizerRepository extends JpaRepository<Organizer,Long> {
    
}
