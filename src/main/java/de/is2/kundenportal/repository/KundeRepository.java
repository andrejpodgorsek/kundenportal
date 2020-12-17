package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.Kunde;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Kunde entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KundeRepository extends JpaRepository<Kunde, Long> {
}
