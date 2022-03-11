package sn.free.selfcare.service.mapper;


import sn.free.selfcare.domain.*;
import sn.free.selfcare.service.dto.PaiementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring", uses = {FactureMapper.class, AdjustmentMapper.class})
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {

    @Mapping(source = "adjustment.id", target = "adjustmentId")
    @Mapping(source = "facture.id", target = "factureId")
    PaiementDTO toDto(Paiement paiement);

    @Mapping(source = "adjustmentId", target = "adjustment")
    @Mapping(source = "factureId", target = "facture")
    Paiement toEntity(PaiementDTO paiementDTO);

    default Paiement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Paiement paiement = new Paiement();
        paiement.setId(id);
        return paiement;
    }
}
