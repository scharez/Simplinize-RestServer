package service;

import annotation.Secure;
import dto.CourseDTO;
import entity.Course;
import entity.Proficiency;
import entity.Role;
import entity.SkiTeacher;
import repository.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("app")
public class AppService {

    @Secure(Role.EVERYONE)
    @Path("addChildrenToCourse/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String addChildrenToCourse(@PathParam("id") long courseId, CourseDTO course) {

        return Repository.getInstance().assignCourse(course.getFrom(), course.getTo(), course.getPlace(), course.getInstructor());
    }


    /**
     * Assign a course
     *
     * @param  course the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.ADMIN)
    @Path("assignCourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String assignCourse(CourseDTO course) {

        return Repository.getInstance().assignCourse(course.getFrom(), course.getTo(), course.getPlace(), course.getInstructor());
    }

    @Secure(Role.ADMIN)
    @Path("createGroup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String test () {

        return "";
    }

    @Secure(Role.ADMIN)
    @Path("addTeacherToGroup/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String addTeacherToGroup(@PathParam("id") long groupId, SkiTeacher skiTeacher) {

        return Repository.getInstance().addTeacherToGroup(groupId, skiTeacher);
    }

    @Secure(Role.ADMIN)
    @Path("getAllGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getAllGroups() {

        return Repository.getInstance().getAllGroups();
    }

    @Secure(Role.ADMIN)
    @Path("getAllMembers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getAllMembers() {

        return Repository.getInstance().getAllMembers();
    }

    @Secure(Role.SKITEAM)
    @Path("getGroupMembers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getGroupMembers(@PathParam("id") long groupId) {

        return "";
    }

    @Secure(Role.SKITEAM)
    @Path("getGroup/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getGroup(@PathParam("id") long groupId) {

        return "";
    }

    @Secure(Role.SKITEAM)
    @Path("getGroupMembers/{proficiency}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String getGroup(@PathParam("proficiency") Proficiency proficiency) {

        return "";
    }

    @Secure(Role.SKITEAM)
    @Path("getProficiency/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getProficiency(@PathParam("id") long skiTeacherId) {

        return "";
    }
}
