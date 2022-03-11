package sn.free.selfcare.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PeriodeEnvoiMapperTest {

    private PeriodeEnvoiMapper periodeEnvoiMapper;

    @BeforeEach
    public void setUp() {
        periodeEnvoiMapper = new PeriodeEnvoiMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(periodeEnvoiMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(periodeEnvoiMapper.fromId(null)).isNull();
    }
}
