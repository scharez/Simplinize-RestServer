package utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonBuilder {

    /**
     *
     * @param typ
     * @param resource
     * @param message
     * @return
     */

    public String genRes(String typ, String resource, String message) {

        JSONObject json = new JSONObject();

        json.put("typ", typ) // can be HINT or OK
                .put("resource", resource)
                .put("message", message);

        System.out.println();
        System.out.println(ConsoleColor.BLUE + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }

    /**
     *
     * @param typ
     * @param msg
     * @return
     */

    public String genRes(String typ, String msg) {

        JSONObject json = new JSONObject();

        json.put("typ", typ)
                .put("message", msg);

        System.out.println();
        System.out.println(ConsoleColor.BLUE + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }

    /**
     *
     * @param resource
     * @param data
     * @return
     */

    public String genDataRes(String resource, JSONArray data) {

        JSONObject json = new JSONObject();

        json.put("typ", "data")
                .put("resource", resource)
                .put("data", data);

        System.out.println();
        System.out.println(ConsoleColor.green() + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }

    /**
     *
     * @param resource
     * @param msg
     * @param data
     * @return
     */

    public String genDataRes(String resource, String msg, JSONArray data) {

        JSONObject json = new JSONObject();

        json.put("typ", "data")
                .put("resource", resource)
                .put("message", msg)
                .put("data", data);

        System.out.println();
        System.out.println(ConsoleColor.green() + json.toString() + ConsoleColor.RESET);

        return json.toString();
    }



}
