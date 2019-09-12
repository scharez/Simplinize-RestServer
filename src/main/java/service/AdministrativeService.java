package service;


import javax.ws.rs.Path;

@Path("administrative")
public class AdministrativeService {

    /**
     * Login a Ski-Teacher
     *
     * @param  the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Path("assignCourse")
    public String assignCourse() {

        return "";
    }

}
