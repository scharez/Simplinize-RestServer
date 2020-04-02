package filter;

import utils.JsonBuilder;
import utils.ResponseBuilder;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//@Provider
public class ExceptionFilter implements ExceptionMapper<Exception>  {

    private ResponseBuilder responseBuilder = new ResponseBuilder();
    private JsonBuilder jsonBuilder = new JsonBuilder();

    @Override
    public Response toResponse(Exception e) {

        // Es wurde keine Route gefunden, die mit der URL und der Request-Methode identisch ist.

        // type=Not Found message= ^

        return responseBuilder.genNotFoundRes(jsonBuilder.genRes("not found","Something went wrong!"));
    }
}
