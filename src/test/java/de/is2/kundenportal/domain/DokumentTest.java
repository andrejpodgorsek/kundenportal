package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class DokumentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dokument.class);
        Dokument dokument1 = new Dokument();
        dokument1.setId(1L);
        Dokument dokument2 = new Dokument();
        dokument2.setId(dokument1.getId());
        assertThat(dokument1).isEqualTo(dokument2);
        dokument2.setId(2L);
        assertThat(dokument1).isNotEqualTo(dokument2);
        dokument1.setId(null);
        assertThat(dokument1).isNotEqualTo(dokument2);
    }
}
