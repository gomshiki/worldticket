package com.worldticket.fifo.payment.ui;

import com.worldticket.fifo.payment.dto.PaymentRequestDto;
import com.worldticket.fifo.payment.application.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/payment")
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.enrollPayment(paymentRequestDto);
        return ResponseEntity.ok("결제가 등록됐습니다.");
    }

}
