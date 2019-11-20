package utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

public class ResponseBuilder {

    public Response genOkRes(String msg) { //type = hint oder ok

        return genResponse(Response.Status.OK, msg);
    }

    public Response genErrorRes(String msg) { //type = error z.B Beim email senden oder jwt Error im Repository

        return genResponse(Response.Status.INTERNAL_SERVER_ERROR, msg);
    }

    public Response genDataRes(String msg) {

        return genResponse(Response.Status.FOUND, msg);
    }

    public Response genForbiddenRes(String msg) {

        return genResponse(Response.Status.FORBIDDEN, msg);
    }

    public Response genUnauthorizedRes(String msg) {

        return genResponse(Response.Status.UNAUTHORIZED, msg);
    }

    private Response genResponse(Response.Status status, String msg) {

        CacheControl cc = new CacheControl();
        cc.setNoStore(true);

        return Response.status(status)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }
}
