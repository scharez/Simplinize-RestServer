package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;

public class JsonBuilder {

    public JsonBuilder() {}

    public String generateResponse(String typ, String message, String content) {

        JSONObject json = new JSONObject();

        json.put("typ", typ)
                .put("message", message)
                .put("content", content);

        System.out.println();
        System.out.println(ConsoleColor.BLUE + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }

    public String generateDataResponse(String typ, String message, JSONArray data) {

        JSONObject json = new JSONObject();

        json.put("typ", typ)
                .put("message", message)
                .put("data", data);

        System.out.println();
        System.out.println(ConsoleColor.green() + json.toString() + ConsoleColor.RESET);

        return json.toString();

    }

}
