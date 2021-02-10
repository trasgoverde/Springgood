package com.dipassio.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.dipassio.myapp.web.rest.TestUtil;

public class VtopicTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vtopic.class);
        Vtopic vtopic1 = new Vtopic();
        vtopic1.setId(1L);
        Vtopic vtopic2 = new Vtopic();
        vtopic2.setId(vtopic1.getId());
        assertThat(vtopic1).isEqualTo(vtopic2);
        vtopic2.setId(2L);
        assertThat(vtopic1).isNotEqualTo(vtopic2);
        vtopic1.setId(null);
        assertThat(vtopic1).isNotEqualTo(vtopic2);
    }
}
