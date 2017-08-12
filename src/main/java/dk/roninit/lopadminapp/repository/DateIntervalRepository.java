package dk.roninit.lopadminapp.repository;

import dk.roninit.lopadminapp.domain.DateInterval;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DateInterval entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DateIntervalRepository extends JpaRepository<DateInterval,Long> {
    
}
