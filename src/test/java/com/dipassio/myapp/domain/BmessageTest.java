package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class BmessageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bmessage.class);
        Bmessage bmessage1 = new Bmessage();
        bmessage1.setId(1L);
        Bmessage bmessage2 = new Bmessage();
        bmessage2.setId(bmessage1.getId());
        assertThat(bmessage1).isEqualTo(bmessage2);
        bmessage2.setId(2L);
        assertThat(bmessage1).isNotEqualTo(bmessage2);
        bmessage1.setId(null);
        assertThat(bmessage1).isNotEqualTo(bmessage2);
    }
}
