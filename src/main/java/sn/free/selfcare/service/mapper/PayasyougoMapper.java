package sn.free.selfcare.service.mapper;


import sn.free.selfcare.domain.*;
import sn.free.selfcare.service.dto.PayasyougoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payasyougo} and its DTO {@link PayasyougoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PayasyougoMapper extends EntityMapper<PayasyougoDTO, Payasyougo> {



    default Payasyougo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payasyougo payasyougo = new Payasyougo();
        payasyougo.setId(id);
        return payasyougo;
    }
}
