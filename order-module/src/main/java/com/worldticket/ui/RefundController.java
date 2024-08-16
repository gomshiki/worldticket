package com.worldticket.ui;

import com.worldticket.application.RefundService;
import com.worldticket.dto.RefundRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/refund")
@RestController
public class RefundController {
    private final RefundService refundService;

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollRefund(@RequestBody RefundRequestDto refundRequestDto) {
        refundService.enrollRefund(refundRequestDto);
        return ResponseEntity.ok("환불 등록이 완료됐습니다.");
    }
}
