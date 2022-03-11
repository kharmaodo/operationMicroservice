package sn.free.selfcare.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.free.selfcare.web.rest.TestUtil;

public class PayasyougoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayasyougoDTO.class);
        PayasyougoDTO payasyougoDTO1 = new PayasyougoDTO();
        payasyougoDTO1.setId(1L);
        PayasyougoDTO payasyougoDTO2 = new PayasyougoDTO();
        assertThat(payasyougoDTO1).isNotEqualTo(payasyougoDTO2);
        payasyougoDTO2.setId(payasyougoDTO1.getId());
        assertThat(payasyougoDTO1).isEqualTo(payasyougoDTO2);
        payasyougoDTO2.setId(2L);
        assertThat(payasyougoDTO1).isNotEqualTo(payasyougoDTO2);
        payasyougoDTO1.setId(null);
        assertThat(payasyougoDTO1).isNotEqualTo(payasyougoDTO2);
    }
}
