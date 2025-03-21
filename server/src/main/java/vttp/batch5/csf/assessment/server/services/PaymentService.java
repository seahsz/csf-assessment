package vttp.batch5.csf.assessment.server.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class PaymentService {

    @Value("${payment.payee}")
    private String payee;

    private Logger logger = Logger.getLogger(PaymentService.class.getName());

    public static final String PAYMENT_URL = "https://payment-service-production-a75a.up.railway.app/api/payment";

    public String makePayment(String orderId, JsonObject orderJson, double totalPrice) {

        JsonObject payload = Json.createObjectBuilder()
            .add("order_id", orderId)
            .add("payer", orderJson.getString("username"))
            .add("payee", payee)
            .add("payment", totalPrice)
            .build();
        
        RequestEntity<String> postReq = RequestEntity
                .post(PAYMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Authenticate", orderJson.getString("username"))
                .body(payload.toString(), String.class);

        // Create rest template
        RestTemplate template = new RestTemplate();

        try {
            logger.info("Sending payment request: %s".formatted(payload.toString()));
            ResponseEntity<String> resp = template.exchange(postReq, String.class);
            String respPayload = resp.getBody();

            logger.info("Awaing payment response");
            logger.info("Payment response: %s".formatted(respPayload));

            return respPayload;

        } catch (Exception e) {
            throw e;
        }
    }
    
}
