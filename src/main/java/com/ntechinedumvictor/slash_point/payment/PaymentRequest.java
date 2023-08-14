package com.ntechinedumvictor.slash_point.payment;

import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @Id
    private String tx_ref;
    private String amount;
    private String currency;
    private String redirect_url;
    private Customer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PaymentRequest payment = (PaymentRequest) o;
        return getTx_ref() != null && Objects.equals(getTx_ref(), payment.getTx_ref());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
