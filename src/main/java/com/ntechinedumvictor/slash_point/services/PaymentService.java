package com.ntechinedumvictor.slash_point.services;

import com.ntechinedumvictor.slash_point.payment.PaymentRequest;
import com.ntechinedumvictor.slash_point.payment.VerifyPaymentRequest;

public interface PaymentService {
    String payment(PaymentRequest payment);

    String verifyPayment(VerifyPaymentRequest request, int userID);
}
