package com.ntechinedumvictor.slash_point.payment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPaymentRequest {
    private String status;
    private String txRef;
    private String transaction_id;
}
