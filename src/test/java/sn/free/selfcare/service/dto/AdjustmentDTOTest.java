package sn.free.selfcare.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sn.free.selfcare.web.rest.TestUtil;

public class AdjustmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdjustmentDTO.class);
        AdjustmentDTO adjustmentDTO1 = new AdjustmentDTO();
        adjustmentDTO1.setId(1L);
        AdjustmentDTO adjustmentDTO2 = new AdjustmentDTO();
        assertThat(adjustmentDTO1).isNotEqualTo(adjustmentDTO2);
        adjustmentDTO2.setId(adjustmentDTO1.getId());
        assertThat(adjustmentDTO1).isEqualTo(adjustmentDTO2);
        adjustmentDTO2.setId(2L);
        assertThat(adjustmentDTO1).isNotEqualTo(adjustmentDTO2);
        adjustmentDTO1.setId(null);
        assertThat(adjustmentDTO1).isNotEqualTo(adjustmentDTO2);
    }
}
