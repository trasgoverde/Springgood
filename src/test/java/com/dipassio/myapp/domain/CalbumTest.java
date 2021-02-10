package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class CalbumTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calbum.class);
        Calbum calbum1 = new Calbum();
        calbum1.setId(1L);
        Calbum calbum2 = new Calbum();
        calbum2.setId(calbum1.getId());
        assertThat(calbum1).isEqualTo(calbum2);
        calbum2.setId(2L);
        assertThat(calbum1).isNotEqualTo(calbum2);
        calbum1.setId(null);
        assertThat(calbum1).isNotEqualTo(calbum2);
    }
}
