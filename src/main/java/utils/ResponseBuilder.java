package utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

public class ResponseBuilder {

    public Response genOkRes(String msg) {

        CacheControl cc = new CacheControl();
        cc.setNoStore(true);

        return Response.status(Response.Status.OK)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }

    public Response genErrorRes(String msg) {

        CacheControl cc = new CacheControl();
        cc.setNoStore(true);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }

    public Response genForbiddenRes(String msg) {

        CacheControl cc = new CacheControl();
        cc.setNoStore(true);

        return Response.status(Response.Status.FORBIDDEN)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }

    public Response genUnauthorizedRes(String msg) {

        CacheControl cc = new CacheControl();
        cc.setNoStore(true);

        return Response.status(Response.Status.UNAUTHORIZED)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }
}
