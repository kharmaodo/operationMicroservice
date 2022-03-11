package sn.free.selfcare.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.free.selfcare.web.rest.TestUtil;

public class AdjustmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adjustment.class);
        Adjustment adjustment1 = new Adjustment();
        adjustment1.setId(1L);
        Adjustment adjustment2 = new Adjustment();
        adjustment2.setId(adjustment1.getId());
        assertThat(adjustment1).isEqualTo(adjustment2);
        adjustment2.setId(2L);
        assertThat(adjustment1).isNotEqualTo(adjustment2);
        adjustment1.setId(null);
        assertThat(adjustment1).isNotEqualTo(adjustment2);
    }
}
