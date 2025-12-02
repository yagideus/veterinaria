package com.backend.luaspets.web.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.luaspets.domain.Services.PayPalService;

@Controller
@RestController
@RequestMapping("/api/paypal")
@CrossOrigin(origins = { "http://localhost:4200" })
public class PayPalController {

    private final PayPalService payPalService;

     // Inyección del servicio PayPalService
    public PayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam double totalAmount, @RequestParam String currency) {
        String orderId = payPalService.createOrder(totalAmount, currency);

        if (orderId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la orden en PayPal");
        }

       
        return ResponseEntity.ok(orderId);
    }
    

    @GetMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestParam String orderId) {
        boolean isPaymentVerified = payPalService.verifyPayment(orderId);

        if (isPaymentVerified) {
            return ResponseEntity.ok("Pago verificado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al verificar el pago");
        }
    }

}
