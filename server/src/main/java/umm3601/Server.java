/*
* This Java source file was generated by the Gradle 'init' task.
*/
package umm3601;

import java.util.Arrays;

import spark.Request;
import spark.Response;

import static spark.Spark.*;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import umm3601.user.UserController;
import umm3601.user.UserRequestHandler;


public class Server {

  static String appName = "Lab 1";
  static String appVersion = "0.0.1";

  public static void main(String[] args) {

    // Get the MongoDB address and database name from environment variables and
    // if they aren't set, use the defaults of "localhost" and "dev".
    String mongoAddr = System.getenv().getOrDefault("MONGO_ADDR", "localhost");
    String databaseName = System.getenv().getOrDefault("MONGO_DB", "dev");

    // Set the port for the server, you shouldn't need to change this
    port(4567);

    // Setup the MongoDB client object with the information we set earlier
    MongoClient mongoClient = MongoClients.create(
      MongoClientSettings.builder()
      .applyToClusterSettings(builder ->
      builder.hosts(Arrays.asList(new ServerAddress(mongoAddr))))
      .build());

    // Get the database
    MongoDatabase database = mongoClient.getDatabase(databaseName);

    UserController userController = new UserController(database);
    UserRequestHandler userRequestHandler = new UserRequestHandler(userController);

    // Simple route
    get("/api", (req, res) -> appName + " API version " + appVersion);

    get("api/users", userRequestHandler::getUsers);
    get("api/users/:id", userRequestHandler::getUserJSON);
    post("api/users/new", userRequestHandler::addNewUser);

  }
}
