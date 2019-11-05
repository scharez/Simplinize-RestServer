package filter;

import annotation.Secure;
import entity.Role;
import repository.Repository;
import utils.JsonBuilder;
import utils.JwtHelper;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthFilter implements ContainerRequestFilter {

    private JsonBuilder jb = new JsonBuilder();
    private JwtHelper jwt = new JwtHelper();

    private String token;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext rc) {

        String authorizationHeader = rc.getHeaderString(HttpHeaders.AUTHORIZATION);

        Method resourceMethod = resourceInfo.getResourceMethod();

        Secure secure = resourceMethod.getAnnotation(Secure.class);

        if(resourceMethod.isAnnotationPresent(Secure.class)) {
            try {
                token = authorizationHeader.substring("Bearer".length()).trim();
                jwt.checkSubject(token);

                if (isUserInRole(jwt.getRoles(token), secure.value())) {
                    Repository.getInstance().saveHeader(token);
                } else {
                    Response res = validateRequest("You are not allowed", resourceMethod, Response.Status.FORBIDDEN);
                    rc.abortWith(res);
                }
            } catch (Exception ex) {
                Response res = validateRequest("Auth-Token Error, Please login first!", resourceMethod, Response.Status.UNAUTHORIZED);
                rc.abortWith(res);
            }
        }
    }

    private boolean isUserInRole(Role[] userRoles, Role[] roles) {

        for(Role role: roles) {
            for(Role userRole: userRoles) {
                if(role.equals(userRole)) /*|| role.equals(Role.EVERYONE))*/ return true;
            }
        }
        return false;
    }

    private Response validateRequest(String content, Method method, Response.Status status) {

        String msg = jb.generateResponse("error", method.getName(), content);
        CacheControl cc = new CacheControl();
        cc.setNoStore(true);
        return Response.status(status)
                .cacheControl(cc)
                .entity(msg)
                .build();
    }
}

