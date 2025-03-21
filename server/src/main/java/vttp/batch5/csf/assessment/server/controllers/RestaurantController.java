package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.models.PaymentReceipt;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantSvc;

  private Logger logger = Logger.getLogger(RestaurantController.class.getName());

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path = "/menu")
  public ResponseEntity<String> getMenus() {
    String menusAsJsonArr = restaurantSvc.getMenu();
    return ResponseEntity.ok(menusAsJsonArr);
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path = "/food_order")
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    JsonObject payloadJson = Json.createReader(new StringReader(payload)).readObject();
    
    logger.info("Receiving order from Angular");
    logger.info("Received order: %s".formatted(payloadJson.toString()));

    // Check credentials
    boolean credsValid = restaurantSvc.isCredentialValid(
      payloadJson.getString("username"), payloadJson.getString("password"));

    if (!credsValid) {
      JsonObject invalidCredObj = Json.createObjectBuilder()
          .add("message", "Invalid username and/or password")
          .build();
      return ResponseEntity.status(401).body(invalidCredObj.toString());
    }

    // Generate UUID
    String orderId = UUID.randomUUID().toString().substring(0, 8);

    try {
      // Place and save the order
      PaymentReceipt receipt = restaurantSvc.placeAndSaveOrder(orderId, payloadJson);

      return ResponseEntity.ok(receipt.toJson().toString());
    } catch (Exception e) {
      JsonObject errorResponse = Json.createObjectBuilder()
          .add("message", e.getMessage())
          .build();
      return ResponseEntity.status(500).body(errorResponse.toString());
    }
  }
}
