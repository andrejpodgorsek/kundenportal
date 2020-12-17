package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class VertragTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vertrag.class);
        Vertrag vertrag1 = new Vertrag();
        vertrag1.setId(1L);
        Vertrag vertrag2 = new Vertrag();
        vertrag2.setId(vertrag1.getId());
        assertThat(vertrag1).isEqualTo(vertrag2);
        vertrag2.setId(2L);
        assertThat(vertrag1).isNotEqualTo(vertrag2);
        vertrag1.setId(null);
        assertThat(vertrag1).isNotEqualTo(vertrag2);
    }
}
