package sn.free.selfcare.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FactureMapperTest {

    private FactureMapper factureMapper;

    @BeforeEach
    public void setUp() {
        factureMapper = new FactureMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(factureMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(factureMapper.fromId(null)).isNull();
    }
}
