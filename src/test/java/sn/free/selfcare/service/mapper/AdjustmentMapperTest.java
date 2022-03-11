package sn.free.selfcare.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AdjustmentMapperTest {

    private AdjustmentMapper adjustmentMapper;

    @BeforeEach
    public void setUp() {
        adjustmentMapper = new AdjustmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(adjustmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(adjustmentMapper.fromId(null)).isNull();
    }
}
