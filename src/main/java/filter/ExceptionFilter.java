package filter;

import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import utils.JsonBuilder;
import utils.ResponseBuilder;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionFilter implements ExceptionMapper<Throwable>  {

    private ResponseBuilder responseBuilder = new ResponseBuilder();
    private JsonBuilder jsonBuilder = new JsonBuilder();

    @Override
    public Response toResponse(Throwable e) {
        
        return responseBuilder.genErrorRes(jsonBuilder.genExceptionRes("Something went wrong!"));
    }
}
