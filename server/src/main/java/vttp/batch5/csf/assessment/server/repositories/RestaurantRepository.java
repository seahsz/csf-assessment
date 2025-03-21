package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean checkCredentials(String username, String password) {
        final String MYSQL_CHECK_CREDENTIALS = """
                SELECT * FROM customers WHERE 
                username = ? AND
                password = sha2(?, 224);
                """;

        // Converted username to lowercase - assumption is that username is case insensitive
        return template.queryForRowSet(MYSQL_CHECK_CREDENTIALS, username.toLowerCase(), password).first();        
    }

    public void insertOrder(JsonObject toInsert) {
        final String MYSQL_INSERT_ORDER = """
                INSERT INTO place_orders (order_id, payment_id, order_date, total, username)
                VALUES (?, ?, ?, ?, ?)
                """;

        // Convert long milliseconds to date
        Date orderDate = new Date(toInsert.getJsonNumber("paymentDate").longValue());

        template.update(MYSQL_INSERT_ORDER, 
            toInsert.getString("orderId"),
            toInsert.getString("paymentId"),
            orderDate,
            toInsert.getJsonNumber("total").doubleValue(),
            toInsert.getString("username"));
    }

}
