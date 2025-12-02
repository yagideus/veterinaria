package com.backend.luaspets.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.core.PayPalHttpClient;

@Configuration
public class PayPalConfig {
    

    @Bean
    public PayPalHttpClient payPalClient() {
        // Reemplaza con tus credenciales reales
        String clientId = "AZI4NjEvyLgQYTTZ1s_EmTmo-HAp4DtKaVnzc7h8Ea30gAmLH4aNs35yJYtYmJIH_CBjh3O9d2oBQCy-";
        String clientSecret = "EIVK-GCYnn7zyK-2mdAObkFT_vGFiMPokt2gHkHaHWdcNgyd7R4T8Ils20zna2ZbnrSMUMH-3yVUfKDA";

        return new PayPalHttpClient(
                new com.paypal.core.PayPalEnvironment.Sandbox(clientId, clientSecret)
        );
    }

}
