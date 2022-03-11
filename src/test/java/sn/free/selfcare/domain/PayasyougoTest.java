package sn.free.selfcare.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.free.selfcare.web.rest.TestUtil;

public class PayasyougoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payasyougo.class);
        Payasyougo payasyougo1 = new Payasyougo();
        payasyougo1.setId(1L);
        Payasyougo payasyougo2 = new Payasyougo();
        payasyougo2.setId(payasyougo1.getId());
        assertThat(payasyougo1).isEqualTo(payasyougo2);
        payasyougo2.setId(2L);
        assertThat(payasyougo1).isNotEqualTo(payasyougo2);
        payasyougo1.setId(null);
        assertThat(payasyougo1).isNotEqualTo(payasyougo2);
    }
}
