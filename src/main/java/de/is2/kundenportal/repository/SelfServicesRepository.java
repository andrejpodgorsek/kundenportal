package de.is2.kundenportal.repository;

import de.is2.kundenportal.domain.SelfServices;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SelfServices entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SelfServicesRepository extends JpaRepository<SelfServices, Long> {

    @Query("select selfServices from SelfServices selfServices where selfServices.user.login = ?#{principal.username}")
    List<SelfServices> findByUserIsCurrentUser();
}
