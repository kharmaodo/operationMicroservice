package sn.free.selfcare.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.free.selfcare.service.dto.PayasyougoDTO;
import sn.free.selfcare.service.dto.PaymentRequestDTO;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentRequestMapper {

    @Mapping(source = "transactionId", target = "externaltransactionid")
    @Mapping(source = "customerMsisdn", target = "customermsisdn")
    PaymentRequestDTO payasyougoToPaymentRequest(PayasyougoDTO dto);
}
