package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.Nachricht;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Nachricht entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NachrichtRepository extends JpaRepository<Nachricht, Long> {

    @Query("select nachricht from Nachricht nachricht where nachricht.user.login = ?#{principal.username}")
    List<Nachricht> findByUserIsCurrentUser();
}
