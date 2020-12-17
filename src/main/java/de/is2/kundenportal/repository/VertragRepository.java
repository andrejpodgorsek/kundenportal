package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.Vertrag;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Vertrag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VertragRepository extends JpaRepository<Vertrag, Long> {

    @Query("select vertrag from Vertrag vertrag where vertrag.user.login = ?#{principal.username}")
    List<Vertrag> findByUserIsCurrentUser();
}
