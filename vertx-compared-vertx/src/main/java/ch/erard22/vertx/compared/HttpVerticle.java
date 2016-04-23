package ch.erard22.vertx.compared;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


/**
 * @author <a href='https://twitter.com/michelerard'>michelerard</a>
 */
public class HttpVerticle extends AbstractVerticle {

    private static final String HEADER_CONTENT_TYPE = "application/json; charset=utf-8";

    public static final String GET = "GET";

    private JsonObject config = new JsonObject().put("db_name", "default_db");
    private MongoClient mongoClient;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        mongoClient = MongoClient.createShared(vertx, config);
    }

    @Override
    public void start(Future<Void> fut) {

        // Create a router object.
        Router router = Router.router(vertx);

        router.get("/api/users").handler(this::getAll);
        router.route("/api/users*").handler(BodyHandler.create());
        router.post("/api/users").handler(this::add);
        router.delete("/api/users/:id").handler(this::delete);

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        // Retrieve the port from the configuration,
                        // default to 8080.
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );

        vertx.periodicStream(5000).handler(l -> System.out.println("Number of active threads: " + Thread.activeCount()));
    }

    private void delete(RoutingContext routingContext) {
        JsonObject query = new JsonObject().put("_id", routingContext.request().getParam("id"));
        mongoClient.removeOne("users", query, res -> {
            if (res.succeeded()) {
                routingContext.response().setStatusCode(204).end();
            } else {
                routingContext.response().setStatusCode(503).end();
            }
        });
    }

    private void add(RoutingContext routingContext) {
        JsonObject document = new JsonObject(routingContext.getBodyAsString());
        mongoClient.save("users", document, res -> {
            if (res.succeeded()) {
                routingContext.response().setStatusCode(201).end();
            } else {
                routingContext.response().setStatusCode(503).end();
            }

        });
    }


    private void getAll(RoutingContext routingContext) {

        mongoClient.find("users", new JsonObject(),res -> {
            if (res.succeeded()) {
                routingContext.response()
                        .putHeader("content-type", HEADER_CONTENT_TYPE)
                        .end(Json.encodePrettily(res.result()));
            } else {
                routingContext.response().setStatusCode(404).end();
            }
        });
    }
}
