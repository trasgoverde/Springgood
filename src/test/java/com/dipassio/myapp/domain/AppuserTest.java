package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class AppuserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appuser.class);
        Appuser appuser1 = new Appuser();
        appuser1.setId(1L);
        Appuser appuser2 = new Appuser();
        appuser2.setId(appuser1.getId());
        assertThat(appuser1).isEqualTo(appuser2);
        appuser2.setId(2L);
        assertThat(appuser1).isNotEqualTo(appuser2);
        appuser1.setId(null);
        assertThat(appuser1).isNotEqualTo(appuser2);
    }
}
