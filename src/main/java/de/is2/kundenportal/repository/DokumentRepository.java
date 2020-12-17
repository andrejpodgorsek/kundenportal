package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.Dokument;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Dokument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {

    @Query("select dokument from Dokument dokument where dokument.user.login = ?#{principal.username}")
    List<Dokument> findByUserIsCurrentUser();
}
