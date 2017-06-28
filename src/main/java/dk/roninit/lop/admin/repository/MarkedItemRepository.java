package dk.roninit.lop.admin.repository;

import dk.roninit.lop.admin.domain.MarkedItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MarkedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarkedItemRepository extends JpaRepository<MarkedItem,Long> {
    
}
