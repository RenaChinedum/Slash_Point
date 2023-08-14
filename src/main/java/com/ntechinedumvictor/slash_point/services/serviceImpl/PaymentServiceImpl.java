package com.ntechinedumvictor.slash_point.services.serviceImpl;

import com.ntechinedumvictor.slash_point.payment.PaymentRequest;
import com.ntechinedumvictor.slash_point.payment.PaymentResponse;
import com.ntechinedumvictor.slash_point.payment.Transaction;
import com.ntechinedumvictor.slash_point.payment.VerifyPaymentRequest;
import com.ntechinedumvictor.slash_point.services.OrderService;
import com.ntechinedumvictor.slash_point.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;

    @Value("${flutter-wave.secret_key}")
    private String flwSecretKey;

    @Override
    public String payment(PaymentRequest payment){
        String url = "https://api.flutterwave.com/v3/payments";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(flwSecretKey);
        HttpEntity<PaymentRequest> entity = new HttpEntity<>(payment,headers);
        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                PaymentResponse.class
        );
        PaymentResponse paymentResponse = response.getBody();

        assert paymentResponse != null;
        return paymentResponse.getData().getLink();
    }
    @Override
    public String verifyPayment(VerifyPaymentRequest request, int userID){
        Transaction transaction = Transaction.builder()
                .status(request.getStatus())
                .transaction_id(request.getTransaction_id())
                .tx_ref(request.getTxRef())
                .build();
        return "Your Payment Was " + transaction.getStatus();
    }

}
