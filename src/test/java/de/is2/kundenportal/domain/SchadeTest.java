package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class SchadeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schade.class);
        Schade schade1 = new Schade();
        schade1.setId(1L);
        Schade schade2 = new Schade();
        schade2.setId(schade1.getId());
        assertThat(schade1).isEqualTo(schade2);
        schade2.setId(2L);
        assertThat(schade1).isNotEqualTo(schade2);
        schade1.setId(null);
        assertThat(schade1).isNotEqualTo(schade2);
    }
}
