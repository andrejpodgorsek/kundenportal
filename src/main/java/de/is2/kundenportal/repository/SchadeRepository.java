package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.Schade;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Schade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchadeRepository extends JpaRepository<Schade, Long> {

    @Query("select schade from Schade schade where schade.user.login = ?#{principal.username}")
    List<Schade> findByUserIsCurrentUser();
}
