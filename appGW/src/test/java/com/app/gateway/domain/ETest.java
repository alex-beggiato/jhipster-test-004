package com.app.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ETest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(E.class);
        E e1 = new E();
        e1.setId(1L);
        E e2 = new E();
        e2.setId(e1.getId());
        assertThat(e1).isEqualTo(e2);
        e2.setId(2L);
        assertThat(e1).isNotEqualTo(e2);
        e1.setId(null);
        assertThat(e1).isNotEqualTo(e2);
    }
}
