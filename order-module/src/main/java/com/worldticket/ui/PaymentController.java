package com.worldticket.ui;

import com.worldticket.application.PaymentService;
import com.worldticket.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
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
