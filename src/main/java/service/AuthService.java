package service;

import dto.SkiTeacherDTO;
import repository.Repository;

import javax.ws.rs.Path;

@Path("auth")
public class AuthService {


    /**
     * Login a Ski-Teacher
     *
     * @param st the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Path("login")
    public String login(SkiTeacherDTO st) {
        return Repository.getInstance().login(st.getUsername(), st.getPassword());
    }

    /**la
     * Register a Ski-Teacher
     *
     * @param st the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully register message
     */

    @Path("addSkiTeacher")
    public String addSkiTeacher(SkiTeacherDTO st) {

        return Repository.getInstance().addSkiTeacher(st.getFirstName(), st.getLastName(), st.getEmail(), st.getRoles());
    }


}
