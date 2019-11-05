package utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonBuilder {

    public JsonBuilder() {}

    public String genRes(String typ, String resource, String content) {

        JSONObject json = new JSONObject();

        json.put("typ", typ)
                .put("resource", resource)
                .put("content", content);

        System.out.println();
        System.out.println(ConsoleColor.BLUE + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }

    public String genDataRes(String resource, JSONArray data) {

        JSONObject json = new JSONObject();

        json.put("typ", "data")
                .put("resource", resource)
                .put("data", data);

        System.out.println();
        System.out.println(ConsoleColor.green() + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }

}
