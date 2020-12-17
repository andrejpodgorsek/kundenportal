package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class KundeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kunde.class);
        Kunde kunde1 = new Kunde();
        kunde1.setId(1L);
        Kunde kunde2 = new Kunde();
        kunde2.setId(kunde1.getId());
        assertThat(kunde1).isEqualTo(kunde2);
        kunde2.setId(2L);
        assertThat(kunde1).isNotEqualTo(kunde2);
        kunde1.setId(null);
        assertThat(kunde1).isNotEqualTo(kunde2);
    }
}
