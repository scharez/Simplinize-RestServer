package service;

import annotation.Secure;
import dto.ContactPersonDTO;
import dto.LoginDTO;
import dto.SkiTeacherDTO;
import entity.Role;
import repository.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
public class AuthService {

    /**
     * Login Person
     *
     * @param login dto of Login
     * @return Response with a json string
     */

    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response login(LoginDTO login) {

        return Repository.getInstance().login(login);
    }

    /**
     * Add Ski-Teacher
     *
     * @param st dto Ski-Teacher
     * @return Response with a json string
     */

    @Path("addSkiTeacher") // kann ja eigentlich nur der ADMIN aufrufen daher bleibt es eine einzelne Schnittstelle
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response addSkiTeacher(SkiTeacherDTO st) { //kann dann nur die Rolle Admin aufrufen
        return Repository.getInstance().addSkiTeacher(st.getFirstName(), st.getLastName(), st.getEmail(), st.getRoles());
    }

    /**
     * Delete Ski-Teacher
     *
     * @param id of SkiTeacher
     * @return Response with a json string
     */

    @Secure(Role.SKITEACHER)
    @Path("deleteSkiTeacher/{id}") // kann ja eigentlich nur der ADMIN aufrufen daher bleibt es eine einzelne Schnittstelle
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Response deleteSkiTeacher(@PathParam("id") long id) { //kann dann nur die Rolle Admin aufrufen, zum Testen daher keine
        return Repository.getInstance().deleteSkiTeacher(id);
    }

    /**
     * Update Ski-Teacher
     *
     * @param id of SkiTeacher
     * @return Response with a json string
     */

    @Secure(Role.SKITEACHER)
    @Path("updateSkiTeacher/{id}") // kann ja eigentlich nur der ADMIN aufrufen daher bleibt es eine einzelne Schnittstelle
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateSkiTeacher(SkiTeacherDTO teacher, @PathParam("id") long id) { //kann dann nur die Rolle Admin aufrufen
        return Repository.getInstance().updateSkiTeacher(id, teacher);
    }

    /**
     * Set Password for Ski-Teacher
     *
     * @param token token which was generated to set password
     * @param st dto of SkiTeacher
     * @return Response with a json string
     */

    @Path("setPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String setPassword(@QueryParam("token") String token, SkiTeacherDTO st){

        return Repository.getInstance().setPassword4SkiTeacher(token, st.getPassword());
    }

    /**
     * Register ContactPerson
     *
     * @param cp dto of ContactPerson
     * @return Response with a json string
     */

    @Path("registerContactPerson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response registerContactPerson(ContactPersonDTO cp) {

        return Repository.getInstance().registerContactPerson(cp.getFirstName(), cp.getLastName(), cp.getEmail(), cp.getPassword(), cp.getPhoneNumber());
    }

    /**
     * Delete a ContactPerson
     *
     * @param id ContactPerson
     * @return Response with a json string
     */

    @Secure(Role.ADMIN) // Wenn ContactPerson Account löschen will, dann muss der Admin kontaktiert werden, der dann die ContactPerson löscht! Im AdminInterface
    @Path("deleteContactPerson/{id}")                     //Email wird gesendet wenn gelöscht!
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Response deleteContactPerson(@PathParam("id") long id) {

        return Repository.getInstance().deleteContactPerson(id);
    }

    /**
     * Update a ContactPerson
     *
     * @param id dto of ContactPerson
     * @return Response with a json string
     */

    @Secure(Role.ADMIN) // Wenn ContactPerson Account löschen will, dann muss der Admin kontaktiert werden, der dann die ContactPerson löscht! Im AdminInterface
    @Path("updateContactPerson/{id}")                     //Email wird gesendet wenn gelöscht!
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateContactPerson(@PathParam("id") long id, ContactPersonDTO c) {

        return Repository.getInstance().updateContactPerson(id, c);
    }

    /**
     * Verify a ContactPerson
     *
     * @param token which was generated to verify the ContactPerson
     * @return Response with a json string
     */

    @Path("verifyContactPerson")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String confirmMail(@QueryParam("token") String token) {

        return Repository.getInstance().confirmMailContactPerson(token);
    }


}
