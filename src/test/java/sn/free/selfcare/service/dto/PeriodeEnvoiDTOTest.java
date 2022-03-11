package sn.free.selfcare.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.free.selfcare.web.rest.TestUtil;

public class PeriodeEnvoiDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodeEnvoiDTO.class);
        PeriodeEnvoiDTO periodeEnvoiDTO1 = new PeriodeEnvoiDTO();
        periodeEnvoiDTO1.setId(1L);
        PeriodeEnvoiDTO periodeEnvoiDTO2 = new PeriodeEnvoiDTO();
        assertThat(periodeEnvoiDTO1).isNotEqualTo(periodeEnvoiDTO2);
        periodeEnvoiDTO2.setId(periodeEnvoiDTO1.getId());
        assertThat(periodeEnvoiDTO1).isEqualTo(periodeEnvoiDTO2);
        periodeEnvoiDTO2.setId(2L);
        assertThat(periodeEnvoiDTO1).isNotEqualTo(periodeEnvoiDTO2);
        periodeEnvoiDTO1.setId(null);
        assertThat(periodeEnvoiDTO1).isNotEqualTo(periodeEnvoiDTO2);
    }
}
