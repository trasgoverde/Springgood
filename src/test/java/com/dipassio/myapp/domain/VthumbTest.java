package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class VthumbTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vthumb.class);
        Vthumb vthumb1 = new Vthumb();
        vthumb1.setId(1L);
        Vthumb vthumb2 = new Vthumb();
        vthumb2.setId(vthumb1.getId());
        assertThat(vthumb1).isEqualTo(vthumb2);
        vthumb2.setId(2L);
        assertThat(vthumb1).isNotEqualTo(vthumb2);
        vthumb1.setId(null);
        assertThat(vthumb1).isNotEqualTo(vthumb2);
    }
}
