package service;

import dto.ContactPersonDTO;
import dto.LoginDTO;
import dto.SkiTeacherDTO;
import repository.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
public class AuthService {

    /**
     * Login a SkiTeacher
     *
     * @param login the Transfer Object of Login
     * @return a json which can contain an error or a successfully login message
     */

    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response login(LoginDTO login) {

        return Repository.getInstance().login(login);
    }

    /**
     * add a Ski-Teacher
     *
     * @param st the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully register message
     */

    @Path("addSkiTeacher") // kann ja eigentlich nur der ADMIN aufrufen daher bleibt es eine einzelne Schnittstelle
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response addSkiTeacher(SkiTeacherDTO st) {
        return Repository.getInstance().addSkiTeacher(st.getFirstName(), st.getLastName(), st.getEmail(), st.getRoles());
    }

    /**
     * Set Password for Ski-Teacher
     *
     * @param token token which was generated to set password
     * @param st transfer object of SkiTeacher entity
     * @return a json which can contain an error or a successfully register message
     */

    @Path("setPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String setPassword(@QueryParam("token") String token, SkiTeacherDTO st){

        return Repository.getInstance().setPassword4SkiTeacher(token, st.getPassword());
    }

    /**
     * Register a ContactPerson
     *
     * @param register the Transfer Object of ContactPersonDTO
     * @return a json which can contain an error or a successfully login message
     */

    @Path("registerContactPerson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response registerContactPerson(ContactPersonDTO register) {

        return Repository.getInstance().registerContactPerson(register.getFirstName(), register.getLastName(), register.getEmail(), register.getPassword(), register.getPhoneNumber());
    }

    /**
     * Verify a ContactPerson
     *
     * @param token the Transfer Object of ContactPersonDTO
     * @return a json which can contain an error or a successfully login message
     */

    @Path("verifyContactPerson")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String confirmMail(@QueryParam("token") String token) {

        return Repository.getInstance().confirmMailContactPerson(token);
    }


}
