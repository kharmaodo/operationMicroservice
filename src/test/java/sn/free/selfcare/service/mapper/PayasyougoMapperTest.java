package sn.free.selfcare.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PayasyougoMapperTest {

    private PayasyougoMapper payasyougoMapper;

    @BeforeEach
    public void setUp() {
        payasyougoMapper = new PayasyougoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(payasyougoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(payasyougoMapper.fromId(null)).isNull();
    }
}
