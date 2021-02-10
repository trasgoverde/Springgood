package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class VquestionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vquestion.class);
        Vquestion vquestion1 = new Vquestion();
        vquestion1.setId(1L);
        Vquestion vquestion2 = new Vquestion();
        vquestion2.setId(vquestion1.getId());
        assertThat(vquestion1).isEqualTo(vquestion2);
        vquestion2.setId(2L);
        assertThat(vquestion1).isNotEqualTo(vquestion2);
        vquestion1.setId(null);
        assertThat(vquestion1).isNotEqualTo(vquestion2);
    }
}
