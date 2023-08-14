package com.ntechinedumvictor.slash_point.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Transaction {
    private String status;
    private String tx_ref;
    private String transaction_id;
}
