package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class AgenturTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agentur.class);
        Agentur agentur1 = new Agentur();
        agentur1.setId(1L);
        Agentur agentur2 = new Agentur();
        agentur2.setId(agentur1.getId());
        assertThat(agentur1).isEqualTo(agentur2);
        agentur2.setId(2L);
        assertThat(agentur1).isNotEqualTo(agentur2);
        agentur1.setId(null);
        assertThat(agentur1).isNotEqualTo(agentur2);
    }
}
