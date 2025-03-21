package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.models.PaymentReceipt;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import vttp.batch5.csf.assessment.server.utilities.Util;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepo;

  @Autowired
  private RestaurantRepository restaurantRepo;

  @Autowired
  private PaymentService paymentSvc;

  // TODO: Task 2.2
  // You may change the method's signature
  public String getMenu() {
    List<Document> docs = ordersRepo.getMenu();
    String docsAsJsonString = Util.menuDocToJsonString(docs);
    return docsAsJsonString;
  }

  // TODO: Task 4
  public boolean isCredentialValid(String username, String password) {
    return restaurantRepo.checkCredentials(username, password);
  }

  public PaymentReceipt placeAndSaveOrder(String orderId, JsonObject order) throws Exception {
    // Calculate total price
    double totalPrice = 0;
    JsonArray items = order.getJsonArray("items");
    for (int i = 0; i < items.size(); i++) {
      JsonObject item = items.getJsonObject(i);
      totalPrice += item.getJsonNumber("price").doubleValue() * item.getInt("quantity");
    }

    // Make payment
    String paymentResp = paymentSvc.makePayment(orderId, order, totalPrice);
    JsonObject paymentObj = Json.createReader(new StringReader(paymentResp)).readObject();

    // Insert into database
    insertOrderIntoDb(paymentObj, order, orderId, totalPrice);  

    // Return Payment Receipt to client
    return new PaymentReceipt(orderId, paymentObj.getString("payment_id"), totalPrice, paymentObj.getJsonNumber("timestamp").longValue());
  }

  @Transactional
  private void insertOrderIntoDb(JsonObject paymentObj, JsonObject order, String orderId, double totalPrice) {
    String paymentId = paymentObj.getString("payment_id");
    long paymentDate = paymentObj.getJsonNumber("timestamp").longValue();
    String username = order.getString("username");

    // Inserting into MySQL
    JsonObject sqlToInsert = Json.createObjectBuilder()
        .add("orderId", orderId)
        .add("paymentId", paymentId)
        .add("paymentDate", paymentDate)
        .add("total", totalPrice)
        .add("username", username)
        .build();

    restaurantRepo.insertOrder(sqlToInsert);

    // Inserting into MongoDB
    List<Document> items = new LinkedList<>();

    JsonArray itemInJsonArr = order.getJsonArray("items");
    for (int i = 0; i < itemInJsonArr.size(); i++) {
      JsonObject obj = itemInJsonArr.getJsonObject(i);
      Document doc = new Document()
          .append("id", obj.getString("id"))
          .append("price", obj.getJsonNumber("price").doubleValue())
          .append("quantity", obj.getInt("quantity"));
      items.add(doc);
    }

    Document mongoToInsert = new Document()
        .append("_id", orderId)
        .append("order_id", orderId)
        .append("payment_id", paymentId)
        .append("username", username)
        .append("total", totalPrice)
        .append("timestamp", new Date(paymentDate))
        .append("items", items);

    boolean mongoInserted = ordersRepo.insertOrder(mongoToInsert);

    if (!mongoInserted) {
      throw new RuntimeException("Did not insert into MongoDB successfully");
    }
  }

}
