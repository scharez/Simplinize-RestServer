package service;

import annotation.Secure;
import entity.Course;
import entity.Proficiency;
import entity.Role;
import entity.SkiTeacher;
import repository.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("app")
public class AppService {

    /**
     * Assign a course
     *
     * @param  course the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.ADMIN)
    @Path("assignCourse")
    @POST
    public String assignCourse(Course course) {

        return Repository.getInstance().assignCourse(course);
    }

    @Secure(Role.ADMIN)
    @Path("addTeacherToGroup/{id}")
    @POST
    public String addTeacherToGroup(@PathParam("id") long groupId, SkiTeacher skiTeacher) {

        return Repository.getInstance().addTeacherToGroup(groupId, skiTeacher);
    }

    @Secure(Role.ADMIN)
    @Path("getAllGroups")
    @GET
    public String getAllGroups() {

        return Repository.getInstance().getAllGroups();
    }

    @Secure(Role.ADMIN)
    @Path("getAllMembers")
    @GET
    public String getAllMembers() {

        return Repository.getInstance().getAllMembers();
    }

    @Secure(Role.EVERYONE)
    @Path("getGroupMembers/{id}")
    @GET
    public String getGroupMembers(@PathParam("id") long groupId) {

        return "";
    }

    @Secure(Role.EVERYONE)
    @Path("getGroup/{id}")
    @GET
    public String getGroup(@PathParam("id") long groupId) {

        return "";
    }

    @Secure(Role.EVERYONE)
    @Path("getGroupMembers/{proficiency}")
    @GET
    public String getGroup(@PathParam("proficiency") Proficiency proficiency) {

        return "";
    }

    @Secure(Role.EVERYONE)
    @Path("getProficiency/{id}")
    @GET
    public String getProficiency(@PathParam("id") long skiTeacherId) {

        return "";
    }
}
