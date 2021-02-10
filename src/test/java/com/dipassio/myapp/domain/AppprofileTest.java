package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class AppprofileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appprofile.class);
        Appprofile appprofile1 = new Appprofile();
        appprofile1.setId(1L);
        Appprofile appprofile2 = new Appprofile();
        appprofile2.setId(appprofile1.getId());
        assertThat(appprofile1).isEqualTo(appprofile2);
        appprofile2.setId(2L);
        assertThat(appprofile1).isNotEqualTo(appprofile2);
        appprofile1.setId(null);
        assertThat(appprofile1).isNotEqualTo(appprofile2);
    }
}
