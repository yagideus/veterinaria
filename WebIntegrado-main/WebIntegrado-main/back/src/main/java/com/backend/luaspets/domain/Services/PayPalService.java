package com.backend.luaspets.domain.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;

@Service
public class PayPalService {
    
     private final PayPalHttpClient payPalClient;

    public PayPalService(PayPalHttpClient payPalClient) {
        this.payPalClient = payPalClient;
    }

    public String createOrder(double totalAmount, String currency) {

        List<String> supportedCurrencies = Arrays.asList("USD", "PEN"); // Monedas aceptadas
    if (!supportedCurrencies.contains(currency)) {
        throw new IllegalArgumentException("Currency not supported: " + currency);
    }
    
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName("LuasPets")
                .landingPage("BILLING")
                .cancelUrl("http://localhost:8080/cancel")
                .returnUrl("http://localhost:8080/success");

        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode(currency)
                        .value(String.valueOf(totalAmount)));
        purchaseUnits.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.requestBody(orderRequest);

        try {
            HttpResponse<Order> response = payPalClient.execute(request);
            return response.result().id();
        } catch (HttpException e) {
            System.err.println("Error al crear la orden: " + e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean verifyPayment(String orderId) {
        // Configuración del entorno de PayPal (sandbox o production)
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
            "AZI4NjEvyLgQYTTZ1s_EmTmo-HAp4DtKaVnzc7h8Ea30gAmLH4aNs35yJYtYmJIH_CBjh3O9d2oBQCy-", 
            "EIVK-GCYnn7zyK-2mdAObkFT_vGFiMPokt2gHkHaHWdcNgyd7R4T8Ils20zna2ZbnrSMUMH-3yVUfKDA" 
        );

        PayPalHttpClient client = new PayPalHttpClient(environment);

        OrdersGetRequest request = new OrdersGetRequest(orderId);

        try {
            // Ejecutar la solicitud para obtener los detalles de la orden
            HttpResponse<Order> response = client.execute(request);
            Order order = response.result();

            // Verificar si el estado de la transacción es "COMPLETED"
            return "COMPLETED".equals(order.status());
        } catch (IOException e) {
            // Manejar errores en la comunicación con PayPal
            e.printStackTrace();
            return false;
        }
    }

}
