package sn.free.selfcare.service;

import java.util.Map;

public interface PaymentMethod<T> {
    Map pay(T paymentRequest);
}
