package sn.free.selfcare.service.mapper;


import sn.free.selfcare.domain.*;
import sn.free.selfcare.service.dto.AdjustmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Adjustment} and its DTO {@link AdjustmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaiementMapper.class})
public interface AdjustmentMapper extends EntityMapper<AdjustmentDTO, Adjustment> {

    AdjustmentDTO toDto(Adjustment adjustment);

    @Mapping(target = "paiement", ignore = true)
    Adjustment toEntity(AdjustmentDTO adjustmentDTO);

    default Adjustment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Adjustment adjustment = new Adjustment();
        adjustment.setId(id);
        return adjustment;
    }
}
