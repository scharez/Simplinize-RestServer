package utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

public class ResponseBuilder {

    /**
     *
     * @param msg
     * @return
     */

    public Response genRes(String msg) { //type = hint oder ok

        return genResponse(Response.Status.OK, msg);
    }

    /**
     *
     * @param msg
     * @return
     */

    public Response genErrorRes(String msg) { //type = error z.B Beim email senden oder jwt Error im Repository

        return genResponse(Response.Status.INTERNAL_SERVER_ERROR, msg);
    }

    /**
     *
     * @param msg
     * @return
     */

    public Response genForbiddenRes(String msg) {

        return genResponse(Response.Status.FORBIDDEN, msg);
    }

    /**
     *
     * @param msg
     * @return
     */

    public Response genUnauthorizedRes(String msg) {

        return genResponse(Response.Status.UNAUTHORIZED, msg);
    }

    /**
     *
     * @param msg
     * @return
     */

    public Response genNotAllowedRes(String msg) {

        return genResponse(Response.Status.METHOD_NOT_ALLOWED, msg);
    }

    /**
     *
     * @param msg
     * @return
     */

    public Response genNotFoundRes(String msg) {

        return genResponse(Response.Status.NOT_FOUND, msg);
    }

    /**
     *
     * @param status
     * @param msg
     * @return
     */

    private Response genResponse(Response.Status status, String msg) {

        CacheControl cc = new CacheControl();
        cc.setNoStore(true);

        return Response.status(status)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }
}
