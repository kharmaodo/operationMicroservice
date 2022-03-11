package sn.free.selfcare.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.free.selfcare.web.rest.TestUtil;

public class PeriodeEnvoiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodeEnvoi.class);
        PeriodeEnvoi periodeEnvoi1 = new PeriodeEnvoi();
        periodeEnvoi1.setId(1L);
        PeriodeEnvoi periodeEnvoi2 = new PeriodeEnvoi();
        periodeEnvoi2.setId(periodeEnvoi1.getId());
        assertThat(periodeEnvoi1).isEqualTo(periodeEnvoi2);
        periodeEnvoi2.setId(2L);
        assertThat(periodeEnvoi1).isNotEqualTo(periodeEnvoi2);
        periodeEnvoi1.setId(null);
        assertThat(periodeEnvoi1).isNotEqualTo(periodeEnvoi2);
    }
}
