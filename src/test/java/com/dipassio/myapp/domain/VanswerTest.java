package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class VanswerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vanswer.class);
        Vanswer vanswer1 = new Vanswer();
        vanswer1.setId(1L);
        Vanswer vanswer2 = new Vanswer();
        vanswer2.setId(vanswer1.getId());
        assertThat(vanswer1).isEqualTo(vanswer2);
        vanswer2.setId(2L);
        assertThat(vanswer1).isNotEqualTo(vanswer2);
        vanswer1.setId(null);
        assertThat(vanswer1).isNotEqualTo(vanswer2);
    }
}
