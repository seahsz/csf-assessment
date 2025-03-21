package vttp.batch5.csf.assessment.server.utilities;

import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Util {

    public static String menuDocToJsonString(List<Document> docs) {

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Document d: docs) {
            JsonObject obj = Json.createObjectBuilder()
                .add("id", d.getString("_id"))
                .add("name", d.getString("name"))
                .add("description", d.getString("description"))
                .add("price", d.getDouble("price"))
                .build();
            arrBuilder.add(obj);
        }

        return arrBuilder.build().toString();
    }
    
}

/*
 *         "_id": "89ab618a",
        "name": "Tofu Bao",
        "price": 3.80,
        "description": "Crispy, fried tofu bites mixed in a spicy, topped Sausage"
 */
