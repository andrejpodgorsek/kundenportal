package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.Agentur;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Agentur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgenturRepository extends JpaRepository<Agentur, Long> {

    @Query("select agentur from Agentur agentur where agentur.user.login = ?#{principal.username}")
    List<Agentur> findByUserIsCurrentUser();
}
