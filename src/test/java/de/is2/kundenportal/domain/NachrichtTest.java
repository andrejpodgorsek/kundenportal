package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class NachrichtTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nachricht.class);
        Nachricht nachricht1 = new Nachricht();
        nachricht1.setId(1L);
        Nachricht nachricht2 = new Nachricht();
        nachricht2.setId(nachricht1.getId());
        assertThat(nachricht1).isEqualTo(nachricht2);
        nachricht2.setId(2L);
        assertThat(nachricht1).isNotEqualTo(nachricht2);
        nachricht1.setId(null);
        assertThat(nachricht1).isNotEqualTo(nachricht2);
    }
}
