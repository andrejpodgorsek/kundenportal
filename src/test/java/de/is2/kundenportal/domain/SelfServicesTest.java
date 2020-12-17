package de.is2.kundenportal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.is2.kundenportal.web.rest.TestUtil;

public class SelfServicesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SelfServices.class);
        SelfServices selfServices1 = new SelfServices();
        selfServices1.setId(1L);
        SelfServices selfServices2 = new SelfServices();
        selfServices2.setId(selfServices1.getId());
        assertThat(selfServices1).isEqualTo(selfServices2);
        selfServices2.setId(2L);
        assertThat(selfServices1).isNotEqualTo(selfServices2);
        selfServices1.setId(null);
        assertThat(selfServices1).isNotEqualTo(selfServices2);
    }
}
