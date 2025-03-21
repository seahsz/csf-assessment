package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate template;

  private static final String C_MENUS = "menus";
  private static final String C_ORDERS = "orders";

  private static final String F_NAME = "name";

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  /*
   *  db.menus.find({}).sort({ name: 1 })
   */
  public List<Document> getMenu() {
    Query query = Query.query(new Criteria())
      .with(Sort.by(Direction.ASC, F_NAME));
    return template.find(query, Document.class, C_MENUS);
  }




  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here
  /*
   *  db.orders.insert({
        "_id": "abcd1234",
        "order_id": "abcd1234",
        "payment_id": "xyz789",
        "timestamp": new Date(1742534059063),
        "items": [
            { "id": "xxx", "price": 7.70, "quantity": 2 },
            { "id": "yyy", "price": 8.20, "quantity": 1 }
        ]
      })
   */
  public boolean insertOrder(Document toInsert) {
    return template.insert(toInsert, C_ORDERS) != null;
  }
  
}
