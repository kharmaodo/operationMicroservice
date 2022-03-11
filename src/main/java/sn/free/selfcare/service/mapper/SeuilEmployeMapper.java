package sn.free.selfcare.service.mapper;


import sn.free.selfcare.domain.*;
import sn.free.selfcare.service.dto.SeuilEmployeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SeuilEmploye} and its DTO {@link SeuilEmployeDTO}.
 */
@Mapper(componentModel = "spring", uses = {AlertMapper.class})
public interface SeuilEmployeMapper extends EntityMapper<SeuilEmployeDTO, SeuilEmploye> {

	@Mapping(source = "alert.id", target = "alertId")
    SeuilEmployeDTO toDto(SeuilEmploye seuilEmploye);

	@Mapping(source = "alertId", target = "alert")
    SeuilEmploye toEntity(SeuilEmployeDTO seuilEmployeDTO);

    default SeuilEmploye fromId(Long id) {
        if (id == null) {
            return null;
        }
        SeuilEmploye seuilEmploye = new SeuilEmploye();
        seuilEmploye.setId(id);
        return seuilEmploye;
    }
}
