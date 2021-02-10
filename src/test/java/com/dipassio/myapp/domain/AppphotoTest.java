package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class AppphotoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appphoto.class);
        Appphoto appphoto1 = new Appphoto();
        appphoto1.setId(1L);
        Appphoto appphoto2 = new Appphoto();
        appphoto2.setId(appphoto1.getId());
        assertThat(appphoto1).isEqualTo(appphoto2);
        appphoto2.setId(2L);
        assertThat(appphoto1).isNotEqualTo(appphoto2);
        appphoto1.setId(null);
        assertThat(appphoto1).isNotEqualTo(appphoto2);
    }
}
