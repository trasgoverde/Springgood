package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class CelebTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Celeb.class);
        Celeb celeb1 = new Celeb();
        celeb1.setId(1L);
        Celeb celeb2 = new Celeb();
        celeb2.setId(celeb1.getId());
        assertThat(celeb1).isEqualTo(celeb2);
        celeb2.setId(2L);
        assertThat(celeb1).isNotEqualTo(celeb2);
        celeb1.setId(null);
        assertThat(celeb1).isNotEqualTo(celeb2);
    }
}
