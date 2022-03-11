package sn.free.selfcare.service.mapper;


import sn.free.selfcare.domain.*;
import sn.free.selfcare.service.dto.AlertDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alert} and its DTO {@link AlertDTO}.
 */
@Mapper(componentModel = "spring", uses = {SeuilEmployeMapper.class})
public interface AlertMapper extends EntityMapper<AlertDTO, Alert> {

    AlertDTO toDto(Alert alert);

    Alert toEntity(AlertDTO alertDTO);

    default Alert fromId(Long id) {
        if (id == null) {
            return null;
        }
        Alert alert = new Alert();
        alert.setId(id);
        return alert;
    }
}
