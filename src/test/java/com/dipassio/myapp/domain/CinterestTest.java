package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class CinterestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cinterest.class);
        Cinterest cinterest1 = new Cinterest();
        cinterest1.setId(1L);
        Cinterest cinterest2 = new Cinterest();
        cinterest2.setId(cinterest1.getId());
        assertThat(cinterest1).isEqualTo(cinterest2);
        cinterest2.setId(2L);
        assertThat(cinterest1).isNotEqualTo(cinterest2);
        cinterest1.setId(null);
        assertThat(cinterest1).isNotEqualTo(cinterest2);
    }
}
