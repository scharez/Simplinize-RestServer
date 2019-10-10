package service;

import dto.SkiTeacherDTO;
import repository.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("auth")
public class AuthService {

    /**
     * Login a Ski-Teacher
     *
     * @param st the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String login(SkiTeacherDTO st) {

        return Repository.getInstance().login(st.getUsername(), st.getPassword());
    }

    /**
     * add a Ski-Teacher
     *
     * @param st the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully register message
     */

    @Path("addSkiTeacher")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String addSkiTeacher(SkiTeacherDTO st) {
        return Repository.getInstance().addSkiTeacher(st.getFirstName(), st.getLastName(), st.getEmail(), st.getRoles());
    }

    /**
     * Set Password for Ski-Teacher
     *
     * @param token token which was generated to set password
     * @param st transfer object of SkiTeacher entity
     * @return a json which can contain an error or a successfully register message
     */

    @Path("setPassword/{token}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String setPassword(@PathParam("token") String token, SkiTeacherDTO st){

        return Repository.getInstance().setPassword4SkiTeacher(token, st.getPassword());
    }

    /**
     * add a Ski-Teacher
     *
     * @param st transfer object of SkiTeacher entity
     * @return a json which can contain an error or a successfully register message
     */

    @Path("changeDetails")
    public String changeDetails() {

        return "";
    }


}
