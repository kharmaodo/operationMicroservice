package sn.free.selfcare.service.mapper;


import sn.free.selfcare.domain.*;
import sn.free.selfcare.service.dto.PeriodeEnvoiDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PeriodeEnvoi} and its DTO {@link PeriodeEnvoiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodeEnvoiMapper extends EntityMapper<PeriodeEnvoiDTO, PeriodeEnvoi> {



    default PeriodeEnvoi fromId(Long id) {
        if (id == null) {
            return null;
        }
        PeriodeEnvoi periodeEnvoi = new PeriodeEnvoi();
        periodeEnvoi.setId(id);
        return periodeEnvoi;
    }
}
