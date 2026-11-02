package server;

import com.google.gson.JsonObject;

public class Controller {

    private  JsonObject db;

    public Controller(JsonObject db) {
        this.db = db;
    }

    public JsonObject processCommand(JsonObject request) {
        String type = request.get("type").getAsString();

        switch (type) {
            case "set":
                return set(request);

            case "get":
                return get(request);

            case "delete":
                return delete(request);

            case "exit":
                return createOkResponse();

                 }
        return null;
    }

    private JsonObject set(JsonObject request) {
        String key = request.get("key").getAsString();
        String value = request.get("value").getAsString();

        db.addProperty(key, value);

        return createOkResponse();
    }

    private JsonObject get(JsonObject request) {
        String key = request.get("key").getAsString();

        if (!db.has(key)) {
            return createErrorResponse("No such key");
        }

        JsonObject response = createOkResponse();
        response.addProperty(
                "value",
                db.get(key).getAsString()
        );

        return response;
    }

    private JsonObject delete(JsonObject request) {
        String key = request.get("key").getAsString();

        if (!db.has(key)) {
            return createErrorResponse("No such key");
        }

        db.remove(key);

        return createOkResponse();
    }

    private JsonObject createOkResponse() {
        JsonObject response = new JsonObject();
        response.addProperty("response", "OK");

        return response;
    }

    private JsonObject createErrorResponse(String reason) {
        JsonObject response = new JsonObject();
        response.addProperty("response", "ERROR");
        response.addProperty("reason", reason);

        return response;
    }
}